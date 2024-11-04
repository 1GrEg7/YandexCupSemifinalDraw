package com.example.yandexcupsemifinal.UIComponents.Details

import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.UIViewModel
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


@Composable
fun listOfFrames(){
    val viewModel = UIViewModel.viewModel

    val imageModifier = Modifier
        .fillMaxHeight()
        .width(170.dp)
        .padding(10.dp)
        .clip(RoundedCornerShape(24.dp))

    val listOfFrames = viewModel.listOfFrames



        Dialog(
            onDismissRequest = { viewModel.showDialog.value = false }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ){
                LazyRow (modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Unspecified)

                ) {
                     itemsIndexed(listOfFrames) { index,
                        item:  FrameInfo ->Box(){

                        Image(
                            painter = painterResource(R.drawable.canvas),contentDescription = "Белый холст",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop)
                        Box(modifier = Modifier.clickable ( onClick = {
                            viewModel.numberOfCurrentFrame.value = index

                            viewModel.pathList.clear()
                            viewModel.figureList.clear()

                            viewModel.previousPathList.clear()
                            viewModel.previousFigureList.clear()




                            viewModel.pathList.addAll(viewModel.getFrameWithNumber(viewModel.getNumberOfCurrentFrame()).paths.toList().toMutableStateList())
                            viewModel.figureList.addAll(viewModel.getFrameWithNumber(viewModel.getNumberOfCurrentFrame()).figures.toList().toMutableStateList())

                            if (viewModel.numberOfCurrentFrame.value-1>=0){
                                viewModel.previousPathList.addAll(viewModel.getFrameWithNumber(viewModel.getNumberOfCurrentFrame()-1).paths.toList().toMutableStateList())
                                viewModel.previousFigureList.addAll(viewModel.getFrameWithNumber(viewModel.getNumberOfCurrentFrame()-1).figures.toList().toMutableStateList())
                            }

                            Log.d("Current FrameInfo", " PathSize:${viewModel.pathList.size}  ${viewModel.figureList.size} $index")
                            Log.d("Current FrameInfo", " ${ viewModel.numberOfCurrentFrame.value}")
                            Log.d("Current FrameInfo", "Path: ${ viewModel.getFrameWithNumber(viewModel.getNumberOfCurrentFrame()).paths.size}")
                        }
                            )){
                            Image(
                                modifier = imageModifier,
                                painter = BitmapPainter(item.bitmap.asImageBitmap()),
                                contentDescription = "Один из кадров",
                                contentScale = ContentScale.Crop
                            )
                        }
                        }
                    }

                }
            }
        }





}

@Preview(showBackground = true)
@Composable
fun listOfFramesPreview(){
    listOfFrames()
}