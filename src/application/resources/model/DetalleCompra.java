package application.resources.model;

public class DetalleCompra {
    String prod_id;
    double cantidad, p_unit, v_total, pvp_unit, p_caja, v_total_caja, pvp_caja;
    String tarifa, fecha_vencimiento;

    public DetalleCompra() {
    }

    @Override
    public String toString() {
        return "DetalleCompra{" +
                "prod_id='" + prod_id + '\'' +
                ", cantidad=" + cantidad +
                ", p_unit=" + p_unit +
                ", v_total=" + v_total +
                ", pvp_unit=" + pvp_unit +
                ", p_caja=" + p_caja +
                ", v_total_caja=" + v_total_caja +
                ", pvp_caja=" + pvp_caja +
                ", tarifa='" + tarifa + '\'' +
                ", fecha_vencimiento='" + fecha_vencimiento + '\'' +
                '}';
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
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

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }
}
