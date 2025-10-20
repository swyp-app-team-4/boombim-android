package com.example.data.network.map

import com.example.data.network.map.request.PostViewPortRequest
import com.example.domain.model.CongestionResponse
import com.example.domain.model.MemberPlaceDetailResponse
import com.example.domain.model.MemberPlaceResponse
import com.example.domain.model.OfficialPlaceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MapApi {

    @POST("api/app/official-place")
    suspend fun postViewPort(
        @Body viewport: PostViewPortRequest
    ): Response<CongestionResponse>

    @GET("api/app/official-place/{officialPlaceId}/overview")
    suspend fun getOfficialPlaceOverview(
        @Path("officialPlaceId") officialPlaceId: Int
    ): Response<OfficialPlaceResponse>

    @POST("api/app/member-place")
    suspend fun postMemberPlace(
        @Body viewport: PostViewPortRequest
    ): Response<MemberPlaceResponse>

    @GET("api/app/member-place/{memberPlaceId}")
    suspend fun getMemberPlaceDetail(
        @Path("memberPlaceId") memberPlaceId: Int,
        @Query("size") size: Int? = 30,
        @Query("cursor") cursor: Long? = null
    ): Response<MemberPlaceDetailResponse>

}