package com.storage.cloudbackup.gui.view.layout.cloud.provider.selector

import android.view.View
import android.widget.LinearLayout
import com.storage.cloudbackup.R
import com.storage.cloudbackup.gui.view.layout.cloud.provider.radiobox.RadioBoxEventArgs
import com.storage.cloudbackup.gui.view.layout.cloud.provider.radiobox.RadioBoxLayout
import com.storage.cloudbackup.gui.view.layout.cloud.provider.radiobox.RadioBoxLayoutArgs
import com.storage.cloudbackup.logic.cloud.provider.model.CloudProvider
import com.storage.cloudbackup.logic.shared.utilities.observable.events.generic.ArgsEvent

class SelectorLayout(val layoutArgs: SelectorLayoutArgs) : LinearLayout(layoutArgs.context)  {
    private val providers: MutableList<RadioBoxLayout> = mutableListOf()

    init {
        initializeComponent()
    }

    private fun initializeComponent(){
        View.inflate(context, R.layout.layout_cloud_provider_selector, this)

        layoutArgs.cloudProviders.forEach {
            val listener = createListener(it)

            val layout = RadioBoxLayout(RadioBoxLayoutArgs(it, layoutArgs.editPermit, listener, layoutArgs.context))
            findViewById<LinearLayout>(R.id.layout_cloud_provider_selector_layout).addView(layout)

            if(it != layoutArgs.cloudProviders.first()){
                val layoutParams = layout.layoutParams as MarginLayoutParams
                layoutParams.setMargins(12, 0, 0, 0)
                layout.layoutParams = layoutParams
            }

            providers.add(layout)
        }
    }

    private fun createListener(cloudProvider: CloudProvider) : ArgsEvent<RadioBoxEventArgs> {
        val listener = object: ArgsEvent<RadioBoxEventArgs> {
            override fun onInvoke(args: RadioBoxEventArgs) {
                if(args.isSelected){
                    providers.forEach {
                        if(it.layoutArgs.cloudProvider.logicComponent.info.id != cloudProvider.logicComponent.info.id) it.setIsProviderSelected(
                            isSelected = false,
                            auto = true
                        )
                    }
                }

                if(!args.auto) layoutArgs.selectionEvent.onInvoke(if(args.isSelected) cloudProvider else null)
            }
        }

        return listener
    }

    fun selectCloudProvider(cloudProvider: CloudProvider?){
        providers.forEach {
            if(cloudProvider == null) it.setIsProviderSelected(isSelected = false, auto = false)
            else if(it.layoutArgs.cloudProvider.logicComponent.info.id == cloudProvider.logicComponent.info.id) it.setIsProviderSelected(
                isSelected = true,
                auto = false
            )
        }
    }
}