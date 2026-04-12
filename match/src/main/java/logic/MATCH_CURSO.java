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

@Entity(name = "MatchCurso")
@Table(
        name = "MATCH_CURSO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_curso", "id_estudiante"})
)
public class MATCH_CURSO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_match")
    private int idMatch;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private DatosEstudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_creador", nullable = false)
    private DatosEstudiante creador;

    public MATCH_CURSO() {
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public DatosEstudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(DatosEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public DatosEstudiante getCreador() {
        return creador;
    }

    public void setCreador(DatosEstudiante creador) {
        this.creador = creador;
    }
}

