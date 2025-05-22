package com.storage.cloudbackup.gui.view.activity.updateplan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.LanguageUtilities
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.folder.browser.FolderBrowserActivity
import com.storage.cloudbackup.gui.view.activity.folder.browser.FolderBrowserActivityArgs
import com.storage.cloudbackup.gui.view.activity.folder.browser.explorer.LocalFolderExplorer
import com.storage.cloudbackup.gui.view.activity.scheme.SchemeActivity
import com.storage.cloudbackup.gui.view.activity.scheme.SchemeActivityArgs
import com.storage.cloudbackup.gui.view.adapter.scheme.SchemeAdapter
import com.storage.cloudbackup.gui.view.adapter.scheme.SchemeAdapterArgs
import com.storage.cloudbackup.gui.view.layout.cloud.provider.selector.SelectorLayout
import com.storage.cloudbackup.gui.view.layout.cloud.provider.selector.SelectorLayoutArgs
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class UpdatePlanActivity : AppCompatActivity() {
    private lateinit var activityArgs : UpdatePlanActivityArgs
    private val schemes: ArrayList<Scheme> = ArrayList()

    private lateinit var nameEditText: EditText
    private lateinit var folderPathEditText: EditText
    private lateinit var adapter: SchemeAdapter
    private lateinit var removeButton: Button

    private var cloudProvider: CloudProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_plan)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("UpdatePlanActivity.Args") as UpdatePlanActivityArgs
        activityArgs.updatePlan.schemes.forEach {
            schemes.add(it.clone())
        }

        initializeComponent()
        setInputs()
    }

    private fun initializeComponent(){
        nameEditText = findViewById(R.id.activity_update_plan_nameText)
        folderPathEditText = findViewById(R.id.activity_update_plan_folderText)
        removeButton = findViewById(R.id.activity_update_plan_removeThisButton)

        cloudProvider = activityArgs.updatePlan.cloudProvider

        val selectionEvent = object : ArgsEvent<CloudProvider?> {
            override fun onInvoke(args: CloudProvider?) {
                cloudProvider = args
            }
        }

        val cloudProviderSelector = SelectorLayout(SelectorLayoutArgs(activityArgs.cloudProviders, activityArgs.editPermit, selectionEvent, this))

        cloudProviderSelector.selectCloudProvider(cloudProvider)
        findViewById<LinearLayout>(R.id.activity_update_plan_cloudProviderSelectorContainer).addView(cloudProviderSelector)

        initializeRecyclerView()

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

    private fun initializeRecyclerView() {
        adapter = SchemeAdapter(SchemeAdapterArgs(schemes, activityArgs.updateEvent, activityArgs.cloudProviders, activityArgs.updatePermit, activityArgs.editPermit, activityArgs.languagePermit, this))

        val view = findViewById<RecyclerView>(R.id.activity_update_plan_schemeRecyclerView)
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
    }

    private fun setInputs() {
        nameEditText.setText(activityArgs.updatePlan.name)
        folderPathEditText.setText(activityArgs.updatePlan.folder)

        setLanguage(activityArgs.editPermit.canEdit)
        setEditable(activityArgs.editPermit.canEdit)
    }

    private fun setLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        findViewById<TextInputLayout>(R.id.activity_update_plan_nameTextLayout).hint = languagePack.getTranslation("updatePlanActivity.nameTextHint")
        findViewById<TextInputLayout>(R.id.activity_update_plan_folderTextLayout).hint = languagePack.getTranslation("updatePlanActivity.folderTextHint")
        findViewById<Button>(R.id.activity_update_plan_browseButton).text = languagePack.getTranslation("updatePlanActivity.browseButton")
        findViewById<Button>(R.id.activity_update_plan_addNewSchemeButton).text = languagePack.getTranslation("updatePlanActivity.addNewSchemeButton")
        findViewById<Button>(R.id.activity_update_plan_saveButton).text = languagePack.getTranslation("updatePlanActivity.saveButton")
        findViewById<Button>(R.id.activity_update_plan_removeThisButton).text = languagePack.getTranslation("updatePlanActivity.removeThisButton")

        setEditableLanguage(editable)
    }
    private fun setEditableLanguage(editable: Boolean){
        val languagePack = activityArgs.languagePermit.currentLanguage

        supportActionBar?.title = LanguageUtilities.translateEditable("updatePlanActivity.title", editable, languagePack)
        findViewById<Button>(R.id.activity_update_plan_cancelButton).text = LanguageUtilities.translateEditable("updatePlanActivity.cancelButton", editable, languagePack)
    }

    private fun setEditable(editable: Boolean) {
        nameEditText.isEnabled = editable
        folderPathEditText.isEnabled = editable
        findViewById<View>(R.id.activity_update_plan_browseButton).isEnabled = editable
        findViewById<View>(R.id.activity_update_plan_addNewSchemeButton).isEnabled = editable

        removeButton.isEnabled = editable
        removeButton.visibility = if (editable) View.VISIBLE else View.INVISIBLE

        findViewById<View>(R.id.activity_update_plan_saveButton).visibility = if (editable) View.VISIBLE else View.GONE
        findViewById<View>(R.id.activity_update_plan_separator).visibility = if (editable) View.VISIBLE else View.GONE
    }

    fun browse(view: View) {
        val event: ArgsEvent<String> = object : ArgsEvent<String> {
            override fun onInvoke(args: String) {
                folderPathEditText.setText(args)
            }
        }

        val args = FolderBrowserActivityArgs(LocalFolderExplorer(activityArgs.languagePermit.currentLanguage, this), folderPathEditText.text.toString(), activityArgs.languagePermit, event)
        Globals.setData("FolderBrowserActivity.Args", args)

        val intent = Intent(view.context, FolderBrowserActivity::class.java)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun addNewScheme(view: View) {
        val scheme = Scheme()
        scheme.name = activityArgs.languagePermit.currentLanguage.getTranslation("scheme.new")
        scheme.owner = activityArgs.updatePlan

        schemes.add(scheme)
        adapter.notifyItemInserted(schemes.size - 1)
        val removedEvent: ArgsEvent<Scheme> = object : ArgsEvent<Scheme> {
            override fun onInvoke(args: Scheme) {
                val index = schemes.indexOf(args)
                schemes.remove(args)
                adapter.notifyItemRemoved(index)
            }
        }

        val args = SchemeActivityArgs(scheme, activityArgs.cloudProviders, activityArgs.editPermit, activityArgs.languagePermit, removedEvent)
        Globals.setData("SchemeActivity.Args", args)

        val intent = Intent(view.context, SchemeActivity::class.java)
        view.context.startActivity(intent)
    }

    fun save(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        if (!activityArgs.editPermit.canEdit) {
            finish()
            return
        }

        activityArgs.updatePlan.invokeWithSingleEventTrigger {
            activityArgs.updatePlan.name = nameEditText.text.toString()
            activityArgs.updatePlan.folder = folderPathEditText.text.toString()

            activityArgs.updatePlan.schemes.clear()
            schemes.forEach {
                activityArgs.updatePlan.schemes.add(it)
            }

            activityArgs.updatePlan.cloudProvider = cloudProvider
        }

        finish()
    }

    fun cancel(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }

    fun removeThis(@Suppress("UNUSED_PARAMETER") ignoredView: View) {
        ViewUtilities.showDeleteConfirmationDialog("updatePlanActivity.removePrompt", activityArgs.languagePermit, supportFragmentManager, object: EmptyEvent {
            override fun onInvoke() {
                activityArgs.updatePlan.owner?.updatePlans?.remove(activityArgs.updatePlan)
                finish()
            }
        })
    }
}