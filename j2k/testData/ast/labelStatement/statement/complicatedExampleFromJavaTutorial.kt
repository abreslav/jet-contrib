@test {
var i : Int = 0
while ((i <= max))
{
{
var n : Int = substring.length()
var j : Int = i
var k : Int = 0
while (((n--) != 0))
{
if ((searchMe.charAt((j++)) != substring.charAt((k++))))
{
continue@test
}
}
foundIt = true
break@test
}
{
(i++)
}
}
}
System.`out`?.println((if (foundIt)
"Found it"
else
"Didn't find it"))