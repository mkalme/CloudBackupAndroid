package com.storage.cloudbackup.gui.view.adapter.update.attempt

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.update.attempt.UpdateAttemptActivity
import com.storage.cloudbackup.gui.view.activity.update.attempt.UpdateAttemptActivityArgs
import com.storage.cloudbackup.logic.model.item.history.attempt.UpdateAttempt
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import java.time.format.DateTimeFormatter

class UpdateAttemptAdapter(val layoutArgs: UpdateAttemptAdapterArgs) :
    RecyclerView.Adapter<UpdateAttemptAdapter.ViewHolder>() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutArgs.updateAttempts.collectionChangedListener.add(object: CollectionChangedEvent<UpdateAttempt> {
            override fun onInvoke(removed: Collection<UpdateAttempt?>, added: Collection<UpdateAttempt?>) {
                removed.forEach {
                    val index = layoutArgs.updateAttempts.indexOf(it)
                    notifyItemRemoved(index)
                }
            }
        })

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_update_attempt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attempt = layoutArgs.updateAttempts[position]
        loadListView(holder, attempt)

        holder.linearLayout.setOnClickListener {
            val args = UpdateAttemptActivityArgs(attempt, layoutArgs.languagePermit)
            Globals.setData("UpdateAttemptActivity.Args", args)

            val intent = Intent(layoutArgs.context, UpdateAttemptActivity::class.java)
            layoutArgs.context.startActivity(intent)
        }

        layoutArgs.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                loadListView(holder, attempt)
            }
        })
    }

    private fun loadListView(holder: ViewHolder, attempt: UpdateAttempt) {
        val scheme = if(attempt.schemeId != null) layoutArgs.schemeProvider.provide(attempt.schemeId!!)
        else null

        val nameDisplayText = "${scheme?.owner?.name} / ${scheme?.name}"
        holder.nameTextView.text = nameDisplayText

        val timeDisplayText = "${attempt.begin.format(formatter)}\t\t${attempt.driveDirectory}"
        holder.timeTextView.text = timeDisplayText

        holder.icon.setImageResource(layoutArgs.cloudProviders.firstOrNull{ it.logicComponent.info.id == attempt.cloudProviderId }!!.logicComponent.info.icon)
    }

    override fun getItemCount(): Int {
        return layoutArgs.updateAttempts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout
        val nameTextView: TextView
        val timeTextView: TextView
        val icon: ImageView

        init {
            linearLayout = itemView.findViewById(R.id.adapter_update_attempt_layout)
            nameTextView = itemView.findViewById(R.id.adapter_update_attempt_nameTextView)
            timeTextView = itemView.findViewById(R.id.adapter_update_attempt_timeTextView)
            icon = itemView.findViewById(R.id.adapter_update_attempt_icon)
            val separator = itemView.findViewById<View>(R.id.adapter_update_attempt_separator)

            ViewUtilities.setOnTouchListener(
                ViewUtilities.getAdapterColor(itemView.context),
                linearLayout,
                linearLayout,
                separator,
                nameTextView,
                timeTextView,
                icon
            )
        }
    }
}