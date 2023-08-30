//package rs.raf.rmaprojekat.data.repositories.category
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import rs.raf.rmaprojekat.data.models.category.Category
//
//class CategoryPagingSource(
//    private val repository: CategoryRepository // Replace with your actual repository
//) : PagingSource<Int, Category>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Category> {
//        try {
//            val page = params.key ?: 1
//            val pageSize = params.loadSize
//            val categories = repository.getCategories(page, pageSize)
//
//            return LoadResult.Page(
//                data = categories,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (categories.isEmpty()) null else page + 1
//            )
//        } catch (exception: Exception) {
//            return LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Category>): Int? {
//        return state.anchorPosition
//    }
//}
