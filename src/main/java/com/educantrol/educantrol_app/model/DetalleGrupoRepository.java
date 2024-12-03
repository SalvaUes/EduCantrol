package com.educantrol.educantrol_app.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleGrupoRepository extends JpaRepository<DetalleGrupo, Integer> {
    Optional<DetalleGrupo> findByGrupoAndEstudiante(Grupo grupo, Estudiante estudiante);

    @Query("SELECT COUNT(dg) FROM DetalleGrupo dg WHERE dg.grupo = :grupo")
    long countByGrupo(@Param("grupo") Grupo grupo);
}