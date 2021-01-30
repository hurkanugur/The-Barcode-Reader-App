package com.example.mobiletermproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder>
{
    private ArrayList<Information> productInformation;
    private Context context;
    private ProductOnClickListener productOnClickListener;
    public InformationAdapter(ArrayList<Information> productInformation, Context context, ProductOnClickListener productOnClickListener)
    {
        this.productInformation = productInformation;
        this.context = context;
        this.productOnClickListener = productOnClickListener;
    }
    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_content, parent, false);
        return new ViewHolder(view, productOnClickListener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Information information = productInformation.get(position);
        viewHolder.priceTextView.setText(information.getPrice());
        viewHolder.titleTextView.setText(information.getTitle());
        viewHolder.sellerTextView.setText(information.getSeller());
        Picasso.get().load(information.getImageURL()).resize(136,111).into(viewHolder.productImageView);
    }

    @Override
    public int getItemCount() {
        return productInformation.size();
    }

    public interface ProductOnClickListener{
        void productOnClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView productImageView;
        TextView titleTextView, priceTextView, sellerTextView;
        ProductOnClickListener productOnClickListener;
        public ViewHolder(@NonNull View itemView, ProductOnClickListener productOnClickListener) {
            super(itemView);
            productImageView = (ImageView) itemView.findViewById(R.id.productImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            priceTextView = (TextView) itemView.findViewById(R.id.priceTextView);
            sellerTextView = (TextView) itemView.findViewById(R.id.sellerTextView);
            this.productOnClickListener = productOnClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            productOnClickListener.productOnClick(getAdapterPosition());
        }
    }


}
