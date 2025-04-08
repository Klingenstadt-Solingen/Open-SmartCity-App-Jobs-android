package de.osca.android.jobs.data.repository

import de.osca.android.essentials.domain.entity.elastic_search.ElasticSearchRequest
import de.osca.android.jobs.data.JobsApiService
import de.osca.android.jobs.domain.boundary.JobRepository
import de.osca.android.jobs.domain.entity.Job
import de.osca.android.networkservice.utils.RequestHandler
import javax.inject.Inject

class JobRepositoryImpl
    @Inject
    constructor(
        private val requestHandler: RequestHandler,
        private val jobsApiService: JobsApiService,
    ) : JobRepository {
        override suspend fun getAll(): List<Job> {
            return requestHandler.makeRequest(jobsApiService::getJobs) ?: emptyList()
        }

        override suspend fun search(query: String): List<Job?> {
            val request =
                ElasticSearchRequest(
                    index = ES_JOBS_INDEX,
                    query = query,
                    raw = false,
                )
            return requestHandler.makeRequest {
                jobsApiService.elasticSearch(request)
            } ?: emptyList()
        }

        override suspend fun getJobById(objectId: String): Job? {
            return requestHandler.makeRequest {
                jobsApiService.getJobById(objectId)
            }
        }

        companion object {
            const val ES_JOBS_INDEX = "job_posting"
        }
    }
