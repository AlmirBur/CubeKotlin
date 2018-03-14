import java.util.*

class Cube(private val size: Int) {

    private val value = List(6) { Array(size) { Array(size) { ' ' } } }

    private val faces = mutableMapOf<Char, Int>()

    private val colors = listOf('W', 'Y', 'R', 'O', 'B', 'G')

    private val facesName = listOf('F', 'B', 'L', 'R', 'D', 'U')

    init {
        for (i in 0..5) {
            for (j in 0 until size) {
                for (k in 0 until  size) {
                    value[i][j][k] = colors[i]
                }
            }
            faces.put(facesName[i], i)
        }
    }

    fun getFace(name: Char): Array<Array<Char>> = value[faces[name]!!]

    private fun turn(name: Char, direction: Int) {
        if (!(name in facesName && direction in 0..1)) throw IllegalArgumentException()
        val rotate = Array(size) { Array(size) { ' ' } }
        when (direction) {
            0 -> for (i in 0 until size) for (j in 0 until size)
                rotate[i][j] = value[faces[name]!!][j][size - 1 - i] //против часовой
            1 -> for (i in 0 until size) for (j in 0 until size)
                rotate[i][j] = value[faces[name]!!][size - 1 - j][i] //по часовой
            else -> throw IllegalArgumentException()
        }
        for (i in 0 until size) for (j in 0 until size)
            value[faces[name]!!][i][j] = rotate[i][j]
    }

    fun rotateFaces(axis: Int, numbers: List<Int>, direction: Int) {
        for (i in 0 until numbers.size) rotateFace(axis, numbers[i], direction)
    }

    fun rotateFace(axis: Int, number: Int, direction: Int) {
        if (!(axis in 0..2 && number in 0 until size && direction in 0..1)) throw IllegalArgumentException()
        val recover = when (axis) {
            1 -> { //Y
                this.rotateCube(4)
                5
            }
            2 -> { //Z
                this.rotateCube(1)
                0
            }
            else -> -1 //X
        }
        var names = listOf<Char>()
        when (direction) {
            0 -> {
                names = listOf('F', 'U', 'B', 'D')//ВВЕРХ
            }
            1 -> {
                names = listOf('F', 'D', 'B', 'U')//ВНИЗ
            }
        }
        val temp = mutableListOf<Char>()
        for (j in 0 until size) {
            temp.add(value[faces[names[3]]!!][j][number])
        }
        for (j in 0 until size) {
            value[faces[names[3]]!!][j][number] = value[faces[names[2]]!!][size - 1 - j][size - 1 - number]
        }
        for (j in 0 until size) {
            value[faces[names[2]]!!][size - 1 - j][size - 1 - number] = value[faces[names[1]]!!][j][number]
        }
        for (j in 0 until size) {
            value[faces[names[1]]!!][j][number] = value[faces[names[0]]!!][j][number]
        }
        for (j in 0 until size) {
            value[faces[names[0]]!!][j][number] = temp[j]
        }
        if (number == 0) this.turn('L', 0 + direction) //вообще '(0 + direction) % 2', но '%2' не влияет
        if (number == size - 1) this.turn('R', (1 + direction) % 2)
        if (recover != -1) this.rotateCube(recover)
    }

    fun rotateCube(direction: Int) {
        val names: List<Char>
        when (direction) {
            0 -> {
                names = listOf('F', 'R', 'B', 'L', 'U', 'D')
            }
            1 -> {
                names = listOf('L', 'B', 'R', 'F', 'D', 'U')
            }
            2 -> {
                names = listOf('F', 'U', 'B', 'D', 'L', 'R')
                this.turn('U', 0)
                this.turn('U', 0)
                this.turn('B', 0)
                this.turn('B', 0)
            }
            3 -> {
                names = listOf('D', 'B', 'U', 'F', 'R', 'L')
                this.turn('D', 0)
                this.turn('D', 0)
                this.turn('B', 0)
                this.turn('B', 0)
            }
            4 -> {
                names = listOf('L', 'D', 'R', 'U', 'F', 'B')
                for (i in 0..3) this.turn(names[i], 0)
            }
            5 -> {
                names = listOf('U', 'R', 'D', 'L', 'B', 'F')
                for (i in 0..3) this.turn(names[i], 1)
            }
            else -> throw IllegalArgumentException()
        }
        //ПЕРЕИМЕНОВКА 4 ГРАНЕЙ
        val temp = faces[names[3]]!!
        for (i in 3 downTo 1) faces[names[i]] = faces[names[i - 1]]!!
        faces[names[0]] = temp
        //ПОВОРОТ 2 ГРАНЕЙ НА 90 ГРАДУСОВ
        this.turn(names[4], 0)
        this.turn(names[5], 1)
    }

    fun confuse() {
        for (i in 0 until 30 * size)
            this.rotateFace(Random().nextInt(3), Random().nextInt(size), Random().nextInt(2))
    }

    fun printlnCube(){
        for (i in 0..5) {
            println(facesName[i] + ":")
            for (j in 0 until this.size) {
                for (k in 0 until this.size) print(value[faces[facesName[i]]!!][j][k])
                println()
            }
        }
    }

    fun printlnFace(name: Char) {
        println(name + ":")
        for (j in 0 until this.size) {
            for (k in 0 until this.size) print(value[faces[name]!!][j][k])
            println()
        }
    }
}

fun main(args: Array<String>) {
    val c = Cube(3)
    c.printlnCube()
    c.rotateFace(0, 2, 0)
    c.printlnCube()
    c.rotateFace(2, 0, 0)
    c.printlnCube()
    c.rotateFace(2, 2, 1)
    c.printlnCube()
    c.rotateFace(0, 0, 1)
    c.printlnCube()
    c.rotateFace(1, 2, 1)
    c.printlnCube()
    c.rotateCube(2)
    c.printlnCube()
    c.rotateCube(4)
    c.printlnCube()
    c.rotateCube(0)
    c.printlnCube()
    c.rotateCube(5)
    c.printlnCube()
    //результат подтвердился на кубике рубике
    val randomCube = Cube(6)
    randomCube.confuse()
    randomCube.printlnCube()
}