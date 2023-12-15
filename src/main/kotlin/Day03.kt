@file:Suppress("MagicNumber")


object Day03 {

    fun part1(input: List<String>): Int {
        val symbolCoordinates: List<Coordinate> = extractSymbolCoordinates(input)
        val numberEntries: List<NumberEntry> = extractNumberEntries(input)
        return numberEntries
            .filter { numberEntry ->
                symbolCoordinates.any { symbolCoordinate ->
                    numberEntry.coordinates.any { symbolCoordinate.isAdjacentTo(it) }
                }
            }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val asteriskCoordinates = extractAsteriskCoordinates(input)
        val numberEntries = extractNumberEntries(input)
        return asteriskCoordinates
            .map { asteriskCoordinate ->
                numberEntries.filter { numberEntry -> numberEntry.coordinates.any { it.isAdjacentTo(asteriskCoordinate) } }
            }
            .filter { it.size == 2 }
            .map { numberEntryDublets -> numberEntryDublets.map { it.value } }
            .fold(0) {acc, numberDublets ->
                acc + numberDublets.first() * numberDublets.last()
            }
    }
}

private class Coordinate(val row: Int, val column: Int) {

    fun isAdjacentTo(other: Coordinate): Boolean {
        return this.row in listOf(other.row - 1, other.row, other.row + 1) &&
                this.column in listOf(other.column - 1, other.column, other.column + 1)
    }
}

private class NumberEntry(val value: Int, val coordinates: List<Coordinate>)

private fun extractSymbolCoordinates(input: List<String>) =
    input.mapIndexed { row, line ->
        line.mapIndexedNotNull { column, char ->
            if (char.isSymbol()) {
                Coordinate(row = row, column = column)
            } else {
                null
            }
        }
    }.flatten()

private fun extractAsteriskCoordinates(input: List<String>) =
    input.mapIndexed { row, line ->
        line.mapIndexedNotNull { column, char ->
            if (char == '*') {
                Coordinate(row = row, column = column)
            } else {
                null
            }
        }
    }.flatten()

private fun Char.isSymbol() = !isDigit() && this != '.'

private fun extractNumberEntries(input: List<String>): List<NumberEntry> {
    val numberEntries: MutableList<NumberEntry> = mutableListOf()

    input.forEachIndexed { row, line ->
        var valueString = ""
        val coordinates = mutableListOf<Coordinate>()
        line.forEachIndexed { column, char ->
            if (char.isDigit()) {
                valueString += char
                coordinates.add(Coordinate(row = row, column = column))
            } else {
                valueString.takeIf { it.isNotBlank() }?.let {
                    numberEntries.add(NumberEntry(it.toInt(), coordinates.toList()))
                }
                valueString = ""
                coordinates.clear()
            }
        }
        valueString.takeIf { it.isNotBlank() }?.let {
            numberEntries.add(NumberEntry(it.toInt(), coordinates.toList()))
        }
    }
    return numberEntries
}

fun main() {

    val testInput = readInput("Day03_test")
    check(Day03.part1(testInput).also(::println) == 4361)
    check(Day03.part2(testInput).also(::println) == 467835)

    val input = readInput("Day03")
    Day03.part1(input).println()
    Day03.part2(input).println()
}
