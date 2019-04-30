package osorio.co.fundacionhuellas.FuncionalidadNavigation;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import osorio.co.fundacionhuellas.BaseDatos.ConexionInternet;
import osorio.co.fundacionhuellas.R;
import osorio.co.fundacionhuellas.myScrollView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformacionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformacionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformacionFragment extends Fragment implements OnMapReadyCallback, PatrocinioAdapter.OnItemClickListenerPatrocinio {

    LinearLayout msm_conexion;
    ConexionInternet conexion;
    FloatingActionMenu actionMenu;
    FloatingActionButton floatingButtonMap, floatingButtonFace, floatingButtonCall, floatingButtonGmail, floatingButtonInstagram, floatingButtonTwitter;
    GoogleMap mGoogleMap;
    MapView mMapView;
    myScrollView scrollView;
    ToggleButton b_direccion, b_horarios;
    TextView direccion, ciudadD, horarios;
    PatrocinioAdapter patrocinioAdapter;
    ImageButton buscaMarca;
    ImageView portadaVideo;
    MediaController mediaController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InformacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformacionFragment newInstance(String param1, String param2) {
        InformacionFragment fragment = new InformacionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informacion, container, false);
        msm_conexion = (LinearLayout) view.findViewById(R.id.msmConexion);
        final VideoView video = (VideoView) view.findViewById(R.id.video);
        portadaVideo = (ImageView) view.findViewById(R.id.portada_video);
        final ImageView play = (ImageView) view.findViewById(R.id.play);
        scrollView = (myScrollView) view.findViewById(R.id.scroll);

        b_direccion = (ToggleButton) view.findViewById(R.id.T_Button_direccion);
        b_horarios = (ToggleButton) view.findViewById(R.id.T_Button_horario);
        final ImageView flecha1 = (ImageView) view.findViewById(R.id.flecha1);
        final ImageView flecha2 = (ImageView) view.findViewById(R.id.flecha2);
        direccion = (TextView) view.findViewById(R.id.direccion);
        ciudadD = (TextView) view.findViewById(R.id.ciudad);
        horarios = (TextView) view.findViewById(R.id.horarios);
        //maps
        View ventana = (View) view.findViewById(R.id.ventana);
        buscaMarca = (ImageButton) view.findViewById(R.id.busca_marca);

        final ProgressBar progressVideo = (ProgressBar) view.findViewById(R.id.progress_video);

        conexion = new ConexionInternet(getContext());
        if (conexion.hayConexion()) {
            msm_conexion.setVisibility(View.INVISIBLE);
            mediaController = new MediaController(this.getContext());

            play.setBackgroundResource(R.drawable.play_video);
            play.setVisibility(View.VISIBLE);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play.setVisibility(View.INVISIBLE);
                    portadaVideo.setVisibility(View.INVISIBLE);
                    video.setVisibility(View.VISIBLE);
                    progressVideo.setVisibility(View.VISIBLE);
                    try
                    {
                        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fundacionhuellas-77c06.appspot.com/o/video_fh%2Fvideo_pirry_fh.mp4?alt=media&token=302b9e75-756d-4e95-8dcb-454fcd0c8541");
                        video.setVideoURI(uri);
                        video.setMediaController(mediaController);
                        mediaController.setAnchorView(video);
                        video.start();
                        play.setVisibility(View.GONE);
                    } catch (Exception e){
                        play.setBackgroundResource(R.drawable.ic_icon_errorcarga_24dp);
                        play.setVisibility(View.VISIBLE);
                    }
                }
            });

            //----------------------------------------------------
           video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressVideo.setVisibility(View.GONE);
                    //Toast.makeText(getContext(),"listo", Toast.LENGTH_SHORT).show();
                }
            });
            video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    progressVideo.setVisibility(View.GONE);
                    return false;
                }
            });
            //-------------------------------------------------------------
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    mediaController.hide();
                }
            });
        } else {
            msm_conexion.setVisibility(View.VISIBLE);
            video.setVisibility(View.INVISIBLE);
            portadaVideo.setVisibility(View.INVISIBLE);
            play.setBackgroundResource(R.drawable.ic_icon_errorcarga_24dp);
            play.setVisibility(View.VISIBLE);
        }

        b_direccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flecha1.setImageResource(R.drawable.ic_flecha_arriba_24dp);
                    direccion.setVisibility(View.VISIBLE);
                    ciudadD.setVisibility(View.VISIBLE);
                } else {
                    flecha1.setImageResource(R.drawable.ic_flecha_abajo_24dp);
                    direccion.setVisibility(View.GONE);
                    ciudadD.setVisibility(View.GONE);
                }
            }
        });

        b_horarios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flecha2.setImageResource(R.drawable.ic_flecha_arriba_24dp);
                    horarios.setVisibility(View.VISIBLE);
                    horarios.setText(getString(R.string.horario1) + "\r\n" + "\r\n" + getString(R.string.horario2));
                } else {
                    flecha2.setImageResource(R.drawable.ic_flecha_abajo_24dp);
                    horarios.setVisibility(View.GONE);
                }
            }
        });

        //---- buttonFlotante--------
        actionMenu = (FloatingActionMenu) view.findViewById(R.id.menu_flotante_informacion);
        floatingButtonFace = (FloatingActionButton) view.findViewById(R.id.openFacebook);
        floatingButtonCall = (FloatingActionButton) view.findViewById(R.id.openTelefono);
        floatingButtonGmail = (FloatingActionButton) view.findViewById(R.id.openGmail);
        floatingButtonInstagram = (FloatingActionButton) view.findViewById(R.id.openInstagram);
        floatingButtonTwitter = (FloatingActionButton) view.findViewById(R.id.openTwitter);
        floatingButtonMap = (FloatingActionButton) view.findViewById(R.id.openMap);
        actionMenu.setClosedOnTouchOutside(true);

        floatingButtonInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/_u/funhuellas");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/funhuellas")));
                }
            }
        });
        floatingButtonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("twitter://user?screen_name=huellasfunda");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.twitter.android");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/huellasfunda")));
                }

            }
        });

        floatingButtonFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/207535722707821")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/FundacionHuellasConSentidoDeVida")));
                }
            }
        });
        floatingButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + getString(R.string.telefonoFH)+"&text=Hola";
                try {
                    PackageManager pm = getContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent intentWhatsApp = new Intent(Intent.ACTION_VIEW);
                    intentWhatsApp.setData(Uri.parse(url));
                    startActivity(intentWhatsApp);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "WhatsApp no esta instalada en tu celular", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        floatingButtonGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.correoFHuellas)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hola Fundacón Huellas");
                    startActivity(Intent.createChooser(intent, "Enviar correo con..."));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });


        floatingButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/place/Fundaci%C3%B3n+Huellas+Con+Sentido+de+Vida/@2.9299712,-75.2810002,17z/data=!3m1!4b1!4m5!3m4!1s0x8e3b746e4395a657:0x211b99a95e988d9f!8m2!3d2.9299712!4d-75.2788115")));
            }
        });

        //--------------patrocinio---------
        RecyclerView listPatrocinio = (RecyclerView) view.findViewById(R.id.list_patrocinio);
        listPatrocinio.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        patrocinioAdapter = new PatrocinioAdapter();
        listPatrocinio.setAdapter(patrocinioAdapter);
        patrocinioAdapter.setOnItemClickListenerPatrocinio(InformacionFragment.this);

        //------ detener scrollViwe para el MAPVIEW--------------

        ventana.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return false;
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.maps);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        LatLng direccionFH = new LatLng(2.934772, -75.278918);
        float zoomlevel = 15;
        float grados = 45;
        float inclinacion = 70;
        mGoogleMap.addMarker(new MarkerOptions()
                .position(direccionFH)
                .title("Fundación Huellas Con Sentido de Vida")
                .snippet(getString(R.string.direccion)));

        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(direccionFH)      //Centramos el mapa en Madrid
                .zoom(zoomlevel)         //Establecemos el zoom en 16
                .bearing(grados)      //Establecemos la orientación con el noreste arriba
                .tilt(inclinacion)         //Bajamos el punto de vista de la cámara 70 grados
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        buscaMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //---- ACCION AL DAR CLICK EN LOS PATROCINADORES ---------------------
    int[] page_id = {R.string.dona_pato_id,R.string.smartgeeks_id,R.string.granadina_id};
    int[] page_url = {R.string.dona_pato_url,R.string.smartgeeks_url,R.string.granadina_url};
    @Override
    public void onItemClickPatrocinio(int position) {
        try {
            getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(page_id[position]))));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(page_url[position]))));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
