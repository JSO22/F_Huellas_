package osorio.co.fundacionhuellas.FuncionalidadNavigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import osorio.co.fundacionhuellas.BaseDatos.ConexionInternet;
import osorio.co.fundacionhuellas.BaseDatos.ContenidoTarjetas;
import osorio.co.fundacionhuellas.BaseDatos.VolleySingleton;
import osorio.co.fundacionhuellas.R;


public class DreamsFragment extends Fragment implements DreamsAdapter.OnItemClickListenerDreams {

    ImageView imagenSinConexion;
    TextView textSinConexion1, textSinConexion2;
    LinearLayout msm_conexion;
    Button buttonCargar;
    ConexionInternet conexion;

    ProgressDialog progress;

    CoordinatorLayout contenidoD;
    RecyclerView recyclerViewDreams;
    RecyclerView.LayoutManager layoutManagerDreams;
    DreamsAdapter dreamsAdapter;

    List<ContenidoTarjetas> listaTarjetasD = new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DreamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DreamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DreamsFragment newInstance(String param1, String param2) {
        DreamsFragment fragment = new DreamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void confi_ConConexion(){
        msm_conexion.setVisibility(View.INVISIBLE);
        textSinConexion1.setVisibility(View.INVISIBLE);
        textSinConexion2.setVisibility(View.INVISIBLE);
        imagenSinConexion.setVisibility(View.INVISIBLE);
    }
    void confi_SinConexion(){
        contenidoD.setVisibility(View.INVISIBLE);
        textSinConexion1.setVisibility(View.VISIBLE);
        textSinConexion2.setVisibility(View.VISIBLE);
        imagenSinConexion.setVisibility(View.VISIBLE);
        msm_conexion.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dreams, container, false);
        contenidoD = (CoordinatorLayout)view.findViewById(R.id.contenidoTarD);
        // -----sin conexion----------------------------------------------------------------------------------
        buttonCargar = (Button) view.findViewById(R.id.reinicio);
        imagenSinConexion = (ImageView) view.findViewById(R.id.imagenSinConexionD);
        textSinConexion1= (TextView) view.findViewById(R.id.text_conexion1);
        textSinConexion2= (TextView) view.findViewById(R.id.text_conexion2);
        msm_conexion = (LinearLayout) view.findViewById(R.id.msmConexion);
        //-----con conexion-----------------------------------------------------------------------------------
        recyclerViewDreams = (RecyclerView) view.findViewById(R.id.recycler_view_Dreams);
        layoutManagerDreams = new LinearLayoutManager(getContext());
        recyclerViewDreams.setLayoutManager(layoutManagerDreams);
        recyclerViewDreams.setHasFixedSize(true);

        //------ validadcion de conexion a internet-------------------------
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();
        conexion = new ConexionInternet(getContext());
        if (conexion.hayConexion()){
            progress.hide();
            cargarWebService(); //-- base datos
        }else{
            imagenSinConexion.setImageResource(R.drawable.icon_conexion_velero);
            textSinConexion1.setText(R.string.sin_internet1);
            textSinConexion2.setText(R.string.sin_internet_text1);
            confi_SinConexion();
            progress.hide();
        }
        buttonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCargar.setVisibility(View.INVISIBLE);
                cargarWebService();
            }
        });
        return view;
    }

    //----------------------------- consulta a la base de datos (volley and Gson) ------------------------------------------
    private void cargarWebService() {
        confi_ConConexion();
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();
        String url = getString(R.string.direccion_web) + getString(R.string.php_tar_finalizado); //finalizado
        JsonArrayRequest jsonreguest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type items = new TypeToken<List<ContenidoTarjetas>>(){}.getType();
                listaTarjetasD = gson.fromJson(response.toString(),items);
                progress.hide();
                contenidoD.setVisibility(View.VISIBLE);
                dreamsAdapter = new DreamsAdapter(listaTarjetasD, getContext());
                recyclerViewDreams.setAdapter(dreamsAdapter);
                dreamsAdapter.setOnItemClickListenerDreams(DreamsFragment.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hide();
                imagenSinConexion.setImageResource(R.drawable.icon_conexion_submarino);
                textSinConexion1.setText(R.string.sin_internet2);
                textSinConexion2.setText(R.string.sin_internet_text2);
                confi_SinConexion();
                buttonCargar.setVisibility(View.VISIBLE);
            }
        });
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonreguest);
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //------------metodo para abrir dialog de cada tarjeta y sus acciones------------------------------
    String _imagenTarjeta_Url;
    ImageView imagenTar;
    ToggleButton meGusta;
    TextView numeroLike;
    Button compartir;

    @Override
    public void onItemClickDreams(final int posicion, Boolean isChecked, String contar) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        final AlertDialog alertDialogD = builder.create();
        int decreciente = listaTarjetasD.size()- posicion - 1 ;
        final ContenidoTarjetas tarjeta = listaTarjetasD.get(decreciente);
        LayoutInflater inflater= LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_fragment_dreams, null);

        final ConstraintLayout conConex = (ConstraintLayout) layout.findViewById(R.id.containerLayout2);
        final ConstraintLayout sinConex = (ConstraintLayout) layout.findViewById(R.id.containerConexion2);

        imagenTar = (ImageView) layout.findViewById(R.id.imagen_dreams_dialog);
        TextView txtTitulo = (TextView) layout.findViewById(R.id.titulo_dreams_dialog);
        TextView txtDetalles = (TextView) layout.findViewById(R.id.detalles_dreams_dialog);
        numeroLike = (TextView)layout.findViewById(R.id.cant_like);
        meGusta = (ToggleButton) layout.findViewById(R.id.button_Like);
        compartir = (Button) layout.findViewById(R.id.button_Compartir);
        final ProgressBar imagenProgress = (ProgressBar) layout.findViewById(R.id.imgTarProgress);

        ImageView cerrarDialogD = (ImageView) layout.findViewById(R.id.atras_dialog_D);

        conexion = new ConexionInternet(getContext());
        if (!conexion.hayConexion()){
            conConex.setVisibility(View.INVISIBLE);
            sinConex.setVisibility(View.VISIBLE);
        }else {
            conConex.setVisibility(View.VISIBLE);
            sinConex.setVisibility(View.INVISIBLE);

            txtTitulo.setText(tarjeta.getTar_nombre());
            txtDetalles.setText(tarjeta.getTar_descripcion());
            numeroLike.setText(contar);
            _imagenTarjeta_Url = getString(R.string.direccion_imagen) + tarjeta.getTar_imagen();

            imagenProgress.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(_imagenTarjeta_Url)
                    .into(imagenTar, new Callback() {
                        @Override
                        public void onSuccess() {
                            imagenProgress.setVisibility(View.GONE);                        }
                        @Override
                        public void onError(Exception e) {}
                    });

            //------------------------------------LIKE-------------------------------------------
            final String ID_TAR = tarjeta.getTar_id().toString();
            contarLike(numeroLike, ID_TAR);

            if (AccessToken.getCurrentAccessToken()!=null){
                SharedPreferences preferences = getContext().getSharedPreferences("credencialesUsuarioFH", Context.MODE_PRIVATE);
                final String U_EMAIL= preferences.getString("correoUser","");
                //-- consulta para verificar los like del usuario en cada tarjeta--------------------------------------
                if (isChecked){
                    meGusta.setBackgroundResource(R.drawable.button_circle_on);
                    meGusta.setChecked(true);
                }else {
                    meGusta.setBackgroundResource(R.drawable.button_circle_off);
                    meGusta.setChecked(false);
                }

                meGusta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (meGusta.isChecked()){  //chequea en que estado esta el boton
                            //---- insert like
                            insertarLike(U_EMAIL, meGusta, ID_TAR, conConex, sinConex);
                            meGusta.setBackgroundResource(R.drawable.button_circle_on);
                            numeroLike.setVisibility(View.VISIBLE);
                            int contar = Integer.parseInt((String) numeroLike.getText());
                            numeroLike.setText(Integer.toString(contar + 1));

                            recyclerViewDreams.setAdapter(dreamsAdapter);
                        }else{
                            //---- delete like
                            borrarLike(U_EMAIL, meGusta, ID_TAR, conConex, sinConex);
                            meGusta.setBackgroundResource(R.drawable.button_circle_off);
                            int contar = Integer.parseInt((String) numeroLike.getText());
                            numeroLike.setText(Integer.toString(contar - 1));
                            if ((contar-1) == 0) {
                                numeroLike.setVisibility(View.INVISIBLE);
                            }

                            recyclerViewDreams.setAdapter(dreamsAdapter);
                        }
                    }
                });

                compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share(tarjeta.getTar_nombre());
                    }
                });
            }else{
                compartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Es necesario Ingresar con Facebook", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        cerrarDialogD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogD.dismiss();
            }
        });
        alertDialogD.setView(layout);
        alertDialogD.getWindow().getAttributes().windowAnimations = R.style.anim_dialog_tar;
        alertDialogD.show();
    }

    private void share(String TarNombre) {
        imagenTar.buildDrawingCache();
        Bitmap bitmapp = imagenTar.getDrawingCache();
        try {
            File file = new File(getContext().getExternalCacheDir(),"sonrisa_share.png");
            FileOutputStream fileOut = new FileOutputStream(file);
            bitmapp.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
            fileOut.flush();
            fileOut.close();
            file.setReadable(true,false);

            //EVITAR POLITICAS
            //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            //StrictMode.setVmPolicy(builder.build());

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, "Sueños por Sonrisa: Se hizo realidad '" + TarNombre + "'");
            intent.putExtra(Intent.EXTRA_SUBJECT,"subject!");
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file));
            startActivity(Intent.createChooser(intent, "Compartir en..."));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void contarLike(final TextView numeroLike, String id_tar) {
        String url_count = getString(R.string.PHP_URL_COUNT) + id_tar; //cada id segun la posicion
        JsonArrayRequest jsonreguest_cunt = new JsonArrayRequest(com.android.volley.Request.Method.GET, url_count, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int count = response.getJSONObject(0).getInt("cantidad");
                    if (count == 0) {
                        numeroLike.setVisibility(View.INVISIBLE);
                    }else{
                        numeroLike.setVisibility(View.VISIBLE);
                        numeroLike.setText(Integer.toString(count));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonreguest_cunt);
    }

    private void borrarLike(final String u_email, final ToggleButton iLike, final String id_tar, final ConstraintLayout conConexion, final ConstraintLayout sinConexion) {
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST, getString(R.string.PHP_URL_DELETE), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                contarLike(numeroLike, id_tar);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sinConexion.setVisibility(View.VISIBLE);
                conConexion.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(reguest_validar);
    }

    private void insertarLike(final String u_email, final ToggleButton iLike, final String id_tar, final ConstraintLayout conConexion, final ConstraintLayout sinConexion) {
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST, getString(R.string.PHP_URL_INSERT), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                contarLike(numeroLike, id_tar);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iLike.setChecked(false);
                sinConexion.setVisibility(View.VISIBLE);
                conConexion.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(reguest_validar);
    }

    private void verificarLike(final String u_email, final ToggleButton iLike, final String id_tar) {
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST, getString(R.string.PHP_URL_VALIDAR), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Si")){
                    iLike.setBackgroundResource(R.drawable.button_circle_on);
                    iLike.setChecked(true);
                } else {
                    iLike.setBackgroundResource(R.drawable.button_circle_off);
                    iLike.setChecked(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(reguest_validar);
    }






}
