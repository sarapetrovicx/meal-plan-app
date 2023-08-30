package rs.raf.rmaprojekat.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.rmaprojekat.data.models.meal.MealsResponse

interface MealService {

    @GET("filter.php")
    fun getByCategory(@Query("c") category: String): Observable<MealsResponse>

    @GET("filter.php")
    fun getByArea(@Query("a") area: String): Observable<MealsResponse>

    @GET("filter.php")
    fun getByIngredient(@Query("i") ingr: String): Observable<MealsResponse>

    @GET("search.php")
    fun getByName(@Query("s") name: String): Observable<MealsResponse>
}

