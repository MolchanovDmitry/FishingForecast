package dmitry.molchanov.fishingforecast.android.ui.result

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
    val context = LocalContext.current
    var shouldOpenFile by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        context.contentResolver.openInputStream(uri)?.let(vm::importResult)
    }
    // TODO сделать красиво
    val writePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    val readPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        shouldOpenFile = true
    }
    if (shouldOpenFile) {
        shouldOpenFile = false
        launcher.launch("text/*")
    }

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
            Button(modifier = Modifier.padding(4.dp), onClick = { vm.onAction(AddResultClickAction()) }) {
                Text("Добавить новый результат.")
            }
            Button(modifier = Modifier.padding(4.dp), onClick = {
                // TODO
                if (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED
                ) {
                    writePermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
                }
                vm.onAction(SaveToStorageAndShareClick())
            }) {
                Text("Поделиться результатами.")
            }
            Button(modifier = Modifier.padding(4.dp), onClick = {
                if (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED
                ) {
                    readPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
                } else {
                    shouldOpenFile = true
                }
            }) {
                Text("Импортировать результаты.")
            }
        }


    }
    if (state.value.shouldShowDialog) {
        AddResultDialog(vm)
    }

    LaunchedEffect(key1 = Unit) {
        vm.messageFlow.onEach { event ->
            when (event) {
                is NullMapPoint -> Toast.makeText(context, "Выберите точку", Toast.LENGTH_SHORT).show()
                is ShareFile -> shareFile(context = context, filePath = event.filePath)
            }
        }.launchIn(this)
    }

}

@Composable
fun importFile() {
    /*val chooseFile: Intent = Intent(Intent.ACTION_GET_CONTENT)
    chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
    chooseFile.type = "file/*"
    val intent = Intent.createChooser(chooseFile, "Choose a file")
    startActivityForResult(context as Activity, intent,1,null)
    */
    */
}

private fun shareFile(context: Context, filePath: String) {

    val contentUri: Uri = FileProvider.getUriForFile(context, "com.example.app.fileprovider", File(filePath))

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
    shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri))
    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
    context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
}
