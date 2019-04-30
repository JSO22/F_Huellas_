package osorio.co.fundacionhuellas.FuncionalidadIntro;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import osorio.co.fundacionhuellas.FuncionalidadSplashScreen.AdmiPreferencia;
import osorio.co.fundacionhuellas.FuncionalidadLogin.Login;
import osorio.co.fundacionhuellas.R;

public class IntroHuellas extends AppCompatActivity {
    ViewPager viewPager;
    AdaptadorPersonalizadoIntro adapterIntro;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private Button btnIntro;
    private AdmiPreferencia admiPreferencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_huellas);

        viewPager = (ViewPager)findViewById(R.id.view_pager_intro);
        dotsLayout = (LinearLayout)findViewById(R.id.layoutDots_intro);
        btnIntro = (Button)findViewById(R.id.boton_intro);

        adapterIntro = new AdaptadorPersonalizadoIntro(this);
        viewPager.setAdapter(adapterIntro);

        addBottomDots(0);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < adapterIntro.getCount()) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private int getItem(int i) {return viewPager.getCurrentItem() + i; }
    // control del boton inferior color activado-desactivado
    private void addBottomDots(int nPage) {
        dots = new TextView[adapterIntro.getCount()];
        int colorsActive = getResources().getColor(R.color.dot_dark_screen);
        int colorsInactive = getResources().getColor(R.color.dot_light_screen);
        //
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[nPage].setTextColor(colorsActive);

    }

    private void launchHomeScreen() {
        admiPreferencia = new AdmiPreferencia(getBaseContext()); //intancia el objeto que almacena la variable de inicio unico
        admiPreferencia.setFirstTimeLaunch(false);  //cambia la variable almacenada
        startActivity(new Intent(IntroHuellas.this, Login.class));
        finish();
    }

    //metodo escucha para cambiar vista
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == adapterIntro.getCount() -1) {
                btnIntro.setText(getString(R.string.boton_intro_continuar));
            } else {
                btnIntro.setText(getString(R.string.boton_intro_siguiente));
            }
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

    };


}
