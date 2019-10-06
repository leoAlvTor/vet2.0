package application.resources.model;

public class CabeceraCompra {
    String compra_autorizacion, compra_numero, compra_fecha, compra_for_pago, prov_ruc;
    int compra_dias;
    double compra_pago_inicial, compra_subtotal12, compra_subtotal0, compra_iva, compra_ice, compra_irbp, compra_total;

    public CabeceraCompra(String compra_autorizacion, String compra_numero, String compra_fecha,
                          String compra_for_pago, String prov_ruc, int compra_dias, double compra_pago_inicial,
                          double compra_subtotal12, double compra_subtotal0, double compra_iva, double compra_ice,
                          double compra_irbp, double compra_total) {
        this.compra_autorizacion = compra_autorizacion;
        this.compra_numero = compra_numero;
        this.compra_fecha = compra_fecha;
        this.compra_for_pago = compra_for_pago;
        this.prov_ruc = prov_ruc;
        this.compra_dias = compra_dias;
        this.compra_pago_inicial = compra_pago_inicial;
        this.compra_subtotal12 = compra_subtotal12;
        this.compra_subtotal0 = compra_subtotal0;
        this.compra_iva = compra_iva;
        this.compra_ice = compra_ice;
        this.compra_irbp = compra_irbp;
        this.compra_total = compra_total;
    }

    public CabeceraCompra() {}

    @Override
    public String toString() {
        return "CabeceraCompra{" +
                "compra_autorizacion='" + compra_autorizacion + '\'' +
                ", compra_numero='" + compra_numero + '\'' +
                ", compra_fecha='" + compra_fecha + '\'' +
                ", compra_for_pago='" + compra_for_pago + '\'' +
                ", prov_ruc='" + prov_ruc + '\'' +
                ", compra_dias=" + compra_dias +
                ", compra_pago_inicial=" + compra_pago_inicial +
                ", compra_subtotal12=" + compra_subtotal12 +
                ", compra_subtotal0=" + compra_subtotal0 +
                ", compra_iva=" + compra_iva +
                ", compra_ice=" + compra_ice +
                ", compra_irbp=" + compra_irbp +
                ", compra_total=" + compra_total +
                '}';
    }

    public String getCompra_autorizacion() {
        return compra_autorizacion;
    }

    public void setCompra_autorizacion(String compra_autorizacion) {
        this.compra_autorizacion = compra_autorizacion;
    }

    public String getCompra_numero() {
        return compra_numero;
    }

    public void setCompra_numero(String compra_numero) {
        this.compra_numero = compra_numero;
    }

    public String getCompra_fecha() {
        return compra_fecha;
    }

    public void setCompra_fecha(String compra_fecha) {
        this.compra_fecha = compra_fecha;
    }

    public String getCompra_for_pago() {
        return compra_for_pago;
    }

    public void setCompra_for_pago(String compra_for_pago) {
        this.compra_for_pago = compra_for_pago;
    }

    public String getProv_ruc() {
        return prov_ruc;
    }

    public void setProv_ruc(String prov_ruc) {
        this.prov_ruc = prov_ruc;
    }

    public int getCompra_dias() {
        return compra_dias;
    }

    public void setCompra_dias(int compra_dias) {
        this.compra_dias = compra_dias;
    }

    public double getCompra_pago_inicial() {
        return compra_pago_inicial;
    }

    public void setCompra_pago_inicial(double compra_pago_inicial) {
        this.compra_pago_inicial = compra_pago_inicial;
    }

    public double getCompra_subtotal12() {
        return compra_subtotal12;
    }

    public void setCompra_subtotal12(double compra_subtotal12) {
        this.compra_subtotal12 = compra_subtotal12;
    }

    public double getCompra_subtotal0() {
        return compra_subtotal0;
    }

    public void setCompra_subtotal0(double compra_subtotal0) {
        this.compra_subtotal0 = compra_subtotal0;
    }

    public double getCompra_iva() {
        return compra_iva;
    }

    public void setCompra_iva(double compra_iva) {
        this.compra_iva = compra_iva;
    }

    public double getCompra_ice() {
        return compra_ice;
    }

    public void setCompra_ice(double compra_ice) {
        this.compra_ice = compra_ice;
    }

    public double getCompra_irbp() {
        return compra_irbp;
    }

    public void setCompra_irbp(double compra_irbp) {
        this.compra_irbp = compra_irbp;
    }

    public double getCompra_total() {
        return compra_total;
    }

    public void setCompra_total(double compra_total) {
        this.compra_total = compra_total;
    }
}
