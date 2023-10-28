package jp.nitech.edamame

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EdamameAppBar(
    title: String,
    isBackButtonShown: Boolean = true,
    onBackButtonClicked: () -> Unit = {},
    right: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .statusBarsPadding()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeaderItem(
            Alignment.CenterStart,
            modifier = Modifier.weight(2f).fillMaxWidth()
        ) {
            if (isBackButtonShown) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { onBackButtonClicked() }
                        .padding(8.dp),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
        }
        HeaderItem(
            Alignment.Center,
            modifier = Modifier.weight(5f).fillMaxWidth()
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        HeaderItem(
            Alignment.CenterEnd,
            modifier = Modifier.weight(2f).fillMaxWidth()
        ) {
            right()
        }
    }
}

@Composable
private fun HeaderItem(
    alignment: Alignment,
    modifier: Modifier = Modifier,
    inner: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = alignment,
    ) {
        inner()
    }
}
