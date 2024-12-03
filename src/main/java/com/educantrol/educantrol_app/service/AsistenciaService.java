package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.Asistencia;
import com.educantrol.educantrol_app.model.AsistenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public List<Asistencia> findAll() {
        return asistenciaRepository.findAll();
    }

    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    public void deleteById(int id) {
        asistenciaRepository.deleteById(id);
    }
}

