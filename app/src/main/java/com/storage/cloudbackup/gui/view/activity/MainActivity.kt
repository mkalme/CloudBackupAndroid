package com.storage.cloudbackup.gui.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.SharedApp
import com.storage.cloudbackup.gui.service.UpdateEvent
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.Permissions
import com.storage.cloudbackup.gui.view.activity.history.HistoryActivity
import com.storage.cloudbackup.gui.view.activity.history.HistoryActivityArgs
import com.storage.cloudbackup.gui.view.activity.settings.SettingsActivity
import com.storage.cloudbackup.gui.view.activity.settings.SettingsActivityArgs
import com.storage.cloudbackup.gui.view.activity.updateplan.UpdatePlanActivity
import com.storage.cloudbackup.gui.view.activity.updateplan.UpdatePlanActivityArgs
import com.storage.cloudbackup.gui.view.adapter.updateplan.UpdatePlanAdapter
import com.storage.cloudbackup.gui.view.adapter.updateplan.UpdatePlanAdapterArgs
import com.storage.cloudbackup.gui.view.layout.image.button.ImageButtonLayout
import com.storage.cloudbackup.gui.view.layout.image.button.ImageButtonLayoutArgs
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.permit.update.StandardUpdatePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.utilities.provider.GenericArgsProvider
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UpdatePlanAdapter
    private lateinit var app: SharedApp
    private var updateEvent: ArgsEvent<List<Scheme>> = UpdateEvent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = SharedApp.getInstance(this)
        supportActionBar?.hide()

        Permissions.askForManageExternalStoragePermission(this)
        Permissions.askForNotificationsPermission(this)

        initializeComponent()

        setLanguage()
        setEditable(app.editPermit.canEdit)
    }

    private fun initializeComponent(){
        val layoutContainer = findViewById<LinearLayout>(R.id.activity_main_cloudProviderLayout)
        app.cloudProviders.forEach {
            val context = this

            val clickEvent = object: EmptyEvent {
                override fun onInvoke() {
                    val args = com.storage.cloudbackup.gui.view.activity.cloud.provider.SettingsActivityArgs(it, app.editPermit, app.languagePermit)
                    Globals.setData("CloudProviderSettingsActivity.Args", args)

                    val intent = Intent(context, com.storage.cloudbackup.gui.view.activity.cloud.provider.SettingsActivity::class.java)
                    context.startActivity(intent)
                }
            }

            val layout = ImageButtonLayout(ImageButtonLayoutArgs(it.logicComponent.info.icon, clickEvent, this))
            layoutContainer.addView(layout)

            if(it != app.cloudProviders.first()) {
                val layoutParams = layout.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(12, 0, 0, 0)
                layout.layoutParams = layoutParams
            }
        }

        initRecyclerView()

        app.editPermit.editListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setEditable(app.editPermit.canEdit)
            }
        })

        app.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage()
                setEditable(app.editPermit.canEdit)
            }
        })
    }

    private fun initRecyclerView() {
        val view = findViewById<RecyclerView>(R.id.activity_main_schemeRecyclerView)
        adapter = UpdatePlanAdapter(
            UpdatePlanAdapterArgs(
                app.updatePlanContainerResource.updatePlanContainer.updatePlans,
                app.cloudProviders,
                updateEvent,
                app.updatePermit,
                app.editPermit,
                app.languagePermit,
                this
            )
        )

        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(this)
    }

    private fun setLanguage(){
        val languagePack = app.settingsResource.settings.getLanguagePack()

        findViewById<TextView>(R.id.mainActivity_toolbar_textView).text = languagePack.getTranslation("mainActivity.title")
        findViewById<Button>(R.id.activity_main_openSettingsButton).text = languagePack.getTranslation("mainActivity.openSettingsButton")
        findViewById<Button>(R.id.activity_main_openHistoryButton).text = languagePack.getTranslation("mainActivity.openHistoryButton")
        findViewById<Button>(R.id.activity_main_addNewUpdatePlanButton).text = languagePack.getTranslation("mainActivity.addNewUpdatePlanButton")
    }

    private fun setEditable(editable: Boolean){
        findViewById<View>(R.id.activity_main_addNewUpdatePlanButton).isEnabled = editable
    }

    fun openSettings(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val args = SettingsActivityArgs(app.settingsResource.settings, app.mobileDataUsageResource.mobileDataUsage.bytesUsage.toULong())
        Globals.setData("SettingsActivity.Args", args)

        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun openHistory(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val updateAttempts = mutableListOf<UpdateAttempt>()
        app.historyResource.schemeHistoryContainer.schemes.forEach {
            updateAttempts.addAll(it.updateAttempts)
        }

        updateAttempts.sortByDescending { it.begin }

        val schemeProvider = object : GenericArgsProvider<UUID, Scheme?> {
            override fun provide(key: UUID): Scheme? {
                for (i in 0 until app.updatePlanContainerResource.updatePlanContainer.updatePlans.size){
                    for(j in 0 until app.updatePlanContainerResource.updatePlanContainer.updatePlans[i].schemes.size){
                        val scheme = app.updatePlanContainerResource.updatePlanContainer.updatePlans[i].schemes[j]
                        if(scheme.id == key) return scheme
                    }
                }

                return null
            }
        }

        val clearEvent = object : EmptyEvent {
            override fun onInvoke() {
                app.historyResource.schemeHistoryContainer.invokeWithSingleEventTrigger {
                    app.historyResource.schemeHistoryContainer.schemes.forEach {
                        it.updateAttempts.clear()
                    }
                }
            }
        }

        val args = HistoryActivityArgs(updateAttempts, app.cloudProviders, schemeProvider, app.editPermit, app.languagePermit, clearEvent)
        Globals.setData("HistoryActivity.Args", args)

        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun addNewUpdatePlan(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        val updatePlan = UpdatePlan()
        updatePlan.name = app.settingsResource.settings.getLanguagePack().getTranslation("updatePlan.new")

        app.updatePlanContainerResource.updatePlanContainer.updatePlans.add(updatePlan)
        adapter.notifyItemInserted(app.updatePlanContainerResource.updatePlanContainer.updatePlans.size - 1)

        val args = UpdatePlanActivityArgs(updatePlan,
            app.cloudProviders,
            updateEvent,
            StandardUpdatePermit(app.updatePermit, updatePlan),
            app.editPermit,
            app.languagePermit,
            this)
        Globals.setData("UpdatePlanActivity.Args", args)

        val intent = Intent(this, UpdatePlanActivity::class.java)
        startActivity(intent)
    }
}