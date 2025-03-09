package com.elitecode.taskplan.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.elitecode.taskplan.R
import com.elitecode.taskplan.components.MenuLateral
import com.elitecode.taskplan.viewmodel.LoginViewModel

@Composable
fun PerfilScreen(navController: NavHostController, viewModel: LoginViewModel){
    MenuLateral(navController) { paddingValues ->
        val context = LocalContext.current
        Column( modifier = Modifier.fillMaxSize().background(Color.White).padding(paddingValues)) {

            // Imagen de portada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Asegura espacio suficiente para la superposición
            ) {
                // Color de portada (Cover Photo)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray),
                )

                // Imagen de perfil superpuesta
                Image(
                    painter = painterResource(id = R.drawable.google_icon), // Reemplaza con tu recurso
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomCenter) // La coloca en la parte inferior del Box
                        .offset(y = 8.dp) // Ajusta la superposición, prueba con otros valores si es necesario
                )
            }

            // Contenido del perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Nombre y nombre de usuario
                Text(
                    text = "Lili Juarez",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "@Lilijuarez333",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Fecha de registro
                Text(
                    text = "Se unió en febrero de 2025",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección "Resumen"
                Text(
                    text = "Resumen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Detalles del resumen
                Column {
                    SummaryItem(title = "5", subtitle = "días de racha")
                    SummaryItem(title = "1 SEMANA Bronce", subtitle = "división actual")
                    SummaryItem(title = "420", subtitle = "EXP totales")
                }

            }
        }
    }



}
@Composable
fun SummaryItem(title: String, subtitle: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 500.dp, height = 100.dp)
        ) {
            Text(
                text = "Filled",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}
