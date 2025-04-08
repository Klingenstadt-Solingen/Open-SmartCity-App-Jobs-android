package de.osca.android.jobs.presentation.args

import de.osca.android.essentials.presentation.component.design.ModuleDesignArgs
import de.osca.android.essentials.presentation.component.design.WidgetDesignArgs

interface JobsDesignArgs : ModuleDesignArgs, WidgetDesignArgs {
    val previewCountForWidget: Int
    val cityLogo: Int
}