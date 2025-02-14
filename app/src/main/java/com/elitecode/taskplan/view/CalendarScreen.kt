    package com.elitecode.taskplan.view

    import android.os.Build
    import android.util.Log
    import androidx.annotation.RequiresApi
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.aspectRatio
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.material3.Card
    import androidx.compose.material3.CardDefaults
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import com.elitecode.taskplan.components.MenuLateral
    import com.elitecode.taskplan.model.Tarea
    import com.elitecode.taskplan.viewmodel.TareaViewModel
    import com.google.firebase.auth.FirebaseAuth
    import java.time.LocalDate
    import java.time.YearMonth
    import java.time.format.DateTimeFormatter
    import java.time.format.TextStyle
    import java.util.Locale

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun CalendarScreen(navController: NavHostController, viewModel: TareaViewModel) {
        val tasks by viewModel.tasks.collectAsState()

        LaunchedEffect(Unit) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                viewModel.observeTasks(userId)
            }
        }

        MenuLateral(navController) { paddingValues ->
            val context = LocalContext.current
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            var currentMonth by remember { mutableStateOf(YearMonth.now()) }
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            Column(modifier = Modifier.padding(paddingValues).fillMaxSize().padding(16.dp)) {
                // Header con el mes
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) { Text("◀") }
                    Text("${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}", fontSize = 20.sp)
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) { Text("▶") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val daysInMonth = currentMonth.lengthOfMonth()
                val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7
                val totalCells = daysInMonth + firstDayOfMonth

                LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                    items(totalCells) { index ->
                        val dayNumber = index - firstDayOfMonth + 1
                        if (dayNumber in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayNumber)
                            Box(
                                modifier = Modifier.aspectRatio(1f).clickable { selectedDate = date }
                                    .background(if (selectedDate == date) Color.LightGray else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(dayNumber.toString(), fontSize = 16.sp)
                            }
                        } else {
                            Box(modifier = Modifier.aspectRatio(1f)) {}
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Eventos del ${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())}",
                    fontSize = 18.sp
                )

                // Filtrar tareas que coincidan con la fecha seleccionada
                val filteredTasks = tasks.filter { task ->
                    try {
                        val taskDate = LocalDate.parse(task.fecha, dateFormatter)
                        Log.d("tasklogs", "Comparando: $taskDate == $selectedDate")
                        taskDate == selectedDate
                    } catch (e: Exception) {
                        Log.e("tasklogs", "Error al parsear fecha: ${task.fecha}", e)
                        false
                    }
                }

                Log.d("tasklogs", "Tareas encontradas: ${filteredTasks.size}")

                LazyColumn {
                    items(filteredTasks) { task ->
                        EventCard(task)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun EventCard(task: Tarea) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dayOfMonth = try {
            LocalDate.parse(task.fecha, dateFormatter).dayOfMonth.toString()
        } catch (e: Exception) {
            "??"
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(dayOfMonth, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black) // 🔥 Solo el día
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(task.titulo, color = Color.Black, fontWeight = FontWeight.SemiBold)
                    Text(task.hora, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(12.dp).background(Color(android.graphics.Color.parseColor(task.color)), shape = CircleShape))
            }
        }
    }

