package application.resources.model;

public class Customers {
    private String ci, nombre, telefono, direccion, email;
    
    public Customers() {}
    
    public Customers(String ci, String nombre, String telefono, String direccion, String email){

        this.ci = ci;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;

    }
    
    

    public void setCi(String ci) {
		this.ci = ci;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getCi() {
        return ci;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "ci='" + ci + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
