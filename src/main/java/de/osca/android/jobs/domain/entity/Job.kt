package de.osca.android.jobs.domain.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.gson.annotations.SerializedName
import de.osca.android.essentials.domain.entity.DateEnvelope
import de.osca.android.essentials.utils.extensions.toLocalDateTime
import de.osca.android.jobs.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Job(
    @SerializedName("objectId")
    val objectId: String? = null,
    @SerializedName("hiringOrganization")
    val company: Company? = null,
    @SerializedName("datePosted")
    val datePosted: DateEnvelope? = null,
    @SerializedName("sourceUrl")
    val sourceUrl: String? = null,
    @SerializedName("sourceId")
    val sourceId: String? = null,
    @SerializedName("title")
    val jobTitle: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("employmentType")
    val employmentType: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
) {
    val companyName get() = "${company?.name}"
    val searchContents get() = "$jobTitle $companyName $datePosted ${company?.branch} $sourceUrl $objectId".lowercase()
    val daysSincePosted: Int get() {
        val today = LocalDateTime.now()
        return datePosted?.value?.toLocalDateTime()?.run {
            ChronoUnit.DAYS.between(datePosted.toLocalDateTime(), today)
        }?.toInt() ?: 0
    }

    @Composable
    fun getLocalizedEmploymentType(): String {
        return when(employmentType) {
            EMPLOYMENT_FULL_TIME -> stringResource(R.string.jobs_full_time)
            EMPLOYMENT_PART_TIME -> stringResource(R.string.jobs_part_time)
            else -> employmentType ?: ""
        }
    }

    companion object {
        const val EMPLOYMENT_FULL_TIME = "full-time"
        const val EMPLOYMENT_PART_TIME = "part-time"
    }
}
