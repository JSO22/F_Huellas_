package osorio.co.fundacionhuellas.FuncionalidadNavigation;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import osorio.co.fundacionhuellas.BaseDatos.ConexionInternet;
import osorio.co.fundacionhuellas.BaseDatos.ContenidoTarjetas;
import osorio.co.fundacionhuellas.BaseDatos.VolleySingleton;
import osorio.co.fundacionhuellas.FuncionalidadLogin.GuardarDatosUsuarios;
import osorio.co.fundacionhuellas.R;


public class ParticipaFragment extends Fragment implements AportesAdapter.OnItemClickListenerAportes {

    private int posicionD;
    GuardarDatosUsuarios guardar;

    ImageView imagenSinConexion;
    TextView textSinConexion1, textSinConexion2;
    LinearLayout msm_conexion;
    Button buttonCargar;
    ImageButton buttonDemo;
    ConexionInternet conexion;
    ProgressDialog progress;
    CoordinatorLayout contenido;
    RecyclerView recyclerViewAportes;
    RecyclerView.LayoutManager layoutManagerAportes;
    AportesAdapter aportesAdapter;

    private int[] images_demo = {R.drawable.demo0,R.drawable.demo1,R.drawable.demo2,R.drawable.demo3,R.drawable.demo4,R.drawable.demo5,
            R.drawable.demo6,R.drawable.demo7,R.drawable.demo8,R.drawable.demo9,R.drawable.demo10,R.drawable.demo11,R.drawable.demo12};

