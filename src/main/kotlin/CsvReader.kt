import java.io.*
class CsvReader {
    private val fileName: String = "./src/main/kotlin/ORDERS.csv"

    fun CreateOrderInventory(): MutableMap<Int, Order> {

        val orders = mutableMapOf<Int, Order>()

        BufferedReader(FileReader(fileName)).use { reader ->
            // Read and skip the first line (header)
            reader.readLine()

            // Read the remaining lines
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val values = line!!.split(",")
                val orderNumber = values[0].toInt()
                val tempBrand = values[1]
                val quantity = values[2].toInt()
                val product = values[3]
                val unitSize = values[4].toInt()
                val groceryItem = GroceryItem(product, quantity, unitSize, PickWalks.valueOf(tempBrand), orderNumber)

                val order = orders.getOrPut(orderNumber) { Order(mutableListOf(), orderNumber) }
                order.getItems().add(groceryItem)
            }
        }
        return orders
    }
}