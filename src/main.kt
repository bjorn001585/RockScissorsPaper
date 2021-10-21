import java.lang.Math.abs
import kotlin.math.floor

data class Rule(val type: String, val rules: Array<String>, val score: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rule

        if (type != other.type) return false
        if (!rules.contentEquals(other.rules)) return false
        if (score != other.score) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + rules.contentHashCode()
        result = 31 * result + score
        return result
    }
}

data class Score(private var _you: Int = 0, private var _AI: Int = 0) {
    var you: Int
        get() = _you
        set(score) { _you += score }

    var comp: Int
        get() = _AI
        set(score) { _AI += score }
}

val rules = arrayOf(
    Rule("Победа", arrayOf("rock", "scissors"), 1),
    Rule("Победа", arrayOf("scissors", "paper"), 1),
    Rule("Победа", arrayOf("paper", "rock"), 1),
    Rule("Проиграл", arrayOf("rock", "paper"), 1),
    Rule("Проиграл", arrayOf("scissors", "rock"), 1),
    Rule("Проиграл", arrayOf("paper", "scissors"), 1),
    Rule("Ничья ", arrayOf("rock", "rock"), 0),
    Rule("Ничья ", arrayOf("scissors", "scissors"), 0),
    Rule("Ничья ", arrayOf("paper", "paper"), 0)
)

val translateText = mapOf(
    "rock" to "Камень",
    "paper" to "Бумага",
    "scissors" to "Ножницы"
)

val score = Score(0, 0)
fun run(alg: String?) {
    print("Укажите ставку [paper, scissors, rock]: ")
    val you = readLine()
    val comp = if(alg == "1") random() else ai(you)
    clearConsole()
    if(!arrayOf("paper", "scissors", "rock").contains(you)) return run(alg)
    val rule = rules.find { it.rules[0] == you && it.rules[1] == comp }

    if(rule?.type == "Победа") { score.you = rule.score }
    if(rule?.type == "Проиграл") { score.comp = rule.score }

    val scoreText = "Ваш: ${score.you} [ Счёт ] Комп: ${score.comp}"
    val youText = "Вы выбрали: ${translateText[you]}"
    val compText = "Комп выбрал: ${translateText[comp]}"

    val diffScoreText = 38 - scoreText.length
    val diffYouText = 37 - youText.length
    val diffCompText = 37 - compText.length

    val winText = rule?.type
    val diffWinText = 38 - winText?.length!!

    val scene =
        "+------------------------------------+\n" +
        "| Игра: Камень Ножницы Бумага        |\n" +
        "+------------------------------------+\n" +
        "|${" ".repeat(diffScoreText / 2)}${scoreText}${" ".repeat((diffScoreText / 2.5).toInt())}|\n" +
        "+------------------------------------+\n" +
        "| ${youText}${" ".repeat(diffYouText - 2)}|\n" +
        "| ${compText}${" ".repeat(diffCompText - 2)}|\n" +
        "+------------------------------------+\n" +
        "|${" ".repeat(diffWinText / 2)}${winText}${" ".repeat(floor(diffWinText / 2.25).toInt())}|\n" +
        "+------------------------------------+"

    println(scene)
    run(alg)
}

fun main(args: Array<String>) {
    clearConsole()
    println("Выбор алгоритма:")
    println("1 - Рандом (Рандомные ответы от компа)")
    println("2 - Сложный (Его будет сложно обыграть)")
    print("Выберите алгоритм: ")
    val alg = readLine()

    if(alg != "1" && alg != "2") return main(args)

    run(alg)
}

fun random(): String {
    val items = arrayOf("rock", "scissors", "paper")
    val index = floor(Math.random() * items.size)
    return items[index.toInt()]
}

data class Cache(val key: String, var value: Int)

val cache = mutableListOf(
    Cache("paper", 1),
    Cache("scissors", 1),
    Cache("rock", 1)
)

fun ai(key: String?): String {
    for(index in 0 until cache.size) {
        val c = cache[index]
        if(c.key == key) {
            c.value += 1
            cache[index] = c
        }
    }

    val list = cache.sortedByDescending { it.value }

    return when(list[0].key) {
        "paper" -> "scissors"
        "scissors" -> "rock"
        else -> "paper"
    }
}

fun clearConsole() {
    for (clear in 0..100) {
        println("\b")
    }
}
