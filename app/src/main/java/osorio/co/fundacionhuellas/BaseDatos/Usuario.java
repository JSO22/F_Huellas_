package osorio.co.fundacionhuellas.BaseDatos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Juan Sebastian on 5/04/2018.
 */

public class Usuario {
    @SerializedName("txtCodigo")
    @Expose
    private Integer mCodigo;
    @SerializedName("txtNombre")
    @Expose
    private String mNombre;
    @SerializedName("txtCorreo")
    @Expose
    private String mCorreo;
    @SerializedName("txtImagen")
    @Expose
    private String mImagen;
    @SerializedName("txtTelefono")
    @Expose
    private Integer mTelefono;
    @SerializedName("txtDireccion")
    @Expose
    private String mDireccion;
    @SerializedName("txtToken")
    @Expose
    private String mToken;
    @SerializedName("txtDispositivo")
    @Expose
    private String mDispositivo;

    public Usuario(Integer codigo, String nombre, String correo, String imagen, Integer telefono, String direccion, String token, String dispositivo){
        mCodigo = codigo;
        mNombre = nombre;
        mCorreo = correo;
        mImagen = imagen;
        mTelefono = telefono;
        mDireccion = direccion;
        mToken = token;
        mDispositivo = dispositivo;
    }

    public Integer getmCodigo() {
        return mCodigo;
    }

    public String getmNombre() {
        return mNombre;
    }

    public String getmCorreo() {
        return mCorreo;
    }

    public String getmImagen() {
        return mImagen;
    }

    public Integer getmTelefono() {
        return mTelefono;
    }

    public String getmDireccion() {
        return mDireccion;
    }

    public String getmToken() {
        return mToken;
    }

    public String getmDispositivo() {
        return mDispositivo;
    }
}
