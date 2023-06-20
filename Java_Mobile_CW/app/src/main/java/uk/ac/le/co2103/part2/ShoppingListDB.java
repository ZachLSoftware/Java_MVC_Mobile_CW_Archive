package uk.ac.le.co2103.part2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ShoppingList.class, Product.class}, version = 1, exportSchema = false)
public abstract class ShoppingListDB extends RoomDatabase {

    public abstract ShoppingListDao listDao();

    private static volatile ShoppingListDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService executorService= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ShoppingListDB getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (ShoppingListDB.class){
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShoppingListDB.class, "shoppinglistdb").build();
                }
            }
        }
        return INSTANCE;
    }

}
