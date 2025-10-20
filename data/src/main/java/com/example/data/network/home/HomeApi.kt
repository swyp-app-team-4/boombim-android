package com.example.data.network.home

import com.example.data.network.home.request.CheckUserPlaceRequest
import com.example.data.network.home.request.MakeAutoCongestionMessageRequest
import com.example.data.network.home.request.MakeCongestionRequest
import com.example.domain.model.CheckUserPlaceResponse
import com.example.domain.model.Coordinate
import com.example.domain.model.MakeAutoMessageResponse
import com.example.domain.model.MakeCongestionResponse
import com.example.domain.model.PlaceBoomBimResponse
import com.example.domain.model.PlaceLessBoomBimModel
import com.example.domain.model.PlaceLessBoomBimResponse
import com.example.domain.model.RegionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApi {

    @GET("api/region")
    suspend fun getRegionList(
        @Query("date") query: String
    ): Response<List<RegionResponse>>

    @POST("api/app/member-place/resolve")
    suspend fun checkUserPlace(
        @Body data: CheckUserPlaceRequest
    ): Response<CheckUserPlaceResponse>

    @POST("api/app/member-congestion")
    suspend fun postCongestion(
        @Body data: MakeCongestionRequest
    ) : Response<MakeCongestionResponse>

    @GET("api/app/official-place/top-congested")
    suspend fun getTop5BoombimPlace() : Response<PlaceBoomBimResponse>

    @POST("api/app/clova/congestion-message")
    suspend fun makeAutoCongestionMessage(
        @Body data: MakeAutoCongestionMessageRequest
    ) : Response<MakeAutoMessageResponse>

    @GET("api/app/official-place/nearby-non-congested")
    suspend fun getLessBoomBimPlace(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<PlaceLessBoomBimResponse>




}