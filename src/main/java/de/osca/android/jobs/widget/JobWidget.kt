package de.osca.android.jobs.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import de.osca.android.essentials.presentation.component.design.BaseCardContainer
import de.osca.android.essentials.presentation.component.design.BaseListContainer
import de.osca.android.essentials.presentation.component.design.MasterDesignArgs
import de.osca.android.essentials.presentation.nav_items.EssentialsNavItems
import de.osca.android.essentials.utils.extensions.safeTake
import de.osca.android.jobs.R
import de.osca.android.jobs.navigation.JobsNavItems
import de.osca.android.jobs.presentation.JobsViewModel

/**
 * Job widget
 *
 * @param navController from the navigationGraph
 * @param jobsViewModel widget creates the corresponding viewModel
 * @param masterDesignArgs main design arguments for the overall design
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun JobWidget(
    navController: NavController,
    jobsViewModel: JobsViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = jobsViewModel.defaultDesignArgs,
) {
    if(jobsViewModel.jobsDesignArgs.vIsWidgetVisible) {
        val design = jobsViewModel.jobsDesignArgs

        LaunchedEffect(Unit) {
            jobsViewModel.initializeJobs()
        }

        BaseListContainer(
            text = stringResource(id = design.vWidgetTitle),
            showMoreOption = design.vWidgetShowMoreOption,
            moduleDesignArgs = design,
            onMoreOptionClick = {
                navController.navigate(JobsNavItems.JobsNavItem.route)
            },
            masterDesignArgs = masterDesignArgs
        ) {
            jobsViewModel.displayedJobs.safeTake(jobsViewModel.jobsDesignArgs.previewCountForWidget).forEach { job ->
                val daysSincePosted = when (job.daysSincePosted) {
                    0 -> stringResource(id = R.string.global_today)
                    1 -> stringResource(id = R.string.global_yesterday)
                    in 2..30 -> stringResource(
                        id = R.string.global_x_days_ago,
                        job.daysSincePosted
                    )
                    else -> stringResource(id = R.string.global_over_1_month_ago)
                }

                BaseCardContainer(
                    moduleDesignArgs = design,
                    masterDesignArgs = masterDesignArgs
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                navController.navigate(
                                    EssentialsNavItems.getWebViewRoute(
                                        job.url,
                                        job.jobTitle
                                    )
                                )
                            }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(job.company?.imageUrl),
                            contentDescription = stringResource(id = R.string.jobs_company_logo_description),
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .size(52.dp),
                            alignment = Alignment.Center
                        )

                        Column(
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .weight(6f)
                        ) {
                            Row {
                                Text(
                                    text = job.employmentType ?: "",
                                    style = masterDesignArgs.bodyTextStyle,
                                    color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor
                                )
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Text(
                                    text = daysSincePosted,
                                    style = masterDesignArgs.bodyTextStyle,
                                    color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor
                                )
                            }
                            Text(
                                text = job.jobTitle ?: "",
                                style = masterDesignArgs.overlineTextStyle,
                                color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = job.companyName,
                                style = masterDesignArgs.normalTextStyle,
                                color = design.mCardTextColor ?: masterDesignArgs.mCardTextColor,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "",
                            tint = design.mHintTextColor ?: masterDesignArgs.mCardTextColor,
                            modifier = Modifier.weight(0.4f)
                        )
                    }
                }
            }
        }
    }
}