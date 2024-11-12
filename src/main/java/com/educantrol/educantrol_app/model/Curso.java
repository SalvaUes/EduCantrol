package com.educantrol.educantrol_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Curso {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column
private Long id;
@Column
private String nombre;
@Column
private String descripcion;
@Column
private int creditos;

public Curso() {}

public Curso(String nombre, String descripcion, int creditos) {
this.nombre = nombre;
this.descripcion = descripcion;
this.creditos = creditos;
}

        public Long getId() {
        return id;
        }
        public void setId(Long id) {
        this.id = id;
        }
        public String getNombre() {
        return nombre;
        }
        public void setNombre(String nombre) {
        this.nombre = nombre;
        }
        public String getDescripcion() {
        return descripcion;
        }
        public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        }
        public int getCreditos() {
        return creditos;
        }
        public void setCreditos(int creditos) {
        this.creditos = creditos;
        }
        }

