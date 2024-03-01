package Ling270FinalProj.src.main.kotlin

import java.nio.file.Paths

fun main() {
  println(Paths.get("").toAbsolutePath().toString()) 
  println(ipaToScript("wi bɑt nu pɪkəlz ænd ðeɪ ɑɚ nɑt dəlɪʃəs"))
}