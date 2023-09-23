package chap03

fun main(){ // 스택 프레임

    val num1 = 10 // 임시 변수 혹은 지역변수
    val num2 = 3
    val result: Int

    val r : Int = sum(1 , 2) // 두번째 스텍 프레임
    println(r)
    printSum(10 , 3)
    normalVarargs(1 ,2 , 3 , 4)

    // 람다

    val multi: (Int , Int) -> Int  =  {x : Int , y : Int -> x*y}
    result = multi(10 , 20) // 람다식이 할당 된 변수는 함수처럼 사용가능
    println(result)
}


//fun sum(a: Int , b: Int): Int{
//    return a + b
//}

//fun sum(a : Int , b: Int): Int = a+b;

fun sum(a : Int , b: Int) = a + b
fun max(a : Int , b : Int) = if ( a > b) a else b

// 반환값이 없는 경우 : Unit

fun printSum(a: Int , b: Int): Unit{
    println("sum of $a and $b is ${a + b}")
}

// default값을 미리 지정할 수 있다.
fun add(name : String , email : String = "default") {
    val output = "${name}님의 이메일은 ${email}입니다."
    println(output)
}

// 가변 인자( 다양한 인자의 개수를 전달받음 )
fun normalVarargs(vararg counts : Int){
    for ( num in counts){
        print("$num")
    }
    print("\n")
}

// 고차 함수
fun highFunc(sum : (Int , Int) -> Int , a: Int , b: Int) : Int = sum(a , b)

