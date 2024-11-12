package com.educantrol.educantrol_app.repository;

import com.educantrol.educantrol_app.model.ExpedienteAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteAcademicoRepository extends JpaRepository<ExpedienteAcademico, Long> {
}
