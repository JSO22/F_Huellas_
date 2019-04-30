package osorio.co.fundacionhuellas.FuncionalidadIntro;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import osorio.co.fundacionhuellas.R;

/**
 * Created by Juan Sebastian on 29/10/2017.
 */

public class AdaptadorPersonalizadoIntro extends PagerAdapter {
    private int[] TITULO_INTRO = {R.string.slide_titulo1, R.string.slide_titulo2, R.string.slide_titulo3, R.string.slide_titulo4};
    private int[] DEESCRIPCIONES_INTRO = {R.string.slide_1_sabiasque,R.string.slide_2_sabiasque,R.string.slide_3_sabiasque,R.string.slide_4_sabiasque};
    private int[] IMAGENES_INTRO = {R.drawable.icon_intro_suenos, R.drawable.icon_intro_eventos, R.drawable.icon_intro_suenos_cumplidos, R.drawable.icon_intro_nosotros};
    private Context ctx;
    private LayoutInflater layoutInflater;

    public AdaptadorPersonalizadoIntro(Context ctx){this.ctx=ctx;}

    @Override
    public int getCount() {
        return DEESCRIPCIONES_INTRO.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(ConstraintLayout)object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.intro_personalizado, container, false); //Infla una vista desde un recurso XML.
        ImageView imageView = (ImageView)item_view.findViewById(R.id.imagen_intro);
        TextView textView = (TextView)item_view.findViewById(R.id.titulo_intro);
        TextView textView1 = (TextView)item_view.findViewById(R.id.descripcion_intro);
        textView.setText(TITULO_INTRO[position]);
        imageView.setImageResource(IMAGENES_INTRO[position]);
        textView1.setText(DEESCRIPCIONES_INTRO[position]);

        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) { //destruye el view que se va desplazando
        container.removeView((ConstraintLayout)object); //remueve el RelativeLayout
    }
}
