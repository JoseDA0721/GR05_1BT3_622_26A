package logic;

import jakarta.persistence.*;

@Entity
@Table(name = "Curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id_curso")
    private int Id_curso;

    private String nombre_curso;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private DatosEstudiante id;

    public Curso() {
    }

    public Curso(int id_curso, String nombre_curso, DatosEstudiante id) {
        this.Id_curso = Id_curso;
        this.nombre_curso = nombre_curso;
        this.id = id;
    }

    public int getId_curso() {
        return Id_curso;
    }

    public void setId_curso(int id_curso) {
        Id_curso = id_curso;
    }

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }

    public int getId() {
        return id.getId();
    }

    public void setId(DatosEstudiante id) {
        this.id = id;
    }
}
