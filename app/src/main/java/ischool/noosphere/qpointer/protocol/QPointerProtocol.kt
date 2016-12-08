package ischool.noosphere.qpointer.protocol

import ischool.noosphere.qpointer.QPointerCommands

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