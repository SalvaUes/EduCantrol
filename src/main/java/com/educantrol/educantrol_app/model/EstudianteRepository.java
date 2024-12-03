package com.educantrol.educantrol_app.model;

import com.educantrol.educantrol_app.model.Estudiante;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // Buscar estudiantes asociados a un grupo
    @Query("SELECT e FROM Estudiante e JOIN DetalleGrupo dg ON e.idEstudiante = dg.estudiante.idEstudiante WHERE dg.grupo.idGrupo = :idGrupo")
    List<Estudiante> findEstudiantesAsociadosAlGrupo(long idGrupo);

    // Buscar estudiantes no asociados a un grupo
    @Query("SELECT e FROM Estudiante e WHERE e.idEstudiante NOT IN (SELECT dg.estudiante.idEstudiante FROM DetalleGrupo dg WHERE dg.grupo.idGrupo = :idGrupo)")
    List<Estudiante> findEstudiantesNoAsociadosAlGrupo(long idGrupo);

}