package de.osca.android.jobs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.osca.android.essentials.data.client.OSCAHttpClient
import de.osca.android.jobs.data.JobsApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JobsModule {
    @Singleton
    @Provides
    fun jobsApiService(oscaHttpClient: OSCAHttpClient): JobsApiService =
        oscaHttpClient.create(JobsApiService::class.java)
}
