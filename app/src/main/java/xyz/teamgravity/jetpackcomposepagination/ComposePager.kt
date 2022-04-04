package xyz.teamgravity.jetpackcomposepagination

class ComposePager<Key, Model>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (loading: Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Model>>,
    private inline val onGetNextKey: suspend (List<Model>) -> Key,
    private inline val onError: suspend (cause: Throwable?) -> Unit,
    private inline val onSuccess: suspend (data: List<Model>, newKey: Key) -> Unit
) : Pager<Key, Model> {

    private var currentKey = initialKey
    private var loading = false

    override suspend fun onLoadNextData() {
        if (loading) return
        loading = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        val data = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            return
        }
        currentKey = onGetNextKey(data)
        onSuccess(data, currentKey)
        loading = false
        onLoadUpdated(false)
    }

    override fun onReset() {
        currentKey = initialKey
    }
}