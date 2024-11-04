package com.example.yandexcupsemifinal.UIComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.yandexcupsemifinal.UIComponents.Details.bottomBar
import com.example.yandexcupsemifinal.UIComponents.Details.canvas
import com.example.yandexcupsemifinal.UIComponents.Details.listOfFrames
//import com.example.yandexcupsemifinal.UIComponents.Details.canvas
import com.example.yandexcupsemifinal.UIComponents.Details.upBar
import com.example.yandexcupsemifinal.UIViewModel

@Composable
fun mainScreen(navController: NavController){

    val viewModel = UIViewModel.viewModel
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (viewModel.showDialog.value){
            listOfFrames()
        }
        upBar(modifier = Modifier.weight(2f).fillMaxSize().padding(top = 20.dp), navController)
        canvas(modifier = Modifier.weight(16f).padding(10.dp))
        bottomBar(modifier = Modifier.weight(2f).fillMaxSize().padding(top = 10.dp))
    }
}


//@Preview(showBackground = true)
//@Composable
//fun mainScreenPreview(){
//    mainScreen()
//}