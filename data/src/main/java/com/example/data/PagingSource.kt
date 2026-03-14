package com.example.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.network.kakaosearch.KakaoLocalApi
import com.example.domain.model.PlaceDocumentDto

class KakaoSearchPagingSource(
    private val kakaoLocalApi: KakaoLocalApi,
    private val query: String
) : PagingSource<Int, PlaceDocumentDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlaceDocumentDto> {
        return try {

            val page = params.key ?: 1

            val response = kakaoLocalApi.searchPlaces(
                query = query,
                page = page,
                size = params.loadSize
            )

            val documents = response.documents

            LoadResult.Page(
                data = documents,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (documents.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PlaceDocumentDto>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}