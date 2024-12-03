package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.DetalleAsistencia;
import com.educantrol.educantrol_app.model.DetalleAsistenciaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleAsistenciaService {

    private final DetalleAsistenciaRepository detalleAsistenciaRepository;

    public DetalleAsistenciaService(DetalleAsistenciaRepository detalleAsistenciaRepository) {
        this.detalleAsistenciaRepository = detalleAsistenciaRepository;
    }

    public void saveAll(List<DetalleAsistencia> detalles) {
        if (detalles != null && !detalles.isEmpty()) {
            List<DetalleAsistencia> registrosExistentes = detalleAsistenciaRepository.findAllById(
                    detalles.stream().map(DetalleAsistencia::getIdDetalleAsistencia).toList()
            );

            List<DetalleAsistencia> registrosParaGuardar = new ArrayList<>();

            for (DetalleAsistencia detalle : detalles) {
                DetalleAsistencia existente = registrosExistentes.stream()
                        .filter(r -> r.getIdDetalleAsistencia() != null &&
                                r.getIdDetalleAsistencia().equals(detalle.getIdDetalleAsistencia()))
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    // Registro nuevo
                    registrosParaGuardar.add(detalle);
                } else if (!detalle.equals(existente)) {
                    // Registro modificado
                    registrosParaGuardar.add(detalle);
                }
            }


            if (!registrosParaGuardar.isEmpty()) {
                detalleAsistenciaRepository.saveAll(registrosParaGuardar);
            }
        }
    }

    public void saveAllByAsistencia(List<DetalleAsistencia> detalles, Integer idAsistencia) {
        if (detalles != null && !detalles.isEmpty()) {
            // Obtener los registros existentes de la base de datos por idAsistencia
            List<DetalleAsistencia> registrosExistentes = detalleAsistenciaRepository.findAllByAsistenciaId(idAsistencia);

            List<DetalleAsistencia> registrosParaGuardar = new ArrayList<>();

            for (DetalleAsistencia detalle : detalles) {
                DetalleAsistencia existente = registrosExistentes.stream()
                        .filter(r -> r.getEstudiante().getidEstudiante().equals(detalle.getEstudiante().getidEstudiante()))
                        .findFirst()
                        .orElse(null);

                if (existente == null) {
                    // Si no existe, se trata de un registro nuevo, agregarlo a la lista
                    registrosParaGuardar.add(detalle);
                } else if (!detalle.equals(existente)) {
                    // Si existe pero tiene diferencias, actualizar los valores
                    existente.setAsistenciaPresente(detalle.getAsistenciaPresente());
                    // Agrega más campos si hay otros detalles que pueden diferir
                    registrosParaGuardar.add(existente); // Agregar el registro modificado
                }
            }

            if (!registrosParaGuardar.isEmpty()) {
                detalleAsistenciaRepository.saveAll(registrosParaGuardar);
            }
        }
    }



    public List<DetalleAsistencia> findAll() {
        return detalleAsistenciaRepository.findAll();
    }

    public DetalleAsistencia save(DetalleAsistencia detalleAsistencia) {
        return detalleAsistenciaRepository.save(detalleAsistencia);
    }

    public void deleteById(int id) {
        detalleAsistenciaRepository.deleteById(id);
    }

    public List<DetalleAsistencia> findAllByAsistencia(Integer idAsistencia) {
        return detalleAsistenciaRepository.findAllByAsistenciaId(idAsistencia);
    }
}

