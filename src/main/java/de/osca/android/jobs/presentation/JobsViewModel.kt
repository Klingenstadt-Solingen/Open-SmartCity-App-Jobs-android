package de.osca.android.jobs.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.osca.android.essentials.presentation.base.BaseViewModel
import de.osca.android.essentials.utils.extensions.displayContent
import de.osca.android.essentials.utils.extensions.loading
import de.osca.android.essentials.utils.extensions.resetWith
import de.osca.android.jobs.domain.boundary.JobRepository
import de.osca.android.jobs.domain.entity.Job
import de.osca.android.jobs.presentation.args.JobsDesignArgs
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Jobs view model
 *
 * @param jobsDesignArgs the design arguments of the module
 * @param repository to get the Job List and search for a given query
 *
 * @property allJobs list of all available jobs
 * @property displayedJobs list of the filtered jobs that need to be shown on screen
 * @property searchJob Job object for excecuting the search query
 */

@HiltViewModel
class JobsViewModel
    @Inject
    constructor(
        val jobsDesignArgs: JobsDesignArgs,
        private val repository: JobRepository,
    ) : BaseViewModel() {
        val allJobs = mutableListOf<Job>()
        val displayedJobs = mutableStateListOf<Job>()
        private var searchJob: kotlinx.coroutines.Job? = null
        private var deeplinkObjectId: String? = null

        /**
         * call this function to initialize all jobs.
         * it sets the screen to loading, fetches the data from parse and when
         * it finished successful then displays the content and when an error
         * occurred it displays an message screen
         */
        fun initializeJobs() {
            viewModelScope.launch {
                wrapperState.loading()
                async {
                    fetchJobs()
                }
            }
        }

        /**
         * fetches all jobs from parse and when successfully loaded then
         * displays the content
         */
        fun fetchJobs(): kotlinx.coroutines.Job =
            launchDataLoad {
                val result = repository.getAll()
                allJobs.resetWith(result.sortedBy { it.daysSincePosted })

                wrapperState.displayContent()

                showAllJobs()
            }

        /**
         * shows all jobs.
         */
        fun showAllJobs() {
            displayedJobs.resetWith(allJobs)
        }

        /**
         * search jobs based on a specific text.
         * @property searchText text for matching jobs
         */
        fun search(searchText: String) {
            when (searchText.length) {
                in 0..3 -> showAllJobs()
                else -> {
                    searchJob?.cancel()
                    searchJob = elasticSearchJobs(searchText)
                }
            }
        }

        /**
         * !!! call only via search()
         * search jobs based on a specific text.
         * @property searchedContent text for matching jobs
         */
        private fun elasticSearchJobs(searchedContent: String): kotlinx.coroutines.Job =
            launchDataLoad {
                val result = repository.search(searchedContent).filterNotNull()

                displayedJobs.resetWith(result)
            }

        /**
         * Fetches a Job with the specified objectId and calls the navigate function if the job is found
         * */
        fun navigateToJobDetail(
            objectId: String,
            navigateToJob: (Job) -> Unit,
        ) {
            if (deeplinkObjectId != objectId) {
                viewModelScope.launch {
                    repository.getJobById(objectId)?.let {
                        deeplinkObjectId = objectId
                        navigateToJob(it)
                    }
                }
            }
        }
    }
