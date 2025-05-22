package com.storage.cloudbackup.gui.view.adapter.scheme

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.scheme.SchemeActivity
import com.storage.cloudbackup.gui.view.activity.scheme.SchemeActivityArgs
import com.storage.cloudbackup.logic.model.item.scheme.Scheme
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent

class SchemeAdapter(val layoutArgs: SchemeAdapterArgs) :
    RecyclerView.Adapter<SchemeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_scheme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheme: Scheme = layoutArgs.schemes[position]
        loadListView(holder, scheme)

        scheme.propertyChangedListener.add(object : EmptyEvent {
            override fun onInvoke() {
                Handler(Looper.getMainLooper()).post {
                    loadListView(holder, scheme)
                }
            }
        })

        val removeEvent: ArgsEvent<Scheme> = object : ArgsEvent<Scheme> {
            override fun onInvoke(args: Scheme) {
                val index = layoutArgs.schemes.indexOf(args)
                layoutArgs.schemes.remove(args)
                notifyItemRemoved(index)
            }
        }

        holder.schemeLinearLayout.setOnClickListener {
            val args = SchemeActivityArgs(scheme, layoutArgs.cloudProviders, layoutArgs.editPermit, layoutArgs.languagePermit, removeEvent)
            Globals.setData("SchemeActivity.Args", args)

            val intent = Intent(layoutArgs.context, SchemeActivity::class.java)
            layoutArgs.context.startActivity(intent)
        }

        holder.updateButton.isEnabled = layoutArgs.updatePermit.canUpdate
        layoutArgs.updatePermit.updateListener.add(object: EmptyEvent {
            override fun onInvoke() {
                holder.updateButton.isEnabled = layoutArgs.updatePermit.canUpdate
            }
        })

        layoutArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(holder)
            }
        })

        holder.updateButton.setOnClickListener {
            holder.updateButton.isEnabled = false
            layoutArgs.updateEvent.onInvoke(listOf(scheme))
        }

        setLanguage(holder)
    }

    private fun setLanguage(holder: ViewHolder){
        holder.updateButton.text = layoutArgs.languagePermit.currentLanguage.getTranslation("schemeAdapter.updateButton")
    }

    private fun loadListView(holder: ViewHolder, scheme: Scheme) {
        holder.nameTextView.text = scheme.name
        holder.driveFolderPathTextView.text = scheme.driveFolder
    }

    override fun getItemCount(): Int {
        return layoutArgs.schemes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val schemeLinearLayout: LinearLayout
        val nameTextView: TextView
        val driveFolderPathTextView: TextView
        val updateButton: Button

        init {
            schemeLinearLayout = itemView.findViewById(R.id.adapter_scheme_layout)
            nameTextView = itemView.findViewById(R.id.adapter_scheme_nameTextView)
            driveFolderPathTextView = itemView.findViewById(R.id.adapter_scheme_driveFolderPathTextView)
            updateButton = itemView.findViewById(R.id.adapter_scheme_updateButton)
            ViewUtilities.setOnTouchListener(
                ViewUtilities.getAdapterColor(itemView.context),
                schemeLinearLayout,
                itemView.findViewById(R.id.adapter_scheme_subLayout),
                itemView.findViewById(R.id.adapter_scheme_separatorView),
                schemeLinearLayout,
                nameTextView,
                driveFolderPathTextView
            )
        }
    }
}