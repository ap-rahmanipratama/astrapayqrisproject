package com.astrapay.astrapay_qris_project

import android.content.Intent
import android.os.Bundle
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
import com.astrapay.apqrisscanner.ApQrisScannerActivity
import com.astrapay.astrapay_qris_project.ui.theme.AstrapayqrisprojectTheme
import com.astrapay.qrissdk.QrisMainActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AstrapayqrisprojectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .padding(16.dp) // Optional padding
            .fillMaxWidth() // Optional fill the width of the parent
    ) {
        Text(
            text = "Hello $name!",
            modifier = Modifier.padding(bottom = 8.dp) // Space between text and button
        )
        Button(
            onClick = {
                val intent = Intent(context, QrisMainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.align(CenterHorizontally) // Optional: center the button
        ) {
            Text("Qris Sdk")
        }
        Button(
            onClick = {
                val intentQris = Intent(context, ApQrisScannerActivity::class.java)
                context.startActivity(intentQris)
            },
            modifier = Modifier.align(CenterHorizontally) // Optional: center the button
        ) {
            Text("Scanner")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AstrapayqrisprojectTheme {
        Greeting("Android")
    }
}