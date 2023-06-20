package uk.ac.le.co2103.part2;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

//Relational class
public class ShoppingListWithProducts implements Serializable {
    @Embedded public ShoppingList shoppingList;
    @Relation(
            parentColumn = "listId",
            entityColumn = "fk_listId"
    )
    public List<Product> products;

}
