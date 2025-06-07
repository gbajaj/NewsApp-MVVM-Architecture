package com.gauravbajaj.newsapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.SHARE_STATE_ON
import androidx.core.content.ContextCompat
import com.gauravbajaj.newsapp.R
import androidx.core.net.toUri
import androidx.core.graphics.createBitmap

/**
 * Helper class for launching URLs in Custom Tabs.
 * Provides a consistent way to open web content with a customizable browser experience.
 */
object CustomTabsHelper {

    /**
     * Launches a URL in a Custom Tab.
     *
     * @param context The context to use for starting the activity
     * @param url The URL to open in the Custom Tab
     * @param toolbarColor The color to use for the browser toolbar (defaults to primary color)
     */
    fun launchUrl(
        context: Context,
        url: String,
        toolbarColor: Int = R.color.purple_500
    ) {
        val uri = url.toUri()
        val builder = CustomTabsIntent.Builder()
        
        // Configure the appearance of the Custom Tab
        builder.setToolbarColor(ContextCompat.getColor(context, toolbarColor))
        builder.setShowTitle(true)
        
        // Add share button to the menu
        builder.setShareState(SHARE_STATE_ON);
        
        // Configure the close button
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)?.let { drawable ->
            val bitmap = getBitmapFromDrawable(drawable)
            builder.setCloseButtonIcon(bitmap)
        }
        
        // Animate the Custom Tab opening and closing
        builder.setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
        
        // Build the CustomTabsIntent
        val customTabsIntent = builder.build()
        
        // Try to launch the Custom Tab, fall back to default browser if Chrome is not available
        try {
            customTabsIntent.launchUrl(context, uri)
        } catch (e: Exception) {
            // Fallback to default browser if Custom Tabs is not available
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }
    }
    
    /**
     * Converts a Drawable to a Bitmap.
     * @param drawable The Drawable to convert
     * @return A Bitmap representation of the Drawable
     */
    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
