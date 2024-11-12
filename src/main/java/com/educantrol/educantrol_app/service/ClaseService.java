package com.educantrol.educantrol_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.educantrol.educantrol_app.model.Clase;
import com.educantrol.educantrol_app.repository.ClaseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {


    private final ClaseRepository claseRepository;


    
    @Autowired


    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    // Obtener todas las clases
    public List<Clase> getAllClases() {
        return claseRepository.findAll();
    }


    public Optional<Clase> getClaseById(Long id) {
        return claseRepository.findById(id);
    }

    
    public Clase createClase(Clase clase) {
        return claseRepository.save(clase);
    }

    
    public Clase updateClase(Long id, Clase claseDetails) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id " + id));

        clase.setNombre(claseDetails.getNombre());
        clase.setDescripcion(claseDetails.getDescripcion());
        clase.setFecha(claseDetails.getFecha());

        return claseRepository.save(clase);
    }

    
    public void deleteClase(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id " + id));
        claseRepository.delete(clase);
    }

    
}
