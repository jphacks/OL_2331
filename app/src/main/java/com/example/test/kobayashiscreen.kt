package com.example.test

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.format.DateTimeFormatter


val formatter = DateTimeFormatter.ofPattern("  HH:mm")

@Composable
fun samplescreen2(){
    val context = LocalContext.current
    Column (
        //modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.background(
            color = Color(0xFFffc1ff)
        )
    ){
        destination()
        time()
    }

    /*Button(onClick = { Toast.makeText(context,"やっほい", Toast.LENGTH_LONG).show() }) {
        Text(text = "始め")

    }*/
}

@Composable
fun destination(){
    var context = LocalContext.current
    var text = remember{ mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "目的地", fontSize = 25.sp)
        OutlinedTextField(
            value = text.value,
            onValueChange = {text.value = it},
            enabled = false,
            placeholder = { Text(text = "目的地を選択してください")},
            colors = TextFieldDefaults.textFieldColors(disabledTextColor =
            MaterialTheme.colors.onPrimary, backgroundColor = Color(0xFFffffff) )

            //modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun time(){
    var context = LocalContext.current
    val text = remember{ mutableStateOf("") }
    var dialogState = rememberMaterialDialogState()
    MaterialDialog (
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker { text.value = it.format(formatter)
            //Do stuff with java.time.LocalDate object which is passed in
        }
    }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text(text = "到着時間", fontSize = 25.sp)
        OutlinedTextField(
            value = text.value,
            onValueChange = {text.value = it},
            enabled = false,
            placeholder = { Text(text = "到着時間を選択してください")},
            modifier = Modifier
                .clickable {
                    dialogState.show()
                },
            colors = TextFieldDefaults.textFieldColors(disabledTextColor =
                MaterialTheme.colors.primary, backgroundColor = Color(0xFFffffff) )
        )
    }
}