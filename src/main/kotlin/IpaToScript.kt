package Ling270FinalProj.src.main.kotlin

fun makeVowelsToScriptBinaryMap(inputFileName: String): Map<String, String> {
  val lines = object {}.javaClass.getResourceAsStream("/" + inputFileName)?.bufferedReader()?.readLines()

  if (lines == null) {
    throw IllegalArgumentException("Error Processing File: $inputFileName")
  }

  val vowelsToScriptBinaryMap = mutableMapOf<String, String>()

  for (line in 1 until lines.size) {
    val intVowel = lines[line].split(" = ")
    vowelsToScriptBinaryMap.put(intVowel[1], Integer.toBinaryString(intVowel[0].toInt()).padStart(4, '0'))
  }

  return vowelsToScriptBinaryMap
}

fun makeConsonantsToScriptBinaryMap(inputFileName: String): Map<String, String> {
  val lines = object {}.javaClass.getResourceAsStream("/" + inputFileName)?.bufferedReader()?.readLines()

  if (lines == null) {
    throw IllegalArgumentException("Error Processing File: $inputFileName")
  }

  val consonantsToScriptBinaryMap = mutableMapOf<String, String>()

  for (line in lines) {
    val intConsonantPair = line.split(" = ")
    val int = intConsonantPair[0].toInt()
    val voiceless = intConsonantPair[1].split(", ")[0].replace("(", "")
    val voiced = intConsonantPair[1].split(", ")[1].replace(")", "")
    if (voiceless != "null") {
      consonantsToScriptBinaryMap.put(voiceless, Integer.toBinaryString(int).replace('0', 'o').replace('1', 'n').padStart(4, 'o'))
    }
    if (voiced != "null") {
      consonantsToScriptBinaryMap.put(voiced, Integer.toBinaryString(int).replace('0', 'o').replace('1', 'n').padStart(4, 'o').uppercase())
    }
  }
  
  return consonantsToScriptBinaryMap
}

fun consonantToConsonantVowel(consonant: String, vowel: String): String {
  var consonantVowel = ""
  for (i in consonant.indices) {
    when {
      consonant[i] == 'o' && vowel[i] == '0' -> consonantVowel += 'o'
      consonant[i] == 'o' && vowel[i] == '1' -> consonantVowel += 'u'
      consonant[i] == 'O' && vowel[i] == '0' -> consonantVowel += 'O'
      consonant[i] == 'O' && vowel[i] == '1' -> consonantVowel += 'U'
      consonant[i] == 'n' && vowel[i] == '0' -> consonantVowel += 'n'
      consonant[i] == 'n' && vowel[i] == '1' -> consonantVowel += 'm'
      consonant[i] == 'N' && vowel[i] == '0' -> consonantVowel += 'N'
      consonant[i] == 'N' && vowel[i] == '1' -> consonantVowel += 'M'
    }
  }
  return consonantVowel
}

fun addConsonantVowelPairsToConsonantMap(consonants: Map<String, String>, vowels: Map<String, String>): Map<String, String> {
  val consonantsVowelPairsToScipt = consonants.toMutableMap()
  for (consonant in consonants.keys) {
    for (vowel in vowels.keys) {
      consonantsVowelPairsToScipt.put(consonant + vowel, consonantToConsonantVowel(consonants[consonant]!!, vowels[vowel]!!))
    }
  } 
  return consonantsVowelPairsToScipt
}

fun ipaToScript(input: String): String {
  val vowels = makeVowelsToScriptBinaryMap("vowels.txt")
  val consonants = makeConsonantsToScriptBinaryMap("consonants.txt")
  val consonantVowelPairs = addConsonantVowelPairsToConsonantMap(consonants, vowels)
  val syllabary = consonantVowelPairs + vowels

  var translation = mutableListOf<String>()
  var i = 0
  var letter: String
  while (i < input.length) {
    letter = input[i].toString()
    if (letter !in syllabary.keys) {
      translation += letter
    } else {
      if (i < input.length - 1 && input.substring(i, i+2) in syllabary.keys) {
        translation += syllabary[input.substring(i, i+2)]!!
        i++
      } else {
        translation += syllabary[letter]!!
      }
    }
    i++
  }

  println(syllabary)
  return translation.reversed().joinToString(" ")
}

