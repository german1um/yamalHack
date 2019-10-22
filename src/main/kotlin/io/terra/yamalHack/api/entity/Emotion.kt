package io.terra.yamalHack.api.entity

data class Emotion (
        val anger: Double,
        val contempt: Double,
        val disgust: Double,
        val fear: Double,
        val happiness: Double,
        val neutral: Double,
        val sadness: Double,
        val surprise: Double
) {
    companion object {
        fun fuckYura(): Emotion {
            return Emotion(
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
            )
        }
    }
}

fun List<Emotion>.average(): Emotion {
    val size = this.size
    return Emotion(
            this.sumByDouble { it.anger }/size,
            this.sumByDouble { it.contempt }/size,
            this.sumByDouble { it.disgust }/size,
            this.sumByDouble { it.fear }/size,
            this.sumByDouble { it.happiness }/size,
            this.sumByDouble { it.neutral }/size,
            this.sumByDouble { it.sadness }/size,
            this.sumByDouble { it.surprise }/size
    )
}