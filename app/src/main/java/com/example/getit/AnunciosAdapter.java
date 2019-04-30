package com.example.getit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
public class AnunciosAdapter extends RecyclerView.Adapter<AnunciosAdapter.ViewHolder> implements View.OnClickListener {

    private final List<Producto> productoList;
    private Context context;

    public AnunciosAdapter(List<Producto> items) {
        productoList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_anuncios, parent, false);
        //view.setOnClickListener(this);
        CardView cardView = view.findViewById(R.id.cardProducto);
        cardView.setOnClickListener(this);
        context = parent.getContext();
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cardProducto:
                TextView idProducto = (TextView) v.findViewById(R.id.item_number) ;
                int idProductoInt = Integer.parseInt(idProducto.getText().toString());

                //save session in shared pref
                SharedPreferences prefs = context.getSharedPreferences("Productos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt("ProductoId", idProductoInt);

                editor.commit();

                Bundle bundle = new Bundle();
                bundle.putInt("ProductId", idProductoInt); // Put anything what you want

                AnuncioDetalleFragment anuncioDetalleFragment = new AnuncioDetalleFragment();
                anuncioDetalleFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, anuncioDetalleFragment).addToBackStack(null).commit();

                break;
        }
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
