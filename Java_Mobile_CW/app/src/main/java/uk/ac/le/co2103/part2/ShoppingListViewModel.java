package uk.ac.le.co2103.part2;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {

    private ShoppingListRepository repo;

    private final LiveData<List<ShoppingListWithProducts>> allLists;

    public ShoppingListViewModel(Application application){
        super(application);
        repo = new ShoppingListRepository(application);
        allLists= repo.getAllLists();
    }

    LiveData<List<ShoppingListWithProducts>> getAllLists(){return allLists;}


    public boolean insertList(ShoppingList shoppingList){return repo.insertList(shoppingList);}

    public boolean insertProduct(Product product, long id){return repo.insertProduct(product, id);}

    public void deleteList(ShoppingList list){repo.deleteList(list);}
    public void deleteProduct(Product product){repo.deleteProduct(product);}
    public void updateProduct(Product product){repo.updateProduct(product);}
}
