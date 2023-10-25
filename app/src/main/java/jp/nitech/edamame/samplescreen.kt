package jp.nitech.edamame

import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun samplescreen(){
    val context = LocalContext.current
    Button(onClick = { Toast.makeText(context,"やっほい",Toast.LENGTH_LONG).show() }) {
        Text(text = "始め")

    }
}