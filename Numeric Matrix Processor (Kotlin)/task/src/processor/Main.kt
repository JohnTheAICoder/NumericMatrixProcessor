package processor

fun main() {
    while (true) {
        println(
            """
            1. Add matrices
            2. Multiply matrix by a constant
            3. Multiply matrices
            4. Transpose matrix
            5. Calculate a determinant
            6. Inverse matrix
            0. Exit
        """.trimIndent()
        )
        print("Your choice: ")

        val choice = readLine()?.toIntOrNull()
        when (choice) {
            1 -> addMatrices()
            2 -> multiplyMatrixByConstant()
            3 -> multiplyMatrices()
            4 -> transposeMatrix()
            5 -> getDeterminant()
            6 -> inverseMatrix()
            0 -> {
                break
            }

            else -> println("Invalid choice. Please enter a valid number.")
        }
    }
}

fun enterMatrix(name: String = ""): MutableList<List<Double>> {
    print("Enter$name matrix size: ")
    val dimensions = readLine()?.split(" ")?.mapNotNull { it.toIntOrNull() }
    val matrix = mutableListOf<List<Double>>()

    println("Enter$name matrix: ")
    for (i in 1..(dimensions?.get(0) ?: 0)) {
        val row = readLine()?.split(" ")?.mapNotNull { it.toDoubleOrNull() }
        if (row == null || row.size != (dimensions?.get(1) ?: 0)) {
            return matrix
        }
        matrix.add(row)
    }
    return matrix
}

fun addMatrices() {
    val matrix = enterMatrix(" first")
    val secondMatrix = enterMatrix(" second")

    if (matrix.size != secondMatrix.size || matrix[0].size != secondMatrix[0].size) {
        println("The operation cannot be performed.")
        println()
        return
    }
    val matricesAdded = matrix.indices.map { i -> matrix[i].indices.map { j -> matrix[i][j] + secondMatrix[i][j] } }
    printMatrix(matricesAdded)
}

fun multiplyMatrixByConstant() {
    val matrix = enterMatrix()
    print("Enter constant: ")
    val scalar = readLine()?.toDoubleOrNull()

    if (scalar == null || matrix.isEmpty()) {
        println("The operation cannot be performed.")
        println()
        return
    }
    val scaledMatrix = matrix.map { row -> row.map { it * scalar } }
    printMatrix(scaledMatrix)
}

fun multiplyMatrices() {
    val matrix = enterMatrix(" first")
    val secondMatrix = enterMatrix(" second")

    if (matrix[0].size != secondMatrix.size) {
        println("The operation cannot be performed.")
        println()
        return
    }
    val matrixProduct = matrix.map { row -> (secondMatrix[0].indices).map { col -> row.indices.sumOf { k -> row[k] * secondMatrix[k][col] } } }
    printMatrix(matrixProduct)
}

fun transposeMatrix() {
    println()
    println(
        """
        1. Main diagonal
        2. Side diagonal
        3. Vertical line
        4. Horizontal line
        """.trimIndent()
    )
    print("Your choice: ")

    val choice = readLine()?.toIntOrNull()
    when (choice) {
        1 -> transposeMain()
        2 -> transposeSide()
        3 -> transposeVertical()
        4 -> transposeHorizontal()
        else -> println("Invalid choice. Please enter a valid number.")
    }
}

fun transposeMain(passedMatrix: List<List<Double>>? = null): List<List<Double>>? {
    val matrix = passedMatrix ?: enterMatrix()
    val rows = matrix.size
    val cols = matrix[0].size
    val result = List(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            result[j][i] = matrix[i][j]
        }
    }
    return if (passedMatrix != null) {
        result
    } else {
        printMatrix(result)
        null
    }
}

fun transposeSide() {
    val matrix = enterMatrix()
    val rows = matrix.size
    val cols = matrix[0].size
    val result = List(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            result[(rows - 1) - j][(cols - 1) - i] = matrix[i][j]
        }
    }
    printMatrix(result)
}

fun transposeVertical() {
    val matrix = enterMatrix()
    val rows = matrix.size
    val cols = matrix[0].size
    val result = List(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            result[i][(cols - 1) - j] = matrix[i][j]
        }
    }
    printMatrix(result)
}

fun transposeHorizontal() {
    val matrix = enterMatrix()
    val rows = matrix.size
    val cols = matrix[0].size
    val result = List(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            result[(rows - 1) - i][j] = matrix[i][j]
        }
    }
    printMatrix(result)
}

fun getDeterminant() {
    val matrix = enterMatrix()
    println("The result is:")
    println(recursiveDetCalc(matrix))
    println()
}

fun recursiveDetCalc(matrix: List<List<Double>>): Double {
    if (matrix.size == 2 && matrix[0].size == 2) {
        return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
    } else {
        var determinant = 0.0
        for (j in 0 until matrix.size) {
            determinant += (if (j % 2 == 0) 1 else -1) * matrix[0][j] *
                    recursiveDetCalc(matrix.filterIndexed { rowIndex, _ -> rowIndex != 0 }
                        .map { row -> row.filterIndexed { colIndex, _ -> colIndex != j } })
        }
        return determinant
    }
}

fun inverseMatrix() {
    val matrix = enterMatrix()
    val determinant = recursiveDetCalc(matrix)

    if (determinant == 0.0) {
        println("This matrix doesn't have an inverse.")
        println()
    } else {
        val transposedCofactor = transposeMain(getCofactor(matrix))!!
        val inversedMatrix = transposedCofactor.map { row ->
            row.map { element -> element / determinant }
        }
        printMatrix(inversedMatrix)
        println()
    }
}

fun getCofactor(matrix: List<List<Double>>): MutableList<MutableList<Double>> {
    val rows = matrix.size
    val cols = matrix[0].size
    val coFactorMatrix = MutableList(rows) { MutableList(cols) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            val minor = matrix.filterIndexed { rowIndex, _ -> rowIndex != i }
                .map { row -> row.filterIndexed { colIndex, _ -> colIndex != j } }
            val cofactor = (if ((i + j) % 2 == 0) 1 else -1) * recursiveDetCalc(minor)
            coFactorMatrix[i][j] = cofactor
        }
    }
    return coFactorMatrix
}

fun printMatrix(result: List<List<Double>>) {
    println("The result is: ")
    for (row in result) {
        println(row.joinToString(" ") { "%.2f".format(it) })
    }
    println()
}