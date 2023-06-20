package uk.ac.le.co2103.part2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "shoppingLists")
public class ShoppingList implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long listId;

    @NonNull
    @ColumnInfo(name="name")
    public String name;

    public String image;

    public ShoppingList(){
        this.image="default";
    }

    public long getListId() {
        return listId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }


    public void setName(String name) {
        this.name=name;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
