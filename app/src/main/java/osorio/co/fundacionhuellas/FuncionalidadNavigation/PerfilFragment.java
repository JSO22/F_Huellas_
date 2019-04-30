package osorio.co.fundacionhuellas.FuncionalidadNavigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import osorio.co.fundacionhuellas.BaseDatos.ConexionInternet;
import osorio.co.fundacionhuellas.FuncionalidadLogin.GuardarDatosUsuarios;
import osorio.co.fundacionhuellas.FuncionalidadLogin.Login;
import osorio.co.fundacionhuellas.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    LinearLayout msm_conexion;
    ConexionInternet conexion;
    TextInputLayout textInputTelefono, textInputDireccion, textInputEmail;
    TextView txtUser;
    Button btnCerrarSesion, btnAtualizar;
    ImageView logofacebook;
    CircleImageView imagePerfil;
    ProgressBar imageProgress;

    CallbackManager callbackManagerPerfil;
    GuardarDatosUsuarios guardar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txtUser = (TextView)view.findViewById(R.id.tvUsuario);
        textInputEmail= (TextInputLayout) view.findViewById(R.id.inputLayout_correo);
        textInputTelefono = (TextInputLayout) view.findViewById(R.id.inputLayout_telefono);
        textInputDireccion = (TextInputLayout) view.findViewById(R.id.inputLayout_Direccion);
        logofacebook = (ImageView)view.findViewById(R.id.iconoFacebook);
        btnCerrarSesion = (Button)view.findViewById(R.id.cerrarSesion);
        btnAtualizar = (Button)view.findViewById(R.id.actualizarData);
        imagePerfil = (CircleImageView) view.findViewById(R.id.imgProfile);
        imageProgress = (ProgressBar)view.findViewById(R.id.imgProgressBar);
        msm_conexion = (LinearLayout) view.findViewById(R.id.msmConexion);

        conexion = new ConexionInternet(getContext());
        if (conexion.hayConexion()){
            msm_conexion.setVisibility(View.INVISIBLE);
        }else{
            msm_conexion.setVisibility(View.VISIBLE);
        }

        imagePerfil.setImageResource(R.drawable.perfil);
        if (AccessToken.getCurrentAccessToken()!=null){
            cargarPerfil();
            configButton_cerrarSesion();
        }else{
            configButton_inicioSesion();
        }
        return view;
    }

    public void configButton_inicioSesion() {
        btnAtualizar.setEnabled(false);
        logofacebook.setVisibility(View.VISIBLE);
        btnCerrarSesion.setBackgroundResource(R.drawable.button_login_f);
        //--- al oprimir el boton inicio sesion
        callbackManagerPerfil = CallbackManager.Factory.create();
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(PerfilFragment.this,
                        Arrays.asList("public_profile","email"));
                loginFacebook(callbackManagerPerfil);
            }
        });
    }

    public void configButton_cerrarSesion() {
        logofacebook.setVisibility(View.INVISIBLE);
        btnCerrarSesion.setBackgroundResource(R.drawable.button_login_r);
        btnCerrarSesion.setText("Cerrar sesión");
        //cerrar sesion
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                irPantallaLogin();
            }
        });
        //actualizar
        btnAtualizar.setEnabled(true);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textInputTelefono.getEditText().getText().toString().isEmpty()){
                    if (textInputTelefono.getEditText().getText().toString().length() == 10 || textInputTelefono.getEditText().getText().toString().length() == 7){
                        guardar = new GuardarDatosUsuarios(getContext(),txtUser.getText().toString(),
                                "", "", "", textInputEmail.getEditText().getText().toString(),textInputTelefono.getEditText().getText().toString(), textInputDireccion.getEditText().getText().toString());
                        guardar.guardarBaseDatos();
                        guardar.guardarPreferencias();
                        Toast.makeText(getContext(),"Datos actualizados exitosamente "+ txtUser.getText().toString(), Toast.LENGTH_LONG).show();
                        textInputTelefono.setErrorEnabled(false);
                    }else{

                        textInputTelefono.setError("Numero de teléfono no válido");
                    }
                }else{
                    textInputTelefono.setError("Este campo no puede quedar vacío");
                }

            }
        });
    }

    //metodo para obtener datos de facebook
    private void loginFacebook(CallbackManager callbackManager) {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //----------------obtener datos-----------------------------------
                Profile profile = Profile.getCurrentProfile();
                if ((AccessToken.getCurrentAccessToken() != null) && (profile != null)) {
                    final String firstName = profile.getFirstName();
                    final String middleName = profile.getMiddleName();
                    final String lastName = profile.getLastName();
                    final String pictureUrl = profile.getProfilePictureUri(200, 200).toString();
                    //------------obtener email------//
                    GraphRequest peticion = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                String email =  object.getString("email");
                                //clase para guardar datos
                                guardar = new GuardarDatosUsuarios(getContext(),firstName,
                                        middleName, lastName, pictureUrl, email, "", "");
                                guardar.guardarBaseDatos(); //base de datos remota
                                guardar.guardarPreferencias(); //memoria del dispositivo
                                Toast.makeText(getContext(),"Bienvenido: "+ firstName, Toast.LENGTH_LONG).show();
                                irPantallaPrincipal();
                                //guardarDatosPerfil(nombre, pictureUrl, email,"","");
                            } catch (JSONException e) {
                                Toast.makeText(getContext(),R.string.error_login, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "email");
                    peticion.setParameters(parameters);
                    peticion.executeAsync();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(),R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(),R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerPerfil.onActivityResult(requestCode, resultCode, data);
    }

    //-----------------  se carga el perfil si inicio seccion con facebook ---------------------
    private void cargarPerfil() {
        //datos almacenados en el dispositivos
        SharedPreferences preferences = getContext().getSharedPreferences("credencialesUsuarioFH", Context.MODE_PRIVATE);
        String uName= preferences.getString("nombreUser","");
        String uEmail= preferences.getString("correoUser","");
        String uImagenUrl= preferences.getString("imagenUser","");
        String uTelefono= preferences.getString("telefonoUser","");
        String uDireccion= preferences.getString("direccionUser","");
        txtUser.setText(uName);
        textInputEmail.getEditText().setText(uEmail);
        textInputTelefono.getEditText().setText(uTelefono);
        textInputDireccion.getEditText().setText(uDireccion);
        if (uImagenUrl!=""){
            imageProgress.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(uImagenUrl)
                    .into(imagePerfil, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageProgress.setVisibility(View.GONE);                        }
                        @Override
                        public void onError(Exception e) {}
                    });
        }
    }
    //acciones de carga de pantalla con el boton cerrar sesion
    public void irPantallaLogin() {
        Intent intent = new Intent(this.getContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    public void irPantallaPrincipal() {
        Intent intent = new Intent(this.getContext(), MainNavigation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
