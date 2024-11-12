package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.ExpedienteAcademico;
import com.educantrol.educantrol_app.model.ExpedienteAcademicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpedienteAcademicoService {
    private final ExpedienteAcademicoRepository expedienteRepository;

    @Autowired
    public ExpedienteAcademicoService(ExpedienteAcademicoRepository expedienteRepository) {
        this.expedienteRepository = expedienteRepository;
    }

    public List<ExpedienteAcademico> obtenerTodos() {
        return expedienteRepository.findAll();
    }

    public Optional<ExpedienteAcademico> obtenerPorId(Long id) {
        return expedienteRepository.findById(id);
    }

    public ExpedienteAcademico guardarExpediente(ExpedienteAcademico expediente) {
        return expedienteRepository.save(expediente);
    }

    public void eliminarExpediente(Long id) {
        expedienteRepository.deleteById(id);
    }

    // Métodos adicionales de negocio si son necesarios
}