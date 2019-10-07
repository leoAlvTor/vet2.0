package application.resources.model;

public class ModelDetailPurchase {
    String codigo, descripcion;
    int cantidad;
    double p_unit, v_total, pvp_unit;
    String tarifa;
    double p_caja, v_total_caja, pvp_caja;
    String fecha_vencimiento;

    public ModelDetailPurchase(){}

    @Override
    public String toString() {
        return "ModelDetailPurchase{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", p_unit=" + p_unit +
                ", v_total=" + v_total +
                ", pvp_unit=" + pvp_unit +
                ", tarifa='" + tarifa + '\'' +
                ", p_caja=" + p_caja +
                ", v_total_caja=" + v_total_caja +
                ", pvp_caja=" + pvp_caja +
                ", fecha_vencimiento='" + fecha_vencimiento + '\'' +
                '}';
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getP_unit() {
        return p_unit;
    }

    public void setP_unit(double p_unit) {
        this.p_unit = p_unit;
    }

    public double getV_total() {
        return v_total;
    }

    public void setV_total(double v_total) {
        this.v_total = v_total;
    }

    public double getPvp_unit() {
        return pvp_unit;
    }

    public void setPvp_unit(double pvp_unit) {
        this.pvp_unit = pvp_unit;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public double getP_caja() {
        return p_caja;
    }

    public void setP_caja(double p_caja) {
        this.p_caja = p_caja;
    }

    public double getV_total_caja() {
        return v_total_caja;
    }

    public void setV_total_caja(double v_total_caja) {
        this.v_total_caja = v_total_caja;
    }

    public double getPvp_caja() {
        return pvp_caja;
    }

    public void setPvp_caja(double pvp_caja) {
        this.pvp_caja = pvp_caja;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }
}
