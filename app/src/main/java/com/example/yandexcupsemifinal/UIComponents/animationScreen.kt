package com.example.yandexcupsemifinal.UIComponents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.UIComponents.Details.bottomBar
import com.example.yandexcupsemifinal.UIComponents.Details.canvas
import com.example.yandexcupsemifinal.UIComponents.Details.listOfFrames
import com.example.yandexcupsemifinal.UIComponents.Details.upBar
import com.example.yandexcupsemifinal.UIViewModel
import kotlinx.coroutines.delay




val viewModel = UIViewModel.viewModel

@Composable
fun animationScreen(navController: NavController){
    val viewModel = UIViewModel.viewModel
    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        animationMenu(Modifier.fillMaxWidth().weight(1f), navController)
        onlyCanvas(Modifier.padding(10.dp).weight(10f))
        Spacer(modifier = Modifier.weight(0.5f))

    }
}
@Composable
fun animationMenu(modifier: Modifier, navController: NavController){
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Row(Modifier.align(Alignment.CenterVertically).padding(top = 10.dp)){
            IconButton(onClick = {navController.navigate("mainScreen");viewModel.isPlaying.value = false } ) {
                Icon( painter = painterResource( R.drawable.square), contentDescription = "",tint = Color.Unspecified)
            }
            IconButton(onClick = { viewModel.isPlaying.value = false} ) {
                Icon( painter = painterResource( R.drawable.pauseunactive), contentDescription = "",tint = Color.Unspecified)
            }
            IconButton(onClick = { viewModel.isPlaying.value = true} ) {
                Icon( painter = painterResource( R.drawable.playactive), contentDescription = "",tint = Color.Unspecified)
            }
        }
    }
}


@Composable
fun onlyCanvas(modifier: Modifier){
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter){
        Image(
            painter = painterResource(R.drawable.canvas),
            contentDescription = "Черный квадрат",
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )
        val currentFrameIndex = remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                if (viewModel.isPlaying.value) {
                    delay(100)
                    currentFrameIndex.value = (currentFrameIndex.value + 1) % viewModel.listOfFrames.size
                }else{
                    delay(100)
                }
            }
        }


        Image(
            modifier = Modifier.padding(bottom = 40.dp),
            bitmap = viewModel.listOfFrames[currentFrameIndex.value].bitmap.asImageBitmap(),
            contentDescription = "12345"
        )
    }
}



//@Composable
//@Preview(showBackground = true)
//fun animationScreenPreview(){
//    animationScreen()
//}