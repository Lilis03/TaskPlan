package com.elitecode.taskplan.components

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.callback.UploadCallback

object CloudinaryManager {
    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            val config: HashMap<String, String> = HashMap()
            config["cloud_name"] = "dgrvrwdk9"
            config["api_key"] = "171191794273368"
            config["api_secret"] = "l4DzH9Xkyy_BwZY5x3M8SRCDmeI"

            com.cloudinary.android.MediaManager.init(context, config)
            isInitialized = true
        }
    }
}

fun uploadClouadinaryImage(context: Context, uri: Uri, onResult: (String?) -> Unit){
    Log.d("ImagenPerfil", "Entra a saveImageToExternalStorage")
    CloudinaryManager.init(context)
    try {

        com.cloudinary.android.MediaManager.get().upload(uri)
            .option("folder", "DailyPass/image") // Configura la carpeta en Cloudinary
            .unsigned("d6odtz3u") // Preset de carga no firmada
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("ImagenPerfil", "Inicio de la subida")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    Log.d("ImagenPerfil", "Progreso de subida: $bytes/$totalBytes")
                }

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val imageUrl = resultData?.get("url") as? String
                    Log.d("ImagenPerfil", "Subida exitosa. URL: $imageUrl")
                    onResult(imageUrl) // Retorna la URL exitosa al callback
                }

                override fun onError(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {
                    Log.e("ImagenPerfil", "Error al subir la imagen: ${error?.description}")
                    onResult(null) // Retorna null en caso de error
                }

                override fun onReschedule(
                    requestId: String?,
                    error: com.cloudinary.android.callback.ErrorInfo?
                ) {
                    Log.w("ImagenPerfil", "Subida reprogramada: ${error?.description}")
                }
            })
            .dispatch()
    } catch (e: Exception) {
        Log.e("ImagenPerfil", "Error al inicializar o cargar: ${e.message}")
        onResult(null)
    }
}