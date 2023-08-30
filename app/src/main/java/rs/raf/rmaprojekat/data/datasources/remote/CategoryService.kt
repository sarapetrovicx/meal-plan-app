package rs.raf.rmaprojekat.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import rs.raf.rmaprojekat.data.models.category.CategoriesResponse
import rs.raf.rmaprojekat.data.models.category.CategoryResponse

interface CategoryService {

    @GET("categories.php")
    fun getAll(): Observable<CategoriesResponse>
}
