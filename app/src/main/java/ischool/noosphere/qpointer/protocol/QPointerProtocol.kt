package ischool.noosphere.qpointer.protocol

import android.util.Log
import ischool.noosphere.qpointer.QPointerCommands
import ischool.noosphere.sensebridge.model.Alarm
import java.util.*


object QPointerProtocol {

    fun setHour(hour: Int) : String {
        return buildCommand(QPointerCommands.UH, formatTime(hour))
    }

    fun setMinute(minute: Int) : String {
        return buildCommand(QPointerCommands.UM, formatTime(minute))
    }

    fun setSecond(second: Int) : String {
        return buildCommand(QPointerCommands.US,  formatTime(second))
    }

    fun setColor(color: String) : String {
        return buildCommand(QPointerCommands.UC, color)
    }

    fun unsetAlarm():  String {
        return buildCommand(QPointerCommands.AOFF, "")
    }

    fun setAlarm(alarm: Alarm): List<String> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, alarm.hour)
        cal.set(Calendar.MINUTE, alarm.minute)
        cal.add(Calendar.MINUTE, -5)
        val commands = LinkedList<String>()
        commands.add(buildCommand(QPointerCommands.AOFF, ""))
        Log.d("HOURS", formatTime(cal.get(Calendar.HOUR_OF_DAY)))
        Log.d("MINUTE", formatTime(cal.get(Calendar.MINUTE)))
        commands.add(buildCommand(QPointerCommands.AH, formatTime(cal.get(Calendar.HOUR_OF_DAY))))
        commands.add(buildCommand(QPointerCommands.AM, formatTime(cal.get(Calendar.MINUTE))))
        return commands
    }

    fun setLasers(leftLaser: Boolean, rightLaser: Boolean) : String {
        val rightState = if(rightLaser == true)  "1" else "0"
        val leftState = if(leftLaser == true)  "1" else "0"
        return buildCommand(QPointerCommands.UL, rightState + leftState)
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