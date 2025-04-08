package de.osca.android.jobs.data

import de.osca.android.essentials.domain.entity.elastic_search.ElasticSearchRequest
import de.osca.android.essentials.utils.annotations.UnwrappedResponse
import de.osca.android.jobs.domain.entity.Job
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JobsApiService {
    @GET("classes/JobPosting?limit=1000")
    suspend fun getJobs(): Response<List<Job>>

    @POST("functions/elastic-search")
    suspend fun elasticSearch(
        @Body elasticSearchRequest: ElasticSearchRequest,
    ): Response<List<Job?>>

    @GET("classes/JobPosting/{objectId}")
    @UnwrappedResponse
    suspend fun getJobById(
        @Path("objectId") objectId: String,
    ): Response<Job>
}
