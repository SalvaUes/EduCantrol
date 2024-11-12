package com.educantrol.educantrol_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.educantrol.educantrol_app.model.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    // Puedes añadir métodos de consulta personalizados si es necesario
}