package osorio.co.fundacionhuellas.FuncionalidadSplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import osorio.co.fundacionhuellas.FuncionalidadIntro.IntroHuellas;
import osorio.co.fundacionhuellas.FuncionalidadLogin.Login;
import osorio.co.fundacionhuellas.R;

public class SplashScreen extends Activity {
    private AdmiPreferencia admiPreferencia;
    ImageView imagenFondo, imagenLogo;
    ProgressBar imageProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //instanciar layout
        setContentView(R.layout.activity_splash_screen);
        //instanciar elementos asociados a la vista
        imagenFondo = (ImageView) findViewById(R.id.imagen_SplashScreen_fondo);
        imagenLogo = (ImageView) findViewById(R.id.imagen_SplashScreen_logo);
        imageProgress = (ProgressBar) findViewById(R.id.progressBarSplash);
        imagenFondo.setImageResource(R.drawable.bg_water_mark_uno);
        imagenLogo.setImageResource(R.drawable.logo_huellas);
        imageProgress.setVisibility(View.VISIBLE);

        //despues de 5seg se ejecuta el siguiente codigo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                admiPreferencia = new AdmiPreferencia(getBaseContext()); //intancia el objeto que almacena la variable de inicio unico
                imageProgress.setVisibility(View.INVISIBLE);
                if (admiPreferencia.isFirstTimeLaunch()) { //primera vez que abre la app?
                    Intent intent = new Intent(getBaseContext(), IntroHuellas.class);
                    startActivity(intent); //inicia la siguiente actividad
                    finish(); // saca la actividad de la pila
                } else{
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5000);


    }

}
