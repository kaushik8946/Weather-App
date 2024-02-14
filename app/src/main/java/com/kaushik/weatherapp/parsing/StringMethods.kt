package com.kaushik.weatherapp.parsing

fun capitalizeFirstLetterOfEachWord(sentence: String): String {
    return sentence.split(" ").joinToString(" ") { capitalizeFirstLetter(it) }

}

fun capitalizeFirstLetter(word: String): String {
    return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()
}