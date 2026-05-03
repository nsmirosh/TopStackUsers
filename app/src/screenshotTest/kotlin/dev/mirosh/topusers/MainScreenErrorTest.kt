package dev.mirosh.topusers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.mirosh.topusers.ui.main.MainScreenError


@PreviewTest
@Preview(showBackground = true)
@Composable
fun MainScreenErrorPreview() {
    MainScreenError(modifier = Modifier.fillMaxSize())
}
