@file:Suppress("MagicNumber")


object Day02 {

    private const val MAX_RED = 12
    private const val MAX_GREEN = 13
    private const val MAX_BLUE = 14


    fun part1(input: List<String>) =
        input.map { it.toGame() }
            .filter { game -> game.showings.all { it.red <= MAX_RED && it.green <= MAX_GREEN && it.blue <= MAX_BLUE } }
            .sumOf { it.index }

    fun part2(input: List<String>) =
        input.map { it.toGame() }
            .map { it.showings }
            .sumOf { it.maxOf(Showing::red) * it.maxOf(Showing::green) * it.maxOf(Showing::blue) }
}

private fun String.toGame(): Game {
    val indexAndShowingsRaw = this.split(": ")

    val index = convertToIndex(indexAndShowingsRaw)
    val showings = convertToShowings(indexAndShowingsRaw)

    return Game(index, showings)
}

private fun convertToShowings(indexAndShowingsRaw: List<String>) = indexAndShowingsRaw
    .last()
    .split("; ")
    .map { showingString -> showingString.split(", ") }
    .map { showingStrings ->
        showingStrings
            .map { it.split(" ") }
            .map { it.first() to it.last() }
    }
    .map { showingPairs ->
        Showing(
            red = showingPairs.getAmountForColor("red"),
            green = showingPairs.getAmountForColor("green"),
            blue = showingPairs.getAmountForColor("blue")
        )
    }

private fun List<Pair<String, String>>.getAmountForColor(color: String) =
    find { it.second == color }?.first?.toInt() ?: 0

private fun convertToIndex(indexSplitFromShowings: List<String>) =
    indexSplitFromShowings
        .first()
        .substringAfter("Game ")
        .toInt()

private class Game(val index: Int, val showings: List<Showing>)

private class Showing(val red: Int, val green: Int, val blue: Int)

fun main() {

    val testInput = readInput("Day02_test")
    check(Day02.part1(testInput).also(::println) == 8)
    check(Day02.part2(testInput).also(::println) == 2286)


    val input = readInput("Day02")
    Day02.part1(input).println()
    Day02.part2(input).println()
}
