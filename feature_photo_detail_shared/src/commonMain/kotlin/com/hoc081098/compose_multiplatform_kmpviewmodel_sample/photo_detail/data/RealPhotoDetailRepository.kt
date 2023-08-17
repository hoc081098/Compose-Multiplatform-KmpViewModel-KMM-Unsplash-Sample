package com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data

import arrow.core.Either
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.UnsplashApi
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.data.remote.response.CoverPhotoResponse
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoCreator
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetail
import com.hoc081098.compose_multiplatform_kmpviewmodel_sample.photo_detail.domain.PhotoDetailRepository
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Singleton

@Singleton(
  binds = [
    PhotoDetailRepository::class,
  ],
)
internal class RealPhotoDetailRepository(
  private val unsplashApi: UnsplashApi,
  private val photoDetailErrorMapper: PhotoDetailErrorMapper,
) : PhotoDetailRepository {
  override suspend fun getPhotoDetailById(id: String) = Either.catch {
    unsplashApi
      .getPhotoDetailById(id)
      .toPhotoDetail()
  }
    .onLeft {
      Napier.e(
        throwable = it,
        tag = "RealPhotoDetailRepository",
        message = "getPhotoDetailById($id) failed",
      )
    }
    .mapLeft(photoDetailErrorMapper)
}

private fun CoverPhotoResponse.toPhotoDetail(): PhotoDetail = PhotoDetail(
  id = id,
  fullUrl = urls.full,
  description = description,
  alternativeDescription = altDescription,
  createdAt = createdAt,
  updatedAt = updatedAt,
  promotedAt = promotedAt,
  creator = PhotoCreator(
    id = user.id,
    username = user.username,
    name = user.name,
    smallProfileImageUrl = user.profileImage.small,
  ),
)
