package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListRepository {
    private ShoppingListDao shoppingListDao;
    private LiveData<List<ShoppingListWithProducts>> allLists;

    ShoppingListRepository(Application application){
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        shoppingListDao = db.listDao();
        allLists = shoppingListDao.getShoppingListWithProducts();

    }

    LiveData<List<ShoppingListWithProducts>> getAllLists(){return allLists;}


    boolean insertList(ShoppingList list) {

        //Checks if the list name already exists
        for (ShoppingListWithProducts l : allLists.getValue()) {
            if (l.shoppingList.getName().equalsIgnoreCase(list.getName())) {
                return false;
            }
        }
        ShoppingListDB.executorService.execute(() -> {
            shoppingListDao.insertList(list);
        });
        return true;
    }

    boolean insertProduct(Product product, long listId){
        long id=-1;
        ShoppingListWithProducts sl=null;

        //Gets the shoppingListWithProducts that matches
        while(id!=listId){
            for(ShoppingListWithProducts s : allLists.getValue()){
                if(s.shoppingList.getListId()==listId){
                    sl=s;
                    id=s.shoppingList.getListId();
                }
                else{continue;}
            }
        }

        //Checks if the product name already exists inside the product
        for(Product p : sl.products){
            if(p.getName().equalsIgnoreCase(product.getName())){
                return false;
            }
        }
        ShoppingListDB.executorService.execute(() -> {

            shoppingListDao.insertProduct(product);
        });
        return true;
    }

    void deleteList(ShoppingList list){
        ShoppingListDB.executorService.execute(() ->{
            shoppingListDao.deleteList(list);
        });
    }

    void deleteProduct(Product product){
        ShoppingListDB.executorService.execute(() ->{
            shoppingListDao.deleteProduct(product);
        });
    }

    void updateProduct(Product product){
        ShoppingListDB.executorService.execute(() ->{
            shoppingListDao.updateProduct(product);
        });
    }


}
