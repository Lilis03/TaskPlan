    package com.elitecode.taskplan.components

    import android.annotation.SuppressLint
    import android.app.AlarmManager
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.os.Build
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import com.elitecode.taskplan.MainActivity
    import com.elitecode.taskplan.R

    /**
     * Receptor de alarmas que muestra una notificación cuando se activa.
     */
    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val title = intent.getStringExtra("title") ?: "Recordatorio"
            val message = intent.getStringExtra("message") ?: "¡Es hora de tu recordatorio!"
            showNotification(context, title, message)
        }
    }

    /**
     * Muestra una notificación en el dispositivo.
     *
     * @param context Contexto de la aplicación.
     * @param title Título de la notificación.
     * @param message Mensaje de la notificación.
     */
    fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent para abrir la app al hacer clic en la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val notification = NotificationCompat.Builder(context, "recordatorio_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.google_icon) // Icono de la notificación
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Cierra la notificación al hacer clic
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad alta para que suene y vibre
            .build()

        // Mostrar la notificación
        notificationManager.notify(1, notification)
    }

    /**
     * Programa una notificación para que se muestre en una fecha y hora específicas.
     *
     * @param context Contexto de la aplicación.
     * @param timeInMillis Tiempo en milisegundos para la notificación.
     * @param title Título de la notificación.
     * @param message Mensaje de la notificación.
     */
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(context: Context, timeInMillis: Long, title: String, message: String) {
        // Verificar si el permiso está concedido en Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val canScheduleExactAlarms = context.getSystemService(AlarmManager::class.java).canScheduleExactAlarms()
            if (!canScheduleExactAlarms) {
                Log.e("AlarmReceiver", "No se tiene permiso para programar alarmas exactas.")
                return // No continuar si no tiene el permiso
            }
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Programar la alarma
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Usar setExactAndAllowWhileIdle para evitar problemas con el modo Doze
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } else {
            // Para versiones anteriores a Android 6.0
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Crea un canal de notificaciones para la aplicación.
     *
     * @param context Contexto de la aplicación.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "recordatorio_channel", // ID del canal
                "Recordatorios", // Nombre del canal
                NotificationManager.IMPORTANCE_HIGH // Importancia de la notificación
            ).apply {
                description = "Canal para recordatorios de tareas"
            }

            // Registrar el canal en el sistema
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }