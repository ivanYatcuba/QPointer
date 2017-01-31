package ischool.noosphere.qpointer

enum class QPointerCommands(val command: String) {

    SET_SECONDS("US"), SET_MINUTES("UM"), SET_HOURS("UH"),
    SET_COLOR("UC"), SET_LASER_STATE("UL"), SET_ALARM_HOURS("AH"),
    SET_ALARM_MINUTES("AM"), SET_ALARM_OFF("AOFF")

}