package com.denisbrandi.migrateretrofit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class JsonGithubProject(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String,
    @SerialName("owner") val owner: JsonOwner
)

@Serializable
class JsonOwner(
    @SerialName("avatar_url") val avatarUrl: String
)