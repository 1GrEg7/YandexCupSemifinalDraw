package com.example.yandexcupsemifinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yandexcupsemifinal.UIComponents.animationScreen
import com.example.yandexcupsemifinal.UIComponents.mainScreen
import com.example.yandexcupsemifinal.ui.theme.YandexCupSemifinalTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            YandexCupSemifinalTheme {

                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") { mainScreen(navController) }
                    composable("animationScreen") {  animationScreen(navController) }
                }

            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavController) {


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YandexCupSemifinalTheme {
        Greeting("Android")
    }
}