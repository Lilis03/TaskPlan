package com.elitecode.taskplan.view

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elitecode.taskplan.R
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.components.editarTarea
import com.elitecode.taskplan.components.nuevaTarea
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.viewmodel.TareaViewModel
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarTareaScreen(navController: NavController, id_tarea: String, viewModel: TareaViewModel){
    var tarea by remember { mutableStateOf<Tarea?>(null) }
    val showTareaEditada by viewModel.showTareaEditada
    val context = LocalContext.current

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea){ tareaObtenida ->
            tarea = tareaObtenida
        }
    }
    if(tarea == null){
        CircularProgressIndicator()
        return
    }

   // var titulo by remember { mutableStateOf(tarea?.titulo ?: "") }


    MenuLateral(navController) { paddingValues ->
        Column( modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .padding(26.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier
                    .background(Color(0xffC5E7FF).copy(alpha = 0.1f))
                    .padding(16.dp)
                    //0xffA8D1E7
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Editar tarea",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                            OutlinedTextField(
                                value= viewModel.tarea.value?.titulo ?: "",
                                //value = tarea!!.titulo ?: "",
                                //value = tarea!!.titulo,
                                onValueChange = { viewModel.onTituloChange(it) },
                                placeholder = { Text("Título") },
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(R.drawable.title),
                                        contentDescription = "Icono título",
                                        colorFilter = ColorFilter.tint(Color(0xFF769AC4))
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = Color(0xFF769AC4),
                                    focusedBorderColor = Color(0xFF769AC4),
                                    cursorColor = Color(0xFF769AC4)
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 22.sp
                                ),
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                        OutlinedTextField(
                            value = viewModel.tarea.value?.descripcion ?: "",
                            //value = tarea!!.descripcion ?: "",
                            onValueChange = {  viewModel.onDescripcionChange(it)

                            },
                            placeholder = { Text("Descripción") },
                            leadingIcon = {
                                Image(
                                    painter = painterResource(R.drawable.description),
                                    contentDescription = "Icono descripción",
                                    colorFilter = ColorFilter.tint(Color(0xFF769AC4))
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = Color(0xFF769AC4),
                                focusedBorderColor = Color(0xFF769AC4),
                                cursorColor = Color(0xFF769AC4)
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 17.sp
                            ),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 26.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            TimePickerToModals(modifier = Modifier.weight(1f), onTimeSelected = { hora -> viewModel.onHoraChange(hora)}, id_tarea= id_tarea)
                            DatePickerFieldToModals(modifier = Modifier.weight(1f), onDateSelected = {fecha -> viewModel.onFechaChange(fecha)}, onInvaliDate = {Toast.makeText(context, "La fecha debe ser hoy o una futura", Toast.LENGTH_LONG).show()}, id_tarea = id_tarea )

                        }
                        CategoriasOpcione(id_tarea = id_tarea, onCategoriaSelecionada = { categoria -> viewModel.onCategoriaChange(categoria)})
                        PrioridadOpcione(id_tarea = id_tarea, onPrioridadSelecionada = {prioridad -> viewModel.onPrioridadChange(prioridad)})
                        RecordatorioButtonn(id_tarea = id_tarea, onRecordatorioSelecionado = { recordatorio -> viewModel.onRecordChange(recordatorio)})
                        ColoresButtonn(id_tarea = id_tarea, onColorSelecionado = {color -> viewModel.onColorChange(color)})

                        Button(
                            onClick = {
                                viewModel.editarTarea(id_tarea, context)
                            },
                            modifier = Modifier
                                .size(width = 230.dp, height = 50.dp)
                                .align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(
                                Color(0xFF769AC4), Color.White
                            ),
                            border = BorderStroke(1.dp, Color(0xFF769AC4)),
                        ) {
                            Text(text = "Guardar", fontSize = 20.sp)
                        }
                        if(showTareaEditada){
                            editarTarea(onDismiss = { viewModel.setShowTareaEditada(false) }, navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasOpcione( id_tarea: String, onCategoriaSelecionada: (String) -> Unit ){
    var tarea by remember { mutableStateOf<Tarea?>(null) }
    var expanden by remember { mutableStateOf(false) }
    var selectedCat by remember { mutableStateOf("") }
    val categorias = listOf("Escolar","Laboral" ,"Personal")

    val viewModel: TareaViewModel = viewModel()

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            selectedCat = tareaObtenida.categoria
        }
    }

    ExposedDropdownMenuBox(expanded = expanden,
        onExpandedChange = {expanden =! expanden }
    ) {
        OutlinedTextField(
            value = selectedCat ?: "",
            onValueChange = { selectedCat = it },
            placeholder = { Text("Categoría") },
            leadingIcon = {
                Icon(
                    Icons.Rounded.List,
                    contentDescription = "Icono categoría",
                    tint = Color(0xFF769AC4)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFF769AC4),
                focusedBorderColor = Color(0xFF769AC4),
                cursorColor = Color(0xFF769AC4)
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanden = !expanden }
                .menuAnchor(),
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 17.sp
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanden)
            }
        )
        ExposedDropdownMenu(
            expanded = expanden,
            onDismissRequest = {expanden = false},
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(text = categoria) },
                    onClick = {
                        selectedCat = categoria
                        expanden = false
                        onCategoriaSelecionada(categoria)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadOpcione(id_tarea: String,onPrioridadSelecionada: (String) -> Unit){
    var tarea by remember { mutableStateOf<Tarea?>(null) }
    var expanden by remember { mutableStateOf(false) }
    var selectedPrio by remember { mutableStateOf("") }
    val prioridades = listOf("Alta","Media" ,"Baja")

    val viewModel: TareaViewModel = viewModel()

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            selectedPrio = tareaObtenida.prioridad
        }
    }

    ExposedDropdownMenuBox(expanded = expanden,
        onExpandedChange = {expanden =! expanden }
    ) {
        OutlinedTextField(
            value = selectedPrio ?: "",
            onValueChange = { selectedPrio = it },
            placeholder = { Text("Prioridad") },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.priority),
                    contentDescription = "Icono prioridad",
                    colorFilter = ColorFilter.tint(Color(0xFF769AC4))
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFF769AC4),
                focusedBorderColor = Color(0xFF769AC4),
                cursorColor = Color(0xFF769AC4)
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanden = !expanden }
                .menuAnchor(),
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 17.sp
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanden)
            }
        )
        ExposedDropdownMenu(
            expanded = expanden,
            onDismissRequest = {expanden = false},
        ) {
            prioridades.forEach { prioridad ->
                DropdownMenuItem(
                    text = { Text(text = prioridad) },
                    onClick = {
                        selectedPrio = prioridad
                        expanden = false
                        onPrioridadSelecionada(prioridad)
                    }
                )
            }
        }
    }
}

