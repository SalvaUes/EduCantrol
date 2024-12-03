package com.educantrol.educantrol_app.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;

    @Column
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materia materia;
    @ManyToOne
    @JoinColumn(name = "idHorario", nullable = false)
    private Horario horario;
    @ManyToOne
    @JoinColumn(name = "idProfesor", nullable = false)
    private Profesor profesor;

    public Long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }


}
