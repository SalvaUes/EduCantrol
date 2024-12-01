package com.educantrol.educantrol_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.educantrol.educantrol_app.model.Periodo;
import com.educantrol.educantrol_app.model.PeriodoRepository;

@Service
public class PeriodoService {

    @Autowired
    private PeriodoRepository periodoRepository;

    // Método para obtener todos los periodos
    public List<Periodo> obtenerTodosLosPeriodos() {
        return periodoRepository.findAll();
    }

    // Método para obtener un periodo por su ID
    public Optional<Periodo> obtenerPeriodoPorId(Long id) {
        return periodoRepository.findById(id);
    }

    // Método para crear o actualizar un periodo
    public Periodo guardarPeriodo(Periodo periodo) {
        return periodoRepository.save(periodo);
    }

    // Método para eliminar un periodo por su ID
    public void eliminarPeriodo(Long id) {
        periodoRepository.deleteById(id);
    }
}
