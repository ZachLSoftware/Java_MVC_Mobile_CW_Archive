package uk.ac.le.co2103.part2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final TextView productName;
    private final TextView productQuantity;
    private final TextView productUnit;
    private OnListClickListener onListClickListener;

    private ProductViewHolder(View itemView, OnListClickListener onListClickListener) {
        super(itemView);
        productName = itemView.findViewById(R.id.productName);
        productQuantity = itemView.findViewById(R.id.productQuantity);
        productUnit = itemView.findViewById(R.id.productUnit);
        this.onListClickListener = onListClickListener;

        itemView.setOnClickListener(this);
    }

    public void bind(String name, String unit, int quantity) {
        productName.setText(name);
        productQuantity.setText(String.valueOf(quantity));
        productUnit.setText(unit);
    }

    static ProductViewHolder create(ViewGroup viewGroup, OnListClickListener onListClickListener) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_products, viewGroup, false);
        return new ProductViewHolder(view, onListClickListener);
    }

    @Override
    public void onClick(View view) {
        onListClickListener.onListClick(getAdapterPosition());
    }

}

