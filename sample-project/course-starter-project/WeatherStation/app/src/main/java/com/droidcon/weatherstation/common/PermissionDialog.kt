package com.droidcon.weatherstation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = onDismiss, buttons = {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider()
            Text(text = if (isPermanentlyDeclined) {
                "Grant permission"
            } else {
                "OK"
            },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isPermanentlyDeclined) {
                            onGoToAppSettingsClick()
                        } else {
                            onOkClick()
                        }
                    }
                    .padding(16.dp))
        }
    }, title = {
        Text(text = "Permission required")
    }, text = {
        Text(
            text = permissionTextProvider.getDescription(
                isPermanentlyDeclined = isPermanentlyDeclined
            )
        )
    }, modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined location permission. " + "You can go to the app settings to grant it."
        } else {
            "This app needs access to your location to fetch the weather data"
        }
    }
}

class StoragePermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined storage permission. " + "You can go to the app settings to grant it."
        } else {
            "This app needs access to your storage to save the weather data to gallery."
        }
    }
}

class NotificationsPermissionProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined notifications permission. " + "You can go to the app settings to grant it."
        } else {
            "This app needs notifications permissions to display notifications on the app."
        }
    }
}