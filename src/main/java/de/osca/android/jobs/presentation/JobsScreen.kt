package de.osca.android.jobs.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import de.osca.android.essentials.presentation.component.design.BaseCardContainer
import de.osca.android.essentials.presentation.component.design.BaseTextField
import de.osca.android.essentials.presentation.component.design.MasterDesignArgs
import de.osca.android.essentials.presentation.component.screen_wrapper.ScreenWrapper
import de.osca.android.essentials.presentation.component.topbar.ScreenTopBar
import de.osca.android.essentials.presentation.nav_items.EssentialsNavItems
import de.osca.android.essentials.utils.extensions.SetSystemStatusBar
import de.osca.android.jobs.R

/**
 * @param navController from the navigationGraph
 * @param jobsViewModel screen creates the corresponding viewModel
 * @param masterDesignArgs main design arguments for the overall design
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoilApi::class)
@ExperimentalMaterialApi
@Composable
fun JobsScreen(
    navController: NavController,
    objectId: String?,
    jobsViewModel: JobsViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = jobsViewModel.defaultDesignArgs,
) {
    val design = jobsViewModel.jobsDesignArgs

    LaunchedEffect(objectId) {
        jobsViewModel.initializeJobs()
        objectId?.let { id ->
            jobsViewModel.navigateToJobDetail(id) { job ->
                navController.navigate(
                    EssentialsNavItems.getWebViewRoute(
                        job.url,
                        job.jobTitle,
                    ),
                )
            }
        }
    }

    val jobsList = remember { jobsViewModel.displayedJobs }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    SetSystemStatusBar(
        !(design.mIsStatusBarWhite ?: masterDesignArgs.mIsStatusBarWhite),
        Color.Transparent,
    )

    ScreenWrapper(
        topBar = {
            ScreenTopBar(
                title = stringResource(id = design.vModuleTitle),
                navController = navController,
                overrideBackgroundColor = design.mTopBarBackColor,
                overrideTextColor = design.mTopBarTextColor,
                masterDesignArgs = masterDesignArgs,
            )
        },
        navController = navController,
        screenWrapperState = jobsViewModel.wrapperState,
        retryAction = {
            jobsViewModel.initializeJobs()
        },
        masterDesignArgs = masterDesignArgs,
        moduleDesignArgs = design,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding =
                PaddingValues(
                    top = 12.dp,
                    bottom = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
        ) {
            item {
                BaseCardContainer(
                    moduleDesignArgs = design,
                    masterDesignArgs = masterDesignArgs,
                ) {
                    BaseTextField(
                        textFieldTitle = "",
                        isError = false,
                        isOutlined = false,
                        onTextChange = {
                            jobsViewModel.search(it)
                        },
                        onClear = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            jobsViewModel.search("")
                        },
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        },
                        masterDesignArgs = masterDesignArgs,
                        moduleDesignArgs = design,
                    )
                }
            }

            items(
                items = jobsList,
                itemContent = { job ->
                    val daysSincePosted =
                        when (job.daysSincePosted) {
                            0 -> stringResource(id = R.string.global_today)
                            1 -> stringResource(id = R.string.global_yesterday)
                            in 2..30 ->
                                stringResource(
                                    id = R.string.global_x_days_ago,
                                    job.daysSincePosted,
                                )

                            else -> stringResource(id = R.string.global_over_1_month_ago)
                        }

                    BaseCardContainer(
                        moduleDesignArgs = design,
                        masterDesignArgs = masterDesignArgs,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier
                                    .clickable {
                                        navController.navigate(
                                            EssentialsNavItems.getWebViewRoute(
                                                job.url,
                                                job.jobTitle,
                                            ),
                                        )
                                    },
                        ) {
                            if ((job.company?.imageUrl != null) && (job.company.imageUrl.length >= 5)) {
                                Image(
                                    painter = rememberAsyncImagePainter(job.company.imageUrl),
                                    contentDescription = "jobImage",
                                    contentScale = ContentScale.Inside,
                                    modifier =
                                        Modifier
                                            .size(52.dp),
                                    alignment = Alignment.Center,
                                )
                            } else {
                                Image(
                                    painter = painterResource(design.cityLogo),
                                    contentDescription = "placeholderImage",
                                    contentScale = ContentScale.Inside,
                                    modifier =
                                        Modifier
                                            .size(52.dp),
                                    alignment = Alignment.Center,
                                )
                            }

                            Column(
                                modifier =
                                    Modifier
                                        .padding(start = 6.dp)
                                        .weight(6f),
                            ) {
                                Row {
                                    Text(
                                        text = job.getLocalizedEmploymentType(),
                                        style = masterDesignArgs.bodyTextStyle,
                                        color =
                                            design.mCardTextColor
                                                ?: masterDesignArgs.mCardTextColor,
                                    )
                                    Spacer(
                                        modifier =
                                            Modifier
                                                .weight(1f),
                                    )

                                    Text(
                                        text = daysSincePosted,
                                        style = masterDesignArgs.bodyTextStyle,
                                        color =
                                            design.mCardTextColor
                                                ?: masterDesignArgs.mCardTextColor,
                                    )
                                }
                                Text(
                                    text = job.jobTitle ?: "",
                                    style = masterDesignArgs.overlineTextStyle,
                                    color =
                                        design.mCardTextColor
                                            ?: masterDesignArgs.mCardTextColor,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = job.companyName,
                                    style = masterDesignArgs.normalTextStyle,
                                    color =
                                        design.mCardTextColor
                                            ?: masterDesignArgs.mCardTextColor,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "",
                                tint = design.mHintTextColor ?: masterDesignArgs.mHintTextColor,
                                modifier =
                                    Modifier
                                        .weight(0.4f),
                            )
                        }
                    }
                },
            )
        }
    }
}
