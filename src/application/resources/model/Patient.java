package application.resources.model;

public class Patient {
    private int id;
    private String nombre, clase, genero, direccion, fecha_nac, raza, pelaje, prop_ci, ciudad;
    private String edad;

    public Patient() {}
    
    public Patient(int id, String nombre, String clase, String genero, String direccion, String fecha_nac, String raza,
                   String pelaje, String prop_ci, String ciudad, String edad) {
        this.id = id;
        this.nombre = nombre;
        this.clase = clase;
        this.genero = genero;
        this.direccion = direccion;
        this.fecha_nac = fecha_nac;
        this.raza = raza;
        this.pelaje = pelaje;
        this.prop_ci = prop_ci;
        this.ciudad = ciudad;
        this.edad = edad;
    }
    
    

    public void setId(int id) {
		this.id = id;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public void setClase(String clase) {
		this.clase = clase;
	}



	public void setGenero(String genero) {
		this.genero = genero;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}



	public void setRaza(String raza) {
		this.raza = raza;
	}



	public void setPelaje(String pelaje) {
		this.pelaje = pelaje;
	}



	public void setProp_ci(String prop_ci) {
		this.prop_ci = prop_ci;
	}
	
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}



	public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClase() {
        return clase;
    }

    public String getGenero() {
        return genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public String getRaza() {
        return raza;
    }

    public String getPelaje() {
        return pelaje;
    }

    public String getProp_ci() {
        return prop_ci;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", clase='" + clase + '\'' +
                ", genero='" + genero + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fecha_nac='" + fecha_nac + '\'' +
                ", raza='" + raza + '\'' +
                ", pelaje='" + pelaje + '\'' +
                ", prop_ci='" + prop_ci + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", edad='" + edad + '\'' +
                '}';
    }
}
