open class PickWalk(private val pickWalkType: PickWalks,
                    private val availableItems: MutableList<GroceryItem>,
                    private val orderNums: List<Int>) {


    private var firstWalkI = -1
    private var lastItemsI: Int = 0
    private var currentItem: GroceryItem? = null
    private var skippedItemsI = mutableListOf<Int>()
    private var currentSkippedI = 0
    private var onSkippedItems: Boolean = false
    private var totalNumPicked = 0

    private var totes = listOf<Tote>()
    private var thisWalksItems = listOf<GroceryItem>()

    init {
        val (t, i) = delegateTotesAndItems()
        totes = t
        thisWalksItems = i
    }

    fun startWalk() {
        startAction()

        lastItemsI = thisWalksItems.size - 1
        val totalNumItems = thisWalksItems.sumOf { it.getQuantity() - it.getNumPicked() }

        println("\nYou have begun a(n) $pickWalkType pick walk.\n")
        while (true) {
            // Reset skippedI for another go around
            if (currentSkippedI >= skippedItemsI.size && onSkippedItems) {
                if (!pickWalkComplete()) println("\nRecycling through skipped items...")
                currentSkippedI = 0
            }

            currentItem = nextItem()
            currentItem ?: break

            println("\nItem ${totalNumPicked}/${totalNumItems}")
            if (currentItem!!.getQuantity() > 1) {
                repeatedItem()
            } else {
                singleItem()
            }
            if (pickWalkComplete()) {
                println("Pick walk is complete!")
            }
        }
        endAction()
    }

    protected open fun startAction() {}
    protected open fun endAction() {}

    private fun singleItem() {
        val toteNum = totes.find { it.getOrderId() == currentItem!!.getOrderId() }!!.getToteNum()
        var userInput: String
        while (true) {
            println("Your next item is: ${currentItem!!.getName()} (x1)\n" +
                    " for tote #$toteNum\n")
            printOptions()
            userInput = readln()
            if (userInput == "1") {
                setPickWhileHandlingPartedOrder()
                placeItemInTote(currentItem!!)
                currentItem!!.setNumPicked(1)
                totalNumPicked += 1
                accountSkippedItem(currentSkippedI)
                break
            } else if (userInput == "2") {
                currentItem!!.setNotFound()
                totalNumPicked += 1
                accountSkippedItem(currentSkippedI)
                break
            } else if (userInput == "3")  {
                if (onSkippedItems) currentSkippedI++
                if (currentItem!!.getPickStatus() != PickStatus.SKIPPED) {
                    currentItem!!.setSkipped()
                    skippedItemsI.add(firstWalkI)
                }
                break
            } else if (userInput == "4") {
                showItemsBasedOnStatus(PickStatus.PICKED)
            } else if (userInput == "5") {
                showItemsBasedOnStatus(PickStatus.NOT_PICKED)
            } else if (userInput == "6") {
                showItemsBasedOnStatus(PickStatus.SKIPPED)
            } else {
                println("\nTry again.\n")
            }
        }
        checkSkippedItem()
    }

    private fun repeatedItem() {
        val toteNum = totes.find { it.getOrderId() == currentItem!!.getOrderId() }!!.getToteNum()

        var userInput: String
        while (true) {
            val itemsLeft = currentItem!!.getQuantity() - currentItem!!.getNumPicked()
            val itemsTotalNumPicked = currentItem!!.getNumPicked()

            println("Your next item is: ${currentItem!!.getName()} (x${itemsLeft})\n for tote #$toteNum\n")

            printOptions()
            userInput = readln()
            if (userInput == "1") {
                println("How many? ")
                userInput = readln()
                if (userInput.toInt() == itemsLeft) {
                    setPickWhileHandlingPartedOrder()
                    placeItemInTote(currentItem!!)
                    accountSkippedItem(currentSkippedI)
                    totalNumPicked += itemsLeft
                    break
                }
                else if (userInput.toInt() > itemsLeft) {
                    println("Please select $itemsLeft")
                } else if (userInput.toInt() < 1) {
                    println("Please select at least one")
                }
                else {
                    placeItemInTote(currentItem!!)
                    // Missing items still
                    val newTotal = itemsTotalNumPicked + userInput.toInt()
                    totalNumPicked += userInput.toInt()
                    currentItem!!.setNumPicked(newTotal)
                    val numLeft = currentItem!!.getQuantity() - newTotal
                    println("$numLeft is left to pick.")
                }
            } else if (userInput == "2") {
                currentItem!!.setNotFound()
                totalNumPicked += itemsLeft
                accountSkippedItem(currentSkippedI)
                break
            } else if (userInput == "3")  {
                if (onSkippedItems) currentSkippedI++
                if (currentItem!!.getPickStatus() != PickStatus.SKIPPED) {
                    currentItem!!.setSkipped()
                    skippedItemsI.add(firstWalkI)
                }
                break
            } else if (userInput == "4") {
            showItemsBasedOnStatus(PickStatus.PICKED)
            } else if (userInput == "5") {
                showItemsBasedOnStatus(PickStatus.NOT_PICKED)
            } else if (userInput == "6") {
                showItemsBasedOnStatus(PickStatus.SKIPPED)
            }
            else {
                println("\nTry again.\n")
            }
        }
        checkSkippedItem()
    }

    private fun setPickWhileHandlingPartedOrder() {
        if (currentItem!!.isPartedPick()) {
            val numPicks = currentItem!!.getNumPicked()
            currentItem!!.finishPick()
            currentItem!!.setNumPicked(numPicks)
            if (currentItem!!.getNumPicked() == currentItem!!.getQuantity()) {
                currentItem!!.setPicked()
            }
        } else {
            currentItem!!.setPicked()
        }
    }

    private fun checkSkippedItem () {
        if (firstWalkI == lastItemsI && skippedItemsI.isNotEmpty() && currentSkippedI != skippedItemsI.size) {
            onSkippedItems = true
            println("\nUpcoming skipped item...")
        }
    }

    private fun accountSkippedItem(iToRemove: Int) {
        if (onSkippedItems) skippedItemsI.removeAt(iToRemove)
    }

    private fun printOptions() {
        println("1. Found Item.")
        println("2. Item not found.")
        println("3. Skip item.")
        println("4. Show Picked items")
        println("5. Show Unpicked items")
        println("6. Show Skipped items")
    }

    private fun showItemsBasedOnStatus(status: PickStatus) {
        var hasMatch = false
        println()
        var i = 1
        for (item in thisWalksItems) {
            if (item.getPickStatus() == status) {
                println("$i. $item")
                i++
                hasMatch = true
            }
        }

        if (!hasMatch) println("No items found.")
        println()
    }

    private fun delegateTotesAndItems(): Pair<List<Tote>, List<GroceryItem>> {
        val totes = mutableListOf<Tote>()
        val items = mutableListOf<GroceryItem>()
        var toteNum = 1
        // 1 <= orderNums <= 6
        for (orderNum in orderNums) {
            val newTote = Tote(orderNum, toteNum)
            val orderItems = filterItems(orderNum)
            tote@ for (item in orderItems) {
                if (item.getPickStatus() != PickStatus.NOT_PICKED) continue

                val numLeft = item.getQuantity() - item.getNumPicked()
            // print(numLeft);println(); print(item.getQuantity());println(); print(item.getNumPicked())
                for (n in 1..numLeft) {
                    // Stop delegating items if the tote would otherwise overflow
                    if (!newTote.placeItem(item)) {
                        if (n == 1) break@tote
                        item.setNumToPickInSession(n-1)
                        items.add(item)
                        break@tote
                    } else if (n == numLeft) {
                        items.add(item)
                    }
                }
            }
            totes.add(newTote)
            toteNum++
        }
        return Pair(totes.toList(), items.toList())
    }

    private fun filterItems(orderId: Int): List<GroceryItem> {
        return availableItems.filter { it.getOrderId() == orderId }
    }

    private fun nextItem(): GroceryItem? {
        return if (pickWalkComplete()) {
            null
        }
        else if (firstWalkI != lastItemsI) {
            firstWalkI++
            thisWalksItems[firstWalkI]
        } else {
            // Unlike thisWalksItems, skippedItemsI is mutable
            // and removing items will make updating the index
            // unnecessary (items will shift down).
            thisWalksItems[skippedItemsI[currentSkippedI]]
        }
    }

    private fun pickWalkComplete(): Boolean {
        return ((firstWalkI == this.lastItemsI) && (skippedItemsI.isEmpty()))
    }

    private fun findTote(orderNum: Int): Tote {
        val tote = totes.find { it.getOrderId() == orderNum }
        tote ?: throw Error("Order number $orderNum not found in totes!")
        return tote
    }
    private fun placeItemInTote(gItem: GroceryItem) {
        val tote: Tote = findTote(gItem.getOrderId())
        var userInput: String
        while (true) {
            println("Item belongs in tote #${tote.getToteNum()} (tote Id ${tote.getToteId()}).")
            userInput = readln()
            if (userInput != tote.getToteNum().toString()) {
                println("Not the right tote!")
            } else {
                break
            }
        }
    }
}