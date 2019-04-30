package osorio.co.fundacionhuellas.FuncionalidadNavigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

import osorio.co.fundacionhuellas.BaseDatos.ContenidoTarjetas;
import osorio.co.fundacionhuellas.R;

public class AportesAdapter extends RecyclerView.Adapter<AportesAdapter.ViewHolder> {

    List<ContenidoTarjetas> listaTarjetas;
    Context context;
    private OnItemClickListenerAportes aListener;

    public interface OnItemClickListenerAportes{
        void onItemClickAportes(int position);
    }
    public void setOnItemClickListenerAportes(OnItemClickListenerAportes listener){
        aListener = listener;
    }

    public AportesAdapter(List<ContenidoTarjetas> listaAportes, Context ctx) {
        this.listaTarjetas = listaAportes;
        this.context = ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage, itemClase;
        TextView itemTitle, itemDetail;

        final ProgressBar imageProgressCard;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.imagen_aportes);
            itemTitle = (TextView)itemView.findViewById(R.id.titulo_aportes);
            itemDetail = (TextView)itemView.findViewById(R.id.detalles_aportes);
            itemClase = (ImageView)itemView.findViewById(R.id.clase);
            imageProgressCard = (ProgressBar)itemView.findViewById(R.id.imgProgressBarCardView);

            itemClase.setVisibility(itemView.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(aListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            aListener.onItemClickAportes(position);
                        }
                    }
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) { //inflamos la pantalla del cardView aportes
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int decreciente = listaTarjetas.size()- position - 1 ;
        ContenidoTarjetas tarjeta = listaTarjetas.get(decreciente);
        Log.i("GSON "+decreciente, tarjeta.toString());
        holder.itemTitle.setText(tarjeta.getTar_nombre());
        holder.itemDetail.setText(tarjeta.getTar_descripcion());
        holder.imageProgressCard.setVisibility(View.VISIBLE);

        String imagenTarjeta_Url = context.getString(R.string.direccion_imagen) + tarjeta.getTar_imagen();

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
        if (tarjeta.getTar_tipo().equals("Sueno")){
            holder.itemClase.setImageResource(R.drawable.icon_intro_suenos);
        }if (tarjeta.getTar_tipo().equals("Evento")){
            holder.itemClase.setImageResource(R.drawable.icon_intro_eventos);
        }

    }

    @Override
    public int getItemCount() {
        return listaTarjetas.size();
    }
}

