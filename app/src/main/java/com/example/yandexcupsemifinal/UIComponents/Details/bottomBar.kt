package com.example.yandexcupsemifinal.UIComponents.Details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.TypesOfDrawable
import com.example.yandexcupsemifinal.UIViewModel


@Composable
fun bottomBar(modifier: Modifier){
    val viewModel = UIViewModel.viewModel

    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        IconButton(modifier = Modifier.padding(end = 5.dp), onClick = {viewModel.chosenOne(1); viewModel.isArasing.value = false; viewModel.currentTypeOfDrawble.value = TypesOfDrawable.LINE } ) {
            Icon( painter = painterResource( R.drawable.pencil), contentDescription = "",tint = viewModel.pencilTint.value)
        }
        IconButton(modifier = Modifier.padding(end = 5.dp), onClick = {viewModel.isArasing.value = false; viewModel.chosenOne(2)} ) {
            Icon( painter = painterResource( R.drawable.brush), contentDescription = "",tint = viewModel.brushTint.value)
        }
        IconButton(modifier = Modifier.padding(end = 5.dp),onClick = {viewModel.chosenOne(3); viewModel.isArasing.value = true } ) {
            Icon( painter = painterResource( R.drawable.erase), contentDescription = "",tint = viewModel.eraserTint.value)
        }
        IconButton(modifier = Modifier.padding(end = 5.dp),onClick = { viewModel.isArasing.value = false;UIViewModel.viewModel.changeMenuExpandedState(); viewModel.chosenOne(4)} ) {
                Icon( painter = painterResource( R.drawable.instruments), contentDescription = "", tint = viewModel.instrumentsTint.value)
        }

        IconButton(modifier = Modifier.padding(end = 5.dp), onClick = {viewModel.isArasing.value = false;UIViewModel.viewModel.changeMenuColorsExpandedState(); viewModel.colorCircleRingTint.value = Color(0xFFA8DB10) ; viewModel.deleteGreenFromIcons()} ) {
            //Icon( painter = painterResource( R.drawable.green_ring_circle), contentDescription = "", tint = viewModel.colorTint.value)
            CombinedIcon()
        }
    }
}

 // комбинированная иконка из зеленого кольца и внутреннего цвета
@Composable
fun FillOnlyIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_circle_fill_only),
        contentDescription = null,
        tint = UIViewModel.viewModel.colorCircleTint.value // Не накладываем доп. цвет
    )
}

@Composable
fun StrokeOnlyIcon() {
    Icon(
        painter = painterResource(R.drawable.ic_circle_stroke_only),
        contentDescription = null,
        tint = UIViewModel.viewModel.colorCircleRingTint.value // Не накладываем доп. цвет
    )
}

@Composable
fun CombinedIcon() {
    Box(
        modifier = Modifier.size(33.dp)
    ) {
        FillOnlyIcon()  // слой с заливкой
        StrokeOnlyIcon()  // слой с обводкой
    }
}


//@Preview(showBackground = true)
//@Composable
//fun bottomBarPreview(){
//    bottomBar()
//}