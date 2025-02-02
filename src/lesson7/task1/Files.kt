@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1


import java.io.File
import java.lang.StringBuilder

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    for (char in substrings)
        map[char] = 0
    for (str in File(inputName).readLines()) {
        println(str)
        for (word in str.split(" ")) {
            for (char in substrings) {
                var count = 0
                if (word.toLowerCase() == char.toLowerCase()) {
                    count = map[char]!! + 1
                    map.put(char, count)
                    continue
                }
                if (word.toLowerCase().contains(char.toLowerCase())) {
                    val newChar = char.toLowerCase()
                    val regex = Regex("""[$newChar]""").findAll(word.toLowerCase()).toList()
                    count = map[char]!! + regex.size / char.length
                    map.put(char, count)
                }
            }
        }
    }
    return map
}


/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val output = File(outputName).bufferedWriter()
    for (str in File(inputName).readLines()) {
        if (!str.contains(Regex("""[ЖжШшЧчЩщ][ЫыЯяЮю]"""))) {
            output.write(str)
            output.newLine()
            continue
        }
        val words = str.split(" ")
        var shiftUnderCont = words.size
        for (word in words) {
            if (!word.contains(Regex("""[ЖжШшЧчЩщ][ЫыЯяЮю]"""))) {
                output.write(word)
                shiftUnderCont -= 1
                if (shiftUnderCont != 0) output.write(" ")
                continue
            }
            val newWord = StringBuilder()
            var char = word.toList()
            newWord.append(char[0])
            for (i in 1 until char.size) {
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[Ы]"""))
                ) {
                    newWord.append("И")
                    continue
                }
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[ы]"""))
                ) {
                    newWord.append("и")
                    continue
                }
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[Я]"""))
                ) {
                    newWord.append("А")
                    continue
                }
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[я]"""))
                ) {
                    newWord.append("а")
                    continue
                }
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[Ю]"""))
                ) {
                    newWord.append("У")
                    continue
                }
                if (char[i - 1].toString().matches(Regex("""[ЖжШшЧчЩщ]"""))
                    && char[i].toString().matches(Regex("""[ю]"""))
                ) {
                    newWord.append("у")
                    continue
                }
                newWord.append(char[i])
            }
            shiftUnderCont -= 1
            print(newWord)
            print((" "))
            output.write(newWord.toString())
            if (shiftUnderCont != 0) output.write(" ")
        }
        output.newLine()
    }
    output.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val map = mutableMapOf<String, Int>()
    for (line in File(inputName).readLines()) {
        if (!line.contains(Regex("""([A-Z]|[a-z]|[А-Я]|[а-я]|[Ёё])+"""))) continue
        val words = Regex("""([A-Z]|[a-z]|[А-Я]|[а-я]|[Ёё])+""").findAll(line).toList()
        for (word in words) {
            val key = word.value.toLowerCase()
            if (map.containsKey(key))
                map[key] = map[key]!! + 1
            else map[key] = 1
        }
    }

    return if (map.size <= 20) map
    else {
        val newMap = map.filter { it.value > 1 }
        newMap.toList().sortedByDescending { it.second }.toMap().toList().take(20).toMap()
    }
}


/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val output = File(outputName).bufferedWriter()
    if (dictionary.isNotEmpty()) {
        for (line in File(inputName).readLines()) {
            var shiftUnderCont = line.split(" ").size
            for (word in line.split(" ")) {
                val char = word.toList()
                val newWord = StringBuilder()
                if (char[0] == char[0].toUpperCase()) {
                    if (dictionary.containsKey(char[0].toLowerCase())) {
                        if ((dictionary[char[0].toLowerCase()] ?: error("")).length > 1) {
                            val newDic = StringBuilder()
                            val a = dictionary[char[0].toLowerCase()]
                            newDic.append(a!![0].toUpperCase())
                            for (i in 1 until a.length)
                                newDic.append(a[i].toLowerCase())
                            newWord.append(newDic.toString())
                        } else newWord.append((dictionary[char[0].toLowerCase()] ?: error("")).toUpperCase())
                    } else newWord.append(char[0])
                }
                if (newWord.isNotEmpty()) {
                    for (i in 1 until char.size) {
                        if (!dictionary.contains(char[i].toUpperCase()) && !dictionary.contains(char[i])) {
                            newWord.append(char[i])
                            continue
                        }
                        if (dictionary.contains(char[i].toUpperCase())) {
                            newWord.append(dictionary[char[i].toUpperCase()])
                            continue
                        }
                        if (dictionary.contains(char[i]))
                            newWord.append(dictionary[char[i]])
                    }
                } else {
                    for (i in 0 until char.size) {
                        if (!dictionary.contains(char[i].toUpperCase()) && !dictionary.contains(char[i]) || dictionary.isEmpty()) {
                            newWord.append(char[i])
                            continue
                        }
                        if (dictionary.contains(char[i].toUpperCase())) {
                            newWord.append((dictionary[char[i].toUpperCase()] ?: error("")).toLowerCase())
                            continue
                        }
                        if (dictionary.contains(char[i]))
                            newWord.append((dictionary[char[i]] ?: error("")).toLowerCase())

                    }
                }
                shiftUnderCont -= 1
                output.write(newWord.toString())
                if (shiftUnderCont != 0) output.write(" ")
            }
            output.newLine()
        }
    } else {
        val text = File(inputName).readText()
        output.write(text)
    }
    output.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val output = File(outputName).bufferedWriter()
    var max = 0
    val result = mutableListOf<String>()
    for (word in File(inputName).readLines()) {
        val list = word.toLowerCase().toList()
        val set = word.toLowerCase().toSet()
        if (word.length >= max && list.size == set.size) {
            result.add(word)
            max = word.length
        }
    }
    val str = mutableListOf<String>()
    for (word in result)
        if (word.length == max)
            str.add(word)
    output.write(str.joinToString())
    output.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val output = File(outputName).bufferedWriter()
    output.write("<html>")
    output.newLine()
    output.write("<body>")
    output.newLine()
    output.write("<p>")
    output.newLine()
    var i = 0
    var b = 0
    var s = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            output.write("</p>")
            output.newLine()
            output.write("<p>")
        }
        var shift = line.split(" ").size
        for (str in line.split(" ")) {
            if (!str.contains(Regex("""[*~]"""))) {
                shift -= 1
                output.write(str)
                if (shift != 0) output.write(" ")
                continue
            }
            var newStr = String()
            if (str.contains(Regex("""[~]"""))) {
                val d = crossedOut(str, s).first
                s = crossedOut(str, s).second
                val word = seconds(d, b).first
                b = seconds(d, b).second
                newStr = ones(word, i).first
                i = ones(word, i).second
                shift -= 1
                output.write(newStr)
                if (shift != 0) output.write(" ")
                continue
            }
            if (str.contains(Regex("""[~][^*]"""))) {
                newStr = crossedOut(str, s).first
                s = crossedOut(str, s).second
            }
            if (str.matches(Regex("""\*[^*]+"""))) {
                newStr = ones(str, i).first
                i = ones(str, i).second
                shift -= 1
                output.write(newStr)
                if (shift != 0) output.write(" ")
                continue
            }
            if (str.contains(Regex("""[*]+"""))) {
                val a = seconds(str, b).first
                b = seconds(str, b).second
                newStr = ones(a, i).first
                i = ones(a, i).second
                shift -= 1
                output.write(newStr)
                if (shift != 0) output.write(" ")
                continue
            }
            shift -= 1
            output.write(newStr)
            if (shift != 0) output.write(" ")
        }
        output.newLine()
    }
    output.write("</p>")
    output.newLine()
    output.write("</body>")
    output.newLine()
    output.write("</html>")
    output.close()
}


fun ones(str: String, i: Int): Pair<String, Int> {
    val newStr = StringBuilder()
    var n = i
    loop@ for (char in str) {
        if (char == '*')
            when (n) {
                0 -> {
                    newStr.append("<i>")
                    n++
                    continue@loop
                }
                1 -> {
                    newStr.append("</i>")
                    n--
                    continue@loop
                }
            }
        newStr.append(char)
    }
    return Pair(newStr.toString(), n)
}

fun seconds(str: String, b: Int): Pair<String, Int> {
    val newStr = StringBuilder()
    var n = b
    var c = 0
    while (c < str.length - 1) {
        if (str[c] == '*' && str[c + 1] == '*')
            when (n) {
                0 -> {
                    newStr.append("<b>")
                    n++
                    c += 2
                }
                1 -> {
                    newStr.append("</b>")
                    n--
                    c += 2
                }
            }
        if (c < str.length) newStr.append(str[c])
        else break
        c++
    }
    if (c < str.length) {
        newStr.append(str.last())
    }
    return Pair(newStr.toString(), n)
}

fun crossedOut(str: String, s: Int): Pair<String, Int> {
    val newStr = StringBuilder()
    var n = s
    var c = 0
    while (c < str.length - 1) {
        if (str[c] == '~' && str[c + 1] == '~')
            when (n) {
                0 -> {
                    newStr.append("<s>")
                    n++
                    c += 2
                }
                1 -> {
                    newStr.append("</s>")
                    n--
                    c += 2
                }
            }
        if (c < str.length) newStr.append(str[c])
        else break
        c++
    }
    if (c < str.length) newStr.append(str.last())
    return Pair(newStr.toString(), n)
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>
Или колбаса
</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>
Фрукты
<ol>
<li>Бананы</li>
<li>
Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    val output = File(outputName).bufferedWriter()
    var a = rhv
    var max = 0
    max = if (lhv.toString().length + rhv.toString().length == (lhv * rhv).toString().length)
        lhv.toString().length + rhv.toString().length + 1
    else lhv.toString().length + rhv.toString().length
    for (i in 1..max - lhv.toString().length)
        output.write(" ")
    output.write(lhv.toString())
    output.newLine()
    output.write("*")
    for (i in 1 until max - rhv.toString().length)
        output.write(" ")
    output.write(rhv.toString())
    output.newLine()
    for (i in 1..max)
        output.write("-")
    output.newLine()
    var sum = lhv * (a % 10)
    for (i in 1..max - sum.toString().length)
        output.write(" ")
    output.write(sum.toString())
    output.newLine()
    a /= 10
    var l = 2
    while (a != 0) {
        sum = lhv * (a % 10)
        a /= 10
        output.write("+")
        for (i in 1..max - l - sum.toString().length)
            output.write(" ")
        output.write(sum.toString())
        l++
        output.newLine()
    }
    for (i in 1..max)
        output.write("-")
    output.newLine()
    val result = lhv * rhv
    for (i in 1..max - result.toString().length)
        output.write(" ")
    output.write(result.toString())
    output.close()
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}

