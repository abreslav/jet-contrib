package js;

import js.annotations.library
import js.annotations.library
import js.annotations.native

library("println")
fun println() {}
library("println")
fun println(s : Any?) {}
library("print")
fun print(s : Any?) {}
library("parseInt")
fun parseInt(s : String) : Int = 0
library
open class Exception() {}
library
class NumberFormatException() : Exception() {}

native
fun setTimeout(callback : ()-> Unit) {}

native
fun setInterval(callback : ()-> Unit, ms : Int) {}
native
fun setInterval(callback : ()-> Unit) {}