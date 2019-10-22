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
        val actionTimetable: List<Int>,
        val emotions: Emotion,
        val bestFriends: List<BestFriends>
) {
    companion object {
        fun fuckYura(): ChildStatsDto {
            return ChildStatsDto(
                    "", "","", 0L, 0, 0, 0, emptyList(), Emotion.fuckYura(), emptyList()
            )
        }
    }
}

data class ActionTimetableItem(
        val iconUrl: String,
        val name: String,
        val durationString: String
)

data class BestFriends(
        val photoUrl: String,
        val name: String,
        val timeTogether: Long
) {
    companion object {
        fun getTestFriends(seed: Int): BestFriends {
            return BestFriends(
                    "https://api.terra-incognita.tk/static/901977d5-0afe-4c21-95c9-e30d93be5d7d",
                    "Юрец",
                    seed*1000L
            )
        }
    }
}

