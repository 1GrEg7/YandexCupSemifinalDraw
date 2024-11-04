package com.example.yandexcupsemifinal.UIComponents.Details

import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.Log
import androidx.collection.intFloatMapOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.AndroidPath
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yandexcupsemifinal.R
import com.example.yandexcupsemifinal.UIViewModel
import java.util.Stack
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text


import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size


import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.toRect
import com.example.yandexcupsemifinal.Circle
import com.example.yandexcupsemifinal.Figure
import com.example.yandexcupsemifinal.Rectangle
import com.example.yandexcupsemifinal.TypesOfDrawable
import kotlin.math.sqrt


data class FrameInfo(
    var paths: SnapshotStateList<PathData>,
    var figures: SnapshotStateList<Figure>,
    var bitmap: Bitmap
)

data class PathData(
     val path: Path = Path(),
     val color: Color = Color.Black,
     var strokeWidth: Float = 10f
)

fun erasePathAtPoint2(point: Offset, paths: MutableList<PathData>, eraserRadius: Float) {
    val iterator = paths.listIterator()
    while (iterator.hasNext()) {
        val pathData = iterator.next()
        val path = pathData.path
        Log.d("PPPPP","11111")
        // Проверяем, находится ли точка на расстоянии меньше радиуса ластика от пути
        if (isPointNearPath2(point, path, eraserRadius)) {

            iterator.remove()
            break // Если требуется удалить только один путь. Уберите break, если нужно удалить все подходящие пути
        }
    }
}


fun isPointNearPath2(point: Offset, path: Path, eraserRadius: Float): Boolean {
    val androidPath = path.asAndroidPath()
    val pathMeasure = android.graphics.PathMeasure()
    pathMeasure.setPath(androidPath, false)

    // Итерируем по всем контурам пути
    do {
        val pathLength = pathMeasure.length

        // Разбиваем текущий контур на небольшие отрезки и проверяем расстояние до каждой точки
        val step = eraserRadius / 2 // Шаг может быть меньше радиуса для большей точности
        var distance = 0f
        val position = FloatArray(2)
        while (distance <= pathLength) {
            // Получаем координаты точки на пути на расстоянии 'distance' от начала
            val gotPos = pathMeasure.getPosTan(distance, position, null)
            if (gotPos) {
                val pathPoint = Offset(position[0], position[1])
                // Вычисляем расстояние от точки касания до точки на пути
                val d = (point - pathPoint).getDistance()
                if (d <= eraserRadius) {
                    return true // Если расстояние меньше радиуса ластика, путь подлежит удалению
                }
            }
            distance += step
        }
    } while (pathMeasure.nextContour()) // Переходим к следующему контуру пути

    return false
}


fun eraseFigureAtPoint( offset: Offset) {
    // Ищем фигуру, содержащую точку касания
    // Перебираем список в обратном порядке, чтобы сначала проверять верхние фигуры
    val figureToRemove = UIViewModel.viewModel.figureList.asReversed().find { figure ->
        when (figure) {
            is Circle-> {
                val dx = figure.center.x - offset.x
                val dy = figure.center.y - offset.y
                sqrt(dx * dx + dy * dy) <= figure.radius
            }
            is Rectangle -> {
                val rect = Rect(
                    topLeft = figure.topLeft,
                    bottomRight = Offset(
                        x = figure.topLeft.x - figure.size.width,
                        y = figure.topLeft.y - figure.size.height
                    )
                )
                rect.contains(offset)
            }
            // Добавьте проверки для других типов фигур при необходимости
            else -> false
        }
    }

    // Если фигура найдена, удаляем её
    if (figureToRemove != null) {
        UIViewModel.viewModel.removeFigure(figureToRemove)
    }
}



fun saveDrawing(
    paths: List<PathData>,
    figures: List<Figure>,
    width: Int,
    height: Int,
    onSaved: (Bitmap) -> Unit
) {
    // Создаем Bitmap для отрисовки
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    // Создаем Android Canvas для рисования на Bitmap
    val canvas = Canvas(bitmap.asImageBitmap())

    // Настраиваем Paint для отрисовки
    val paint = Paint().apply {
        style = Paint.Style.STROKE // Линия
        strokeWidth = 10f
        isAntiAlias = true
    }

    // Отрисовываем пути (paths)
    paths.forEach { drawPath ->
        paint.color = drawPath.color.toArgb()
        // Предполагается, что PathData.path уже преобразован к android.graphics.Path
        canvas.drawPath(drawPath.path, paint.asComposePaint())
    }

    // Отрисовываем фигуры (circles и rectangles)
    figures.forEach { figure ->

        when (figure) {
            is Circle -> {
                // Отрисовка круга
                paint.color = figure.color.toArgb()
                canvas.drawCircle(
                    Offset(figure.center.x, figure.center.y),
                    figure.radius,
                    paint.asComposePaint()
                )
            }

            is Rectangle -> {
                // Создаем RectF из topLeft и size
                paint.color = figure.color.toArgb()
                val rect = RectF(
                    figure.topLeft.x,
                    figure.topLeft.y,
                    figure.topLeft.x + figure.size.width,
                    figure.topLeft.y + figure.size.height
                )
                // Отрисовка прямоугольника
                canvas.drawRect(rect.toRect().toComposeRect(), paint.asComposePaint())
            }
            // Добавьте другие фигуры, если необходимо
        }
    }

    // Вызываем коллбек с готовым Bitmap
    onSaved(bitmap)
}



