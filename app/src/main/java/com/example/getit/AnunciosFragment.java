package com.example.getit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getit.dummy.DummyContent;
import com.example.getit.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AnunciosFragment extends Fragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    //lista de productos
    private List<Producto> productoArrayList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnunciosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AnunciosFragment newInstance(int columnCount) {
        AnunciosFragment fragment = new AnunciosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set toolbar text
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Anuncios");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anuncios_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new AnunciosAdapter(DummyContent.ITEMS, mListener));

            //data de prueba
            //DummyItem dummyItem = new DummyItem("1","Vendo Samsung Galaxy S10","---", GetImageResource("@drawable/galaxy"), "S./3600");
            //dummyItemList.add(dummyItem);

            ObtenerProductos(new AnunciosFragment.VolleyCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        productoArrayList.clear();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i< jsonArray.length(); i++){
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Producto producto = new Producto(
                                    jo.getInt("ProductId"),
                                    jo.getInt("UserId"),
                                    jo.getString("Title"),
                                    jo.getString("Description"),
                                    jo.getInt("Amount"),
                                    jo.getDouble("Price"),
                                    jo.getString("ImageCode"),
                                    jo.getDouble("Latitude"),
                                    jo.getDouble("Longitude"),
                                    jo.getString("Date"),
                                    jo.getBoolean("IsActive")
                                    );
                            productoArrayList.add(producto);
                        }

                        recyclerView.setAdapter(new AnunciosAdapter(productoArrayList));

                        //show toast
                        CharSequence text = "Datos obtenidos exitosamente";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                        //
                    }
                    catch (Exception error)
                    {
                        //show toast
                        CharSequence text = error.toString();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                        //
                    }
                }});

            recyclerView.setAdapter(new AnunciosAdapter(productoArrayList));
            //
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface FragmentCommunication {
        void respond(int position,String name,String job);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imagen:
                TextView idProducto = (TextView) view.findViewById(R.id.item_number) ;
                int idProductoInt = Integer.parseInt(idProducto.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putInt("ProductId", idProductoInt); // Put anything what you want

                AnuncioDetalleFragment anuncioDetalleFragment = new AnuncioDetalleFragment();
                anuncioDetalleFragment.setArguments(bundle);
                //go to anuncios fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor, new AnuncioDetalleFragment()).addToBackStack(null).commit();
                //
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }

    public int GetImageResource(String drawroute){
        int resId = getContext().getResources().getIdentifier(
                drawroute, "drawable", getContext().getPackageName());
        return resId;
    }

    public void ObtenerProductos(final AnunciosFragment.VolleyCallBack callBack){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("getitrest.azurewebsites.net")
                    .appendPath("api")
                    .appendPath("products");
            String url = builder.build().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        callBack.onSuccess(response);
                    }
                    catch (Exception error)
                    {
                        Log.e("Error",error.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            //show toast
            CharSequence text = e.getMessage();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            //
        }
    }

    public interface VolleyCallBack {
        void onSuccess(String response);
    }


}
