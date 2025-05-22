package com.storage.cloudbackup.gui.utilities.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat

object Permissions {
    fun askForManageExternalStoragePermission(activity: ComponentActivity){

        if(checkManageExternalStoragePermission()) return

        val activityResultLauncher = createManageExternalStorageActivityResultLauncher(activity)

        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.setData(uri)

            activityResultLauncher.launch(intent)
        } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            activityResultLauncher.launch(intent)
        }
    }

    private fun checkManageExternalStoragePermission(): Boolean {
        return Environment.isExternalStorageManager()
    }

    private fun createManageExternalStorageActivityResultLauncher(activity: ComponentActivity): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(checkManageExternalStoragePermission()) return@registerForActivityResult
            }
        }
    }

    fun askForNotificationsPermission(activity: ComponentActivity){
        if(checkNotificationsPermission(activity)) return

        val activityResultLauncher = createNotificationsActivityResultLauncher(activity)

        try {
            val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, 5)

            activityResultLauncher.launch(settingsIntent)
        } catch (e: Exception) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            activityResultLauncher.launch(intent)
        }
    }

    private fun checkNotificationsPermission(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun createNotificationsActivityResultLauncher(activity: ComponentActivity): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if(checkNotificationsPermission(activity)) return@registerForActivityResult
            }
        }
    }
}