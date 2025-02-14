package com.elitecode.taskplan.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
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
import com.elitecode.taskplan.R
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
    val color = when (tarea.color) {
        "Verde" -> Color(0xfF7C9A78)
        "Rosita" -> Color(0xFFFFAA9A)
        "Moradito" -> Color(0xff9E6999)
        "Amarillo" -> Color(0xffFFD372)
        "Café" -> Color(0xffC08769)
        "Naranja" -> Color(0xffC4661F)
        "Rojo" -> Color(0xff962c2c)
        "Azul" -> Color(0xff3c6390)
        else -> Color.Transparent
    }

    OutlinedCard(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        //elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
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
        ){
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
                        color = Color.White,
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
                        Icon(Icons.Outlined.MoreVert,
                           modifier = Modifier.size(25.dp)
                                 .padding(end= 4.dp),
                           contentDescription = "Menú de opciones",
                           tint = Color.White
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Outlined.DateRange, contentDescription = "Icono fecha", tint = Color.White)
                        Text(text = tarea.fecha, color = Color.White, fontSize = 17.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(painter = painterResource(R.drawable.accesstime), contentDescription = "Icono hora", colorFilter = ColorFilter.tint(Color.White))
                        Text(text = tarea.hora, color = Color.White, fontSize = 17.sp)
                    }

                    Row(
                       verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(50.dp),
                    ){

                        Box( modifier = Modifier
                            .size(20.dp)
                            .background(color, shape = CircleShape)) {

                        }
                    }
                   /* Icon(Icons.Outlined.DateRange, contentDescription = "Icono fecha")
                    Text(text = tarea.fecha, color = Color.DarkGray, fontSize = 17.sp)
                    Icon(Icons.Outlined.Search, contentDescription = "Icono hora")
                    Text(text = tarea.hora, color = Color.DarkGray, fontSize = 17.sp)
                    val color = when (tarea.color) {
                        "Verde" -> Color(0xfF7C9A78)
                        "Rosita" -> Color(0xFFFFAA9A)
                        "Moradito" -> Color(0xff9E6999)
                        "Amarillo" -> Color(0xffFFD372)
                        "Café" -> Color(0xffC08769)
                        "Naranja" -> Color(0xffC4661F)
                        "Rojo" -> Color(0xff962c2c)
                        "Azul" -> Color(0xff3c6390)
                        else -> Color.Transparent
                    }
                    Box( modifier = Modifier
                        .size(20.dp)
                        .background(color, shape = CircleShape)) {

                    }*/
                    //Text(text = tarea.color, color = Color.DarkGray,fontSize = 16.sp ,modifier = Modifier.padding(end = 26.dp))
                }
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
