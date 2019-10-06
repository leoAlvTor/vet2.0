package application.resources.model;

public class RegistroMedico {
	private int id;
	private String fecha,  hora, next_fecha, tipo, descripcion;
	private int paciente_id;
	
	public RegistroMedico() {}
	
	public RegistroMedico(int id, String fecha, String hora, String next_fecha, String tipo, String descripcion,
			int paciente_id) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.hora = hora;
		this.next_fecha = next_fecha;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.paciente_id = paciente_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getNext_fecha() {
		return next_fecha;
	}

	public void setNext_fecha(String next_fecha) {
		this.next_fecha = next_fecha;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(int paciente_id) {
		this.paciente_id = paciente_id;
	}

	@Override
	public String toString() {
		return "RegistroMedico [id=" + id + ", fecha=" + fecha + ", hora=" + hora + ", next_fecha=" + next_fecha
				+ ", tipo=" + tipo + ", descripcion=" + descripcion + ", paciente_id=" + paciente_id + "]";
	}
	
	
	
	
	
}
