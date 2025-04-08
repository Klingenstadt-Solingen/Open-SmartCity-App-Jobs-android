package de.osca.android.jobs.domain.boundary

import de.osca.android.jobs.domain.entity.Job

interface JobRepository {
    suspend fun getAll(): List<Job>

    suspend fun search(query: String): List<Job?>

    suspend fun getJobById(objectId: String): Job?
}
