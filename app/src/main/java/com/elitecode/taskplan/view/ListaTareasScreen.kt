package com.elitecode.taskplan.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elitecode.taskplan.R
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.components.eliminarTarea
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.viewmodel.LoginViewModel
import com.elitecode.taskplan.viewmodel.TareaViewModel
import com.elitecode.taskplan.components.HorizontalDivider as HorizontalDivider1

@Composable
fun ListaTareasScreen(navController: NavController, viewModel: TareaViewModel){
    val context = LocalContext.current
    viewModel.obtenerTareas(context)
    val listaTareas by viewModel.tareasFiltradas.collectAsState()

    MenuLateral(navController,  LoginViewModel()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                   // .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                //Buttons(viewModel, tarea, navController)
                CategoriasYPrioridades(viewModel)
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 26.dp)
                        .padding(bottom = 90.dp)
                ) {
                    items(listaTareas) { tarea ->
                        ListaTareas(viewModel, tarea, navController)
                    }
                }
            }
            FloatingActionButton(
                onClick = { navController.navigate("nuevaTarea") },
                modifier = Modifier
                     .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = Color(0xFF769AC4),

                ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar tarea", tint = Color.White)
            }
        }
    }
}//Cierre función

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasYPrioridades(viewModel: TareaViewModel){
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    var prioridadSeleccionada by remember { mutableStateOf<String?>(null) }

    val categorias = listOf("Todas", "Escolar", "Laboral", "Personal")
    val prioridades = listOf("Todas", "Alta", "Media", "Baja")

    Row( modifier = Modifier.padding(26.dp)) {
        DropdownMenuFiltro(
            label = "Categoría",
            opciones = categorias,
            seleccion = categoriaSeleccionada,
            onSeleccion = { nuevaCategoria ->
                categoriaSeleccionada = if (nuevaCategoria == "Todas") null else nuevaCategoria
                viewModel.filtrarTareas(categoriaSeleccionada, prioridadSeleccionada)
            },

        )
        Spacer(modifier = Modifier.width(10.dp))
        DropdownMenuFiltro(
            label = "Prioridad",
            opciones = prioridades,
            seleccion = prioridadSeleccionada,
            onSeleccion = { nuevaPrioridad ->
                prioridadSeleccionada = if (nuevaPrioridad == "Todas") null else nuevaPrioridad
                viewModel.filtrarTareas(categoriaSeleccionada, prioridadSeleccionada)
            },

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuFiltro(
    label: String,
    opciones: List<String>,
    seleccion: String?,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var texto = seleccion ?: label
    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = {},
               // placeholder = { seleccion },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF769AC4),
                    unfocusedIndicatorColor = Color(0xFF769AC4),
                    cursorColor = Color(0xFF769AC4),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                shape =  RoundedCornerShape(50.dp),
                modifier = Modifier
                    .width(200.dp)
                    .clickable { expanded = !expanded }
                    .menuAnchor(),
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 19.sp
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(text = opcion) },
                        onClick = {
                            onSeleccion(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

    @Composable
    fun ListaTareas(viewModel: TareaViewModel, tarea: Tarea, navController: NavController) {
        var showEliminarTarea by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }
        var expandedMenuItems by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }

        val color = when (tarea.color) {
            "Verde" -> Color(0xfF7C9A78)
            "Rosita" -> Color(0xFFE783A1)
            "Moradito" -> Color(0xFFD5ABCF)
            "Amarillo" -> Color(0xffFFD372)
            "Café" -> Color(0xffC08769)
            "Naranja" -> Color(0xffC4661F)
            "Rojo" -> Color(0xff962c2c)
            "Azul" -> Color(0xff3c6390)
            else -> Color.Transparent
        }

        val colorLetra = when (tarea.color) {
            "Verde" -> Color(0xFFF5E1C8)
            "Rosita" -> Color(0xFFFFEBD2)
            "Moradito" -> Color(0xFF4B0248)
            "Amarillo" -> Color(0xFF6D4C41)
            "Café" -> Color(0xFFF5E1C8)
            "Naranja" -> Color(0xFFFFEBD2)
            "Rojo" -> Color(0xFFF5E1C8)
            "Azul" -> Color(0xFFFFEBD2)
            else -> Color.Transparent
        }
        OutlinedCard(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = color,
            ),
            border = BorderStroke(1.dp, color = color)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                //.background(Color(0xffADD8E6).copy(alpha = 0.1f)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.weight(1f)
                        .padding(8.dp)
                ) {
                    //Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tarea.titulo,
                            color = colorLetra,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        IconButton(onClick = {
                            //expanded = !expanded
                            val key = tarea.id_tarea
                            expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                put(key ?: "", expandedMenuItems[key] != true)
                            }
                        }) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                modifier = Modifier.size(25.dp)
                                    .padding(end = 4.dp),
                                contentDescription = "Menú de opciones",
                                tint = colorLetra
                            )
                        }

                        DropdownMenu(
                            expanded = expandedMenuItems[tarea.id_tarea ?: ""] == true,
                            onDismissRequest = {
                                expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                    put(tarea.id_tarea ?: "", false)
                                }
                            }
                            //onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar") },
                                onClick = {
                                    navController.navigate("editar_tarea/${tarea.id_tarea}")
                                    expandedMenuItems = expandedMenuItems.toMutableMap().apply {
                                        put(tarea.id_tarea ?: "", false)
                                    }
                                    Log.d("ID de la tarea", "${tarea.id_tarea}")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar") },
                                onClick = {
                                    expanded = false
                                    showEliminarTarea = true
                                    //viewModel.eliminarTarea(tarea.id_tarea)
                                }
                            )
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(22.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Outlined.DateRange,
                                contentDescription = "Icono fecha",
                                tint = colorLetra
                            )
                            Text(text = tarea.fecha, color = colorLetra, fontSize = 17.sp)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.accesstime),
                                contentDescription = "Icono hora",
                                colorFilter = ColorFilter.tint(colorLetra)
                            )
                            Text(text = tarea.hora, color = colorLetra, fontSize = 17.sp)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(50.dp),
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(color, shape = CircleShape)
                            ) {

                            }
                        }
                    }
                }
                if (showEliminarTarea) {
                    eliminarTarea(
                        onDismiss = { showEliminarTarea = false },
                        navController = navController,
                        viewModel,
                        tarea
                    )
                }
            }
        }
    }
