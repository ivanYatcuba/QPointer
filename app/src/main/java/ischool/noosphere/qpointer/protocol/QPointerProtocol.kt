package ischool.noosphere.qpointer.protocol

import android.util.Log
import ischool.noosphere.qpointer.QPointerCommands
import ischool.noosphere.sensebridge.model.Alarm
import java.util.*


object QPointerProtocol {

    fun setHour(hour: Int) : String {
        return buildCommand(QPointerCommands.SET_HOURS, formatTime(hour))
    }

    fun setMinute(minute: Int) : String {
        return buildCommand(QPointerCommands.SET_MINUTES, formatTime(minute))
    }

    fun setSecond(second: Int) : String {
        return buildCommand(QPointerCommands.SET_SECONDS,  formatTime(second))
    }

    fun setColor(color: String) : String {
        return buildCommand(QPointerCommands.SET_COLOR, color.toUpperCase())
    }

    fun unsetAlarm():  String {
        return buildCommand(QPointerCommands.SET_ALARM_OFF, "")
    }

    fun setAlarm(alarm: Alarm): List<String> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, alarm.hour)
        cal.set(Calendar.MINUTE, alarm.minute)
        cal.add(Calendar.MINUTE, -5)
        val commands = LinkedList<String>()
        commands.add(buildCommand(QPointerCommands.SET_ALARM_OFF, ""))
        Log.d("HOURS", formatTime(cal.get(Calendar.HOUR_OF_DAY)))
        Log.d("MINUTE", formatTime(cal.get(Calendar.MINUTE)))
        commands.add(buildCommand(QPointerCommands.SET_ALARM_HOURS, formatTime(cal.get(Calendar.HOUR_OF_DAY))))
        commands.add(buildCommand(QPointerCommands.SET_ALARM_MINUTES, formatTime(cal.get(Calendar.MINUTE))))
        return commands
    }

    fun setLasers(leftLaser: Boolean, rightLaser: Boolean) : String {
        val rightState = if(rightLaser)  "1" else "0"
        val leftState = if(leftLaser)  "1" else "0"
        return buildCommand(QPointerCommands.SET_LASER_STATE, rightState + leftState)
    }

    private fun buildCommand(command: QPointerCommands, commandValue: String): String {
        return command.command + commandValue
    }

    private fun formatTime(time: Int) : String {
        if(time < 0) throw IllegalArgumentException("wrong time!")
        if(time < 10) {
            return "0" + time.toString() //adding leading zero
        } else{
            return time.toString()
        }
    }

}