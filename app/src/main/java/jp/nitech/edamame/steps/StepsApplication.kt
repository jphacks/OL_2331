package jp.nitech.edamame.steps

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import jp.nitech.edamame.ToDo
import jp.nitech.edamame.extension.formatCommaSplit
import jp.nitech.edamame.steps.StepType.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * 出発の準備から、目的地につくまでのステップに関するアプリケーションです。
 */
class StepsApplication(
    val apiKey: String,
) {

    /**
     * ステップ探索の設定
     *
     * @property walkingSpeed
     */
    data class ExploreStepsSettings(
        val walkingSpeed: WalkingSpeed = WalkingSpeed.NORMAL,
    )

    /**
     * ステップ探索のリクエスト情報
     *
     * @property arrivalDateTime 到着日時
     * @property start 出発位置
     * @property goal 到着位置
     * @property preparingTodos 準備に必要なTodoリスト
     * @property preparingMinutes 準備時間
     */
    data class ExploreStepsRequest(
        val arrivalDateTime: LocalDateTime,
        val start: LatLng,
        val goal: LatLng,
        val preparingTodos: List<ToDo>,
        val preparingMinutes: Int,
    )

    /**
     * ステップの探索を行います。
     * @return ステップの探索結果。探索が失敗した場合またはエラーの場合はExceptionを返します・
     */
    fun exploreSteps(
        exploreStepsRequest: ExploreStepsRequest,
        exploreStepsSettings: ExploreStepsSettings,
    ): Result<List<List<Step>>, Exception> {
        try {
            // NAVITIME RapidAPI ルート検索（トータルナビ）で、出発位置から到着位置までの経路探索を行う。
            // 到着日時、歩く速度の指定も行う。
            // 経路候補を取得する。
            val apiUrl = "https://navitime-route-totalnavi.p.rapidapi.com/route_transit"
                .toHttpUrlOrNull() ?: return Err(IllegalStateException("URL is cannot be parsed."))
            val requestUrl = apiUrl.newBuilder()
                .addQueryParameter("start", exploreStepsRequest.start.formatCommaSplit())
                .addQueryParameter("goal", exploreStepsRequest.goal.formatCommaSplit())
                .addQueryParameter(
                    "goal_time",
                    exploreStepsRequest.arrivalDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
                )
                .addQueryParameter("walk_speed", exploreStepsSettings.walkingSpeed.km_per_hour.toString())
                .build()
            val request = Request.Builder()
                .url(requestUrl)
                .header("X-RapidAPI-Key", apiKey)
                .build()
            val responseBody = client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return Err(Exception("Unexpected code $response"))
                val body = response.body ?: return Err(Exception("Response body is null."))
                return@use Gson().fromJson(body.string(), JsonObject::class.java)
            }

            // 経路候補が0個の場合はnullを返す。
            if (responseBody.getAsJsonArray("items").size() == 0)
                return Err(Exception("No route candidates."))

            // RouteCandidateParser、経路候補の最初のアイテムを解析する。
            val stepsCandidates = mutableListOf<List<Step>>()
            responseBody.getAsJsonArray("items").forEach { item ->
                val routeCandidate = item.asJsonObject
                val routeSteps = RouteCandidateParser().parse(routeCandidate)

                // 準備のステップを追加
                val firstStepStartTime = routeSteps[0].startTime
                val preparingStep = Step(
                    startTime = firstStepStartTime.minusMinutes(
                        exploreStepsRequest.preparingMinutes.toLong()
                    ),
                    title = "準備",
                    detailMessage = exploreStepsRequest.preparingTodos.joinToString("\n") { it.title },
                    type = StepType.PREPARE,
                )
                val steps = listOf(preparingStep, *routeSteps.toTypedArray())
                stepsCandidates.add(steps)
            }

            return Ok(stepsCandidates)
        } catch (ex: Exception) {
            return Err(ex)
        }
    }

    companion object {
        val client = OkHttpClient()
    }

}

class RouteCandidateParser {

    /**
     * 経路候補のJSONObjectを解析し、ステップのリストを返します。
     *
     * @throws Exception 解析に失敗した場合
     */
    @Throws(Exception::class)
    fun parse(routeCandidate: JsonObject): List<Step> {
        val steps = mutableListOf<Step>()
        val sections = routeCandidate.getAsJsonArray("sections")

        // NOTE: 最後のSectionはゴール地点であるため処理しないことに注意
        for (i in 0..(sections.size() - 2) step 2) {
            // 偶数番目はポイントセクション、奇数番目は移動セクションと仮定する。
            val pointSection = sections[i].asJsonObject
            val moveSection = sections[i + 1].asJsonObject

            // タイトルをポイントセクションから取得する。
            val title = pointSection.get("name").asString

            // 出発時間、移動種別、詳細メッセージを移動セクションから取得する。
            val startTime = LocalTime.parse(
                moveSection.get("from_time").asString,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME
            )
            val stepType = StepType.fromNaviTimeAPIString(
                moveSection.get("move").asString
            ) ?: throw Exception("Unknown transit type.")
            val detailMessage = getDetailMessageFromMoveSectionJsonObject(moveSection)

            steps.add(Step(
                startTime = startTime,
                title = title,
                detailMessage = detailMessage,
                type = stepType,
            ))
        }
        return steps
    }

    private fun getDetailMessageFromMoveSectionJsonObject(moveSection: JsonObject): String {
        val type = StepType.fromNaviTimeAPIString(
            moveSection.get("move").asString
        ) ?: throw Exception("Unknown transit type.")

        return when (type) {
            WALK -> "徒歩 ${moveSection.get("time").asInt}分"
            CAR -> "車 ${moveSection.get("time").asInt}分"
            BICYCLE -> "自転車 ${moveSection.get("time").asInt}分"
            UNKNOWN -> "${moveSection.get("time").asInt}分"
            else -> {
                val transport = moveSection.get("transport").asJsonObject
                return """${moveSection.get("line_name").asString}
                |${moveSection.get("time").asInt}分
                |(${getLinksMessageFromLinksJsonArray(transport.getAsJsonArray("links"))})""".trimMargin()
            }
        }
    }

    private fun getLinksMessageFromLinksJsonArray(links: JsonArray): String {
        val linkMessages = mutableListOf<String>()
        links.forEach { link ->
            val linkJsonObject = link.asJsonObject
            val from = linkJsonObject.get("from").asJsonObject
            val fromName = from.get("name").asString
            val to = linkJsonObject.get("to").asJsonObject
            val toName = to.get("name").asString
            linkMessages.add("$fromName〜$toName")
        }
        return linkMessages.joinToString("、")
    }

}