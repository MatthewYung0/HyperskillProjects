package first_project

fun main() {
    val bubblegum: Int = 202
    val toffee: Int = 118
    val iceCream: Int = 2250
    val milkChocolate: Int = 1680
    val doughnut: Int = 1075
    val pancake: Int = 80

    println("Earned amount: ")
    println("Bubblegum: $$bubblegum")
    println("Toffee: $$toffee")
    println("Ice cream: $$iceCream")
    println("Milk chocolate: $$milkChocolate")
    println("Doughnut: $$doughnut")
    println("Pancake: $$pancake")
    println(
        "Income: $${
            bubblegum +
                    toffee +
                    iceCream +
                    milkChocolate +
                    doughnut +
                    pancake
        }"
    )
}