package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.DetalleGrupo;
import com.educantrol.educantrol_app.model.DetalleGrupoRepository;
import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.model.Grupo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleGrupoService {

    private final DetalleGrupoRepository detalleGrupoRepository;

    public DetalleGrupoService(DetalleGrupoRepository detalleGrupoRepository) {
        this.detalleGrupoRepository = detalleGrupoRepository;
    }

    public List<DetalleGrupo> findAll() {
        return detalleGrupoRepository.findAll();
    }

    public DetalleGrupo save(DetalleGrupo detalleGrupo) {
        return detalleGrupoRepository.save(detalleGrupo);
    }

    public void deleteById(Integer idDetalleGrupo) {
        detalleGrupoRepository.deleteById(idDetalleGrupo);
    }

    public DetalleGrupo findById(Integer idDetalleGrupo) {
        return detalleGrupoRepository.findById(idDetalleGrupo).orElse(null);
    }

    public void delete(DetalleGrupo detalleGrupo) {
        detalleGrupoRepository.delete(detalleGrupo);
    }

    public DetalleGrupo findByGrupoAndEstudiante(Grupo grupo, Estudiante estudiante) {
        return detalleGrupoRepository.findByGrupoAndEstudiante(grupo, estudiante)
                .orElse(null);
    }

    public long countByGrupo(Grupo grupo) {
        return detalleGrupoRepository.countByGrupo(grupo);
    }
}

