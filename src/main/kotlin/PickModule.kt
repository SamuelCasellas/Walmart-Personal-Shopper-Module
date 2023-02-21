import java.lang.Exception
import kotlin.io.*

class PickModule(private var ordInv: OrdersInventory) {

    private var userInput: String = ""
    fun runPickModule() {
        var menuListing: MutableList<String> = mutableListOf()
        while (true) {
            menuListing = generateMenu()
            displayMenu(menuListing)
            userInput = readln()
            if (userInput == menuListing.size.toString()) {
                println("Logging out...")
                break
            }
            var userInputInt: Int
            try {
                userInputInt = userInput.toInt()
            } catch (e: Exception) {
                println("\nTry again\n")
                continue
            }
            if (1 <= userInputInt && userInputInt <= menuListing.size - 1) {
                val chosenPick: PickWalks = PickWalks.valueOf(menuListing[userInput.toInt()-1])
                val orders = retrievePickWalksOrders(chosenPick)
                val ordersItems = ordInv.getAllItmesForOrderNums(orders.toSet())
                if (chosenPick == PickWalks.Chilled || chosenPick == PickWalks.Frozen) {
                    val pickWalkTimed = PickWalkTimed(chosenPick, ordersItems, orders)
                    pickWalkTimed.startWalk()
                } else {
                    val pickWalk = PickWalk(chosenPick, ordersItems, orders)
                    pickWalk.startWalk()
                }
            } else {
                println("\nTry again.\n")
            }
        }
    }

    private fun displayMenu(options: MutableList<String>) {
        println("\nGrocery Picker\n")
        if (options.size == 1) println("\nAll caught up on picks!\n")

        var i = 1
        for (option in options) {
            println("$i. $option")
            i++
        }
    }

    private fun generateMenu(): MutableList<String> {
        val pickList = listOf("Ambient", "General", "Chilled", "Frozen")
        var menuListing = mutableListOf<String>()

        for (pick in pickList) {
            val ordersWithTempBrand = ordInv.filterOrdersOnCriteria("tempBrand", PickWalks.valueOf(pick))
            if (ordersWithTempBrand.isNotEmpty()) {
                if (ordInv.filterOrdersOnCriteria("pickStatus", PickStatus.NOT_PICKED, ordersWithTempBrand).isNotEmpty()) {
                    menuListing.add(pick)
                }
            }
        }
        menuListing.add("Log out")
        return menuListing
    }

    private fun retrievePickWalksOrders(tempBrand: PickWalks): List<Int> {
        val orderNums: MutableList<Int> = mutableListOf()
        var toteLimit = if (tempBrand == PickWalks.Chilled) 6 else 8

        ordInv.filterOrdersOnCriteria("tempBrand", tempBrand).forEach { (orderNum, _) ->
            orderNums.add(orderNum)
            // Maximum number of totes is 6
            if (orderNums.size == toteLimit) return@forEach // break
        }
        return orderNums.toList()
    }

}