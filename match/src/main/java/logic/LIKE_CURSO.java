package logic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "LikeCurso")
@Table(
        name = "LIKE_CURSO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_estudiante", "id_curso"})
)
public class LIKE_CURSO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id_like;

    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private DatosEstudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    public LIKE_CURSO() {
    }

    public LIKE_CURSO(int id_like, DatosEstudiante estudiante, Curso curso){
        this.id_like = id_like;
        this.estudiante = estudiante;
        this.curso = curso;
    }

    public int getId_like() {
        return id_like;
    }

    public void setId_like(int id_like) {
        this.id_like = id_like;
    }

    public int getId() {
        return estudiante.getId();
    }

    public void setId(DatosEstudiante id) {
        this.estudiante = id;
    }

    public int getId_curso() {
        return curso.getId_curso();
    }

    public void setId_curso(Curso id_curso) {
        this.curso = id_curso;
    }
}
