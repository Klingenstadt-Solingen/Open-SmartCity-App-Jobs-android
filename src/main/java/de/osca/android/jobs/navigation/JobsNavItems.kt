package de.osca.android.jobs.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import de.osca.android.essentials.domain.entity.navigation.NavigationItem
import de.osca.android.jobs.R

sealed class JobsNavItems {
    object JobsNavItem : NavigationItem(
        title = R.string.jobs_title,
        route = "jobs?${ARG_JOB}={${ARG_JOB}}",
        icon = R.drawable.ic_circle,
        arguments =
            listOf(
                navArgument(ARG_JOB) {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        deepLinks =
            listOf(
                navDeepLink {
                    uriPattern = "solingen://jobs"
                },
                navDeepLink {
                    uriPattern = "solingen://jobs/detail?${ARG_JOB}={${ARG_JOB}}"
                },
            ),
    )

    companion object {
        const val ARG_JOB = "object"
    }
}
