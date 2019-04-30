package osorio.co.fundacionhuellas.FuncionalidadNavigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import osorio.co.fundacionhuellas.BaseDatos.ContenidoTarjetas;
import osorio.co.fundacionhuellas.BaseDatos.VolleySingleton;
import osorio.co.fundacionhuellas.R;

public class DreamsAdapter extends RecyclerView.Adapter<DreamsAdapter.ViewHolder> {

    List<ContenidoTarjetas> listaTarjetas;
    Context context;
    private OnItemClickListenerDreams dListener;

    public interface OnItemClickListenerDreams{
        void onItemClickDreams(int position, Boolean isChecked, String cont);
    }
    public void setOnItemClickListenerDreams(OnItemClickListenerDreams listener){
        dListener = listener;
    }

    public DreamsAdapter(List<ContenidoTarjetas> listaTarjetas, Context ctx) {
        this.listaTarjetas = listaTarjetas;
        this.context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage, like_coraz;
        ToggleButton itemLike;
        TextView itemTitle, itemDetail, numeroLike;
        final ProgressBar imageProgressCard;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.imagen_aportes);
            imageProgressCard = (ProgressBar)itemView.findViewById(R.id.imgProgressBarCardView);
            itemTitle = (TextView)itemView.findViewById(R.id.titulo_aportes);
            itemDetail = (TextView)itemView.findViewById(R.id.detalles_aportes);
            // ----- LIKE ----
            like_coraz = (ImageView)itemView.findViewById(R.id.like_corazon);
            itemLike = (ToggleButton) itemView.findViewById(R.id.like);
            numeroLike = (TextView)itemView.findViewById(R.id.cant_like);

            itemLike.setVisibility(View.VISIBLE);
            like_coraz.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int decreciente = listaTarjetas.size()- position - 1 ;
        ContenidoTarjetas tarjeta = listaTarjetas.get(decreciente);
        holder.itemTitle.setText(tarjeta.getTar_nombre());
        holder.itemDetail.setText(tarjeta.getTar_descripcion());
        holder.imageProgressCard.setVisibility(View.VISIBLE);

        final String IMAG_TAR = tarjeta.getTar_imagen();
        final String ID_TAR = tarjeta.getTar_id().toString();

        String imagenTarjeta_Url = context.getString(R.string.direccion_imagen) + IMAG_TAR;
        Picasso.get()
                .load(imagenTarjeta_Url)
                .into(holder.itemImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imageProgressCard.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) {}
                });

        //---------------------------------- consulta para contar like en cada tarjeta--------------------------------------
        contarLike(holder.numeroLike, ID_TAR);
        //---------------------------------------Solo si esta registrado con facebook---------------------------------------------------------------------
        //-------------------------------------------- podra dar like ---------------------------------------------------------------------------------
        if (AccessToken.getCurrentAccessToken()!=null){
            SharedPreferences preferences = context.getSharedPreferences("credencialesUsuarioFH", Context.MODE_PRIVATE);
            final String U_EMAIL= preferences.getString("correoUser","");
            //-- consulta para verificar los like del usuario en cada tarjeta--------------------------------------
            verificarLike(U_EMAIL, holder.itemLike, ID_TAR);
            //---------- consulta para insertar o borrar un like en cada tarjeta -------------------------
            holder.itemLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.itemLike.isChecked()){  //chequea en que estado esta el boton
                        //---- insert like
                        insertarLike(U_EMAIL, holder.itemLike, ID_TAR, holder.numeroLike);
                        holder.itemLike.setBackgroundResource(R.drawable.button_circle_on);
                        holder.numeroLike.setVisibility(View.VISIBLE);
                        int contar = Integer.parseInt((String) holder.numeroLike.getText());
                        holder.numeroLike.setText(Integer.toString(contar + 1));
                    }else{
                        //---- delete like
                        borrarLike(U_EMAIL, holder.itemLike, ID_TAR, holder.numeroLike);
                        holder.itemLike.setBackgroundResource(R.drawable.button_circle_off);
                        int contar = Integer.parseInt((String) holder.numeroLike.getText());
                        holder.numeroLike.setText(Integer.toString(contar - 1));
                        if ((contar-1) == 0) {
                           holder.numeroLike.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
        // ---- ACCION DE CLICK EN CADA TARJETA ------
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dListener != null){
                    //int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        dListener.onItemClickDreams(position, holder.itemLike.isChecked(), (String) holder.numeroLike.getText());
                    }
                }
            }
        });
    }

    private void contarLike(final TextView numeroLike, String id_tar) {
        String url_count = context.getString(R.string.PHP_URL_COUNT) + id_tar; //cada id segun la posicion
        JsonArrayRequest jsonreguest_cunt = new JsonArrayRequest(com.android.volley.Request.Method.GET,
                url_count, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int count = response.getJSONObject(0).getInt("cantidad");
                    if (count == 0) {
                        numeroLike.setVisibility(View.INVISIBLE);
                    }else{
                        numeroLike.setVisibility(View.VISIBLE);
                        numeroLike.setText(Integer.toString(count));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                numeroLike.setText("0");
                numeroLike.setVisibility(View.INVISIBLE);
            }
        });
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(jsonreguest_cunt);
    }

    private void insertarLike(final String u_email, final ToggleButton itemLike, final String id_tar, final TextView numeroLike) {
        final String like = (String) numeroLike.getText();
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST,
                context.getString(R.string.PHP_URL_INSERT), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                contarLike(numeroLike, id_tar);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                itemLike.setBackgroundResource(R.drawable.button_circle_off);
                itemLike.setChecked(false);
                if (like.equals("0")){
                    numeroLike.setVisibility(View.INVISIBLE);
                }
                numeroLike.setText(like);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(reguest_validar);
    }

    private void borrarLike(final String u_email, final ToggleButton itemLike, final String id_tar, final TextView numeroLike) {
        final String like = (String) numeroLike.getText();
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST,
                context.getString(R.string.PHP_URL_DELETE), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                contarLike(numeroLike, id_tar);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                itemLike.setBackgroundResource(R.drawable.button_circle_on);
                itemLike.setChecked(true);
                if (like.equals("0")){
                    numeroLike.setVisibility(View.INVISIBLE);
                }
                numeroLike.setText(like);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(reguest_validar);
    }

    private void verificarLike(final String u_email, final ToggleButton itemLike, final String id_tar) {
        StringRequest reguest_validar = new StringRequest(com.android.volley.Request.Method.POST,
                context.getString(R.string.PHP_URL_VALIDAR), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Si")){
                    itemLike.setBackgroundResource(R.drawable.button_circle_on);
                    itemLike.setChecked(true);
                } else {
                    itemLike.setBackgroundResource(R.drawable.button_circle_off);
                    itemLike.setChecked(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                itemLike.setBackgroundResource(R.drawable.button_circle_off);
                itemLike.setChecked(false);
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtCodigo",id_tar);
                params.put("txtCorreo",u_email);
                return params;
            }
        };
        VolleySingleton.getInstanciaVolley(context).addToRequestQueue(reguest_validar);
    }

    @Override
    public int getItemCount() {
        return listaTarjetas.size();
    }


}
