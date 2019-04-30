package osorio.co.fundacionhuellas.FuncionalidadNavigation;


import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import osorio.co.fundacionhuellas.R;


public class MainNavigation extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_aportes:
                    transaction.replace(R.id.content,new ParticipaFragment()).commit();
                    return true;
                case R.id.navigation_dream:
                    transaction.replace(R.id.content, new DreamsFragment()).commit();
                    return true;
                case R.id.navigation_informacion:
                    transaction.replace(R.id.content, new InformacionFragment()).commit();
                    return true;
                case R.id.navigation_perfil:
                    transaction.replace(R.id.content, new PerfilFragment()).commit();
                    return true;
            }return false;
        }

    };
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new ParticipaFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (navigation.getSelectedItemId() == R.id.navigation_aportes) {
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

        } else {
            navigation.setSelectedItemId(R.id.navigation_aportes);
        }
    }


}
