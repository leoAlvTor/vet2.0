package application.resources.model;

public class DeudaCabecera {

    private int id;
    private String ruc;
    private int id_factura;
    private String fecha_inicial;
    private double monto_original;
    private double monto_cancelado;
    private String estado;
    private String especial_fecha;

    public DeudaCabecera() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEspecial_fecha() {
        return especial_fecha;
    }

    public void setEspecial_fecha(String especial_fecha) {
        this.especial_fecha = especial_fecha;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public String getFecha_inicial() {
        return fecha_inicial;
    }

    public void setFecha_inicial(String fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public double getMonto_original() {
        return monto_original;
    }

    public void setMonto_original(double monto_original) {
        this.monto_original = monto_original;
    }

    public double getMonto_cancelado() {
        return monto_cancelado;
    }

    public void setMonto_cancelado(double monto_cancelado) {
        this.monto_cancelado = monto_cancelado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "DeudaCabecera{" +
                "id=" + id +
                ", ruc='" + ruc + '\'' +
                ", id_factura=" + id_factura +
                ", fecha_inicial='" + fecha_inicial + '\'' +
                ", monto_original=" + monto_original +
                ", monto_cancelado=" + monto_cancelado +
                ", estado='" + estado + '\'' +
                '}';
    }
}
