package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.educantrol.educantrol_app.model.Materia;
import com.educantrol.educantrol_app.model.MateriaRepository;

@Service
public class MateriaService {
    @Autowired
    private MateriaRepository materiaRepository;

    // Método para obtener todos las materias
    public List<Materia> findAll() {
        return materiaRepository.findAll();
    }

    // Método para obtener un materia por su ID
    public Optional<Materia> obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id);
    }

    // Método para crear o actualizar un materia
    public Materia save(Materia materia) {
        return materiaRepository.save(materia);
    }

    // Método para eliminar un materia por su ID
    public void deleteById(Long id) {
        materiaRepository.deleteById(id);
    }



    
}