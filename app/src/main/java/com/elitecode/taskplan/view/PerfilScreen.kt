package com.elitecode.taskplan.view

import android.os.Build
import android.text.Layout
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.elitecode.taskplan.R
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.viewmodel.LoginViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.auto
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PerfilScreen(navController: NavHostController, viewModel: LoginViewModel) {
    val context = LocalContext.current
    val usuario = viewModel.usuario
    val imageUrl = viewModel.imageUrl

    LaunchedEffect(Unit) {
        try {
            viewModel.cargarTareas()
            viewModel.cargarUsuario()
        } catch (e: Exception) {
            Log.e("PerfilScreen", "Error cargando datos: ${e.message}")
        }
    }

    MenuLateral(navController) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(paddingValues)
        ) {
            if (usuario == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }

            val colorPortada = try {
                Color(android.graphics.Color.parseColor(usuario.color_portada))
            } catch (e: Exception) {
                Color.LightGray // En caso de error, usa un color por defecto
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(colorPortada),
                )

                if (!usuario.foto_perfil.isNullOrEmpty()) {
                    Log.d("PerfilScreen", "Foto ${usuario.foto_perfil}")
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(110.dp)
                            .offset(y = 8.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop, //  Para que la imagen se ajuste bien
                            modifier = Modifier
                                .size(100.dp) // Tamaño de la imagen
                                .clip(CircleShape) // Recorte en forma de círculo
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.usuario),
                        contentDescription = "Imagen de perfil predeterminada",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.BottomCenter)
                            .offset(y = 8.dp)
                    )
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = usuario.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Se unió en ${formatearFecha(usuario.fecha_registro)}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        usuario.user_id?.let { id ->
                            Log.d("PerfilEdit", "Navegando a editarPerfil con ID: $id")
                            navController.navigate("editarPerfil/$id")
                        } ?: Log.e("PerfilEdit", "El usuario o su ID es nulo")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Editar perfil")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Resumen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))
                CarouselTablets(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarouselTablets(viewModel: LoginViewModel) {
    var rangoSeleccionado by remember { mutableStateOf("Hoy") }
    val tareas = viewModel.tareas

    if (tareas.isEmpty()) {
        Text("No hay tareas para mostrar")
        return
    }

    SelectorRango { rango ->
        rangoSeleccionado = rango
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // ⬅️ Flecha izquierda (Desplazar hacia atrás)
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem((listState.firstVisibleItemIndex - 1).coerceAtLeast(0))
                }
            },
            enabled = listState.firstVisibleItemIndex > 0
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Retroceder",
                tint = if (listState.firstVisibleItemIndex > 0) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        LazyRow(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(2) { index ->
                when (index) {
                    0 -> TarjetaResumen(tareas = tareas, rango = rangoSeleccionado)
                    1 -> TarjetaGrafica(viewModel, rangoSeleccionado)
                }
            }
        }

        // ➡️ Flecha derecha (Desplazar hacia adelante)
        IconButton(
            modifier = Modifier.size(20.dp),
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem((listState.firstVisibleItemIndex + 1).coerceAtMost(1))
                }
            },
            enabled = listState.firstVisibleItemIndex < 1
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Avanzar",
                tint = if (listState.firstVisibleItemIndex < 1) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorRango(onRangoSeleccionado: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedRango by remember { mutableStateOf("Hoy") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedRango,
            onValueChange = { }, // No se permite edición manual
            label = { Text("Seleccionar rango") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Expandir menú"
                )
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Hoy", "Semana", "Mes").forEach { rango ->
                DropdownMenuItem(
                    text = { Text(rango) },
                    onClick = {
                        Log.d("SelectorRango", "Rango seleccionado: $rango")
                        selectedRango = rango // Actualizar el estado local
                        onRangoSeleccionado(rango) // Notificar la selección
                        expanded = false // Cerrar el menú
                    }
                )
            }
        }
    }
}


