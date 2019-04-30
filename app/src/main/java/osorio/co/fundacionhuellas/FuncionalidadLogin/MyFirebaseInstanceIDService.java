package osorio.co.fundacionhuellas.FuncionalidadLogin;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Juan Sebastian on 14/04/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    String idToken;
    public void onToken(){
        idToken = FirebaseInstanceId.getInstance().getToken();
    }

    public String getToken (){
        return idToken;
    }

}
