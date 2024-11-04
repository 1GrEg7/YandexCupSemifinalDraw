package com.example.yandexcupsemifinal.UIComponents.Details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.yandexcupsemifinal.Figure
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.TypesOfDrawable
import com.example.yandexcupsemifinal.TypesOfFrame
import com.example.yandexcupsemifinal.UIViewModel

import kotlinx.coroutines.launch



@Composable
fun upBar(modifier: Modifier, navController: NavController){
    val viewModel = UIViewModel.viewModel

    val rightArrowColor = remember{ mutableStateOf(Color.Gray) }


    val lineToReturn = remember { mutableStateOf(PathData()) }
    val figureToReturn = remember { mutableStateOf(Figure()) }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(horizontalArrangement = Arrangement.spacedBy(-20.dp)){
            IconButton(onClick = {
                Log.d("IIIIII","${viewModel.pathList.size}")
                if (!viewModel.deleteWas.value){
                    if (viewModel.lastAction == TypesOfDrawable.LINE && viewModel.pathList.size>0){
                        if (viewModel.pathList.size==3){
                            viewModel.pathList.removeLast()
                            viewModel.pathList.removeLast()
                            viewModel.pathList.removeLast()
                        }else{
                            lineToReturn.value = viewModel.pathList.removeLast()
                            viewModel.pathList.removeLast()

                        }


                    }else{
                        if (viewModel.figureList.size>0)
                            figureToReturn.value = viewModel.figureList.removeLast()
                    }
                    viewModel.deleteWas.value = true
                }

            } ) {
                Icon( painter = painterResource( R.drawable.left_unactive), contentDescription = "",tint = rightArrowColor.value)
            }
            IconButton(onClick = {Log.d("IIIIII","${viewModel.pathList.size}")
                if (viewModel.lastAction == TypesOfDrawable.LINE ) {
                    viewModel.pathList.add(lineToReturn.value)
                }else{
                    viewModel.figureList.add(figureToReturn.value)
                }
                viewModel.deleteWas.value=false
            } ) {
                Icon( painter = painterResource( R.drawable.right_unactive), contentDescription = "",tint = Color.Unspecified)
            }
        }
        Row(){
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            IconButton(onClick = {viewModel.changeDeletingVariantsIsVisible(); viewModel.closeAllMenu(); viewModel.addingFramesIsVisible.value = false} ) {
                Icon( painter = painterResource( R.drawable.bin), contentDescription = "",tint = Color.Unspecified)
            }
            IconButton(onClick = {
                viewModel.addingFramesIsVisible.value = true
                viewModel.closeAllMenu()
                viewModel.deletingVariantsIsVisible.value = false
//                viewModel.saveFrame()
//                viewModel.addFrame(TypesOfFrame.EMPTY)
            }) {
                Icon( painter = painterResource( R.drawable.file_plus), contentDescription = "",tint = Color.Unspecified)
            }
            IconButton(onClick = { viewModel.showDialog.value = true } ) {
                Icon( painter = painterResource( R.drawable.layers), contentDescription = "",tint = Color.Unspecified)
            }
        }
        Row(){
            IconButton(onClick = {} ) {
                Icon( painter = painterResource( R.drawable.pauseunactive), contentDescription = "",tint = Color.Unspecified)
            }
            IconButton(onClick = { if (viewModel.listOfFrames.size>=1){navController.navigate("animationScreen")}; viewModel.isPlaying.value = true } ) {
                Icon( painter = painterResource( R.drawable.playactive), contentDescription = "",tint = Color.Unspecified)
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun upBarPreniew(){
//    upBar()
//}