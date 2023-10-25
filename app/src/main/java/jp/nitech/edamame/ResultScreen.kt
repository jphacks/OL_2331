package jp.nitech.edamame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.nitech.edamame.steps.Step
import jp.nitech.edamame.steps.StepType
import jp.nitech.edamame.utils.rememberInMemory
import java.time.LocalTime


@Composable
fun ResultScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val vm = rememberInMemory {
        ResultScreenViewModel(context, coroutineScope)
    }
    Scaffold(
        topBar = {
            EdamameAppBar(
                title = "",
                right = {
                    Row() {
                        //modifier = Modifier.clickable { fav = !fav }
                        Icon(Icons.Default.Favorite, "", tint = Color(0xFFff0000))
                        Icon(Icons.Default.Settings, "")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(
                        color = Color(0xFFffc1ff)
                    )
                    .padding(paddingValues)
            ) {
                //Divider(
                //    modifier = Modifier.height(10.dp),
                //    color = Color(0xFF000000)
                //)
                arrivaltime(vm.arrivalDate, vm.arrivalTime)
                destination(vm.destination)
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    reversal(
                        modifier = Modifier
                            .clickable {
                                       if(vm.isFavorited){
                                           vm.unlike()
                                       }else{
                                           vm.like()
                                       }
                            },
                        fav = vm.isFavorited
                    )
                }

                Divider(
                    modifier = Modifier.height(6.dp),
                    color = Color(0xFF000000)
                )
                list(
                    step = listOf(
                        Step(
                            LocalTime.now(),
                            "起きる",
                            "To do list\n・ごみを出す",
                            StepType.WALK
                        ),
                        Step(
                            LocalTime.now(),
                            "家を出る",
                            "詳細\n",
                            StepType.WALK
                        ),
                        Step(
                            LocalTime.now(),
                            "名駅",
                            "乗り換え\n",
                            StepType.LOCAL_TRAIN
                        ),
                        Step(
                            LocalTime.now(),
                            "鶴舞駅",
                            "詳細",
                            StepType.WALK
                        ),
                        Step(
                            LocalTime.now(),
                            "名工大",
                            "詳細",
                            StepType.WALK
                        )
                    )
                )
            }
        }
    )
}

@Composable
fun arrivaltime(date: String, time: String) {
    var context = LocalContext.current
    var text = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = date, fontSize = 25.sp)
        Text(text = time, fontSize = 60.sp)
    }
}

@Composable
private fun destination(dist: String) {
    var context = LocalContext.current
    var text = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = dist, fontSize = 14.sp)
    }
}

@Composable
fun list(step: List<Step>) {
    var context = LocalContext.current
    var text = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        for (i in 0 until step.size) {
            val s = step[i]
            var editable by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .clickable { editable = !editable }
                    .padding(
                        horizontal = 32.dp,
                        vertical = 16.dp
                    ),
                //horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = s.startTime.format(formatterTime), fontSize = 20.sp)
                    Text(text = s.title, fontSize = 20.sp)
                    Text(text = s.type.name, fontSize = 20.sp)
                }
                AnimatedVisibility(
                    visible = editable,
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(text = s.detailMessage)
                }
                Divider(
                    modifier = Modifier.height(2.dp),
                    color = Color(0xFF000000)
                )
            }
        }
    }
}

@Composable
fun reversal(modifier: Modifier, fav: Boolean) {
    Box(
        modifier = modifier
    ) {
        if (fav == true) {
            Icon(Icons.Default.Favorite, "", tint = Color(0xFFff0000))
        } else {
            Icon(Icons.Default.FavoriteBorder, "", tint = Color(0xFFff0000))
        }
    }
}