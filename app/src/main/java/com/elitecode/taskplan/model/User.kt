package com.elitecode.taskplan.model

data class User(
    val id: String,
    val userId: String,
    val nombre: String,
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "nombre" to this.nombre
        )
    }
}
