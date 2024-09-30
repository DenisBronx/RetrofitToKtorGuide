@file:OptIn(ExperimentalCoroutinesApi::class)

package com.denisbrandi.migrateretrofit.data.repository

import com.denisbrandi.migrateretrofit.data.api.GithubProjectApiService
import com.denisbrandi.migrateretrofit.domain.model.GetProjectsError
import com.denisbrandi.migrateretrofit.domain.model.GithubProject
import com.denisbrandi.migrateretrofit.prelude.Answer
import com.denisbrandi.netmock.Method
import com.denisbrandi.netmock.NetMockRequest
import com.denisbrandi.netmock.NetMockResponse
import com.denisbrandi.netmock.resources.readFromResources
import com.denisbrandi.netmock.server.NetMockServerRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class RealGithubProjectRepositoryTest {

    @get:Rule
    val netMock = NetMockServerRule()

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(netMock.interceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val sut = RealGithubProjectRepository(retrofit.create<GithubProjectApiService>())

    @Test
    fun `EXPECT empty list of projects WHEN body is empty list`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 200,
                mandatoryHeaders = MANDATORY_HEADERS,
                body = "[]"
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Success(emptyList<GithubProject>()), result)
    }

    @Test
    fun `EXPECT generic error WHEN error is 500`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 500,
                mandatoryHeaders = MANDATORY_HEADERS,
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Error(GetProjectsError.GenericError), result)
    }

    @Test
    fun `EXPECT not found error WHEN error is 404`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 404,
                mandatoryHeaders = MANDATORY_HEADERS
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Error(GetProjectsError.NoOrganizationFound), result)
    }

    @Test
    fun `EXPECT generic error WHEN response is successful but body is empty`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 200,
                mandatoryHeaders = MANDATORY_HEADERS,
                body = ""
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Error(GetProjectsError.GenericError), result)
    }

    @Test
    fun `EXPECT generic error WHEN response is successful but body is not parseable`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 200,
                mandatoryHeaders = MANDATORY_HEADERS,
                body = "null"
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Error(GetProjectsError.GenericError), result)
    }

    @Test
    fun `EXPECT mapped list of projects WHEN body is non empty list`() = runTest {
        netMock.addMock(
            request = EXPECTED_REQUEST,
            response = NetMockResponse(
                code = 200,
                mandatoryHeaders = MANDATORY_HEADERS,
                body = readFromResources("responses/github_projects_success.json")
            )
        )

        val result = sut.getProjectsForOrganisation(ORGANIZATION)

        assertEquals(Answer.Success(EXPECTED_GH_PROJECTS), result)
    }

    private companion object {
        const val BASE_URL = "https://api.github.com"
        const val ORGANIZATION = "square"
        val MANDATORY_HEADERS = mapOf("Content-Type" to "application/json")
        val EXPECTED_REQUEST = NetMockRequest(
            requestUrl = "$BASE_URL/orgs/$ORGANIZATION/repos",
            method = Method.Get,
            mandatoryHeaders = MANDATORY_HEADERS
        )

        val EXPECTED_GH_PROJECTS = listOf(
            GithubProject(
                id = "892275",
                name = "retrofit",
                fullName = "square/retrofit",
                description = "A type-safe HTTP client for Android and the JVM",
                imageUrl = "https://avatars.githubusercontent.com/u/82592?v=4"
            ),
            GithubProject(
                id = "1907635",
                name = "JSONKit",
                fullName = "square/JSONKit",
                description = "Objective-C JSON",
                imageUrl = "https://avatars.githubusercontent.com/u/82592?v=4"
            )
        )
    }

}