package de.osca.android.jobs.domain.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Company(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("branch")
    val branch: String? = null,
)
