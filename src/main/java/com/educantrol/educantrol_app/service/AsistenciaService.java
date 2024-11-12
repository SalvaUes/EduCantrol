package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.Asistencia;
import com.educantrol.educantrol_app.model.AsistenciaRepository;
import com.vaadin.flow.data.provider.DataProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    @Autowired
    public AsistenciaService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    public List<Asistencia> obtenerAsistencias() {
        return asistenciaRepository.findAll();
    }

    public Optional<Asistencia> obtenerAsistenciaPorId(Long id) {
        return asistenciaRepository.findById(id);
    }

    public Asistencia guardarAsistencia(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    public void eliminarAsistencia(Long id) {
        asistenciaRepository.deleteById(id);
    }

    public DataProvider<Asistencia, Void> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    public void save(Asistencia asistenciaSeleccionada) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    public void delete(Asistencia asistenciaSeleccionada) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    
}