// Funciones auxiliares para filtrar por fecha
@RequiresApi(Build.VERSION_CODES.O)
fun esHoy(fecha: String): Boolean {
    if (fecha.isNullOrEmpty()) return false
    return try {
        val formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val hoy = LocalDate.now()
        val fechaTarea = LocalDate.parse(fecha, formatoEntrada)
        hoy.isEqual(fechaTarea)
    } catch (e: DateTimeParseException) {
        Log.e("PerfilScreen", "Error al parsear la fecha: $fecha", e)
        false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun esEstaSemana(fecha: String): Boolean {
    return try {
        val formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaTarea = LocalDate.parse(fecha, formatoEntrada)

        val hoy = LocalDate.now()
        val primerDiaSemana = hoy.with(DayOfWeek.MONDAY)
        val ultimoDiaSemana = hoy.with(DayOfWeek.SUNDAY)

        fechaTarea.isAfter(primerDiaSemana.minusDays(1)) && fechaTarea.isBefore(ultimoDiaSemana.plusDays(1))
    } catch (e: DateTimeParseException) {
        Log.e("PerfilScreen", "Error al parsear la fecha: $fecha", e)
        false
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun esEsteMes(fecha: String): Boolean {
    return try {
        val formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaTarea = LocalDate.parse(fecha, formatoEntrada)
        val mesActual = LocalDate.now().monthValue
        mesActual == fechaTarea.monthValue
    } catch (e: DateTimeParseException) {
        Log.e("PerfilScreen", "Error al parsear la fecha: $fecha", e)
        false
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun esFormatoFechaValido(fecha: String): Boolean {
    return try {
        LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        true
    } catch (e: DateTimeParseException) {
        false
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaResumen(tareas: List<Tarea>, rango: String) {
    // Filtrar las tareas según el rango
    val tareasFiltradas = remember(tareas, rango) {
        when (rango.lowercase()) {  // Aseguramos que 'rango' se compare en minúsculas
            "hoy" -> tareas.filter { esHoy(it.fecha) }
            "semana" -> tareas.filter { esEstaSemana(it.fecha) }
            "mes" -> tareas.filter { esEsteMes(it.fecha) }
            else -> tareas
        }
    }

    val tareasCompletadas = tareasFiltradas.count { it.completada }
    val tareasPendientes = tareasFiltradas.size - tareasCompletadas
    val progreso = if (tareasFiltradas.isNotEmpty()) {
        tareasCompletadas.toFloat() / tareasFiltradas.size
    } else {
        0f
    }

    // Mostrar la tarjeta con el contador y la barra de progreso
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .width(315.dp)
            .height(250.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Tareas $rango", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar las tareas pendientes
            Text("Tareas pendientes: $tareasPendientes de ${tareasFiltradas.size}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progreso
            LinearProgressIndicator(
                progress = progreso.coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF769AC4), // Color de la barra de progreso
                trackColor = Color.White // Color de fondo de la barra
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tareas completadas
            Text("Tareas completadas: $tareasCompletadas", fontSize = 14.sp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TarjetaGrafica(viewModel: LoginViewModel, rango: String) {
    val tareas = viewModel.tareas
    Log.d("PerfilScreen", "Se recargan las tareas ${tareas}")
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .width(315.dp)
            .height(250.dp)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Tareas por $rango",
                style = MaterialTheme.typography.bodyMedium
            )
            if (tareas.isNotEmpty()) {
                GraficaTareasPorSemana(tareas = tareas, rango = rango)
                Log.d("PerfilScreen", "Se manda a llamar la grafica")
            } else {
                Column( modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.agregar_tarea), // Reemplaza con tu ícono de "No hay tareas"
                        contentDescription = "Sin tareas",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = "No hay tareas para $rango",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GraficaTareasPorSemana(
    tareas: List<Tarea>,
    rango: String,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    // Filtrar las tareas según el rango
    val tareasFiltradas = remember(tareas, rango) {
        when (rango) {
            "Hoy" -> tareas.filter { esFormatoFechaValido(it.fecha) && esHoy(it.fecha) }
            "Semana" -> tareas.filter { esFormatoFechaValido(it.fecha) && esEstaSemana(it.fecha) }
            "Mes" -> tareas.filter { esFormatoFechaValido(it.fecha) && esEsteMes(it.fecha) }
            else -> tareas.filter { esFormatoFechaValido(it.fecha) }
        }
    }

    // Agrupar las tareas por día de la semana
    val tareasPorDia = tareasFiltradas.groupBy {
        try {
            val fechaTarea = LocalDate.parse(it.fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            fechaTarea.dayOfWeek.name
        } catch (e: DateTimeParseException) {
            Log.e("PerfilScreen", "Error al parsear la fecha: ${it.fecha}", e)
            null
        }
    }.filterValues { it != null }
    val diasDeLaSemana = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY")

    val cantidadTareasPorDia = diasDeLaSemana.map { dia ->
        tareasPorDia[dia]?.size ?: 0
    }
    Log.d("PerfilScreen", "Datos de la gráfica: $cantidadTareasPorDia")

    val valueFormatter = remember {
        CartesianValueFormatter { _, value, _ ->
            val diaCompleto = diasDeLaSemana.getOrNull(value.toInt()) ?: ""
            diaCompleto.take(1)
        }
    }

    // Configuran el eje X (horizontal)
    val axisLine = rememberLineComponent(thickness = 1.dp)
    val axisLabel = rememberTextComponent(
        color = Color.Gray,
        textSize = 12.sp,
        textAlignment = Layout.Alignment.ALIGN_CENTER
    )
    val axisTick = rememberLineComponent(thickness = 1.dp)
    val axisGuideline = rememberLineComponent(thickness = 1.dp)

    // Configuran el eje X (horizontal)
    val bottomAxis = HorizontalAxis.rememberBottom(
        line = axisLine,
        label = axisLabel,
        labelRotationDegrees = 0f,
        valueFormatter = valueFormatter,
        tick = axisTick,
        tickLength = 4.dp,
        guideline = axisGuideline,
        itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
        size = BaseAxis.Size.auto()
    )

    // Configurar el eje Y (vertical) para mostrar solo números enteros
    val startAxis = VerticalAxis.rememberStart(
        line = axisLine,
        label = axisLabel,
        valueFormatter = CartesianValueFormatter { _, value, _ ->
            value.toInt().toString() // Convertir a entero y formatear como cadena
        },
        tick = axisTick,
        tickLength = 1.dp,
        guideline = axisGuideline,
        itemPlacer = VerticalAxis.ItemPlacer.step(),
        size = BaseAxis.Size.auto()
    )

    // Mostrar la gráfica
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .width(310.dp)
            .height(250.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cantidadTareasPorDia.all { it == 0 }) {
                // Mostrar un icono y mensaje cuando no haya tareas
                Icon(
                    painter = painterResource(id = R.drawable.agregar_tarea),
                    contentDescription = "Sin tareas",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "No hay tareas para $rango",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                // Si hay datos, mostrar la gráfica
                LaunchedEffect(cantidadTareasPorDia) {
                    modelProducer.runTransaction {
                        columnSeries { series(cantidadTareasPorDia.map { it.toFloat() }) }
                    }
                }

                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberColumnCartesianLayer(),
                        startAxis = startAxis, // Eje Y configurado
                        bottomAxis = bottomAxis, // Eje X configurado
                    ),
                    modelProducer = modelProducer,
                    modifier = modifier
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseFechaTarea(fecha: String): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        LocalDate.parse(fecha, formatter)
    } catch (e: DateTimeParseException) {
        Log.e("PerfilScreen", "Error al parsear la fecha de tarea: $fecha", e)
        null
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun formatearFecha(fecha: String?): String {
    if (fecha.isNullOrEmpty()) return "Fecha desconocida"

    val formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formatoSalida = DateTimeFormatter.ofPattern("MMMM 'de' yyyy", Locale("es"))

    return try {
        val fechaLocal = LocalDate.parse(fecha, formatoEntrada)
        fechaLocal.format(formatoSalida)
    } catch (e: DateTimeParseException) {
        Log.e("PerfilScreen", "Error al parsear la fecha: $fecha", e)
        "Fecha desconocida"
    }
}

