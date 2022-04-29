package com.loboda.james.testlauncher1.models

import android.graphics.drawable.Drawable

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 4/27/22
 * www.papayev.com
 */

/**
 * Get info from package manager & put into model
 */
data class PackageItem(
    val packageName: String,
    val iconRes: Int,
    val iconDrawable: Drawable,
    val id: Int,
    val label: String
)
