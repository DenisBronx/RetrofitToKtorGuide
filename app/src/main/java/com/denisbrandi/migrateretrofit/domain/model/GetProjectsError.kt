package com.denisbrandi.migrateretrofit.domain.model

sealed interface GetProjectsError {
    data object GenericError : GetProjectsError
    data object NoOrganizationFound: GetProjectsError
}