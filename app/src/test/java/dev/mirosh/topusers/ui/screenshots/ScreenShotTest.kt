package dev.mirosh.topusers.ui.screenshots

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import dev.mirosh.topusers.ui.main.ListItemWithLongNameFollowing
import dev.mirosh.topusers.ui.main.ListItemWithLongNameNotFollowing
import dev.mirosh.topusers.ui.main.ListItemWithShortNameFollowing
import dev.mirosh.topusers.ui.main.ListItemWithShortNameNotFollowing
import org.junit.Rule
import org.junit.Test


//Just playing with this here
class ScreenShotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = RenderingMode.SHRINK,
        maxPercentDifference = 2.0,
    )

    @Test
    fun testListItemWithLongNameFollowing() {
        paparazzi.snapshot {
            ListItemWithLongNameFollowing()
        }
    }

    @Test
    fun testListItemWithLongNameNotFollowing() {
        paparazzi.snapshot {
            ListItemWithLongNameNotFollowing()
        }
    }

    @Test
    fun testListItemWithShortNameFollowing() {
        paparazzi.snapshot {
            ListItemWithShortNameFollowing()
        }
    }

    @Test
    fun testListItemWithShortNameNotFollowing() {
        paparazzi.snapshot {
            ListItemWithShortNameNotFollowing()
        }
    }
}
