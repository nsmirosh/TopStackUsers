package dev.mirosh.topusers.ui.screenshots

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import dev.mirosh.topusers.ui.main.MainScreenError
import dev.mirosh.topusers.ui.theme.TopUsersTheme
import org.junit.Rule
import org.junit.Test


class ScreenShotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun newsContentScreenshotTest() {
        paparazzi.snapshot {
            TopUsersTheme {
                MainScreenError()
            }
        }
    }
}

