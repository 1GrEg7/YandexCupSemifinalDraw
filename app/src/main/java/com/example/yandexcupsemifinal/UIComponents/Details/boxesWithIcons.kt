package com.example.yandexcupsemifinal.UIComponents.Details

import android.provider.Contacts.Intents.UI
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.TypesOfDrawable
import com.example.yandexcupsemifinal.TypesOfFrame
import com.example.yandexcupsemifinal.UIViewModel
import com.example.yandexcupsemifinal.UIViewModel.Companion
import kotlinx.coroutines.launch


@Composable
fun recBoxIconInstruments(modifier: Modifier) {
    val viewModel = UIViewModel.viewModel
    AnimatedVisibility(visible = UIViewModel.viewModel.menuExpanded.value) {
        Box(
            modifier = modifier
        ) {
            Row (
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(onClick = { viewModel.currentTypeOfDrawble.value = TypesOfDrawable.RECTANGLE }) {
                    Icon(
                        painter = painterResource(R.drawable.square),
                        contentDescription = "Иконка 1",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = { viewModel.currentTypeOfDrawble.value = TypesOfDrawable.CIRCLE }) {
                    Icon(
                        painter = painterResource(R.drawable.circle),
                        contentDescription = "Иконка 2",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = { /* Действие для икноки 3 */ }) {
                    Icon(
                        painter = painterResource(R.drawable.triangle),
                        contentDescription = "Иконка 3",
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = { /* Действие для икноки 4 */ }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_up),
                        contentDescription = "Иконка 4",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}


@Composable
fun recBoxIconColors(modifier: Modifier) {
    val viewModel = UIViewModel.viewModel
    AnimatedVisibility(visible = UIViewModel.viewModel.menuColorsExpanded.value) {
        Box(
            modifier = modifier
        ) {
            Row (
                modifier = Modifier
                    .padding(8.dp)
            ) {
                IconButton(onClick = { viewModel.changePalletTint() }) { // Открытие большой гаммы цветов
                    Icon(
                        painter = painterResource(R.drawable.palette),
                        contentDescription = "Иконка 1",
                        tint = viewModel.palletTint.value
                    )
                }
                IconButton(onClick = { viewModel.colorCircleTint.value =Color.White; viewModel.turnOfGreenPallet()  }) {
                    Icon(
                        painter = painterResource(R.drawable.color_blue),
                        contentDescription = "Иконка 2",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { viewModel.colorCircleTint.value =Color.Red; viewModel.turnOfGreenPallet()  }) {
                    Icon(
                        painter = painterResource(R.drawable.color_blue),
                        contentDescription = "Иконка 3",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { viewModel.colorCircleTint.value =Color.Black ; viewModel.turnOfGreenPallet() }) {
                    Icon(
                        painter = painterResource(R.drawable.color_blue),
                        contentDescription = "Иконка 4",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = { viewModel.colorCircleTint.value =Color.Blue ; viewModel.turnOfGreenPallet() }) {
                    Icon(
                        painter = painterResource(R.drawable.color_blue),
                        contentDescription = "Иконка 4",
                        tint = Color.Blue
                    )
                }
            }
        }
    }
}

@Composable
fun recBoxOfDeletingFrames(modifier: Modifier) {
    val viewModel = UIViewModel.viewModel
        val context = LocalContext.current
        AnimatedVisibility(visible = UIViewModel.viewModel.deletingVariantsIsVisible.value) {

                Column (modifier = modifier.width(200.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedButton(modifier = Modifier.padding(top = 5.dp), onClick = {viewModel.deleteCurrentFrame(); Toast.makeText(context,"Кадр удален",Toast.LENGTH_SHORT).show()}) {
                        Text(color = Color.Black, text ="Удалить текущий кадр")
                    }
                    OutlinedButton(modifier = Modifier.padding(bottom =  5.dp), onClick = {viewModel.deleteAllFrames(); Toast.makeText(context,"Все кадры удалены",Toast.LENGTH_SHORT).show()} ) {
                        Text( color = Color.Black, text ="Удалить все кадры")
                    }
                }
        }

}

@Composable
fun recBoxOfAnddingFrames(modifier: Modifier) {
    val viewModel = UIViewModel.viewModel
    val context = LocalContext.current
    AnimatedVisibility(visible = UIViewModel.viewModel.addingFramesIsVisible.value) {
        val scope = rememberCoroutineScope()

        Column (modifier = modifier.width(220.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(modifier = Modifier.padding(top = 5.dp), onClick = {
                Toast.makeText(context,"Добавлен пустой кадр",Toast.LENGTH_SHORT).show()
                viewModel.saveFrame()
                viewModel.addFrame(TypesOfFrame.EMPTY)
            }) {
                Text(color = Color.Black, text ="Добавить пустой кадр")
            }
            OutlinedButton(modifier = Modifier.padding(bottom =  5.dp), onClick = { viewModel.saveFrame()
                Toast.makeText(context,"Кадр сохранен",Toast.LENGTH_SHORT).show()
            } ) {

                Text( color = Color.Black, text ="Сохранить текущий кадр")
            }
        }
    }

}