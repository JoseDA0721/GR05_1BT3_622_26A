package logic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DatosEstudiante")
public class DatosEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String cedula;
    private String nombre;
    private String apellido;
    private String edad;
    private String carrera;
    private String usuario;
    private String contrasena;

    public DatosEstudiante(){

    }

    public DatosEstudiante(int id, String cedula, String nombre, String apellido, String edad, String carrera, String usuario, String contrasena){
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.carrera = carrera;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public int getId(){

        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCedula() {

        return cedula;
    }

    public void setCedula(String cedula){

        this.cedula = cedula;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre){

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(String apellido){

        this.apellido = apellido;
    }

    public String getEdad() {

        return edad;
    }

    public void setEdad(String edad){

        this.edad = edad;
    }

    public String getCarrera() {

        return carrera;
    }

    public void setCarrera(String carrera){

        this.carrera = carrera;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getContrasena(){
        return contrasena;
    }

    public void setContrasena(String contrasena){
        this.contrasena = contrasena;
    }

}
