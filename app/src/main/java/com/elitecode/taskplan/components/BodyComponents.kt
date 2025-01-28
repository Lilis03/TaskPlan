package com.elitecode.taskplan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainTextField(value: String, onvalueChange: (String) -> Unit, placeholder: String, leadingIcon: (@Composable (()-> Unit))? = null){
    OutlinedTextField(
        value = value,
        onValueChange = onvalueChange,
        placeholder = { Text( text= placeholder) },
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(50.dp))
    )
}