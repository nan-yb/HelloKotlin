package observer

import EventListener

class EventPrinter() {

    fun start(){
        val counter = Counter(object: EventListener {
            override fun onEvent(count: Int) {
                print("${count}-");
            }
        })


        counter.count();

    }
}