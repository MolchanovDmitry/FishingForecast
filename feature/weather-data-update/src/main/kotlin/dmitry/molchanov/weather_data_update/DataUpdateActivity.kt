package dmitry.molchanov.weather_data_update

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class DataUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<DataUpdateViewModel>()
        setContent {
            Test()
        }
    }
}

@Composable
private fun Test() {
    Text("hello world")
}