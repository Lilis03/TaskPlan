package com.elitecode.taskplan.model

data class User(
    val user_id:  String = "",
    val nombre:  String = "",
    val email:  String = "",
    val fecha_registro: String = "",
    val foto_perfil: String = "",
    val color_portada: String = ""
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.user_id,
            "nombre" to this.nombre,
            "email" to this.email,
            "fecha_registro" to this.fecha_registro,
            "foto_perfil" to this.foto_perfil,
            "color_portada" to this.color_portada
        )
    }
}