//----------------------------------------------
@Composable
fun canvas(modifier: Modifier){

    val viewModel = UIViewModel.viewModel

    var tempPath = Path()

    var xFigure = 0f
    var yFigure = 0f




    Box(modifier = modifier,contentAlignment = Alignment.Center ) { //contentAlignment = Alignment.BottomCenter

        Image(
            painter = painterResource(R.drawable.canvas),
            contentDescription = "Черный квадрат",
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop
        )
        Box(Modifier.align(Alignment.TopCenter).zIndex(1f)){
            recBoxOfDeletingFrames( Modifier
                .padding(start = 50.dp)
                .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .align(Alignment.TopCenter)
            )
            recBoxOfAnddingFrames(Modifier
                .padding(start = 50.dp)
                .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .align(Alignment.TopCenter)
            )

        }
       // Text(modifier = Modifier.align(Alignment.BottomCenter), text = "jkjkjkjhk")



        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .clipToBounds()
            .pointerInput(true) {


                detectDragGestures(
                    onDragStart = { offset ->
                        tempPath = Path()
                        if (viewModel.currentTypeOfDrawble.value == TypesOfDrawable.LINE) {
                            viewModel.lastAction = TypesOfDrawable.LINE
                            if (viewModel.isArasing.value) {
                                erasePathAtPoint2(offset, viewModel.pathList, 10f)
                            }

                        } else if (viewModel.currentTypeOfDrawble.value == TypesOfDrawable.CIRCLE) {
                            viewModel.lastAction = TypesOfDrawable.CIRCLE
                            xFigure = offset.x
                            yFigure = offset.y
                            viewModel.figureList.add(
                                Circle(
                                    color = viewModel.colorCircleTint.value,
                                    center = Offset(xFigure, yFigure),
                                    radius = 100f
                                )
                            )
                        } else if (viewModel.currentTypeOfDrawble.value == TypesOfDrawable.RECTANGLE) {
                            viewModel.lastAction = TypesOfDrawable.RECTANGLE
                            xFigure = offset.x
                            yFigure = offset.y
                            viewModel.figureList.add(
                                Rectangle(
                                    topLeft = Offset(xFigure, yFigure),
                                    size = Size(200f, 200f),
                                    color = viewModel.colorCircleTint.value
                                )
                            )
                        }
                        viewModel.canvasWidth.value = this.size.width
                        viewModel.canvasHight.value = this.size.height
                        Log.d("Touch", "${viewModel.pathList.size}")
                    },


                    onDragEnd = {
                            viewModel.pathList.add( // после того как отпускаем палец, рисуем еще одну линию полностью, чтобы при добавлении следующей стерлась только одна
                                PathData(
                                    path = tempPath,
                                    color = viewModel.colorCircleTint.value
                                )
                            )
                        viewModel.deleteWas.value = false
                    }
                ) { change, dragAmount ->
                    if (viewModel.isArasing.value) {
                        erasePathAtPoint2(
                            Offset(change.position.x, change.position.y),
                            viewModel.pathList,
                            20f
                        )
                        eraseFigureAtPoint(Offset(change.position.x, change.position.y))
                    } else {
                        if (viewModel.currentTypeOfDrawble.value == TypesOfDrawable.LINE) {
                            tempPath.moveTo(
                                change.position.x - dragAmount.x,
                                change.position.y - dragAmount.y
                            )
                            tempPath.lineTo(
                                change.position.x,
                                change.position.y
                            )

                            if (viewModel.pathList.size > 1) {
                                viewModel.pathList.removeAt(viewModel.pathList.size - 1) // удаляем предыдущую часть линии
                            }
                            viewModel.pathList.add(
                                PathData(
                                    path = tempPath,
                                    color = viewModel.colorCircleTint.value
                                )
                            ) // передать настройки цвета и размер строки для Path
                            if (!viewModel.isLastLineWas.value){
                                viewModel.pathList.add(viewModel.lastLine.value as PathData)
                                viewModel.isLastLineWas.value = true
                            }

                        }

                    }


                }
            }
        ) {
            for (i in 0 until viewModel.pathList.size){
                val it = viewModel.pathList[i]
                drawPath(
                    it.path,
                    color = it.color,
                    style = Stroke(10f)
                )
            }

            viewModel.figureList.forEach {figure ->
                if (figure is Circle){
                    drawCircle(figure.color, radius = figure.radius, center = figure.center, style = Stroke(width = 3.dp.toPx() ))
                }
                if (figure is Rectangle){
                    drawRect(color = figure.color, topLeft =Offset(figure.topLeft.x- size.width/12, figure.topLeft.y- size.height/12) , size = figure.size,style = Stroke(width = 3.dp.toPx()))
                }
            }
            if (viewModel.previousPathList.size>0 ){
                for (i in 0 until viewModel.previousPathList.size){
                    val it = viewModel.previousPathList[i]
                    drawPath(
                        it.path,
                        color = it.color.copy(alpha = 0.07f),
                        style = Stroke(10f)
                    )
                }
            }
            if (viewModel.previousFigureList.size>0 ){
                viewModel.previousFigureList.forEach {figure ->
                    if (figure is Circle){
                        drawCircle(figure.color.copy(alpha = 0.5f), radius = figure.radius, center = figure.center, style = Stroke(width = 3.dp.toPx() ))
                    }
                    if (figure is Rectangle){
                        drawRect(color = figure.color.copy(alpha = 0.5f), topLeft = Offset(figure.topLeft.x - size.width, figure.topLeft.y - size.height) , size = figure.size,style = Stroke(width = 3.dp.toPx()))
                    }
                }
            }






        }


        Box(Modifier.align(Alignment.BottomCenter)) {
            recBoxIconInstruments(
                Modifier
                    .padding(start = 50.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    .align(Alignment.BottomStart))
            recBoxIconColors(
                Modifier
                    .padding(start = 50.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    .align(Alignment.BottomStart))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun canvasPreview(){
//    onlyCanvas()
//}