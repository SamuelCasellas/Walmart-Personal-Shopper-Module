class OrdersInventory(
    // order number to order (MutableList<GroceryItem>)
    private var orders: MutableMap<Int, Order>
) {

    // Can filter map to contain orders that still have unpicked items
    // as well as those that contain items of a certain temp brand
    fun filterOrdersOnCriteria(key: String, criteria: Any,
                               orders: MutableMap<Int, Order> =this.orders): MutableMap<Int, Order> {
        val klass = GroceryItem::class.java
        val property = klass.getDeclaredField(key)
        property.isAccessible = true

        val filteredOrders = mutableMapOf<Int, Order>()
        for ((orderId, order) in orders) {
            val items = order.getItems().filter { item ->
                property.get(item) == criteria
            }
            if (items.isNotEmpty()) {
                filteredOrders[orderId] = Order(items.toMutableList(), orderId)
            }
        }
        return filteredOrders
    }

    fun getAllItmesForOrderNums(orderNums: Set<Int>): MutableList<GroceryItem> {
        val matchingGroceryItems = mutableListOf<GroceryItem>()
        for ((_, order) in orders) {
            for (groceryItem in order.getItems()) {
                if (groceryItem.getOrderId() in orderNums) {
                    matchingGroceryItems.add(groceryItem)
                }
            }
        }
        return matchingGroceryItems
    }

    fun addOrder(order: Order) {
        orders[order.getOrderId()] = order
    }
    fun removeOrder(order: Order) {
        orders.remove(order.getOrderId())
    }
}