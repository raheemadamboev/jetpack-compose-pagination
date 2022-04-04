package xyz.teamgravity.jetpackcomposepagination

interface Pager<Key, Model> {
    suspend fun onLoadNextData()
    fun onReset()
}