package com.example.yandexcupsemifinal

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.yandexcupsemifinal.UIComponents.Details.FrameInfo

import com.example.yandexcupsemifinal.UIComponents.Details.PathData
import com.example.yandexcupsemifinal.UIComponents.Details.saveDrawing
import kotlinx.coroutines.launch

open class Figure{

}
data class Circle(
    val center: Offset,
    val color:Color,
    val radius: Float
):Figure()

data class Rectangle(
    val topLeft: Offset,
    val size: androidx.compose.ui.geometry.Size,
    val color: Color
):Figure()


enum class TypesOfDrawable{
    LINE,
    CIRCLE,
    TRIANGLE,
    RECTANGLE,
}
enum class TypesOfFrame{
    EMPTY,
    COPY
}

class UIViewModel: ViewModel() {

    val menuExpanded = mutableStateOf(false) // состояние выпадающих фигур
    fun changeMenuExpandedState(){
        menuColorsExpanded.value = false
        menuExpanded.value = !menuExpanded.value
    }

    val menuColorsExpanded = mutableStateOf(false) // состояние выпадающих цветов
    fun changeMenuColorsExpandedState(){
        menuExpanded.value = false
        menuColorsExpanded.value = !menuColorsExpanded.value
    }

    val deletingVariantsIsVisible = mutableStateOf(false)

    fun changeDeletingVariantsIsVisible(){
        deletingVariantsIsVisible.value = !deletingVariantsIsVisible.value
    }

    val addingFramesIsVisible = mutableStateOf(false)
    fun changeAddingFramesIsVisible(){
        addingFramesIsVisible.value = !addingFramesIsVisible.value
    }

    val palletTint =  mutableStateOf(Color.Unspecified)
    fun changePalletTint(){
        if (palletTint.value == Color.Unspecified){
            palletTint.value = Color.Green
        }else{
            palletTint.value = Color.Unspecified
        }
    }
    fun turnOfGreenPallet(){
        palletTint.value = Color.Unspecified
    }

    val pencilTint = mutableStateOf(Color.Unspecified) // bottomBar параметры переключения цвета (на зеленый и обратно) при выборе
    val brushTint =  mutableStateOf(Color.Unspecified)
    val eraserTint = mutableStateOf(Color.Unspecified)
    val instrumentsTint =  mutableStateOf(Color.Unspecified)
    val colorCircleTint = mutableStateOf(Color.White)

    val colorCircleRingTint = mutableStateOf(Color.Unspecified)

    val list = listOf(pencilTint,brushTint,eraserTint,instrumentsTint,palletTint)

    fun deleteGreenFromIcons(){
        list.forEach{
            it.value = Color.Unspecified
        }
    }

    fun closeAllMenu(){
        menuExpanded.value = false // убираем выпадающие окна
        menuColorsExpanded.value = false
        colorCircleRingTint.value = Color.Unspecified // выключение обводки круга
    }

    fun chosenOne(number: Int){
        list.forEach{
            it.value = Color.Unspecified
        }
        when(number){
            1 -> {pencilTint.value = Color.Green; viewModel.closeAllMenu()}
            2 -> {brushTint.value = Color.Green;viewModel.closeAllMenu()}
            3 -> {eraserTint.value = Color.Green; viewModel.closeAllMenu()}
            4 -> {instrumentsTint.value = Color.Green; colorCircleRingTint.value = Color.Unspecified}
            //5 -> colorTint.value = Color.Red
        }
    }

    // --------
    //val listOfSaveFrames = mutableStateListOf<Bitmap>()
    val totalNumberFrames = mutableStateOf(1)
    val numberOfCurrentFrame = mutableStateOf(0) // номер текущего кадра

    fun deleteCurrentFrame(){
        val number = getNumberOfCurrentFrame()
        if (number>=0 && number<listOfFrames.size ){
            listOfFrames.removeAt(getNumberOfCurrentFrame())
            if (listOfFrames.size==0){
                listOfFrames.add(createEmptyFrame())
            }


            Log.i("PPPPPP", "")
            numberOfCurrentFrame.value = listOfFrames.size-1
            changeCurrentFrame()
            changePreviousFigureList()
            changePreviousPathList()



        }

    }

    fun deleteAllFrames(){
        listOfFrames.clear()
        listOfFrames.add(createEmptyFrame())
        numberOfCurrentFrame.value = 0
        changePreviousPathList()
        changePreviousFigureList()
        changeCurrentFrame()

    }

    fun getNumberOfCurrentFrame():Int{
        return numberOfCurrentFrame.value
    }

    val listOfFrames = mutableStateListOf(
        createEmptyFrame()
    )
    //-------
    fun getFrameWithNumber(number: Int):FrameInfo{
        return listOfFrames[number]
    }


    fun createEmptyFrame(): FrameInfo{
        return FrameInfo(
            paths = mutableStateListOf(),
            figures = mutableStateListOf(),
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        )
    }

    val isArasing = mutableStateOf(false)

    val previousPathList = mutableStateListOf<PathData>()
    val previousFigureList = mutableStateListOf<Figure>()

    fun changePreviousPathList(){
        previousPathList.clear()
        if (getNumberOfCurrentFrame()>0){
            previousPathList.addAll(getFrameWithNumber(getNumberOfCurrentFrame()-1).paths.toList().toMutableStateList())
        }

    }

