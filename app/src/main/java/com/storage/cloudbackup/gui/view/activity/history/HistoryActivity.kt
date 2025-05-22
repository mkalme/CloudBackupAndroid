package com.storage.cloudbackup.gui.view.activity.history

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.adapter.update.attempt.UpdateAttemptAdapter
import com.storage.cloudbackup.gui.view.adapter.update.attempt.UpdateAttemptAdapterArgs
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.shared.utilities.observable.collections.ObservableMutableList
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class HistoryActivity : AppCompatActivity() {
    private lateinit var activityArgs: HistoryActivityArgs
    private lateinit var attempts: ObservableMutableList<UpdateAttempt>

    private lateinit var adapter: UpdateAttemptAdapter

    private var clear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("HistoryActivity.Args") as HistoryActivityArgs
        attempts = ObservableMutableList(activityArgs.updateAttempts.toMutableList())

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent(){
        initRecyclerView()

        activityArgs.editPermit.editListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setEditable(activityArgs.editPermit.canEdit)
                setEditableLanguage(activityArgs.editPermit.canEdit)
            }
        })

        activityArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(activityArgs.editPermit.canEdit)
            }
        })
    }

    private fun initRecyclerView() {
        adapter = UpdateAttemptAdapter(UpdateAttemptAdapterArgs(attempts, activityArgs.cloudProviders, activityArgs.schemeProvider, activityArgs.languagePermit, this))

        val view = findViewById<RecyclerView>(R.id.activity_history_recyclerView)
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
    }

    private fun setInputs() {
        setLanguage(activityArgs.editPermit.canEdit)
        setEditable(activityArgs.editPermit.canEdit)
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        findViewById<Button>(R.id.activity_history_saveButton).text = languagePack.getTranslation("updateHistoryActivity.saveButton")
        findViewById<Button>(R.id.activity_history_clearHistoryButton).text = languagePack.getTranslation("updateHistoryActivity.clearHistoryButton")

        setEditableLanguage(editable)
    }

    private fun setEditableLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        supportActionBar?.title = LanguageUtilities.translateEditable("updateHistoryActivity.title", editable, languagePack)

        findViewById<Button>(R.id.activity_history_cancelButton).text = LanguageUtilities.translateEditable("updateHistoryActivity.cancelButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        findViewById<View>(R.id.activity_history_saveButton).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.activity_history_separator).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.activity_history_clearHistoryButton).visibility = if (editable) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun clearHistory(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        ViewUtilities.showDeleteConfirmationDialog("updateHistoryActivity.clearPrompt", activityArgs.languagePermit, supportFragmentManager, object: EmptyEvent {
            override fun onInvoke() {
                clear = true
                attempts.clear()
            }
        })
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        if(!activityArgs.editPermit.canEdit) return

        if(clear) activityArgs.clearEvent.onInvoke()

        finish()
    }

    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}