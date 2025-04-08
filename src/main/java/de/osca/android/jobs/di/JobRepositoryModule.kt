package de.osca.android.jobs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.osca.android.jobs.data.repository.JobRepositoryImpl
import de.osca.android.jobs.domain.boundary.JobRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class JobRepositoryModule {
    @Binds
    abstract fun provideJobRepository(repositoryImpl: JobRepositoryImpl): JobRepository
}