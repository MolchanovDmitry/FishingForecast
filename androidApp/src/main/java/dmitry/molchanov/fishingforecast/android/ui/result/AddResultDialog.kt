package dmitry.molchanov.fishingforecast.android.ui.result

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddResultDialog(
    dismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = { dismiss() },
        text = {
            Text("Some stub text")
        },
        buttons = {
            Text(
                "Отменить",
                color = MaterialTheme.colors.primary,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        dismiss()
                    }
            )
            Text(
                "Сохранить",
                color = MaterialTheme.colors.primary,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        dismiss()
                    }
            )
        }
    )
}