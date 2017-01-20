package ischool.noosphere.sensebridge.fragment

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import ischool.noosphere.sensebridge.model.Alarm
import java.util.*


class TimePickerFragment(val alarmListener: OnAlarmCreated) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    var alarm: Alarm? = null


    interface OnAlarmCreated {
        fun onAlarmCreated(alarm: Alarm)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        if(alarm != null) {
            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, alarm!!.hour, alarm!!.minute,
                    DateFormat.is24HourFormat(activity))
        }  else {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hour, minute,
                    DateFormat.is24HourFormat(activity))
        }

    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(alarm != null) {
            alarmListener.onAlarmCreated(Alarm(hourOfDay, minute, alarm!!.enabled))
        } else {
            alarmListener.onAlarmCreated(Alarm(hourOfDay, minute, false))
        }

    }
}