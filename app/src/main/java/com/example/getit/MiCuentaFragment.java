package com.example.getit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MiCuentaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MiCuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiCuentaFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //campos
    private EditText nombresView;
    private EditText apellidosView;
    private EditText edadView;
    private EditText dniView;
    private EditText celularView;
    private String userSession;

    private OnFragmentInteractionListener mListener;

    public MiCuentaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MiCuentaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MiCuentaFragment newInstance(String param1, String param2) {
        MiCuentaFragment fragment = new MiCuentaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set toolbar text
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Mi Cuenta");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_mi_cuenta, container, false);

        nombresView = myView.findViewById(R.id.nombres);
        apellidosView = myView.findViewById(R.id.apellidos);
        edadView = myView.findViewById(R.id.edad);
        dniView = myView.findViewById(R.id.dni);
        celularView = myView.findViewById(R.id.celular);

        ObtenerDatos(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                //show toast
                CharSequence text = "Datos obtenidos exitosamente";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
                //
            }});

        Button ActualizarBtn = myView.findViewById(R.id.ActualizarBtn);
        ActualizarBtn.setOnClickListener(this);
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

        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        userSession = prefs.getString("Email","");

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

    @Override
    public void onClick(View view) {
        ActualizarDatos(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                //show toast
                CharSequence text = "Datos actualizados exitosamente";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
                //
                //reload fragment
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.contenedor);
                fragment.getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();

                //
            }});
    }

    public void ActualizarDatos(final VolleyCallBack callBack){
        // Reset errors.
        nombresView.setError(null);
        apellidosView.setError(null);
        edadView.setError(null);
        dniView.setError(null);
        celularView.setError(null);

        boolean cancel = false;
        View focusView = null;

        final String nombres = nombresView.getText().toString();
        final String apellidos = apellidosView.getText().toString();
        final Integer edad = Integer.parseInt(edadView.getText().toString());
        final String dni = dniView.getText().toString();
        final String celular = celularView.getText().toString();

        if (TextUtils.isEmpty(nombres)) {
            nombresView.setError(getString(R.string.error_field_required));
            focusView = nombresView;
            cancel = true;
        }

        if (TextUtils.isEmpty(apellidos)) {
            apellidosView.setError(getString(R.string.error_field_required));
            focusView = apellidosView;
            cancel = true;
        }

        if (edad == 0) {
            edadView.setError(getString(R.string.error_field_required));
            focusView = edadView;
            cancel = true;
        }

        if (TextUtils.isEmpty(dni)) {
            dniView.setError(getString(R.string.error_field_required));
            focusView = dniView;
            cancel = true;
        }

        if (TextUtils.isEmpty(celular)) {
            celularView.setError(getString(R.string.error_field_required));
            focusView = celularView;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        } else {
            try {

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("getitrest.azurewebsites.net")
                        .appendPath("api")
                        .appendPath("users")
                        .appendPath(userSession);
                String url = builder.build().toString();

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("Name", nombres);
                jsonBody.put("LastName", apellidos);
                jsonBody.put("Age", edad);
                jsonBody.put("Dni", dni);
                jsonBody.put("Cellphone", celular);
                jsonBody.put("RegisterCompleted", true);
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("OK",response.toString());
                        callBack.onSuccess();
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

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
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
    }

    public void ObtenerDatos(final VolleyCallBack callBack){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("getitrest.azurewebsites.net")
                    .appendPath("api")
                    .appendPath("users")
                    .appendPath(userSession);
            String url = builder.build().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        nombresView.setText(jsonObject.getString("Name"));
                        apellidosView.setText(jsonObject.getString("LastName"));
                        edadView.setText(jsonObject.getString("Age"));
                        dniView.setText(jsonObject.getString("Dni"));
                        celularView.setText(jsonObject.getString("Cellphone"));
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
}
