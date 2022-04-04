package xyz.teamgravity.jetpackcomposepagination

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var state: MainState by mutableStateOf(MainState())
        private set

    private val repository = FakeRepository()

    private val pager = ComposePager(
        initialKey = 0,
        onLoadUpdated = { loading ->
            state = state.copy(loading = loading)
        },
        onRequest = { key ->
            repository.getItems(key, 20)
        },
        onGetNextKey = {
            state.page + 1
        },
        onError = { cause ->
            state = state.copy(error = cause?.message)
        },
        onSuccess = { data, newKey ->
            state = state.copy(
                data = state.data + data,
                endReached = data.isEmpty(),
                page = newKey
            )
        }
    )

    init {
        onLoadNextData()
    }

    fun onLoadNextData() {
        viewModelScope.launch {
            pager.onLoadNextData()
        }
    }


    data class MainState(
        val loading: Boolean = false,
        val data: List<ItemModel> = emptyList(),
        val error: String? = null,
        val endReached: Boolean = false,
        val page: Int = 0
    )
}