@Composable
fun ColoresButtonn(onColorSelecionado: (String) -> Unit, id_tarea: String){
    var tarea by remember { mutableStateOf<Tarea?>(null) }
    val colores = listOf(
        Color(0xfF7C9A78) to "Verde",
        Color(0xFFFFAA9A) to "Rosita",
        Color(0xff9E6999) to "Moradito",
        Color(0xffFFD372) to "Amarillo",
        Color(0xffC08769) to "Café",
        Color(0xffC4661F) to "Naranja",
        Color(0xff962c2c) to "Rojo",
        Color(0xff3c6390) to "Azul"
    )
    var selectedOptions by remember { mutableStateOf(colores[0].first) }
    val viewModel: TareaViewModel = viewModel()

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            val colorNombre = tareaObtenida.color
            colores.find { it.second == colorNombre }?.let {
                selectedOptions = it.first
            }
        }
    }

    Text("Selecciona un color", fontSize = 19.sp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.Center
    ){
        colores.forEach { (color, nombre) ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color, shape = CircleShape)
                    .border(
                        width = if(selectedOptions == color) 3.dp else 1.dp,
                        color= if(selectedOptions == color) Color(0xFF769AC4) else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        selectedOptions = color
                        onColorSelecionado(nombre)
                    }
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            //Text(nombre)
        }
    }
}

