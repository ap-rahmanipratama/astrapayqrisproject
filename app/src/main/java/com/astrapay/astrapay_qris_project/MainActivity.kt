package com.astrapay.astrapay_qris_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astrapay.apqrisscanner.ApQrisScanner
import com.astrapay.apqrisscanner.ApScannerListener
import com.astrapay.apqrisscanner.ApScannerSetup
import com.astrapay.apqrisscanner.EventType
import com.astrapay.astrapay_qris_project.ui.theme.AstrapayqrisprojectTheme
import com.astrapay.qrissdk.QrisMainActivity

class MainActivity : ComponentActivity(), ApScannerListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AstrapayqrisprojectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        onScannerClick = {
                            val setup = ApScannerSetup(context = this, listener = this)
                            ApQrisScanner.execute(apScannerSetup = setup)
                        }
                    )
                }
            }
        }
    }

    override fun onComplete(type: EventType) {
        Log.d("Listener", "onComplete")
    }

    override fun onQrisScanned(data: String) {
        Log.d("Listener", "data: $data")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onScannerClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Hello $name!",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(
            onClick = {
                val intent = Intent(context, QrisMainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text("Qris Sdk")
        }
        Button(
            onClick = onScannerClick,
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text("Scanner")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AstrapayqrisprojectTheme {
        Greeting("Android", onScannerClick = {})
    }
}