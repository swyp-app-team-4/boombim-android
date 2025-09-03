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

    @POST("/official-place")
    suspend fun postViewPort(
        @Body viewport: PostViewPortRequest
    ): Response<CongestionResponse>

    @GET("/official-place/{officialPlaceId}/overview")
    suspend fun getOfficialPlaceOverview(
        @Path("officialPlaceId") officialPlaceId: Int
    ): Response<OfficialPlaceResponse>

    @POST("/member-place")
    suspend fun postMemberPlace(
        @Body viewport: PostViewPortRequest
    ): Response<MemberPlaceResponse>

    @GET("/member-place/{memberPlaceId}")
    suspend fun getMemberPlaceDetail(
        @Path("memberPlaceId") memberPlaceId: Long,
        @Query("size") size: Int? = 30,
        @Query("cursor") cursor: Long? = null
    ): Response<MemberPlaceDetailResponse>

}