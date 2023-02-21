class Order(items: MutableList<GroceryItem>, orderId: Int) {
    private var items = items
    private var orderId = orderId

    fun getItems(): MutableList<GroceryItem> { return this.items }
    fun getOrderId(): Int { return this.orderId }
}