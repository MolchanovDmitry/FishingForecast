package dmitry.molchanov.fishingforecast.android.ui.result

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import dmitry.molchanov.fishingforecast.model.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.viewModel
import java.io.File

@Composable
fun ResultScreen(onResultClick: (Result) -> Unit) {
    val vm by viewModel<ResultViewModel>()
    val state = vm.stateFlow.collectAsState()
    val results = state.value.results
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = results) { result ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = result.name, modifier = Modifier.clickable {
                        onResultClick(result)
                    })
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(90.dp)
                    .padding(8.dp)
                    .clickable { vm.onAction(AddResultClickAction()) }
            )
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
                    .clickable { vm.onAction(ShareClick()) }
            )
            Icon(
                imageVector = Icons.Filled.ImportExport,
                contentDescription = "Share",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
                    .clickable { vm.onAction(ShareClick()) }
            )
        }


    }
    if (state.value.shouldShowDialog) {
        AddResultDialog(vm)
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        vm.messageFlow
            .onEach { event ->
                when (event) {
                    is NullMapPoint -> Toast.makeText(context, "Выберите точку", Toast.LENGTH_SHORT).show()
                    is ShareFile -> shareFile(context = context, filePath = event.filePath)
                }
            }
            .launchIn(this)
    }

}

private fun shareFile(context: Context, filePath: String) {

    val contentUri: Uri = FileProvider.getUriForFile(context, "com.example.app.fileprovider",   File(filePath))

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
    shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri))
    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
    context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
}
