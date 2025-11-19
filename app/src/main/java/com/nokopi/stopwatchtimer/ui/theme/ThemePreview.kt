package com.nokopi.stopwatchtimer.ui.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * ライトモードとダークモードの両方でプレビューを表示するカスタムアノテーション
 */
@Preview(
    name = "Light Mode, Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=pixel_9_pro,orientation=portrait"
)
@Preview(
    name = "Dark Mode, Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_9_pro,orientation=portrait"
)
@Preview(
    name = "Light Mode, Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = "spec:parent=pixel_9_pro,orientation=landscape"
)
@Preview(
    name = "Dark Mode. Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_9_pro,orientation=landscape"
)
annotation class ThemePreview