    fun changePreviousFigureList(){
        previousFigureList.clear()
        if (getNumberOfCurrentFrame()>0){
            previousFigureList.addAll(getFrameWithNumber(getNumberOfCurrentFrame()-1).figures.toList().toMutableStateList())
        }

    }

    fun changeCurrentFrame(){
        pathList.clear()
        figureList.clear()
        pathList.addAll(getFrameWithNumber(if (getNumberOfCurrentFrame()<=0) 0 else getNumberOfCurrentFrame()).paths.toList().toMutableStateList())
        figureList.addAll(getFrameWithNumber(if (getNumberOfCurrentFrame()<=0) 0 else getNumberOfCurrentFrame()).figures.toList().toMutableStateList())
    }


    val pathList = mutableStateListOf<PathData>()
    val figureList = mutableStateListOf<Figure>()

    fun removeFigure(figure: Figure) {
        figureList.remove(figure)
    }

    val currentTypeOfDrawble = mutableStateOf(TypesOfDrawable.LINE) // тип рисования



    fun saveFrame() {
        if (canvasWidth.value > 1 && canvasHight.value>1) {
            saveDrawing(
                paths = pathList,
                figureList,
                canvasWidth.value,
                canvasHight.value
            ) { bitmap ->
                Log.i(
                    "LLLLLLLL",
                    "Текущий номер:${getNumberOfCurrentFrame()},Размер списка:${listOfFrames.size}"
                )

                getFrameWithNumber(getNumberOfCurrentFrame()).paths = pathList.toList().toMutableStateList()            //  |
                getFrameWithNumber(getNumberOfCurrentFrame()).figures = figureList.toList().toMutableStateList()       //   |переписали и сохранили текущий кадр
                getFrameWithNumber(getNumberOfCurrentFrame()).bitmap = bitmap.copy(bitmap.config, true)       //   |
            }
        }
    }
//    fun addFrame(type:TypesOfFrame){
//        val frameCopy = listOfFrames[getNumberOfCurrentFrame()].copy()
//        pathList.clear() // удаляем текущие рисунки
//        figureList.clear() // удаляем текущие фигуры
//
//        if (listOfFrames.size-1 == getNumberOfCurrentFrame()){ //если это последний кадр
//            if (type == TypesOfFrame.EMPTY){
//                listOfFrames.add(createEmptyFrame())// создали и добавили новый пустой кадр
//            }else{
//                listOfFrames.add(frameCopy)
//                Log.d("LLLLL", "999090")
//            }
//            numberOfCurrentFrame.value = listOfFrames.size - 1
//        }else{
//            numberOfCurrentFrame.value = listOfFrames.size - 1
//        }
//        if (numberOfCurrentFrame.value-1>=0){
//            changePreviousPathList()
//            changePreviousFigureList()
//        }
//        if (numberOfCurrentFrame.value-1>=0){
//            previousPathList.addAll(
//                getFrameWithNumber(getNumberOfCurrentFrame()-1).paths.toList().toMutableStateList())
//            previousFigureList.addAll(
//                getFrameWithNumber(getNumberOfCurrentFrame()-1).figures.toList().toMutableStateList())
//        }
//
//    }

    fun addFrame(type: TypesOfFrame) {
        // Создаем новый кадр в зависимости от указанного типа
        val newFrame: FrameInfo = if (type == TypesOfFrame.EMPTY) {
            // Создаем пустой кадр
            createEmptyFrame()
        } else {
            // Создаем копию текущего кадра
            val currentFrame = listOfFrames[getNumberOfCurrentFrame()].copy()
            FrameInfo(
                paths = currentFrame.paths.toList().toMutableStateList(),
                figures = currentFrame.figures.toList().toMutableStateList(),
                bitmap = currentFrame.bitmap.copy(currentFrame.bitmap.config, true)
            )
        }

        // Очищаем текущие списки рисунков и фигур
        //pathList.clear()
        //figureList.clear()

        // Добавляем новый кадр в конец списка
        listOfFrames.add(newFrame)
        Log.d("LogTag", "Добавлен новый кадр в конец списка")

        // Обновляем номер текущего кадра на последний индекс списка
        numberOfCurrentFrame.value = listOfFrames.size - 1
        changeCurrentFrame()
        // Обновляем предыдущие списки рисунков и фигур, если есть предыдущий кадр
        if (numberOfCurrentFrame.value - 1 >= 0) {
            val previousFrame = listOfFrames[numberOfCurrentFrame.value - 1]
            previousPathList.clear()
            previousFigureList.clear()
            previousPathList.addAll(previousFrame.paths)
            previousFigureList.addAll(previousFrame.figures)

        }
    }

    val showDialog = mutableStateOf(false) // показ списка слайдов

    val canvasHight = mutableStateOf<Int>(1) // размеры холста
    val canvasWidth = mutableStateOf<Int>(1)


    var isPlaying = mutableStateOf(true) // воспроизведение анимации

    var lastAction = TypesOfDrawable.LINE
    val lastLine = mutableStateOf<PathData?>(PathData())
    val isLastLineWas = mutableStateOf(false)

    val deleteWas = mutableStateOf(false)

    companion object{ // объект ViewModel
        val viewModel = UIViewModel()
    }
}
