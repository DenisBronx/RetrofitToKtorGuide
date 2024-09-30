package com.denisbrandi.migrateretrofit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class JsonGithubProject(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "full_name") val fullName: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "owner") val owner: JsonOwner
)

@JsonClass(generateAdapter = true)
class JsonOwner(
    @field:Json(name = "avatar_url") val avatarUrl: String
)