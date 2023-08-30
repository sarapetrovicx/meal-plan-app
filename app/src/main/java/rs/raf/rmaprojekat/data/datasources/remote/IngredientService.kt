package rs.raf.rmaprojekat.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.rmaprojekat.data.models.ingredients.IngredientResponse
import rs.raf.rmaprojekat.data.models.ingredients.IngredientsResponse
import rs.raf.rmaprojekat.data.models.meal.MealsResponse

interface IngredientService {

    @GET("list.php?i=list")
    fun getAll(): Observable<IngredientsResponse>
}

