package jp.nitech.edamame

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun LoadingScreen(navController: NavController) {
    EdamameAppPermissionRequester(navController = navController)
}

@Composable
private fun EdamameAppPermissionRequester(
    navController: NavController,
) {
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current

    val requiredPermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    PermissionRequester(
        requiredPermissions = requiredPermissions,
        onAllRequiredPermissionsIsGranted = {
            navController.navigate(Screen.InputCondition.route)
        },
        onNotAllRequiredPermissionsIsGranted = {
            Toast.makeText(context, "位置情報の権限を許可してください", Toast.LENGTH_LONG).show()

            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            activity?.finish()
        }
    )
}

@Composable
private fun PermissionRequester(
    requiredPermissions: List<String>,
    onAllRequiredPermissionsIsGranted: () -> Unit = {},
    onNotAllRequiredPermissionsIsGranted: () -> Unit = {},
) {
    val permissionRequester = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (it.values.all { granted -> granted == true }) {
                onAllRequiredPermissionsIsGranted()
            } else {
                onNotAllRequiredPermissionsIsGranted()
            }
        }
    )
    LaunchedEffect(Unit) {
        permissionRequester.launch(requiredPermissions.toTypedArray())
    }
}