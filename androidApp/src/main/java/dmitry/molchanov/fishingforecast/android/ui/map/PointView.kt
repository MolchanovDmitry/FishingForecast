package dmitry.molchanov.fishingforecast.android.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@Preview
private fun preview() {
    PointView("Сыктывкар", 100.dp, 100.dp)
}

@Composable
fun PointView(title: String, width: Dp, height: Dp) {
    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.Blue)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = Color.White
        )
    }
}