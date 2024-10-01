package com.denisbrandi.migrateretrofit.data.repository

import com.denisbrandi.migrateretrofit.data.model.JsonGithubProject
import com.denisbrandi.migrateretrofit.domain.model.GetProjectsError
import com.denisbrandi.migrateretrofit.domain.model.GithubProject
import com.denisbrandi.migrateretrofit.domain.repository.GithubProjectRepository
import com.denisbrandi.migrateretrofit.prelude.Answer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

class RealGithubProjectRepository(
    private val httpClient: HttpClient
) : GithubProjectRepository {
    override suspend fun getProjectsForOrganisation(
        organisation: String
    ): Answer<List<GithubProject>, GetProjectsError> {
        return try {
            val apiResponse = httpClient.get("https://api.github.com/orgs/$organisation/repos") {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }
            }
            return mapSuccess(apiResponse)
        } catch (t: Throwable) {
            Answer.Error(GetProjectsError.GenericError)
        }
    }

    private suspend fun mapSuccess(
        apiResponse: HttpResponse
    ): Answer<List<GithubProject>, GetProjectsError> {
        return if (apiResponse.status.isSuccess()) {
            val projects = apiResponse.body<List<JsonGithubProject>?>()
            if (projects != null) {
                Answer.Success(mapDtoList(projects))
            } else {
                Answer.Error(GetProjectsError.GenericError)
            }
        } else {
            mapError(apiResponse.status.value)
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