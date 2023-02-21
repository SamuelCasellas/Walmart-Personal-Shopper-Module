fun main(args: Array<String>) {
    /**
     * Similar to Walmart's picking module for personal shoppers,
     * Here is a program that selects from a list of items to pick from multiple orders,
     * each "walk" for a specific category like "general", "chilled", "frozen", and
     * "ambient".
     *
     * - I will use enumerations for the different categories that I mentioned previously.
     * - I will also incorporate lambda functions that filter the items based on this category.
     * - The walk's items will be contained within a list.
     * - Each item will be in their distinctive child classes derived from the general grocery item class.
     * - There will be functions that return a string for the type of pick walk as well as the list of items (double).
     * - Once these items have been picked, they will be removed from the "pickList" member variable in the respective
     *   grocery category object.
     */

    val ordersInventory = OrdersInventory(CsvReader().CreateOrderInventory())
    val pickModule = PickModule(ordersInventory)

    pickModule.runPickModule()
}