package Ling270FinalProj.src.main.kotlin

fun makeVowelsToBinaryMap(inputFileName: String): Map<String, String> {
  val lines = object {}.javaClass.getResourceAsStream("/" + inputFileName)?.bufferedReader()?.readLines()

  if (lines == null) {
    throw IllegalArgumentException("Error Processing File: $inputFileName")
  }

  val vowelsToBinary = mutableMapOf<String, String>()

  for (line in lines) {
    val vowelNumber = line.split(" = ")[0]
    val vowelNumberInBinary = Integer.toBinaryString(vowelNumber.toInt()).padStart(4, '0')

    val vowelSymbol = line.split(" = ")[1]

    if (vowelSymbol != "null") {
      vowelsToBinary.put(vowelSymbol, vowelNumberInBinary)
    }
  }

  return vowelsToBinary
}

fun makeVowelsToScriptMap(vowelsAsBinary: Map<String, String>): Map<String, String> {
  val vowelsToScript = mutableMapOf<String, String>()
  for (binary in vowelsAsBinary) {
    vowelsToScript.put(binary.key, encodeVowelOnTopOfConsonant("oooo", binary.value))
  }
  return vowelsToScript
}

fun makeConsonantsToScriptMap(inputFileName: String): Map<String, String> {
  val lines = object {}.javaClass.getResourceAsStream("/" + inputFileName)?.bufferedReader()?.readLines()

  if (lines == null) {
    throw IllegalArgumentException("Error Processing File: $inputFileName")
  }

  val consonantsToScript = mutableMapOf<String, String>()

  for (line in lines) {
    if (line.split(" = ")[1] != "null") {
      val consonantNumber = line.split(" = ")[0].toInt()
      val consonantNumberInBinary = Integer.toBinaryString(consonantNumber)
      val consonantInScript = consonantNumberInBinary.replace('0', 'o').replace('1', 'n').padStart(4, 'o')

      val consonantSymbols = line.split(" = ")[1].replace("(", "").replace(")", "").split(", ")
      val voicelessConsonantSymbol =  consonantSymbols[0]
      val voicedConsonantSymbol = consonantSymbols[1]

      if (voicelessConsonantSymbol != "null") {
        consonantsToScript.put(voicelessConsonantSymbol, consonantInScript)
      }
      if (voicedConsonantSymbol != "null") {
        consonantsToScript.put(voicedConsonantSymbol, consonantInScript.uppercase())
      }
    }
  }
  
  return consonantsToScript
}

fun encodeVowelOnTopOfConsonant(consonant: String, vowel: String): String {
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
      consonantsVowelPairsToScipt.put(consonant + vowel, encodeVowelOnTopOfConsonant(consonants[consonant]!!, vowels[vowel]!!))
    }
  } 
  return consonantsVowelPairsToScipt
}

fun ipaToScript(input: String): String {
  val vowelsAsBinary = makeVowelsToBinaryMap("vowels.txt")
  val vowels = makeVowelsToScriptMap(vowelsAsBinary)
  val consonants = makeConsonantsToScriptMap("consonants.txt")
  val consonantVowelPairs = addConsonantVowelPairsToConsonantMap(consonants, vowelsAsBinary)
  val syllabary = consonantVowelPairs + vowels

  var translation = mutableListOf<String>()
  var i = 0
  var letter: String
  while (i < input.length) {
    letter = input[i].toString()
    if (letter !in syllabary.keys) {
      translation += letter
    } else {

      // is this a dumb way to do this, yes
      // am I willing to spend the time to think of a better way rn, no
      if (i < input.length - 3 && input.substring(i, i + 4) in syllabary.keys) {
        translation += syllabary[input.substring(i, i + 4)]!!
      } else if (i < input.length - 2 && input.substring(i, i+3) in syllabary.keys) {
        translation += syllabary[input.substring(i, i+3)]!!
        i += 2
      } else if (i < input.length - 1 && input.substring(i, i+2) in syllabary.keys) {
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

