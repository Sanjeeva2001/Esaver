package com.example.esaver

// WorkManager implementation for ESaver
// Handles background notifications for energy reminders and CO2 alerts


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

// Sets up the three notification channels the app needs.
// Android 8+ requires channels before showing any notification.
// Called once from MainActivity when the app first launches.
object NotificationHelper {

    const val CHANNEL_DAILY  = "esaver_daily_reminder"
    const val CHANNEL_WEEKLY = "esaver_weekly_report"
    const val CHANNEL_ALERT  = "esaver_co2_alert"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)

            // Daily reminder channel - normal priority, just a nudge
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_DAILY,
                    "Daily Energy Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Reminds you to log your daily energy use"
                }
            )

            // Weekly report channel - summary every Sunday morning
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_WEEKLY,
                    "Weekly CO\u2082 Report",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Sunday summary of your weekly carbon footprint"
                }
            )

            // Alert channel - higher priority since user exceeded their CO2 limit
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ALERT,
                    "CO\u2082 Limit Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Fires when your daily CO\u2082 exceeds your goal"
                }
            )
        }
    }
}

// Runs once every 24 hours to remind the user to log their energy use.
// The reminder time is set by the user in ProfileScreen (default 8 PM).
// Tapping the notification takes the user straight to the Log screen.
class DailyReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val hour   = inputData.getInt(KEY_HOUR, 20)
        val minute = inputData.getInt(KEY_MIN, 0)

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("esaver://log"),
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_DAILY)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("\uD83C\uDF31 Time to log your energy use!")
            .setContentText("Tap to record today's appliances and keep your CO\u2082 on track.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "You haven't logged energy use today. " +
                            "Every entry helps build your weekly CO\u2082 report. Tap to log now."
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_DAILY, notification)
        return Result.success()
    }

    companion object {
        const val KEY_HOUR       = "daily_hour"
        const val KEY_MIN        = "daily_min"
        const val NOTIF_ID_DAILY = 1001
        const val WORK_NAME      = "esaver_daily_reminder"
    }
}

// Fires every Sunday at 9 AM with a summary of the week's energy use.
// Pulls total kWh and CO2 from input data and compares against the user's goal.
// Includes an "Email Report" action so the user can share their summary.
class WeeklyReportWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val totalKwh    = inputData.getFloat(KEY_TOTAL_KWH, 27.2f)
        val totalCo2    = inputData.getFloat(KEY_TOTAL_CO2, 10.3f)
        val goalKg      = inputData.getFloat(KEY_GOAL_KG, 15.0f)
        val percentGoal = ((totalCo2 / goalKg) * 100).toInt()
        val onTrack     = totalCo2 <= goalKg

        val summaryBody =
            "Weekly ESaver Report\n\n" +
                    "Total Energy: $totalKwh kWh\n" +
                    "Total CO\u2082:    $totalCo2 kg\n" +
                    "Your Goal:    $goalKg kg\n" +
                    "vs Goal:      $percentGoal% \u2014 ${if (onTrack) "On Track \u2705" else "Over Limit \u26A0\uFE0F"}\n\n" +
                    "Keep making mindful energy choices! \uD83C\uDF0D\n" +
                    "\u2014 ESaver App"

        // Pre-filled email so the user can forward the report with one tap
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "My ESaver Weekly CO\u2082 Report")
            putExtra(Intent.EXTRA_TEXT, summaryBody)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingEmail = PendingIntent.getActivity(
            context, 1, emailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_WEEKLY)
            .setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("\uD83D\uDCCA Your Weekly CO\u2082 Report is ready")
            .setContentText(
                "$totalCo2 kg CO\u2082 this week \u2014 ${if (onTrack) "On Track \u2705" else "Over your goal \u26A0\uFE0F"}"
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(summaryBody))
            .addAction(android.R.drawable.ic_dialog_email, "Email Report", pendingEmail)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_WEEKLY, notification)
        return Result.success()
    }

    companion object {
        const val KEY_TOTAL_KWH   = "weekly_kwh"
        const val KEY_TOTAL_CO2   = "weekly_co2"
        const val KEY_GOAL_KG     = "weekly_goal"
        const val NOTIF_ID_WEEKLY = 1002
        const val WORK_NAME       = "esaver_weekly_report"
    }
}

// Triggered right after the user saves a log entry.
// Checks if today's CO2 total has gone over the daily share of their weekly goal.
// If it has, sends a high priority alert straight away.
class CO2AlertWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val todayCo2   = inputData.getFloat(KEY_TODAY_CO2, 0f)
        val dailyLimit = inputData.getFloat(KEY_DAILY_LIMIT, 2.14f) // weekly goal divided by 7

        // nothing to do if still under limit
        if (todayCo2 <= dailyLimit) return Result.success()

        val overage = String.format("%.1f", todayCo2 - dailyLimit)

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ALERT)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("\u26A0\uFE0F CO\u2082 limit exceeded today")
            .setContentText("You're $overage kg over your daily target. Consider reducing usage.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Today's CO\u2082: $todayCo2 kg\n" +
                            "Daily target: $dailyLimit kg\n" +
                            "Overage: $overage kg\n\n" +
                            "Try switching off standby appliances or reducing heating to get back on track."
                )
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID_ALERT, notification)
        return Result.success()
    }

    companion object {
        const val KEY_TODAY_CO2   = "today_co2"
        const val KEY_DAILY_LIMIT = "daily_limit"
        const val NOTIF_ID_ALERT  = 1003
        const val WORK_NAME       = "esaver_co2_alert"
    }
}

// Central helper to schedule or cancel any of the three workers.
// ProfileScreen calls these when the user flips a notification toggle.
object EsaverScheduler {

    // Works out how long until the next [hour]:[minute] and schedules a daily repeat from there.
    fun scheduleDailyReminder(context: Context, hour: Int = 20, minute: Int = 0) {
        val now    = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        val delay = target.timeInMillis - now.timeInMillis

        val data = workDataOf(
            DailyReminderWorker.KEY_HOUR to hour,
            DailyReminderWorker.KEY_MIN  to minute
        )

        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DailyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(DailyReminderWorker.WORK_NAME)
    }

    // Schedules the weekly report for the next Sunday at 9 AM.
    // Pass current stats in so the worker has data to display.
    fun scheduleWeeklyReport(
        context: Context,
        totalKwh: Float = 0f,
        totalCo2: Float = 0f,
        goalKg:   Float = 15f
    ) {
        val now    = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.WEEK_OF_YEAR, 1)
        }
        val delay = target.timeInMillis - now.timeInMillis

        val data = workDataOf(
            WeeklyReportWorker.KEY_TOTAL_KWH to totalKwh,
            WeeklyReportWorker.KEY_TOTAL_CO2 to totalCo2,
            WeeklyReportWorker.KEY_GOAL_KG   to goalKg
        )

        val request = PeriodicWorkRequestBuilder<WeeklyReportWorker>(7, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WeeklyReportWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelWeeklyReport(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WeeklyReportWorker.WORK_NAME)
    }

    // Call this right after the user saves a log entry.
    // dailyLimit = user's weekly goal divided by 7.
    fun checkCO2Alert(context: Context, todayCo2: Float, dailyLimit: Float = 2.14f) {
        val data = workDataOf(
            CO2AlertWorker.KEY_TODAY_CO2   to todayCo2,
            CO2AlertWorker.KEY_DAILY_LIMIT to dailyLimit
        )

        val request = OneTimeWorkRequestBuilder<CO2AlertWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}
