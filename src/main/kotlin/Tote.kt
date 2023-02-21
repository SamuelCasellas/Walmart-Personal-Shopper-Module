class Tote(private val orderId: Int, private val toteNum: Int) {

    private var toteIdCount = 0

    private val toteId: String = orderId.toString() + "-" + toteIdCount++.toString()

    private  val maximumSpaceValue:Int = 20
    private var currentCapacity:Int = 0
    private var items = mutableListOf<GroceryItem>()
    fun getToteId(): String { return this.toteId }
    fun getToteNum(): Int { return this.toteNum }

    fun getOrderId(): Int { return this.orderId }

    fun getNumItems(): Int { return this.items.size }


    fun placeItem(gItem: GroceryItem): Boolean {
        return if (this.checkOverflowingTote(gItem)) {
           // println("Cannot add. Overflowing tote!")
            false
        } else {
            items.add(gItem)
            this.currentCapacity += gItem.getSizeUnit()
          //  println("Item ${gItem.getName()} has been added.")
          //  println("Tote is at ${this.currentCapacity/this.maximumSpaceValue}% capacity")
            true
        }
    }


    private fun checkOverflowingTote(gItem: GroceryItem): Boolean {
        return this.currentCapacity + gItem.getSizeUnit() > this.maximumSpaceValue
    }

}