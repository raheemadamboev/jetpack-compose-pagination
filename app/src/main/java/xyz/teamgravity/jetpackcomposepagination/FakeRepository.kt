package xyz.teamgravity.jetpackcomposepagination

import kotlinx.coroutines.delay

class FakeRepository {

    private val data = (1..100).map {
        ItemModel(
            title = "Title - $it",
            content = "Content - $it"
        )
    }

    suspend fun getItems(page: Int, pageSize: Int): Result<List<ItemModel>> {
        delay(2_000)
        val startIndex = page * pageSize
        return if (startIndex + pageSize <= data.size) {
            Result.success(data.subList(startIndex, startIndex + pageSize))
        } else Result.success(emptyList())
    }
}