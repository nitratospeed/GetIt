package com.example.getit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.getit.AnunciosFragment.OnListFragmentInteractionListener;
import com.example.getit.dummy.DummyContent;
import com.example.getit.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.ViewHolder> {

    private final List<Producto> productoList;

    public AnunciosAdapter(List<Producto> items) {
        productoList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_anuncios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Producto producto = productoList.get(position);

        holder.ProductIdView.setText(String.valueOf(producto.getProductId()));
        holder.TitleView.setText(producto.getTitle());
        holder.PriceView.setText(String.valueOf(producto.getPrice()));
        //holder.ImageCodeView.setImageResource(mValues.get(position).imagen);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView ProductIdView;
        public final TextView TitleView;
        //public final ImageView ImageCodeView;
        public final TextView PriceView;

        public Producto producto;

        public ViewHolder(View view) {
            super(view);
            ProductIdView = view.findViewById(R.id.item_number);
            TitleView = view.findViewById(R.id.content);
            //ImageCodeView = view.findViewById(R.id.imagen);
            PriceView = view.findViewById(R.id.precio);
        }
    }
}