@Composable
fun RecordatorioButtonn(onRecordatorioSelecionado: (Boolean) -> Unit, id_tarea: String){
    val opciones = listOf(true to "Si", false to "No")
    var selectedOptions by remember { mutableStateOf(opciones[0]) }

    val viewModel: TareaViewModel = viewModel()

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            selectedOptions = if (tareaObtenida.recordatorio) opciones[0] else opciones[1]
        }
    }

    Text("¿Desea recibir un recordatorio?", fontSize = 19.sp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.Center
    ) {

        opciones.forEach { ( boolean, text) ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (selectedOptions.first == boolean),
                        onClick = {
                            selectedOptions = boolean to text
                            onRecordatorioSelecionado(boolean)
                        },
                        role = Role.RadioButton
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedOptions.first == boolean),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF769AC4),
                        unselectedColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDockeds() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate ?: "",
            onValueChange = { },
            label = { Text("Fecha") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecciona fecha"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(Color(0xFF769AC4))
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldToModals(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
    onInvaliDate: () -> Unit,
    id_tarea: String
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    val viewModel: TareaViewModel = viewModel()
    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            tareaObtenida?.fecha?.let { fecha ->
                selectedDate = convertDateStringToMillis(fecha)
            }
        }
    }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDates(it) } ?: "",
        onValueChange = {  },
        placeholder = { Text("Fecha") },
        leadingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Select date",
                tint = Color(0xFF769AC4))
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFF769AC4),
            focusedBorderColor = Color(0xFF769AC4),
            cursorColor = Color(0xFF769AC4)
        ),
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModals(
            onDismiss = { showModal = false },
            onDateSelected = { date ->
                selectedDate = date
                selectedDate?.let {
                    if (isValiDates(it)) {
                        onDateSelected(convertMillisToDates(it))
                    }else {
                        onInvaliDate()
                    }
                }
            }
        )
    }
}
fun convertDateStringToMillis(dateString: String): Long? {
    return try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString)?.time
    } catch (e: Exception) {
        null
    }
}

fun convertMillisToDates(millis: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 12)
    }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(calendar.time)
}

fun isValiDates(millis: Long): Boolean {
    val selectedDate = Date(millis)
    val today = Date(System.currentTimeMillis())
    return selectedDate.after(today) || selectedDate == today
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModals(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(millis)
                }
                // onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Aceptar", color = Color(0xFF769AC4), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF769AC4), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF769AC4),
                    selectedDayContentColor = Color.White
                )
            )
        }
    }
}

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun TimePickerToModals(modifier: Modifier = Modifier, onTimeSelected: (String) -> Unit, id_tarea: String) {
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showModal by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime?.hour?: LocalTime.now().hour,
        initialMinute = selectedTime?.minute ?: LocalTime.now().minute,
        is24Hour = false
    )
    val viewModel: TareaViewModel = viewModel()

    LaunchedEffect(id_tarea) {
        viewModel.obtenerTareaPorId(id_tarea) { tareaObtenida ->
            selectedTime = tareaObtenida.hora?.let { hora ->
                when (hora) {
                    is String -> try {
                        LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm a"))
                    } catch (e: Exception) {
                        null
                    }
                    else -> null
                }
            }
        }
    }
    Box(modifier = modifier.fillMaxWidth()
        .clickable {
            showModal = true
        }) {

        OutlinedButton(
            onClick = { showModal = true },
            border = BorderStroke(1.dp, Color(0xFF769AC4)),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.height(56.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.accesstime),
                contentDescription = "Seleccionar hora",
                tint = Color(0xFF769AC4)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Hora",
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
    if (showModal) {
        TimePickerDialogs(
            onDismiss = { showModal = false },
            onConfirm = {
                selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                selectedTime?.let { time ->
                    onTimeSelected(time.format(DateTimeFormatter.ofPattern("hh:mm a")))
                }
                showModal = false
            }
        ) {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF769AC4),
                    onPrimary = Color.White,
                    secondary = Color(0xFF769AC4),
                    onSecondary = Color.White,
                    surface = Color.White,
                    onSurface = Color.Black
                )
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}

@Composable
fun TimePickerDialogs(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar", color = Color(0xFF769AC4), fontSize = 18.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm()}) {
                Text("Aceptar", color = Color(0xFF769AC4), fontSize = 18.sp)
            }
        },
        text = { content()}
    )
}