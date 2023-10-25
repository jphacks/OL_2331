package jp.nitech.edamame

import android.content.pm.PackageManager
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.google.android.gms.maps.model.LatLng
import jp.nitech.edamame.steps.StepsApplication

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StepsApplicationUnitTest {
    @Test
    fun `exploreSteps_GifuCity_To_HokkaidoKamikawaTown`() {
        val file = File("../local.properties")
        val apiKey = file.readLines().find { it.startsWith("NAVITIME_RAPID_API_KEY=") }?.split("=")?.get(1)
        assertNotNull(apiKey)
        val stepsApplication = StepsApplication(apiKey!!)
        val steps = stepsApplication.exploreSteps(
            exploreStepsRequest = StepsApplication.ExploreStepsRequest(
                arrivalDateTime = LocalDateTime.parse("2023-11-30T18:00:00", DateTimeFormatter.ISO_DATE_TIME),
                start = LatLng(35.447227,136.756165),
                goal = LatLng(43.7384913,142.3067512),
                preparingTodos = listOf(),
                preparingMinutes = 10,
            ),
            exploreStepsSettings = StepsApplication.ExploreStepsSettings()
        )

        steps.onSuccess {  steps ->
            steps.forEach { step ->
                println("${step.startTime}: ${step.title} -- ${step.detailMessage}")
            }
        }.onFailure {
            fail(it.stackTraceToString())
        }
    }
}