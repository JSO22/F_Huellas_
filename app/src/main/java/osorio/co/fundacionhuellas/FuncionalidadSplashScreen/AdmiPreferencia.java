package osorio.co.fundacionhuellas.FuncionalidadSplashScreen;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Juan Sebastian on 29/10/2017.
 */

public class AdmiPreferencia {
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Context _context;

    public AdmiPreferencia(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("IntroFH", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean primeraVez) {
        editor.putBoolean("Es_Primera_Carga", primeraVez);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean("Es_Primera_Carga", true);
    }
}
