package com.gauravbajaj.newsapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.testTag

/**
 * A composable function that displays a loading indicator centered within a Box.
 *
 * This function creates a full-screen Box with a CircularProgressIndicator at its center,
 * indicating a loading state in the UI. The Box is padded according to the provided PaddingValues.
 *
 * @param padding The padding to be applied to the Box containing the loading indicator.
 *                This allows for flexible positioning of the indicator within the parent layout.
 *
 * @return Unit. This function does not return a value, but instead creates and displays
 *         a UI component as a side effect.
 */
@Composable
fun LoadingIndicator(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
    }
}