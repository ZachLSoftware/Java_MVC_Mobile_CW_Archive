package uk.ac.le.co2103.part2;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ShoppingListAdapter extends ListAdapter<ShoppingList, ShoppingListViewHolder> {

    private OnListClickListener AddOnListClickListener;

    public ShoppingListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingList> diffCallback, OnListClickListener onListClickListener) {
        super(diffCallback);
        this.AddOnListClickListener = onListClickListener;
    }

    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ShoppingListViewHolder.create(parent, AddOnListClickListener);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        ShoppingList current = getItem(position);
        holder.bind(current.getName(), current.getImage());
    }

    static class ShoppingListDiff extends DiffUtil.ItemCallback<ShoppingList> {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}
