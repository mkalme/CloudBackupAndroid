package com.storage.cloudbackup.logic.shared.utilities.observable.collections

class ObservableMutableList<T>(list: MutableList<T>) : MutableList<T>, ObservableMutableCollection<T>(list) {
    private val internalList: MutableList<T> = list

    companion object {
        fun<T> emptyList() : ObservableMutableList<T> {
            return ObservableMutableList(mutableListOf())
        }
    }

    override val size: Int
        get() = internalList.size

    override fun isEmpty(): Boolean {
        return internalList.isEmpty()
    }

    override fun contains(element: @UnsafeVariance T): Boolean {
        return internalList.contains(element)
    }
    override fun iterator(): MutableIterator<T> {
        return internalList.iterator()
    }

    override fun containsAll(elements: Collection<@UnsafeVariance T>): Boolean {
        return internalList.containsAll(elements)
    }

    override operator fun get(index: Int): T {
        return internalList[index]
    }

    override fun indexOf(element: @UnsafeVariance T): Int {
        return internalList.indexOf(element)
    }

    override fun lastIndexOf(element: @UnsafeVariance T): Int {
        return internalList.lastIndexOf(element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = internalList.addAll(index, elements)
        if(added) invokeListener(emptyList(), elements)
        return added
    }

    override operator fun set(index: Int, element: T): T {
        val prev = internalList.set(index, element)
        if(prev != element) invokeListener(prev, null)
        return prev
    }

    override fun add(index: Int, element: T) {
        internalList.add(index, element)
    }

    override fun removeAt(index: Int): T {
        val removed = internalList.removeAt(index)
        invokeListener(removed, null)
        return removed
    }

    override fun listIterator(): MutableListIterator<T> {
        return internalList.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return internalList.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return internalList.subList(fromIndex, toIndex)
    }
}