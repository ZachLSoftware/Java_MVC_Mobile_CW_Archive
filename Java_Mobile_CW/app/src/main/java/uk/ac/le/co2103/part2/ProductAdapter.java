package uk.ac.le.co2103.part2;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class ProductAdapter extends ListAdapter<Product, ProductViewHolder> {

    private OnListClickListener AddOnListClickListener;

    public ProductAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback, OnListClickListener onListClickListener) {
        super(diffCallback);
        this.AddOnListClickListener = onListClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return ProductViewHolder.create(viewGroup, AddOnListClickListener);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product current = getItem(position);
        holder.bind(current.getName(), current.getUnit(), current.getQuantity());
    }

    static class ProductDiff extends DiffUtil.ItemCallback<Product> {

        //Compares update
        @Override
        public boolean areItemsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct == newProduct;
        }

        //Compares the name of products, avoids duplicates
        @Override
        public boolean areContentsTheSame(@NonNull Product oldProduct, @NonNull Product newProduct) {
            return oldProduct.getName().equals(newProduct.getName());
        }
    }
}
