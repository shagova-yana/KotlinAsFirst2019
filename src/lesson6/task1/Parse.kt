@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import java.lang.StringBuilder
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int


/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val months = listOf<String>(
        "января", "февраля", "марта", "апреля", "мая", "июня",
        "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    val parts = str.split(" ")
    if (parts.size != 3) return ""
    val month = parts[1]
    val day = parts[0].toIntOrNull()
    val years = parts[2].toIntOrNull()
    var m = 0
    if (months.contains(month))
        m = months.indexOf(month) + 1
    else return ""
    if (day!! <= daysInMonth(m, years!!))
        return String.format("%02d.%02d.%d", day, m, years)
    return ""
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val months = mapOf<Int, String>(
        1 to "января", 2 to "февраля", 3 to "марта",
        4 to "апреля", 5 to "мая", 6 to "июня", 7 to "июля", 8 to "августа",
        9 to "сентября", 10 to "октября", 11 to "ноября", 12 to "декабря"
    )
    val part = digital.split(".").toList()
    try {
        if (part.size == 3) {
            val day = part[0].toInt()
            val month = part[1].toInt()
            val year = part[2].toInt()
            if (day <= daysInMonth(month, year) && month in months)
                return String.format("%d %s %d", day, months[month], year)
        } else return ""
    } catch (e: NumberFormatException) {
        return ""
    }
    return ""
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    return if (!phone.matches(Regex("""\+?\d+[\s-]*(\(\d+[\s-]*\d*\))*[\d*\s-]*""")))
        ""
    else {
        val regex = Regex("""\+\d+|\d+""").findAll(phone)
        val result = StringBuilder()
        for (i in regex)
            result.append(i.value)
        result.toString()
    }
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.matches(Regex("""(\d+[\s-%]*)*.з"""))) {
        val list = mutableListOf<Int>()
        val regex = Regex("""\d+""").findAll(jumps)
        for (i in regex)
            list.add(i.value.toInt())
        return list.max()!!
    }
    return -1
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    var max = -1
    if (jumps.matches(Regex("""\d+([\s-+%]*\d*)*"""))) {
        val reges = Regex("""\d+\s+\+""").findAll(jumps)
        for (i in reges) {
            val number = Regex("""\d+""").find(i.value)
            if (number!!.value.toInt() > max) max = number.value.toInt()
        }
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (expression.matches(Regex("""\d+(\s(\+|\-)\s\d+)*"""))) {
        val number = Regex("""\d+""").findAll(expression).drop(1)
        val char = Regex("""\+|\-""").findAll(expression).toList()
        val first = Regex("""\d+""").find(expression)
        var sum = first!!.value.toInt()
        var i = 0
        for (dig in number) {
            when (char[i].value) {
                "+" -> sum += dig.value.toInt()
                "-" -> sum -= dig.value.toInt()
            }
            i++
        }
        return sum
    } else
        throw IllegalArgumentException(expression)
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val list = str.split(" ").toList()
    if (list.size == 1)
        return -1
    var i = -1
    var sum = 0
    var f = 0
    do {
        i++
        if (i == list.size - 1) {
            f++
            break
        }
        sum += list[i].length
    } while (list[i].toLowerCase() != list[i + 1].toLowerCase())
    if (f != 1) {
        sum += i - list[i].length
        return sum
    } else return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    var max = 0.0
    if (description.matches(Regex("""[А-Я]*[а-я]*[a-z]*[A-Z]*\s\d+(\.\d+)*\;*\s*(\s*[А-Я]*[а-я]*[a-z]*[A-Z]*\s\d+(\.\d+)*\;*)*"""))){
        val list = description.split(" ").toList()
        var index = 0
        for (i in 1 until list.size step 2) {
            val value = Regex("""\d+(\.\d+)*""").find(list[i])!!.value.toDouble()
            if (value >= 0 && value > max) {
                max = value
                index = i
            }
        }
        return if (max != 0.0) list[index - 1]
        else "Any good with price 0.0"
    } else return ""
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int = TODO() /**{
    if (roman.matches(Regex("""[IVXLCDM]*""")))  {
        val map = mutableMapOf<Int, String>(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII",
            8 to "VIII", 9 to "IX", 10 to "X", 11 to "XI", 12 to "XII", 13 to "XIII", 14 to "XIV", 15 to "XV", 16 to "XVI",
            17 to "XVII", 18 to "XVIII", 19 to "XIX", 20 to "XX", 30 to "XXX", 40 to "XL", 50 to "L", 60 to "LX",
            70 to "LXX", 80 to "LXXX", 90 to "XC", 100 to "C", 200 to "CC", 300 to "CCC", 400 to "CD", 500 to "D",
            600 to "DC", 700 to "DCC", 800 to "DCCC", 900 to "CM", 1000 to "M", 2000 to "MM", 3000 to "MMM")
        val thouse = Regex("""^\M*""").findAll(roman).toList()
        val hundred = Regex("""\C*|\CD?|\CM?|\D\C*""").findAll(roman).toList()
    }
}
     */
/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> = TODO()
