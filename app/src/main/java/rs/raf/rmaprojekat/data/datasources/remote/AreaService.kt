package rs.raf.rmaprojekat.data.datasources.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rs.raf.rmaprojekat.data.models.area.AreasResponse

interface AreaService {

//    www.themealdb.com/api/json/v1/1/list.php?a=list
    @GET("list.php?a=list")
    fun getAll(): Observable<AreasResponse>
}

