package com.educantrol.educantrol_app.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleAsistenciaRepository extends JpaRepository<DetalleAsistencia, Integer> {
    @Query("SELECT d FROM DetalleAsistencia d WHERE d.asistencia.idAsistencia = :idAsistencia")
    List<DetalleAsistencia> findAllByAsistenciaId(@Param("idAsistencia") Integer idAsistencia);




}
