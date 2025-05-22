package com.storage.cloudbackup.gui.view.adapter.updateplan

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.utilities.Globals
import com.storage.cloudbackup.gui.utilities.view.ViewUtilities
import com.storage.cloudbackup.gui.view.activity.updateplan.UpdatePlanActivity
import com.storage.cloudbackup.gui.view.activity.updateplan.UpdatePlanActivityArgs
import com.storage.cloudbackup.logic.model.item.updateplan.UpdatePlan
import com.storage.cloudbackup.logic.permit.update.StandardUpdatePermit
import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.EmptyEvent
import java.io.File

class UpdatePlanAdapter(private val args: UpdatePlanAdapterArgs) :
    RecyclerView.Adapter<UpdatePlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_update_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val updatePlan: UpdatePlan = args.updatePlans[position]
        val updatePermit = StandardUpdatePermit(args.shallowUpdatePermit, updatePlan)

        loadListView(holder, updatePlan)
        updatePlan.propertyChangedListener.add(object : EmptyEvent {
            override fun onInvoke() {
                Handler(Looper.getMainLooper()).post {
                    loadListView(holder, updatePlan)
                }
            }
        })

        args.updatePlans.collectionChangedListener.add(object: CollectionChangedEvent<UpdatePlan> {
            override fun onInvoke(removed: Collection<UpdatePlan?>, added: Collection<UpdatePlan?>) {
                if(removed.contains(updatePlan)){
                    args.updatePlans.remove(updatePlan)
                    notifyItemRemoved(holder.adapterPosition)
                }
            }
        })

        holder.updatePlanLinearLayout.setOnClickListener {
            val args = UpdatePlanActivityArgs(updatePlan, args.cloudProviders, args.updateEvent, updatePermit, args.editPermit, args.languagePermit, args.context)
            Globals.setData("UpdatePlanActivity.Args", args)

            val intent = Intent(args.context, UpdatePlanActivity::class.java)
            args.context.startActivity(intent)
        }

        holder.updateButton.isEnabled = updatePermit.canUpdate
        updatePermit.updateListener.add(object: EmptyEvent {
            override fun onInvoke() {
                holder.updateButton.isEnabled = updatePermit.canUpdate
            }
        })

        args.languagePermit.languageChangedListener.add(object: EmptyEvent {
            override fun onInvoke() {
                setLanguage(holder)
            }
        })

        holder.updateButton.setOnClickListener {
            holder.updateButton.isEnabled = false
            args.updateEvent.onInvoke(updatePlan.schemes)
        }

        setLanguage(holder)
    }

    private fun setLanguage(holder: ViewHolder){
        holder.updateButton.text = args.languagePermit.currentLanguage.getTranslation("updatePlanAdapter.updateButton")
    }

    private fun loadListView(holder: ViewHolder, updatePlan: UpdatePlan) {
        holder.nameTextView.text = updatePlan.name

        val length = 35
        val path: String = File(updatePlan.folder).path

        holder.folderPathTextView.text = if (path.length <= length) path else "..." + path.substring(path.length - length + 3)

        if(updatePlan.cloudProvider != null) holder.icon.setImageResource(updatePlan.cloudProvider?.logicComponent?.info?.icon!!)
        else holder.icon.setImageResource(0)
    }

    override fun getItemCount(): Int {
        return args.updatePlans.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val updatePlanLinearLayout: LinearLayout
        val nameTextView: TextView
        val folderPathTextView: TextView
        val icon: ImageView
        val updateButton: Button

        init {
            updatePlanLinearLayout = itemView.findViewById(R.id.adapter_update_plan_layout)
            nameTextView = itemView.findViewById(R.id.adapter_update_plan_nameTextView)
            folderPathTextView = itemView.findViewById(R.id.adapter_update_plan_folderPathTextView)
            icon = itemView.findViewById(R.id.adapter_update_plan_icon)
            updateButton = itemView.findViewById(R.id.adapter_update_plan_updateButton)

            ViewUtilities.setOnTouchListener(
                ViewUtilities.getAdapterColor(itemView.context),
                updatePlanLinearLayout,
                itemView.findViewById(R.id.adapter_update_plan_subLayout),
                itemView.findViewById(R.id.adapter_update_plan_separatorView),
                nameTextView,
                icon,
                folderPathTextView
            )
        }
    }
}