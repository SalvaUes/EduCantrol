package com.educantrol.educantrol_app.model;


import jakarta.persistence.*;

@Entity
@Table(name = "detalle_grupo")
public class DetalleGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleGrupo;

    @ManyToOne
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    // Getters y Setters
    public Integer getIdDetalleGrupo() {
        return idDetalleGrupo;
    }

    public void setIdDetalleGrupo(Integer idDetalleGrupo) {
        this.idDetalleGrupo = idDetalleGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}

