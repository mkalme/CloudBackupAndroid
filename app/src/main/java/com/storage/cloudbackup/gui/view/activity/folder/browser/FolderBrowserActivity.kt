package com.storage.cloudbackup.gui.view.activity.folder.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.view.adapter.folder.browser.FolderListItemAdapter
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent as GenericEvent1
import java.nio.file.Paths

class FolderBrowserActivity : AppCompatActivity() {
    private lateinit var currentFolder: String
    private lateinit var activityArgs: FolderBrowserActivityArgs

    private lateinit var currentFolderTextView: TextView
    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var adapter: FolderListItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_browser)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityArgs = Globals.getData("FolderBrowserActivity.Args") as FolderBrowserActivityArgs

        initializeComponent()
        openStartingFolder()
    }

    private fun initializeComponent(){
        currentFolderTextView = findViewById(R.id.activity_folder_browser_folder_currentFolderTextView)
        folderRecyclerView = findViewById(R.id.activity_folder_browser_folderRecyclerView)

        initializeRecyclerView()
        setLanguage()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedCallback()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        activityArgs.languagePermit.languageChangedListener.add(object : EmptyEvent {
            override fun onInvoke() {
                setLanguage()
            }
        })
    }

    private fun initializeRecyclerView() {
        val folderClickEvent = object : GenericEvent1<String> {
            override fun onInvoke(args: String) {
                setFolder(args, activityArgs.folderExplorer.openFolder(args))
            }
        }

        adapter = FolderListItemAdapter(folderClickEvent)
        folderRecyclerView.adapter = adapter
        folderRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setLanguage(){
        val languagePack = activityArgs.languagePermit.currentLanguage

        supportActionBar?.title = languagePack.getTranslation("folderBrowserActivity.title")
        findViewById<Button>(R.id.activity_folder_browser_selectCurrentFolderButton).text = languagePack.getTranslation("folderBrowserActivity.selectCurrentFolderButton")
        findViewById<Button>(R.id.activity_folder_browser_closeButton).text = languagePack.getTranslation("folderBrowserActivity.closeButton")
    }

    private fun openStartingFolder(){
        val children = activityArgs.folderExplorer.openFolder(activityArgs.currentFolder)

        if(children.isNullOrEmpty()){
            val pair = activityArgs.folderExplorer.openRootFolder(activityArgs.folderExplorer.modes[0].id)
            if(pair != null) setFolder(pair.second, pair.first)
        }else{
            setFolder(activityArgs.currentFolder, children)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.folder_browser_toolbar_menu, menu)

        menu?.clear()
        activityArgs.folderExplorer.modes.forEach{
            menu?.add(Menu.NONE, it.id, Menu.NONE, it.displayText)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pair = activityArgs.folderExplorer.openRootFolder(item.itemId)
        if(pair != null) setFolder(pair.second, pair.first)

        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFolder(path: String, children: List<String>?) {
        currentFolder = path
        currentFolderTextView.text = currentFolder

        adapter.folders.clear()
        if (!children.isNullOrEmpty()) {
            adapter.folders.addAll(children)
        }

        adapter.notifyDataSetChanged()
    }

    private fun openParent() {
        val parentFolder = Paths.get(currentFolder).parent.toString()
        val siblings = activityArgs.folderExplorer.openFolder(parentFolder) ?: return

        setFolder(parentFolder, siblings)
    }

    override fun onSupportNavigateUp(): Boolean {
        openParent()
        return false
    }

    private fun onBackPressedCallback(){
        openParent()
    }

    fun selectCurrentFolder(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        activityArgs.folderSelectedListener.onInvoke(currentFolder)
        finish()
    }

    fun close(@Suppress("UNUSED_PARAMETER") ignoredView: View?) {
        finish()
    }
}