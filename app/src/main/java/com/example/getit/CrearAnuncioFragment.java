package com.example.getit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrearAnuncioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrearAnuncioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearAnuncioFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText TitleV;
    private EditText DescriptionV;
    private EditText AmountV;
    private EditText PriceV;
    private ImageView ImagenV;
    private String encodedImage;
    private int IdUserSession;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private OnFragmentInteractionListener mListener;

    public CrearAnuncioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearAnuncioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearAnuncioFragment newInstance(String param1, String param2) {
        CrearAnuncioFragment fragment = new CrearAnuncioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //step 2 cambiar texto main bar
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Nuevo Anuncio");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView =  inflater.inflate(R.layout.fragment_crear_anuncio, container, false);

        TitleV = myView.findViewById(R.id.Title);
        DescriptionV = myView.findViewById(R.id.Description);
        AmountV = myView.findViewById(R.id.Amount);
        PriceV = myView.findViewById(R.id.Price);
        ImagenV = myView.findViewById(R.id.ImgProduct);

        Button CrearAnuncioBtn = myView.findViewById(R.id.CrearAnuncioBtn);
        CrearAnuncioBtn.setOnClickListener(this);

        ImageButton OpenCameraBtn = myView.findViewById(R.id.OpenCameraBtn);
        OpenCameraBtn.setOnClickListener(this);

        ImageButton OpenMapsBtn = myView.findViewById(R.id.OpenMapsBtn);
        OpenMapsBtn.setOnClickListener(this);

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
        //step4 obtener shared preferences
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        IdUserSession = prefs.getInt("UserId",0);

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

        switch(view.getId()){
            case R.id.CrearAnuncioBtn:
                CrearAnuncio(new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        //show toast
                        CharSequence text = "Anuncio creado exitosamente";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                        //
                        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Latitud", "");
                        editor.putString("Longitud", "");
                        editor.commit();
                        //go to anuncios fragment
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contenedor, new AnunciosFragment()).commit();
                        //
                    }});
                break;

            case R.id.OpenMapsBtn:
                Intent mapsIntent = new Intent(getActivity(), MapsActivity.class);
                startActivity(mapsIntent);
                break;

            case R.id.OpenCameraBtn:
                dispatchTakePictureIntent();
                break;
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getContext(),
                        "com.example.getit.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            // Get the dimensions of the View
            int targetW = ImagenV.getWidth();
            int targetH = ImagenV.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            ImagenV.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] b = baos.toByteArray();

            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        }
    }

    public void CrearAnuncio(final VolleyCallBack callBack){
// Reset errors.
        TitleV.setError(null);
        DescriptionV.setError(null);
        AmountV.setError(null);
        PriceV.setError(null);

        boolean cancel = false;
        View focusView = null;

        final String title = TitleV.getText().toString();
        final String description = DescriptionV.getText().toString();
        final int amount = Integer.parseInt(AmountV.getText().toString());
        final double price = Double.parseDouble(PriceV.getText().toString());

        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        String LAT = prefs.getString("Latitud","");
        String LNG = prefs.getString("Longitud","");

        if (TextUtils.isEmpty(title)) {
            TitleV.setError(getString(R.string.error_field_required));
            focusView = TitleV;
            cancel = true;
        }

        if (TextUtils.isEmpty(description)) {
            DescriptionV.setError(getString(R.string.error_field_required));
            focusView = DescriptionV;
            cancel = true;
        }

        if (amount == 0) {
            AmountV.setError(getString(R.string.error_field_required));
            focusView = AmountV;
            cancel = true;
        }

        if (price == 0) {
            PriceV.setError(getString(R.string.error_field_required));
            focusView = PriceV;
            cancel = true;
        }

        if (LAT.isEmpty() || LNG.isEmpty()){
            //show toast
            CharSequence text = "Es necesario obtener la ubicación del producto";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            //
            cancel = true;
        }

        if (encodedImage.isEmpty()){
            //show toast
            CharSequence text = "Es necesario capturar una imagen del producto";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            //
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
                        .appendPath("products");
                String url = builder.build().toString();

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("UserId", IdUserSession);
                jsonBody.put("Title", title);
                jsonBody.put("Description", description);
                jsonBody.put("Amount", amount);
                jsonBody.put("Price", price);
                //jsonBody.put("ImageCode", encodedImage);
                jsonBody.put("ImageCode", "imagen");
                jsonBody.put("Latitude", Double.parseDouble(LAT));
                jsonBody.put("Longitude", Double.parseDouble(LNG));
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

    public interface VolleyCallBack {
        void onSuccess();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
