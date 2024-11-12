package com.educantrol.educantrol_app.model;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
}
