package com.example.getit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnuncioDetalleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnuncioDetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnuncioDetalleFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int idProducto;

    private TextView tituloV;
    private TextView descripcionV;
    private EditText cantidadV;
    private TextView precioV;
    private ImageView imagenV;

    private OnFragmentInteractionListener mListener;

    public AnuncioDetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnuncioDetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnuncioDetalleFragment newInstance(String param1, String param2) {
        AnuncioDetalleFragment fragment = new AnuncioDetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        ConfirmacionPago();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_anuncio_detalle, container, false);
        // Inflate the layout for this fragment

        Button b = myView.findViewById(R.id.ComprarBtn);
        b.setOnClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences("Productos", Context.MODE_PRIVATE);
        idProducto = prefs.getInt("ProductoId",0);

        if(idProducto != 0){

            tituloV = myView.findViewById(R.id.titulo);
            descripcionV = myView.findViewById(R.id.descripcion);
            cantidadV = myView.findViewById(R.id.cantidad);
            precioV = myView.findViewById(R.id.precio);
            //imagenV = myView.findViewById(R.id.imagen);

            ObtenerDatos(new MiCuentaFragment.VolleyCallBack() {
                @Override
                public void onSuccess() {
                    //show toast
                    CharSequence text = "Datos obtenidos exitosamente";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    //
                }});
        }
        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public void ObtenerDatos(final MiCuentaFragment.VolleyCallBack callBack){
        try {
            String idProductoString = Integer.toString(idProducto);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("getitrest.azurewebsites.net")
                    .appendPath("api")
                    .appendPath("products")
                    .appendPath(idProductoString);
            String url = builder.build().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        tituloV.setText(jsonObject.getString("Title"));
                        descripcionV.setText(jsonObject.getString("Description"));
                        cantidadV.setText(jsonObject.getString("Amount"));
                        precioV.setText(jsonObject.getString("Price"));
                        //imagenV.setText(jsonObject.getString("Cellphone"));
                        callBack.onSuccess();
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
        void onSuccess();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void ConfirmacionPago(){
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de Pago")
                .setMessage("¿Está seguro de realizar la compra para este producto?")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void GoToCompraFragment(View compra_view) {

    }

    public void GoToPerfilFragment(View perfil_view) {

    }

    public void GoToComentarioFragment(View comentario_view) {

    }
}
