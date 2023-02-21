Insights:
* Vars must be initialized

We will keep a track of orders that 
each have their own list of groceryItems.

# Class CRCs:<br>
<u>Class: PickerModule</u><br><br>
    Responsibilities: <br>
- Display a menu that allows you to
    select certain commodities to be picked (Only display the walks that have at least one item from that temp brand.)
- Initiate the selected pick walk, delegating at most 6 orders.
  (each tote represents parts of an order).

Collaborators:
- PickWalk
- OrderInventory

~~<br><u>Class: PickWalk</u><br><br>~~
Responsibilities:
- ~~Contain the current list of items being picked~~<br>
- ~~Create the totes based on what can be filled.~~<br>
- ~~When going through a pick walk, mark each item as picked
once completed.~~<br>
Collaborators: <br>

~~<br><u>Class: TimedPickWalk</u><br><br>~~
Responsibilities: <br>
- ~~Child class of PickWalk, for timing the chilled~~ 

~~<br><u>Class: Tote</u><br><br>~~
Responsibilities: <br>
- ~~Hold a set of items all relating to a single order~~
- ~~Determine when the tote is overflowing~~
- ~~Have an id for the tote~~

~~<br><u>Class: OrdersInventory</u><br><br>~~
Responsibilities: <br>
- ~~Contain a list of orders, each with the associated items.~~
- ~~Filter orders based on if they have the temp brand requested.~~
- ~~Filter items that already have been completed.~~

Collaborators:
- ~~Order~~<br>

~~<br><u>Class: Order</u><br><br>~~
Responsibilities: <br>
- ~~Contain a list of associated items.~~

Collaborators:
- ~~GroceryItem<br>~~


~~<br><u>Class: GroceryItem</u><br><br>~~
Responsibilities: <br>
- ~~Represent a single grocery item.~~ 
- ~~Hold name of product, quantity, unit size, temp brand, picked status, and orderId.<br>~~

Collaborators: 
- None
