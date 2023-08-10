package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data

import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.BuildKonfig
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.data.response.SearchPhotosResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.path

internal class KtorUnsplashApi(
  private val httpClient: HttpClient,
) : UnsplashApi {
  override suspend fun searchPhotos(query: String) = httpClient.get(
    URLBuilder(BuildKonfig.UNSPLASH_BASE_URL)
      .apply {
        path("search/photos")
        parameters.run {
          append("query", query)
          append("page", "1")
          append("per_page", "30")
        }
      }
      .build()
  ).body<SearchPhotosResult>()
}