package com.spacebitlabs.plantly.reminder

import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.spacebitlabs.plantly.data.Prefs
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

open class WorkReminder(private val prefs: Prefs) {

    open fun cancelDailyReminder() {
        val workReminderId = prefs.getWorkReminderId()
        if (workReminderId.isNotEmpty()) {
            val workReminderUuid = UUID.fromString(workReminderId)
            WorkManager.getInstance().cancelWorkById(workReminderUuid)
        }
    }

    open fun scheduleDailyReminder() {
        if (prefs.getWorkReminderId().isNotEmpty()) {
            val workInfoListenable = WorkManager.getInstance().getWorkInfoById(UUID.fromString(prefs.getWorkReminderId()))
            // if work already exists, don't schedule another reminder
            if (workInfoListenable.get() != null) return
        }

        val workReminder = PeriodicWorkRequestBuilder<WaterPlantReminder>(1, TimeUnit.DAYS).build()
        Timber.d("Scheduling work reminder")
        prefs.setWorkReminderId(workReminder.id)
        WorkManager.getInstance().enqueue(workReminder)
    }
}