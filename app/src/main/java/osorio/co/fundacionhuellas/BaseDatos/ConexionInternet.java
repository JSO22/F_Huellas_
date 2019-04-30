package osorio.co.fundacionhuellas.BaseDatos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Juan Sebastian on 30/03/2018.
 */

public class ConexionInternet {
    Context context;
    public ConexionInternet(Context context) {
        this.context = context;
    }
    public boolean hayConexion(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }
}
