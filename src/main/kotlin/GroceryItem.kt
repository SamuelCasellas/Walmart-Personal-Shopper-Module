 class GroceryItem(
     private val name: String, private val quantity: Int,
     private val sizeUnit: Int, private val tempBrand: PickWalks,
     private val orderId: Int
 ) {

      private var numPickedTotal = 0
      private var numPickedSession = 0
      private var numToPickInSession: Int? = null
      private var pickStatus: PickStatus = PickStatus.NOT_PICKED

      override fun toString(): String {
          return "Name: $name, Quantity: ${this.getQuantity()}"
     }

      fun getName(): String { return this.name }
      fun getOrderId(): Int { return this.orderId }
      fun getSizeUnit(): Int { return this.sizeUnit }

      fun getPickStatus(): PickStatus { return this.pickStatus }
      fun setPicked() { this.pickStatus = PickStatus.PICKED }
      fun setNotFound() { this.pickStatus = PickStatus.NOT_FOUND }

      fun setSkipped() { this.pickStatus = PickStatus.SKIPPED }

     fun getQuantity(): Int {
         return if (this.isPartedPick())
             this.numToPickInSession!!
         else
             this.quantity
     }

     fun getNumPicked(): Int {
          return if (this.isPartedPick())
              this.numPickedSession
          else
              this.numPickedTotal
      }
      fun setNumPicked(num: Int) {
          if (this.isPartedPick())
              this.numPickedSession = num
          else
              this.numPickedTotal = num
      }

      fun setNumToPickInSession(num: Int) { this.numToPickInSession = num }

      fun isPartedPick(): Boolean { return this.numToPickInSession != null }

       fun finishPick() {
          this.numToPickInSession = null
          this.numPickedSession = 0
      }
}