package osorio.co.fundacionhuellas.FuncionalidadLogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import osorio.co.fundacionhuellas.BaseDatos.VolleySingleton;
import osorio.co.fundacionhuellas.R;

public class GuardarDatosUsuarios {
    String PrimerNombre, SegundoNombre, Apellido, ImagenURL, Email, Token, Telefono, Direccion;
    Context ctx;
    MyFirebaseInstanceIDService tokenID;

    public GuardarDatosUsuarios(Context context, String uPrimerNombre, String uSegundoNombre, String uApellido,
                                String uImagenURL, String uEmail, String uTelefono, String uDireccion){
        this.ctx= context;
        PrimerNombre = uPrimerNombre;
        SegundoNombre = uSegundoNombre;
        Apellido = uApellido;
        ImagenURL = uImagenURL;
        Email = uEmail;
        Telefono = uTelefono;
        Direccion = uDireccion;
    }

    public void guardarBaseDatos() {
        //----- obtener token-----------
        tokenID = new MyFirebaseInstanceIDService();
        tokenID.onToken();
        Token = tokenID.getToken();
        String url = ctx.getString(R.string.direccion_web) + ctx.getString(R.string.php_guardarUsuario); // consulta php
        StringRequest nreguest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(ctx,"Bienvenido: "+ PrimerNombre, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,"Error de conexión", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("txtCodigo","");
                params.put("txtNombre",PrimerNombre+" "+SegundoNombre+" "+ Apellido);
                params.put("txtCorreo",Email);
                if (ImagenURL!=""){
                    params.put("txtImagen",ImagenURL);
                }
                if (Telefono!=""){
                    params.put("telefonoUser",Telefono);
                }
                if (Direccion!=""){
                    params.put("correoUser", Direccion);
                }
                params.put("txtToken",Token);
                params.put("txtDispositivo","Android");
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(ctx).addToRequestQueue(nreguest);
    }



    public void guardarPreferencias(){
        //------------ GUARDAR PREFERENCIAS-----------------------
        SharedPreferences preferences = ctx.getSharedPreferences("credencialesUsuarioFH", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nombreUser", PrimerNombre+" "+SegundoNombre+" "+ Apellido);
        editor.putString("correoUser", Email);
        editor.putString("tokenUser", Token);
        if (Telefono!=""){
            editor.putString("telefonoUser",Telefono);
        }
        if (Direccion!=""){
            editor.putString("direccionUser", Direccion);
        }
        if(ImagenURL!=""){
            editor.putString("imagenUser", ImagenURL);
        }
        editor.commit();
    }


}
