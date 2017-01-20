package ischool.noosphere.qpointer.presenter

import android.content.Context
import android.content.SharedPreferences
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ischool.noosphere.qpointer.view.AlarmView
import ischool.noosphere.sensebridge.model.Alarm
import java.util.*

@InjectViewState
class AlarmPresenter(context: Context) : MvpPresenter<AlarmView>() {

    val ALARM_PREFERENCES: String = "ALARM_PREFERENCES"
    val ALARM_LIST: String = "ALARM_LIST"

    val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(ALARM_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }

    fun removeAlarm(alarm: Alarm) {
        setAlarmList(getAlarmList() - alarm)
        viewState.setAlarmListData(getAlarmList())
    }

    fun addAlarm(alarm: Alarm) {
        setAlarmList(getAlarmList() + alarm)
        viewState.setAlarmListData(getAlarmList())
    }

    fun checkAlarm(alarm: Alarm, isChecked: Boolean) {
        val alarmsNew = LinkedList<Alarm>()
        for(a in getAlarmList()) {
            if(alarm.equals(a)) {
                alarmsNew.add(Alarm(a.hour, a.minute, isChecked))
            } else {
                alarmsNew.add(Alarm(a.hour, a.minute, false))
            }
        }
        setAlarmList(alarmsNew)
        viewState.setAlarmListData(getAlarmList())
    }

    fun setAlarmList(alarmList: List<Alarm>) {
        prefs.edit().putString(ALARM_LIST, Gson().toJson(alarmList)).apply()
    }

    fun getAlarmList(): List<Alarm> {
        val alarmsType = object : TypeToken<List<Alarm>>() {}.type
        try {
            return Gson().fromJson<List<Alarm>>(prefs.getString(ALARM_LIST, ""), alarmsType)
        } catch (e :Exception) {
            return emptyList()
        }

    }

}