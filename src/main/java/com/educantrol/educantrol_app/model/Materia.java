package com.educantrol.educantrol_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Materia {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idMateria;
        @Column
        private String nombre;
        @Column
        private String descripcion;
        @Column
        private int creditos;

        public Materia() {}



        public Long getIdMateria() {
        return idMateria;
        }
        public void setIdMateria(Long idMateria) {
        this.idMateria = idMateria;
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

