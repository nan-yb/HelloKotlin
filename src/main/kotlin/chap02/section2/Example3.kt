package chap02.section2


fun main(){
    var str1 : String = "Hello"
    var str2 = "World"
    var str3 = "Hello"

    println("str1 === str2: ${str1 == str2}")
    println("str1 === str3: ${str1 == str3}")

    var a = 1
    var s1 = "a is ${a}"
    var s2 = "a = ${a+2}"

    println(s1)
    println("str1 : \"$str1\" , str2: \"${str2}\"")

    val special2 = "${'"'}${'$'}9.99${'"'}"
    println(special2)


    var string1 : String = "Hello kotlin"
//    string1 = null  // 오류! null을 허용하지않음
    println("str1: $str1")

    var string2 : String? = "Hello Kotlin"
    string2 = null
    println("string2 : ${string2}")
    println("${string2?.length}")
    println("string2 : $string2 length: ${string2?.length ?: -1}")

    val i : Int  = 1
//    val d : Double  = i // 자료형 불일치 오류 발생
    val d : Double  = i.toDouble();
    var result = 1L + 3

    val z : Int = 128
    var y : Int = 128

    // Number
    var n1 : Number = 128
    var n2 : Number = 128.134
    var n3 : Number = 128L

    if ( n1 is Int ){
        print(n1)
    }else if (n2 is Double){
        print(n2)
    }else if (n3 is Long){
        print(n3)
    }

    // as 에 의한 스마트 캐스트 , Any = 슈퍼 클래스( 무엇이드 ㄴ될 수 있기 때문에 언제든 필요한 자료형으로 자동 변환 가능)
    val x : Any
    x = "Hello"
    if( x is String){
        println(x.length)
    }

    checkArg(n1)
    checkArg(x)
}

fun checkArg(x: Any){
    if (x is String) {
        println("x is String : $x")
    }

    if (x is Int){
        println("x is Int: $x")
    }
}