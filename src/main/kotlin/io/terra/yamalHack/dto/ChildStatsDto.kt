package io.terra.yamalHack.dto

import io.terra.yamalHack.api.entity.Emotion

data class ChildStatsDto(
        val photoUrl: String,
        val name: String,
        val squad: String,
        val actionDuration: Long,
        val energy: Int,
        val distance: Int,
        val rating: Int,
        val actionTimetable: List<ActionTimetableItem>,
        val emotions: Emotion,
        val bestFriends: List<BestFriends>
)

data class ActionTimetableItem(
        val iconUrl: String,
        val name: String,
        val durationString: String
)

data class BestFriends(
        val photoUrl: String,
        val name: String,
        val timeTogether: Long
)