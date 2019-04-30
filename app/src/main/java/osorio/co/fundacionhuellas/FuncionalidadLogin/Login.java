package osorio.co.fundacionhuellas.FuncionalidadLogin;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import osorio.co.fundacionhuellas.FuncionalidadNavigation.MainNavigation;
import osorio.co.fundacionhuellas.R;

public class Login extends AppCompatActivity {
    Button loginButtonSR, loginButtonCF;
    ImageView imagen_fondo, imagen_logo;
    GuardarDatosUsuarios guardar;


    CallbackManager callbackManager;
    @Override
    public void onBackPressed(){
        //boton back -- evita la pila y da opcion de cerrar la app
        final AlertDialog.Builder builderr = new AlertDialog.Builder(this);
        builderr.setMessage("¿Desea salir de la aplicación?");
        builderr.setCancelable(false);
        builderr.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.cancel();
            }
        });
        builderr.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builderr.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-------- llamado interfaz del xml-------------
        setContentView(R.layout.activity_login);

        loginButtonCF = (Button) findViewById(R.id.loginButtonConFacebook);
        loginButtonSR = (Button) findViewById(R.id.loginButtonSinRegistro);

        imagen_fondo = (ImageView) findViewById(R.id.imagen_Login_fondo);
        imagen_logo = (ImageView) findViewById(R.id.imagen_Login_logo);

        //--------------------------------imagenes de fondo-----------------------------------------
        imagen_fondo.setImageResource(R.drawable.bg_water_mark_uno);
        imagen_logo.setImageResource(R.drawable.logo_huellas);

        //------------------------------- Entrar sin Registro --------------------------------------
        loginButtonSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });

        if (AccessToken.getCurrentAccessToken()!=null) {
            nextActivity();
        }else{
            //---------------------------- Entrar con Facebook -------------------------------------
            callbackManager = CallbackManager.Factory.create();
            loginButtonCF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(Login.this,
                            Arrays.asList("public_profile","email"));

                }
            });

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
                                    //instanciar metodo para almacenar
                                    guardar = new GuardarDatosUsuarios(getApplicationContext(),firstName,
                                            middleName, lastName, pictureUrl, email, "", "");
                                    guardar.guardarBaseDatos(); //en base de datos
                                    guardar.guardarPreferencias(); //en el dispositivo
                                    Toast.makeText(getApplicationContext(),"Bienvenido: "+ firstName, Toast.LENGTH_LONG).show();
                                    nextActivity();
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(),R.string.error_login, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),R.string.cancel_login, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(),R.string.error_login, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void nextActivity(){
        Intent intent = new Intent(Login.this, MainNavigation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
