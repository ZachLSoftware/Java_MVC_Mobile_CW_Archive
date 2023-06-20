package uk.ac.le.co2103.part2;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Products",
        foreignKeys = {@ForeignKey(
                entity=ShoppingList.class,
                parentColumns="listId",
                childColumns="fk_listId",
                onDelete=CASCADE)}, indices = {@Index("fk_listId"),
})
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long productId;
    public long fk_listId;

    @NonNull
    @ColumnInfo(name="name")
    public String name;
    public int quantity;
    public String unit;


    public long getProductId(){return productId;}

    public void setListId(long id){this.fk_listId=id;}

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean setQuantity(int quantity) {
        if (quantity <=0){
            return false;
        }
        else {
            this.quantity = quantity;
            return true;
        }
    }

    public void setUnit(String unit) {
        List<String> units = new ArrayList<String>(){{add("Unit"); add("Kg"); add("Litre");}};
        if (units.contains(unit)){
            this.unit = unit;
        }
    }
}
