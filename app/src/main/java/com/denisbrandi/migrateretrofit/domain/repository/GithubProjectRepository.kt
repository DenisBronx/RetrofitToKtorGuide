package com.denisbrandi.migrateretrofit.domain.repository

import com.denisbrandi.migrateretrofit.domain.model.GetProjectsError
import com.denisbrandi.migrateretrofit.domain.model.GithubProject
import com.denisbrandi.migrateretrofit.prelude.Answer

interface GithubProjectRepository {
    suspend fun getProjectsForOrganisation(
        organisation: String
    ): Answer<List<GithubProject>, GetProjectsError>
}