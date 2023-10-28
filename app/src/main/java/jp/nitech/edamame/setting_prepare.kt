package jp.nitech.edamame

import android.accounts.AuthenticatorDescription
import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Entity
import androidx.room.PrimaryKey
//import jp.nitech.edamame.settingtodo.
//import jp.nitech.edamame.settingtodo.ToDoDao
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Composable
fun settingprepare() {
    Column {
        SimpleOutlinedTextFieldSample()
        List1()

    }

}

//文字を入力させる
@Composable
fun SimpleOutlinedTextFieldSample() {
    var text by remember { mutableStateOf("") }
    val context= LocalContext.current
    LaunchedEffect(text){
        GlobalScope.launch(Dispatchers.IO) {
            saveText(context= context,"preparetime")
        }
    }
    Box(
        modifier = Modifier.padding(20.dp),
        contentAlignment = Alignment.TopCenter
    ) {


        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "分") },
            singleLine = true
        )
    }

}
val TEXT_KEY = stringPreferencesKey("example_text")

suspend fun saveText(context: Context, text: String) {
    context.dataStore.edit { settings ->
        settings[TEXT_KEY] = text
    }
}

@Composable
fun List1() {
    val fruits = listOf("Apple", "Orange", "Grape", "Peach", "Strawberry")
    LazyColumn {
        items(fruits) { fruit ->
            Text(text = "This is $fruit",color = Color.Blue)
        }
    }
}