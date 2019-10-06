package application.resources.model;

public class FacturaDetalle {
	private String codigo, descripcion, tarifa;

	private double cantidad;
	private double precioUnit, precioCaja, total;
	
	public FacturaDetalle() {}
	
	public FacturaDetalle(String codigo, String descripcion, String tarifa, double cantidad, double precioUnit, double precioCaja,
			double total) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.tarifa = tarifa;
		this.cantidad = cantidad;
		this.precioUnit = precioUnit;
		this.precioCaja = precioCaja;
		this.total = total;
	}
	
	public String getTarifa() {
		return tarifa;
	}
	
	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
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

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnit() {
		return precioUnit;
	}

	public void setPrecioUnit(double precioUnit) {
		this.precioUnit = precioUnit;
	}

	public double getPrecioCaja() {
		return precioCaja;
	}

	public void setPrecioCaja(double precioCaja) {
		this.precioCaja = precioCaja;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "FacturaDetalle [codigo=" + codigo + ", descripcion=" + descripcion + ", tarifa=" + tarifa
				+ ", cantidad=" + cantidad + ", precioUnit=" + precioUnit + ", precioCaja=" + precioCaja + ", total="
				+ total + "]";
	}

	
	
	
	
}
