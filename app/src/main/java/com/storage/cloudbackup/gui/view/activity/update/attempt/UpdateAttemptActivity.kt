package com.storage.cloudbackup.gui.view.activity.update.attempt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivity
import com.storage.cloudbackup.gui.view.activity.text.editor.TextEditorActivityArgs
import com.storage.cloudbackup.logic.model.item.history.attempt.AttemptResult
import com.storage.cloudbackup.logic.permit.edit.EmptyEditPermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import java.time.format.DateTimeFormatter

class UpdateAttemptActivity : AppCompatActivity() {
    private lateinit var activityArgs : UpdateAttemptActivityArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_attempt)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("UpdateAttemptActivity.Args") as UpdateAttemptActivityArgs

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent(){
        activityArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage()
            }
        })
    }

    private fun setInputs() {
        setLanguage()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        findViewById<EditText>(R.id.activity_update_attempt_localFolderText).setText(activityArgs.updateAttempt.localDirectory)
        findViewById<EditText>(R.id.activity_update_attempt_driveFolderText).setText(activityArgs.updateAttempt.driveDirectory)
        findViewById<EditText>(R.id.activity_update_attempt_beginUpdateText).setText(activityArgs.updateAttempt.begin.format(formatter))
        findViewById<EditText>(R.id.activity_update_attempt_finishedUpdateText).setText(activityArgs.updateAttempt.finished.format(formatter))
        findViewById<EditText>(R.id.activity_update_attempt_successfulFilesText).setText(activityArgs.updateAttempt.filesUploaded.size.toString())
        findViewById<EditText>(R.id.activity_update_attempt_unsuccessfulFilesText).setText(activityArgs.updateAttempt.unsuccessfulFilesUploaded.size.toString())

        findViewById<Button>(R.id.activity_update_attempt_viewErrorsButton).visibility = if(activityArgs.updateAttempt.schemeErrorMessages.isEmpty()) View.INVISIBLE else View.VISIBLE
    }

    private fun setLanguage(){
        val languagePack = activityArgs.languagePermit.currentLanguage

        supportActionBar?.title = languagePack.getTranslation("updateAttemptActivity.title")

        findViewById<TextInputLayout>(R.id.activity_update_attempt_localFolderHint).hint = languagePack.getTranslation("updateAttemptActivity.localFolderTextHint")
        findViewById<TextInputLayout>(R.id.activity_update_attempt_driveFolderHint).hint = languagePack.getTranslation("updateAttemptActivity.driveFolderTextHint")
        findViewById<TextView>(R.id.activity_update_attempt_beginUpdateTextView).text = languagePack.getTranslation("updateAttemptActivity.updateBeginTextView")
        findViewById<TextView>(R.id.activity_update_attempt_finishedUpdateTextView).text = languagePack.getTranslation("updateAttemptActivity.updateFinishedTextView")
        findViewById<TextView>(R.id.activity_update_attempt_typeTextView).text = languagePack.getTranslation("updateAttemptActivity.typeTextView")
        findViewById<TextView>(R.id.activity_update_attempt_resultTextView).text = languagePack.getTranslation("updateAttemptActivity.resultTextView")
        findViewById<TextView>(R.id.activity_update_attempt_successfulFilesTextView).text = languagePack.getTranslation("updateAttemptActivity.successfulFilesTextView")
        findViewById<Button>(R.id.activity_update_attempt_successfulFilesButton).text = languagePack.getTranslation("updateAttemptActivity.successfulFilesButton")
        findViewById<TextView>(R.id.activity_update_attempt_unsuccessfulFilesTextView).text = languagePack.getTranslation("updateAttemptActivity.unsuccessfulFilesTextView")
        findViewById<Button>(R.id.activity_update_attempt_unsuccessfulFilesButton).text = languagePack.getTranslation("updateAttemptActivity.unsuccessfulFilesButton")
        findViewById<Button>(R.id.activity_update_attempt_viewErrorsButton).text = languagePack.getTranslation("updateAttemptActivity.errorButton")

        val typeKey = if(activityArgs.updateAttempt.isAutomatic) "updateAttemptActivity.type.automatic" else "updateAttemptActivity.type.userInitiated"
        findViewById<EditText>(R.id.activity_update_attempt_typeText).setText(languagePack.getTranslation(typeKey))

        val resultKey = when(activityArgs.updateAttempt.result) {
            AttemptResult.Successful -> "updateAttemptActivity.result.successful"
            AttemptResult.Unsuccessful -> "updateAttemptActivity.result.unsuccessful"
            AttemptResult.PartiallySuccessful -> "updateAttemptActivity.result.partiallySuccessful"
            AttemptResult.Unfinished -> "updateAttemptActivity.result.unfinished"
        }
        findViewById<EditText>(R.id.activity_update_attempt_resultText).setText(languagePack.getTranslation(resultKey))

        findViewById<Button>(R.id.activity_update_attempt_closeButton).text = languagePack.getTranslation("updateAttemptActivity.closeButton")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun seeSuccessfulFiles(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val title = activityArgs.languagePermit.currentLanguage.getTranslation("updateAttemptActivity.successfulFilesView.title")
        val text = joinStringList(activityArgs.updateAttempt.filesUploaded)

        val args = TextEditorActivityArgs(text, title, EmptyEditPermit(false), activityArgs.languagePermit, object: ArgsEvent<String> {
            override fun onInvoke(args: String) {

            }
        })
        Globals.setData("TextEditorActivity.Args", args)

        val intent = Intent(this, TextEditorActivity::class.java)
        startActivity(intent)
    }

    private fun joinStringList(list: List<String>): String{
        val builder = StringBuilder()
        list.forEach {
            builder.append("○  ")
            builder.appendLine(it)
        }

        return builder.toString()
    }

    fun seeUnsuccessfulFiles(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val title = activityArgs.languagePermit.currentLanguage.getTranslation("updateAttemptActivity.unsuccessfulFilesView.title")
        val text = joinStringList(activityArgs.updateAttempt.unsuccessfulFilesUploaded)

        val args = TextEditorActivityArgs(text, title, EmptyEditPermit(false), activityArgs.languagePermit, object: ArgsEvent<String> {
            override fun onInvoke(args: String) {

            }
        })
        Globals.setData("TextEditorActivity.Args", args)

        val intent = Intent(this, TextEditorActivity::class.java)
        startActivity(intent)
    }

    fun viewErrorsButton(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val languagePack = activityArgs.languagePermit.currentLanguage

        val translated = mutableListOf<String>()
        activityArgs.updateAttempt.schemeErrorMessages.forEach {
            val translation = ViewUtilities.translateEnum("update.error", languagePack, it)
            translated.add(if(translation.trim().isEmpty()) it else translation)
        }

        val text = joinStringList(translated)
        val title = languagePack.getTranslation("updateAttemptActivity.errorView.title")

        val args = TextEditorActivityArgs(text, title, EmptyEditPermit(false), activityArgs.languagePermit, object: ArgsEvent<String> {
            override fun onInvoke(args: String) {

            }
        })
        Globals.setData("TextEditorActivity.Args", args)

        val intent = Intent(this, TextEditorActivity::class.java)
        startActivity(intent)
    }

    fun close(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}