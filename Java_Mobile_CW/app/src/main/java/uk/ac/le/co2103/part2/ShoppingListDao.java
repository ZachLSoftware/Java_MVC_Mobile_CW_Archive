package uk.ac.le.co2103.part2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListDao {

    //Insert new list
    @Transaction
    @Insert
    void insertList (ShoppingList shoppingList);

    //Insert new product
    @Insert
    void insertProduct(Product product);

    //Get all shopping lists with products
    @Transaction
    @Query("SELECT * FROM shoppingLists")
    LiveData<List<ShoppingListWithProducts>> getShoppingListWithProducts();

    //delete a single list
    @Delete
    void deleteList(ShoppingList list);

    //delete a single product
    @Delete
    void deleteProduct(Product product);

    //update a product
    @Update
    void updateProduct(Product product);
}
