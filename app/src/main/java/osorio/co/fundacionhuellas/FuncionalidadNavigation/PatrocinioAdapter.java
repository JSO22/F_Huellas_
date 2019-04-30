package osorio.co.fundacionhuellas.FuncionalidadNavigation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;
import osorio.co.fundacionhuellas.R;

/**
 * Created by Juan Sebastian on 22/01/2018.
 */

public class PatrocinioAdapter extends RecyclerView.Adapter<PatrocinioAdapter.PatrocinioViewHolder> {

    private int[] images_patrocinio = {R.drawable.donapato_logo,R.drawable.smartgeeks_logo,R.drawable.granadina_logo};
    private OnItemClickListenerPatrocinio pListener;

    public interface OnItemClickListenerPatrocinio{
        void onItemClickPatrocinio(int position);
    }
    public void setOnItemClickListenerPatrocinio(OnItemClickListenerPatrocinio listener){
        pListener = listener;
    }

    @Override
    public PatrocinioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.patrocinio, parent,false);
        return new PatrocinioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatrocinioViewHolder holder, int position) {
        holder.circleImageView.setImageResource(images_patrocinio[position]);
    }

    @Override
    public int getItemCount() {
        return images_patrocinio.length;
    }

    public class PatrocinioViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;

        public PatrocinioViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.patrocinio_logo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            pListener.onItemClickPatrocinio(position);
                        }
                    }
                }
            });
        }
    }
}
