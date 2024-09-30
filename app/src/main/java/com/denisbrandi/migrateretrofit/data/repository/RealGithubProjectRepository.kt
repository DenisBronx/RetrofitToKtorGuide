package com.denisbrandi.migrateretrofit.data.repository

import com.denisbrandi.migrateretrofit.data.api.GithubProjectApiService
import com.denisbrandi.migrateretrofit.data.model.JsonGithubProject
import com.denisbrandi.migrateretrofit.domain.model.GetProjectsError
import com.denisbrandi.migrateretrofit.domain.model.GithubProject
import com.denisbrandi.migrateretrofit.domain.repository.GithubProjectRepository
import com.denisbrandi.migrateretrofit.prelude.Answer
import retrofit2.Response

class RealGithubProjectRepository(
    private val githubProjectApiService: GithubProjectApiService
) : GithubProjectRepository {
    override suspend fun getProjectsForOrganisation(
        organisation: String
    ): Answer<List<GithubProject>, GetProjectsError> {
        return try {
            val apiResponse = githubProjectApiService.getProjectsForOrganisation(organisation)
            return mapSuccess(apiResponse)
        } catch (t: Throwable) {
            Answer.Error(GetProjectsError.GenericError)
        }
    }

    private fun mapSuccess(
        apiResponse: Response<List<JsonGithubProject>>
    ): Answer<List<GithubProject>, GetProjectsError> {
        return if (apiResponse.isSuccessful) {
            val projects = apiResponse.body()
            if (projects != null) {
                Answer.Success(mapDtoList(projects))
            } else {
                Answer.Error(GetProjectsError.GenericError)
            }
        } else {
            mapError(apiResponse.code())
        }
    }

    private fun mapDtoList(dtoList: List<JsonGithubProject>): List<GithubProject> {
        return dtoList.map { jsonDto ->
            GithubProject(
                jsonDto.id,
                jsonDto.name,
                jsonDto.fullName,
                jsonDto.description,
                jsonDto.owner.avatarUrl
            )
        }
    }

    private fun mapError(errorCode: Int): Answer<List<GithubProject>, GetProjectsError> {
        val error = if (errorCode == 404) {
            GetProjectsError.NoOrganizationFound
        } else {
            GetProjectsError.GenericError
        }
        return Answer.Error(error)
    }
}