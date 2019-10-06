package application.resources.model;

public class Employee {
    private int id;
    private String nombre, titulo, email, telefono, direccion;

    public Employee() {}
    
    public Employee(int id, String nombre, String titulo, String email, String telefono, String direccion){
        this.id = id;
        this.nombre = nombre;
        this.titulo = titulo;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", titulo='" + titulo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
