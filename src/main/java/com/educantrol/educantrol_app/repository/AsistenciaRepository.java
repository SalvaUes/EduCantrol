package main.java.com.educantrol.educantrol_app.repository;

import main.java.com.educantrol.educantrol_app.model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

}