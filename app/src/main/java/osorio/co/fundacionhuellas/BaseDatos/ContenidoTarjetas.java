package osorio.co.fundacionhuellas.BaseDatos;

/**
 * Created by Juan Sebastian on 26/03/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContenidoTarjetas {
    @SerializedName("tarid")
    @Expose
    private Integer tar_id;
    @SerializedName("tarnombre")
    @Expose
    private String tar_nombre;
    @SerializedName("tardescripcion")
    @Expose
    private String tar_descripcion;
    @SerializedName("tarimagen")
    @Expose
    private  String tar_imagen;
    @SerializedName("tartipo")
    @Expose
    private String tar_tipo;
    @SerializedName("tarestado")
    @Expose
    private String tar_estado;
    @SerializedName("tarlikes")
    @Expose
    private String tar_like;

    public Integer getTar_id() {
        return tar_id;
    }

    public void setTar_id(Integer tar_id) {
        this.tar_id = tar_id;
    }

    public String getTar_nombre() {
        return tar_nombre;
    }

    public void setTar_nombre(String tar_nombre) {
        this.tar_nombre = tar_nombre;
    }

    public String getTar_descripcion() {
        return tar_descripcion;
    }

    public void setTar_descripcion(String tar_descripcion) {
        this.tar_descripcion = tar_descripcion;
    }

    public String getTar_imagen() {
        return tar_imagen;
    }

    public void setTar_imagen(String tar_imagen) {
        this.tar_imagen = tar_imagen;
    }

    public String getTar_tipo() {
        return tar_tipo;
    }

    public void setTar_tipo(String tar_tipo) {
        this.tar_tipo = tar_tipo;
    }

    public String getTar_estado() {
        return tar_estado;
    }

    public void setTar_estado(String tar_estado) {
        this.tar_estado = tar_estado;
    }

    public String getTar_like() {
        return tar_like;
    }

    public void setTar_like(String tar_like) {
        this.tar_like = tar_like;
    }
}
