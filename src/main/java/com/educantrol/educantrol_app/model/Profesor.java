package com.educantrol.educantrol_app.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Profesor {

@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)

@Column
private Long id;
@Column
private String nombre;
@Column
private String apellido;
@Column
private String especialidad;
@Column
private String email;


// getters y setters
public Long getId(){
    return id;
}

public void setId(long id) {
    this.id = id;
}

public String getNombre(){
    return nombre;
}
public void setNombre(String nombre){
this.nombre = nombre;
}

public String getApellido() {
    return apellido;
}

public void setApellido(String apellido) {
    this.apellido = apellido;
}

public String getEspecialidad() {
    return especialidad;
}

public void setEspecialidad(String especialidad) {
    this.especialidad = especialidad;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}


}
