package uk.ac.le.co2103.part2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private final TextView listTextView;
    private final ImageView listImageView;
    private OnListClickListener onListClickListener;

    private ShoppingListViewHolder(View itemView, OnListClickListener onListClickListener) {
        super(itemView);
        listTextView = itemView.findViewById(R.id.list_name);
        listImageView = itemView.findViewById(R.id.list_img);
        this.onListClickListener = onListClickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void bind(String text, String img) {
        listTextView.setText(text);
        if (img.equals("default")){
            listImageView.setImageResource(R.drawable.default_list);
        }
        else {
            listImageView.setImageURI(Uri.parse(img));
        }
    }

    static ShoppingListViewHolder create(ViewGroup viewGroup, OnListClickListener onListClickListener) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_lists, viewGroup, false);
        return new ShoppingListViewHolder(view, onListClickListener);
    }

    @Override
    public void onClick(View view) {
        onListClickListener.onListClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        onListClickListener.onListLongClick(getAdapterPosition());
        return true;
    }
}
