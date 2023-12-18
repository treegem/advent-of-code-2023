@file:Suppress("MagicNumber")

import kotlin.math.pow


object Day04 {

    fun part1(input: List<String>): Int =
        getMatchingNumbersAmount(input)
            .filterNot { it == 0 }
            .sumOf { 2.0.pow(it - 1) }
            .toInt()

    fun part2(input: List<String>): Int {
        val matchingNumbers = getMatchingNumbersAmount(input)

        val cardCopies = List(matchingNumbers.size) { it to 1 }.toMap().toMutableMap()

        repeat(matchingNumbers.size) { cardIndex ->
            val relevantMatchingNumbers = matchingNumbers.subList(0, cardIndex)
            relevantMatchingNumbers
                .mapIndexed { index, number -> index to number }
                .filter { indexedMatchingNumber ->
                    cardIndex - indexedMatchingNumber.first <= indexedMatchingNumber.second
                }
                .forEach {
                    cardCopies[cardIndex] = cardCopies[cardIndex]!! + cardCopies[it.first]!!
                }
        }

        return cardCopies.map { it.value }.sum()
    }
}

private fun getMatchingNumbersAmount(input: List<String>): List<Int> =
    input
        .map { line ->
            getWinningNumbers(line)
                .intersect(getMyNumbers(line).toSet())
                .size
        }

private fun getWinningNumbers(line: String): List<Int> =
    line.split(" | ")
        .first()
        .split(": ")
        .last()
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }

private fun getMyNumbers(line: String): List<Int> =
    line.split(" | ")
        .last()
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toInt() }

fun main() {

    val testInput = readInput("Day04_test")
    check(Day04.part1(testInput).also(::println) == 13)
    check(Day04.part2(testInput).also(::println) == 30)

    val input = readInput("Day04")
    Day04.part1(input).println()
    Day04.part2(input).println()
}
