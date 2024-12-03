package com.educantrol.educantrol_app.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistencia;

    @ManyToOne
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(mappedBy = "asistencia", cascade = CascadeType.ALL)
    private List<DetalleAsistencia> detalles;

    // Getters y Setters
    public int getIdAsistencia() {
        return idAsistencia;
    }

    public void setIdAsistencia(int idAsistencia) {
        this.idAsistencia = idAsistencia;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<DetalleAsistencia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleAsistencia> detalles) {
        this.detalles = detalles;
    }
}

