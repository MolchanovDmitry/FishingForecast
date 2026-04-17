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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import dmitry.molchanov.domain.model.Result
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultScreen(onResultClick: (Result) -> Unit) {
    val vm = koinViewModel<ResultViewModel>()
    val state = vm.stateFlow.collectAsState()
    val context = LocalContext.current
    var shouldOpenFile by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let(vm::importResult)
        inputStream?.close() // TODO вынести во viewmodel
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результаты") },
                actions = {
                    IconButton(onClick = { vm.onAction(ToggleSortOrder()) }) {
                        Icon(
                            Icons.Filled.SwapVert,
                            contentDescription = "Сортировка"
                        )
                    }
                    Text(
                        text = if (state.value.sortByRating) "По оценке" else "По дате",
                        modifier = Modifier.padding(end = 16.dp),
                        fontSize = 12.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = state.value.resultsWithDates, key = { it.result.id }) { item ->
                ResultItem(item, vm) { result ->
                    onResultClick(result)
                }
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
            Button(
                modifier = Modifier.padding(4.dp),
                onClick = { vm.onAction(AddResultClickAction()) }
            ) {
                Text("Добавить новый результат.")
            }
            Button(modifier = Modifier.padding(4.dp), onClick = {
                // TODO
                if (ContextCompat.checkSelfPermission(
                        context,
                        WRITE_EXTERNAL_STORAGE
                    ) != PERMISSION_GRANTED
                ) {
                    writePermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
                }
                vm.onAction(SaveToStorageAndShareClick())
            }) {
                Text("Поделиться результатами.")
            }
            Button(modifier = Modifier.padding(4.dp), onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        WRITE_EXTERNAL_STORAGE
                    ) != PERMISSION_GRANTED
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
    if (state.value.shouldShowEditDialog) {
        EditResultDialog(vm)
    }

    LaunchedEffect(key1 = Unit) {
        vm.messageFlow.collect { event ->
            when (event) {
                is Error -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is ShareResult -> shareResult(context, event.data, event.fileName)
            }
        }
    }
    }
}

private fun shareResult(context: Context, data: String, fileName: String) {
    val file = java.io.File(context.cacheDir, fileName)
    file.writeText(data)

    val contentUri: Uri = androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(contentUri, context.contentResolver.getType(contentUri))
        putExtra(Intent.EXTRA_STREAM, contentUri)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Поделиться результатами"))
}

@Composable
private fun ResultItem(item: ResultWithDates, vm: ResultViewModel, onClick: (Result) -> Unit) {
    val dateText = when {
        item.minDate != null && item.maxDate != null && item.minDate != item.maxDate ->
            "${ResultViewModel.formatDate(item.minDate)} — ${ResultViewModel.formatDate(item.maxDate)}"
        item.minDate != null ->
            ResultViewModel.formatDate(item.minDate)
        else -> "нет данных"
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(item.result) }
        .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.result.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dateText,
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
            // Звёзды рейтинга
            Row {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= (item.result.rating ?: 0)) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "$i звезд",
                        tint = if (i <= (item.result.rating ?: 0))
                            androidx.compose.ui.graphics.Color(0xFFFFC107)
                        else
                            androidx.compose.ui.graphics.Color.LightGray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { vm.onAction(SetRating(item.result.id, i)) }
                    )
                }
            }
        }
        IconButton(onClick = { vm.onAction(EditResultClick(item.result)) }) {
            Icon(Icons.Filled.Edit, contentDescription = "Редактировать")
        }
    }
}
