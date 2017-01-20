package ischool.noosphere.sensebridge.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import ischool.noosphere.qpointer.R
import ischool.noosphere.sensebridge.model.Alarm
import kotlinx.android.synthetic.main.item_alarm.view.*
import java.text.DecimalFormat

class AlarmAdapter(val alarms: List<Alarm>, val onAlarmActionListener: OnAlarmActionListener): RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    interface OnAlarmActionListener {

        fun alarmSwitched(alarm: Alarm, isChecked: Boolean)

        fun alarmTouched(alarm: Alarm)
    }


    override fun onBindViewHolder(holder: AlarmViewHolder?, position: Int) {
        val alarm = alarms[position]
        val df = DecimalFormat("00")
        holder?.time?.text = df.format(alarm.hour) + ":" + df.format(alarm.minute)
        holder?.disableOnCheckedChangeListener()
        holder?.enabled?.isChecked = alarm.enabled
        holder?.alarm = alarm
        holder?.enableOnCheckedChangeListener(alarm)
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    fun getItem(position: Int): Alarm {
        return alarms[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlarmViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view, onAlarmActionListener, alarms[0])
    }

    class AlarmViewHolder(view: View, val onAlarmActionListener: OnAlarmActionListener, initAlarm: Alarm) : RecyclerView.ViewHolder(view) {

        val time: TextView = view.alarm_time
        val enabled: Switch = view.alarm_enabled
        var alarm: Alarm = initAlarm

        init {
            view.setOnClickListener {
                onAlarmActionListener.alarmTouched(alarm)
            }
        }

        fun disableOnCheckedChangeListener() {
            enabled.setOnCheckedChangeListener(null)
        }

        fun enableOnCheckedChangeListener(alarm: Alarm) {
            enabled.setOnCheckedChangeListener { compoundButton, b -> onAlarmActionListener.alarmSwitched(alarm, b) }
        }

    }


}