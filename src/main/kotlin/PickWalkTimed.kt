import java.util.*

class PickWalkTimed(
    private val pickWalkType: PickWalks,
    private val items: MutableList<GroceryItem>,
    private val orderNums: List<Int>)
    : PickWalk(pickWalkType, items, orderNums) {

    private val timer: Timer = Timer()
    private val timeGiven: Int
        get() {
            return if (this.pickWalkType == PickWalks.Chilled) {
                40
            } else {
                30
            }
        }

    override fun startAction() {
        this.startTimer()
    }
    override fun endAction() {
        this.timer.cancel()
    }

    private fun startTimer() {
        val task = object : TimerTask() {
            override fun run() {
                println("The groceries are melting! " +
                        "Please return the cart.")
            }
        }
        val totalSeconds: Long = (timeGiven * 60 * 1000).toLong()
        timer.schedule(task, totalSeconds)
    }
}