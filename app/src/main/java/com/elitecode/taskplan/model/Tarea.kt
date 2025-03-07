package com.elitecode.taskplan.model

data class Tarea(
    var id_tarea: String = "",
    var titulo: String = "",
    var descripcion: String = "",
    var hora: String = "",
    var fecha: String = "",
    var categoria: String = "",
    var prioridad: String = "",
    var recordatorio: Boolean = false,
    var hora_recordatorio: String = "",
    var fecha_recordatorio: String = "",
    var color: String=  "",
    var user_id: String = ""
){
    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "titulo" to this.titulo,
            "descripcion" to this.descripcion,
            "hora" to this.hora,
            "fecha" to this.fecha,
            "categoria" to this.categoria,
            "prioridad" to this.prioridad,
            "recordatorio" to this.recordatorio,
            "hora_recordatorio" to this.hora_recordatorio,
            "fecha_recordatorio" to this.fecha_recordatorio,
            "color" to this.color,
            "user_id" to this.user_id
        )
    }
}