//import io.reactivex.rxjava3.kotlin.subscribeBy
//import io.reactivex.rxjava3.kotlin.toObservable

fun main() {

    val list = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")

    println(list)
//    list.toObservable() // extension function for Iterables
//        .filter { it.length >= 5 }
//        .subscribeBy(  // named arguments for lambda Subscribers
//            onNext = { println(it) },
//            onError =  { it.printStackTrace() },
//            onComplete = { println("Done!") }
//        )

}