    List<ContenidoTarjetas> listaTarjetas = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ParticipaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParticipaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParticipaFragment newInstance(String param1, String param2) {
        ParticipaFragment fragment = new ParticipaFragment();
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
        contenido.setVisibility(View.INVISIBLE);
        textSinConexion1.setVisibility(View.VISIBLE);
        textSinConexion2.setVisibility(View.VISIBLE);
        imagenSinConexion.setVisibility(View.VISIBLE);
        msm_conexion.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_participa, container, false);
        contenido = (CoordinatorLayout)view.findViewById(R.id.contenidoTar);
        // -----elementos para - sin conexion----------------------------------------------------------------------------------
        buttonCargar = (Button) view.findViewById(R.id.reinicio);
        imagenSinConexion = (ImageView) view.findViewById(R.id.imagenSinConexionP);
        textSinConexion1= (TextView) view.findViewById(R.id.text_conexion1);
        textSinConexion2= (TextView) view.findViewById(R.id.text_conexion2);
        msm_conexion = (LinearLayout) view.findViewById(R.id.msmConexion);
        //-----elementos para - con conexion-----------------------------------------------------------------------------------
        recyclerViewAportes = (RecyclerView) view.findViewById(R.id.recycler_view_Aportes);
        buttonDemo = (ImageButton) view.findViewById(R.id.button_demo);
        layoutManagerAportes = new LinearLayoutManager(this.getContext());
        recyclerViewAportes.setLayoutManager(layoutManagerAportes);
        recyclerViewAportes.setHasFixedSize(true);
        //------ validadcion de conexion a internet-------------------------
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();
        conexion = new ConexionInternet(getContext());
        if (conexion.hayConexion()){
            progress.hide();
            cargarWebService();//-- base datos
        }else{
            imagenSinConexion.setImageResource(R.drawable.icon_conexion_submarino);
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
        buttonDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo();
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
        String url = getString(R.string.direccion_web) + getString(R.string.php_tar_pendiente); // consulta php
        JsonArrayRequest jsonreguest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type items = new TypeToken<List<ContenidoTarjetas>>(){}.getType();
                listaTarjetas = gson.fromJson(response.toString(),items);
                progress.hide();
                contenido.setVisibility(View.VISIBLE);
                aportesAdapter = new AportesAdapter(listaTarjetas, getContext());
                recyclerViewAportes.setAdapter(aportesAdapter);
                //Log.i("GSON", listaTarjetas.toString());
                //Log.i("GSON", listaTarjetas.get(1).toString());
                aportesAdapter.setOnItemClickListenerAportes(ParticipaFragment.this);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hide();
                imagenSinConexion.setImageResource(R.drawable.icon_conexion_velero);
                textSinConexion1.setText(R.string.sin_internet2);
                textSinConexion2.setText(R.string.sin_internet_text2);
                confi_SinConexion();
                buttonCargar.setVisibility(View.VISIBLE);
            }
        });
        VolleySingleton.getInstanciaVolley(getContext()).addToRequestQueue(jsonreguest);
    }

    public void demo(){
        posicionD = 0;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(), R.style.AppThemeDialogDemo);
        final AlertDialog alert = builder.create();

        LayoutInflater inflater= LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_demo, null);

        final ImageView demoImag = (ImageView) layout.findViewById(R.id.imag_demo);
        Button buttonNext = (Button) layout.findViewById(R.id.button_next);
        Button buttonBack = (Button) layout.findViewById(R.id.button_back);
        ImageButton closeDemo = (ImageButton) layout.findViewById(R.id.close_demo);

        demoImag.setImageResource(images_demo[posicionD]);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posicionD > 0){
                    posicionD --;
                    demoImag.setImageResource(images_demo[posicionD]);
                }
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (posicionD < (images_demo.length - 1)){
                    posicionD ++;
                    demoImag.setImageResource(images_demo[posicionD]);
                }else{
                    posicionD = 0;
                    demoImag.setImageResource(images_demo[posicionD]);
                }
            }
        });

        closeDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.getWindow().getAttributes().windowAnimations = R.style.anim_dialog_demo;
        alert.setView(layout);
        alert.show();
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

    // metodo para abrir dialog de cada tarjeta y sus acciones
    // variables para dialog
    String _Telefono, _Email, _Nombre, _Titulo, _imagenTarjeta_Url;
    EditText txtTelefonoDialog;
    TextView txtTitulo, txtDetalles, txtNombreDialog, txtCorreoDialog;
    ImageView imagenTar;
    Button ayudar1, ayudar2;


    @Override
    public void onItemClickAportes(int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        final AlertDialog alertDialogP = builder.create();
        LayoutInflater inflater= LayoutInflater.from(getActivity());
        View layout = inflater.inflate(R.layout.dialog_fragment_aportes, null);

        ImageView cerrarDialogP = (ImageView) layout.findViewById(R.id.atras_dialog_A);


        int decreciente = listaTarjetas.size()- position - 1 ;
        ContenidoTarjetas tarjeta = listaTarjetas.get(decreciente);

        ConstraintLayout conConex = (ConstraintLayout) layout.findViewById(R.id.containerLayout);
        ConstraintLayout sinConex = (ConstraintLayout) layout.findViewById(R.id.containerConexion);

        imagenTar = (ImageView) layout.findViewById(R.id.imagen_aportes_dialog);
        txtTitulo = (TextView) layout.findViewById(R.id.titulo_aportes_dialog);
        txtDetalles = (TextView) layout.findViewById(R.id.detalles_aportes_dialog);
        txtTelefonoDialog = (EditText) layout.findViewById(R.id.telefono_user_dialog);
        txtNombreDialog = (TextView) layout.findViewById(R.id.nombre_user_dialog);
        txtCorreoDialog = (TextView) layout.findViewById(R.id.email_user_dialog);
        final ProgressBar imagenProgress = (ProgressBar) layout.findViewById(R.id.imgTarProgress);
        ayudar1 = (Button) layout.findViewById(R.id.button_ayudar1);
        ayudar2 = (Button) layout.findViewById(R.id.button_ayudar2);

        conexion = new ConexionInternet(getContext());
        if (!conexion.hayConexion()){
            conConex.setVisibility(View.INVISIBLE);
            sinConex.setVisibility(View.VISIBLE);
        }else{
            conConex.setVisibility(View.VISIBLE);
            sinConex.setVisibility(View.INVISIBLE);

            txtTelefonoDialog.setVisibility(View.GONE);
            if (AccessToken.getCurrentAccessToken()!=null){
                txtTelefonoDialog.setVisibility(View.VISIBLE);
                SharedPreferences preferences = getContext().getSharedPreferences("credencialesUsuarioFH", Context.MODE_PRIVATE);
                _Nombre= preferences.getString("nombreUser","");
                _Email= preferences.getString("correoUser","");
                _Telefono= preferences.getString("telefonoUser","");
                txtNombreDialog.setText(_Nombre);
                txtCorreoDialog.setText(_Email);
                if (_Telefono!=null){
                    txtTelefonoDialog.setText(_Telefono);
                }
            }

            txtTitulo.setText(tarjeta.getTar_nombre());
            _Titulo = txtTitulo.getText().toString();
            txtDetalles.setText(tarjeta.getTar_descripcion());

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
        }

        ayudar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken()!=null){
                    if(!txtTelefonoDialog.getText().toString().isEmpty()){
                        if (txtTelefonoDialog.getText().toString().length() == 10 || txtTelefonoDialog.getText().toString().length() == 7){
                            if (!_Telefono.equals(txtTelefonoDialog.getText().toString())){
                                guardar = new GuardarDatosUsuarios(getContext(),txtNombreDialog.getText().toString(),
                                        "", "", "", txtCorreoDialog.getText().toString(),
                                        txtTelefonoDialog.getText().toString(), "");
                                guardar.guardarBaseDatos();
                                guardar.guardarPreferencias();
                                Toast.makeText(getContext(),"Telefono actualizados exitosamente", Toast.LENGTH_SHORT).show();
                            }
                            irAyudar1();
                        }else{
                            txtTelefonoDialog.setError("Teléfono no válido");
                        }
                    }else{
                        txtTelefonoDialog.setError("Campo vacío");
                    }
                }else{
                    Toast.makeText(getContext(),"Es necesario Ingresar con Facebook", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ayudar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessToken.getCurrentAccessToken()!=null){
                    if(!txtTelefonoDialog.getText().toString().isEmpty()){
                        if (txtTelefonoDialog.getText().toString().length() == 10 || txtTelefonoDialog.getText().toString().length() == 7){
                            //guardarTelefono(txtTelefonoDialog.getText().toString(), txtNombreDialog.getText().toString(), txtCorreoDialog.getText().toString());
                            if (!_Telefono.equals(txtTelefonoDialog.getText().toString())) {
                                guardar = new GuardarDatosUsuarios(getContext(), txtNombreDialog.getText().toString(),
                                        "", "", "", txtCorreoDialog.getText().toString(), txtTelefonoDialog.getText().toString(), "");
                                guardar.guardarBaseDatos();
                                guardar.guardarPreferencias();
                                Toast.makeText(getContext(),"Telefono actualizados exitosamente", Toast.LENGTH_SHORT).show();
                            }
                            irAyudar2();
                        }else{
                            txtTelefonoDialog.setError("Teléfono no válido");
                        }
                    }else{
                        txtTelefonoDialog.setError("Campo vacío");
                    }
                }else{
                    Toast.makeText(getContext(),"Es necesario Ingresar con Facebook", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cerrarDialogP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogP.dismiss();
            }
        });
        alertDialogP.setView(layout);
        alertDialogP.getWindow().getAttributes().windowAnimations = R.style.anim_dialog_tar;
        alertDialogP.show();

    }

    // ----------------------------- accion para el boton de "quiero ayudar"--------------------------------
    public void irAyudar1() {
        String text = "Hola Huellas, soy "+_Nombre + " y deseo ayudar para hacer posible '" +_Titulo+"'";
        String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.telefonoFH)+"&text="+text;
        try {
            Toast.makeText(getActivity(), "WhatsApp", Toast.LENGTH_SHORT).show();
            PackageManager pm = getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent intentWhatsApp = new Intent(Intent.ACTION_VIEW);
            intentWhatsApp.setData(Uri.parse(url));
            startActivity(intentWhatsApp);
        }catch (Exception e){
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{getString(R.string.correoFHuellas)});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Quiero Ayudar!");
            intent.putExtra(Intent.EXTRA_TEXT,"["+_Nombre + ", cuéntanos  como quieres ayudarnos para hacer posible '" +_Titulo+"']" +
                    "\r\n"+"\r\n"+"\r\n"+"Datos de contacto:"+"\r\n" + _Email + "\r\n" + txtTelefonoDialog.getText().toString());
            startActivity(Intent.createChooser(intent, "Enviar correo con..."));
        }
    }
    //---------------------------- accion para el boton de "aporte economico"--------------------------------------
    private void irAyudar2() {
        String text = "Hola Huellas, soy "+_Nombre + ". Deseo realizar un aporte económico para '"
                +_Titulo+"' y me gustaría saber cómo puedo hacer llegar mi aporte?";
        String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.telefonoFH)+"&text="+text;
        try {
            PackageManager pm = getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent intentWhatsApp = new Intent(Intent.ACTION_VIEW);
            intentWhatsApp.setData(Uri.parse(url));
            startActivity(intentWhatsApp);
        } catch (Exception e) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{getString(R.string.correoFHuellas)});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Aporte Economico");
            intent.putExtra(Intent.EXTRA_TEXT,"["+_Nombre + ", contactémonos para hacer posible el aporte económico para '" +_Titulo+"']" +
                    "\r\n"+"\r\n"+"\r\n"+"Datos de contacto:"+"\r\n" + _Email + "\r\n" + txtTelefonoDialog.getText().toString());
            startActivity(Intent.createChooser(intent, "Enviar correo con..."));
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
}

