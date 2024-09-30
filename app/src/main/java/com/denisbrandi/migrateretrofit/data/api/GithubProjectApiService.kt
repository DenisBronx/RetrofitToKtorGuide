package com.denisbrandi.migrateretrofit.data.api

import com.denisbrandi.migrateretrofit.data.model.JsonGithubProject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface GithubProjectApiService {

    @Headers("Content-Type: application/json")
    @GET("orgs/{organisation}/repos")
    suspend fun getProjectsForOrganisation(
        @Path("organisation") organisation: String
    ): Response<List<JsonGithubProject>>

}