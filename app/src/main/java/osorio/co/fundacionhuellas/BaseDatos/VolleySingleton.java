package osorio.co.fundacionhuellas.BaseDatos;

import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Juan Sebastian on 26/03/2018.
 */

public class VolleySingleton {
    private static VolleySingleton instanciaVolley;
    private RequestQueue request;
    private static Context contexto;

    private VolleySingleton(Context context) {
        contexto = context;
        request = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstanciaVolley(Context context) {
        if (instanciaVolley == null){
            instanciaVolley = new VolleySingleton(context);
        }
        return instanciaVolley;
    }

    public RequestQueue getRequestQueue() {
        if (request == null){
            request = Volley.newRequestQueue(contexto.getApplicationContext());
        }
        return request;
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
}
