package com.denisbrandi.migrateretrofit.domain.model

data class GithubProject(
    val id: String,
    val name: String,
    val fullName: String,
    val description: String,
    val imageUrl: String
)