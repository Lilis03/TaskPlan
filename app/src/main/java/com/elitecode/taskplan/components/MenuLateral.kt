@file:OptIn(ExperimentalMaterial3Api::class)

package com.elitecode.taskplan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elitecode.taskplan.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuLateral(navController: NavController, viewModel: LoginViewModel, content: @Composable (PaddingValues) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                if(drawerState.isClosed){
                                    drawerState.open()
                                }else{
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Close,
                                contentDescription = "Menú")
                        }
                    }

                    Text("Menú", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    //Text("Sección 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Perfil") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Person, contentDescription = "Icono de perfil" )},
                        //Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null) },
                        onClick = { navController.navigate("perfil")}
                    )
                    NavigationDrawerItem(
                        label = { Text("Tareas/Pendientes") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.List, contentDescription = "Icono de perfil" )},
                        onClick = { navController.navigate("listado_tareas") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Agregar tarea") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.AddCircle, contentDescription = null) },
                        onClick = { navController.navigate("nuevaTarea") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Calendario") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.DateRange, contentDescription = null) },
                        onClick ={ navController.navigate("calendar")}
                    )
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión", color = Color.Red) }, // 🔹 Texto en rojo para destacar
                        selected = false,
                        icon = { Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.Red) }, // 🔹 Ícono de salida
                        onClick = {
                            viewModel.signOut(
                                navigateHome = { navController.navigate("login") },
                                context
                            )
                        }
                    )

                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TaskPlan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 25.sp) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if(drawerState.isClosed){
                                    drawerState.open()
                                }else{
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xff769ac4)
                    )
                )
            }
        ){ innerPadding ->
            content(innerPadding)
        }
    }
}
