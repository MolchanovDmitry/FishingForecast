package dmitry.molchanov.data

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}