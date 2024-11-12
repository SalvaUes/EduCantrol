package com.educantrol.educantrol_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.model.EstudianteRepository;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    // Inyección de dependencia a través del constructor
    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    // Método para obtener todos los estudiantes
    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    // Método para guardar un estudiante
    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    // Método para eliminar un estudiante por su ID
    public void deleteById(Long idEstudiante) {
        estudianteRepository.deleteById(idEstudiante);
    }
}
