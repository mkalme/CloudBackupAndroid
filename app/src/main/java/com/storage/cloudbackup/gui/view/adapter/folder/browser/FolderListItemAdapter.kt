package com.storage.cloudbackup.gui.view.adapter.folder.browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import java.nio.file.Paths

class FolderListItemAdapter(private val folderClickListener : ArgsEvent<String>) : RecyclerView.Adapter<FolderListItemAdapter.FolderViewHolder>() {
    val folders = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        loadListView(holder, folder)

        holder.folderLinearLayout.setOnClickListener {
            folderClickListener.onInvoke (
                folder
            )
        }
    }

    private fun loadListView(holder: FolderViewHolder, folder: String) {
        holder.folderTextView.text = Paths.get(folder).fileName.toString()
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val folderLinearLayout: LinearLayout
        val folderTextView: TextView

        init {
            folderLinearLayout = itemView.findViewById(R.id.listItem_folder_layout)
            folderTextView = itemView.findViewById(R.id.adapter_folder_textView)

            val imageView = itemView.findViewById<ImageView>(R.id.adapter_folder_imageView)
            ViewUtilities.setOnTouchListener(ViewUtilities.getAdapterColor(itemView.context), folderLinearLayout, imageView, folderTextView)
        }
    }
}