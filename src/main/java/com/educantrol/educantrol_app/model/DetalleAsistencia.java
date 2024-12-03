package com.educantrol.educantrol_app.model;


import jakarta.persistence.*;

@Entity
public class DetalleAsistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  idDetalleAsistencia;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "id_asistencia", nullable = false)
    private Asistencia asistencia;

    private boolean asistenciaPresente;

    public Integer getIdDetalleAsistencia() {
        return idDetalleAsistencia;
    }

    public void setIdDetalleAsistencia(Integer idDetalleAsistencia) {
        this.idDetalleAsistencia = idDetalleAsistencia;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    public boolean isAsistenciaPresente() {
        return asistenciaPresente;
    }

    public Boolean getAsistenciaPresente() {
        return asistenciaPresente;
    }

    public void setAsistenciaPresente(boolean asistenciaPresente) {
        this.asistenciaPresente = asistenciaPresente;
    }
}
