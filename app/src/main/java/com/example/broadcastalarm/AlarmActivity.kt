package com.example.broadcastalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import com.example.broadcastalarm.broadcast.AlertBroadcastReceiver
import com.example.broadcastalarm.databinding.ActivityAlarmBinding
import java.text.DateFormat
import java.util.Calendar


class AlarmActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    private val binding by lazy { ActivityAlarmBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonTimepicker.setOnClickListener {
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "TAG")
        }
        binding.buttonCancel.setOnClickListener {
            cancelBroadcast()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, 0)

        val dataFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        val textTime = "Alarm set for: ${dataFormat}"
        binding.TextView.text = textTime

        setAlarm(c)
    }

    private fun setAlarm(c: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    private fun cancelBroadcast() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        binding.TextView.text = "Alarm cancelled"
    }
}