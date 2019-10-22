package application.resources.model;

public class DeudaDetalle {

    private int id;
    private int deduda_id;
    private double monto_cancelado;
    private String fecha;

    public DeudaDetalle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeduda_id() {
        return deduda_id;
    }

    public void setDeduda_id(int deduda_id) {
        this.deduda_id = deduda_id;
    }

    public double getMonto_cancelado() {
        return monto_cancelado;
    }

    public void setMonto_cancelado(double monto_cancelado) {
        this.monto_cancelado = monto_cancelado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "DeudaDetalle{" +
                "id=" + id +
                ", deduda_id=" + deduda_id +
                ", monto_cancelado=" + monto_cancelado +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
