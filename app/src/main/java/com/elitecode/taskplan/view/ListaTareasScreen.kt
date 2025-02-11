package com.elitecode.taskplan.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.components.eliminarTarea
import com.elitecode.taskplan.model.Tarea
import com.elitecode.taskplan.viewmodel.TareaViewModel

@Composable
fun ListaTareasScreen(navController: NavController, viewModel: TareaViewModel){
    val listaTareas by viewModel.listaTareas.collectAsState()


    MenuLateral(navController) { paddingValues ->
        Column( modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Buttons()
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp)
                    .padding(bottom = 90.dp)
            ) {
                items(listaTareas){ tarea ->
                    ListaTareas( viewModel, tarea, navController)
                }
            }
        }
    }
}//Cierre función

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Buttons(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Todas", "Categoría","Prioridad")

    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun ListaTareas(viewModel: TareaViewModel, tarea: Tarea, navController: NavController ){
    var showEliminarTarea by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var expandedMenuItems by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffC5E7FF).copy(alpha = 0.1f)),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tarea.titulo,
                        color = Color.DarkGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                             .weight(1f)
                            .padding(4.dp),
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
                        Icon(Icons.Outlined.MoreVert,
                           modifier = Modifier.size(30.dp)
                                 .padding(end= 4.dp),
                           contentDescription = "Menú de opciones",
                           tint = Color.Black
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
                                    /////77
                                    //expanded = false
                                    //navController.navigate("editar_tarea/${tarea.id_tarea}")
                                    Log.d("ID de la tarea", "${tarea.id_tarea}")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar")},
                                onClick = {
                                    expanded = false
                                    showEliminarTarea = true
                                    //viewModel.eliminarTarea(tarea.id_tarea)
                                }
                            )
                        }

                }

                Text(text = tarea.descripcion, color = Color(0xFF769AC4))
                Text(text = tarea.color, color = Color.Black)
                Text(text = tarea.categoria, color = Color.Black)
            }
            if(showEliminarTarea){
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
