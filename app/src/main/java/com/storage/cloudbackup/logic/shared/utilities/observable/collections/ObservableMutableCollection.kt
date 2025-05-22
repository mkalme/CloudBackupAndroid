package com.storage.cloudbackup.logic.shared.utilities.observable.collections

import com.storage.cloudbackup.logic.shared.utilities.observable.events.CollectionChangedEvent

open class ObservableMutableCollection<T>(collection: MutableCollection<T>) : MutableCollection<T> {

    private val internalCollection: MutableCollection<T> = collection
    val collectionChangedListener: MutableList<CollectionChangedEvent<T>> = mutableListOf()

    protected fun invokeListener(removed: T?, added: T?){
        collectionChangedListener.forEach{
            it.onInvoke(listOf(removed), listOf(added))
        }
    }
    protected fun invokeListener(removed: Collection<T>, added: Collection<T>){
        collectionChangedListener.forEach{
            it.onInvoke(removed, added)
        }
    }

    override fun iterator(): MutableIterator<T>{
        return internalCollection.iterator()
    }

    override fun add(element: T): Boolean {
        val added = internalCollection.add(element)
        if(added) invokeListener(null, element)
        return added
    }

    override fun remove(element: T): Boolean{
        val removed = internalCollection.remove(element)
        if(removed) invokeListener(element, null)
        return removed
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val elementsAdded = mutableListOf<T>()

        elements.forEach {
            val added = internalCollection.add(it)
            if (added) elementsAdded.add(it)
        }

        if(elementsAdded.isNotEmpty()) invokeListener(emptyList(), elementsAdded)
        return elementsAdded.isNotEmpty()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val elementsRemoved = mutableListOf<T>()

        elements.forEach {
            val removed = internalCollection.remove(it)
            if (removed) elementsRemoved.add(it)
        }

        if(elementsRemoved.isNotEmpty()) invokeListener(elementsRemoved, emptyList())
        return elementsRemoved.isNotEmpty()
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val elementsRemoved = mutableListOf<T>()

        elements.forEach {
            val removed = !internalCollection.contains(it)

            if (removed) {
                internalCollection.remove(it)
                elementsRemoved.add(it)
            }
        }

        if(elementsRemoved.isNotEmpty()) invokeListener(elementsRemoved, emptyList())
        return elementsRemoved.isNotEmpty()
    }

    override val size: Int
        get() = internalCollection.size

    override fun clear() {
        val collection = internalCollection.toList()
        internalCollection.clear()

        if(collection.isNotEmpty()) invokeListener(collection, emptyList())
    }

    override fun isEmpty(): Boolean {
        return internalCollection.isEmpty()
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return internalCollection.containsAll(elements)
    }

    override fun contains(element: T): Boolean {
        return internalCollection.contains(element)
    }
}