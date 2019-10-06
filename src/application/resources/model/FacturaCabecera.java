package application.resources.model;

public class FacturaCabecera {
	private String ruc, nombre, direccion, telefono, fecha;
	private String numeroFactura;
	private String tipoCompra;
	public boolean anulada;
	
	private double subtotal, iva, descuento, total;

	public FacturaCabecera() {}
	
	public FacturaCabecera(String ruc, String nombre, String direccion, String telefono, String fecha,
			String numeroFactura, String tipoCompra, double subtotal, double iva, double descuento, double total) {
		super();
		this.ruc = ruc;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.fecha = fecha;
		this.numeroFactura = numeroFactura;
		this.tipoCompra = tipoCompra;
		this.subtotal = subtotal;
		this.iva = iva;
		this.descuento = descuento;
		this.total = total;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "FacturaCabecera [ruc=" + ruc + ", nombre=" + nombre + ", direccion=" + direccion + ", telefono="
				+ telefono + ", fecha=" + fecha + ", numeroFactura=" + numeroFactura + ", tipoCompra=" + tipoCompra
				+ ", subtotal=" + subtotal + ", iva=" + iva + ", descuento=" + descuento + ", total=" + total + "]";
	}
	
	
	
	
}
