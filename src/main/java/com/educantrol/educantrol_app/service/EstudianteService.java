package com.educantrol.educantrol_app.service;


import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.model.EstudianteRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

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
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            try (InputStream inputStream = buffer.getInputStream()) {
                // Asignar los bytes de la foto al estudiante
                estudiante.setFoto(inputStream.readAllBytes()); // Aquí se asignan los datos de la foto
            } catch (IOException e) {
                Notification.show("Error al cargar la foto: " + e.getMessage());
            }
        });
        return estudianteRepository.save(estudiante);
    }

    // Método para eliminar un estudiante por su ID
    public void deleteById(Long id) {
        estudianteRepository.deleteById(id);
    }
    

    public List<Estudiante> findEstudianteNoAsociadosAlGrupo(long idGrupo) {
        return estudianteRepository.findEstudiantesNoAsociadosAlGrupo(idGrupo);
    }
    public List<Estudiante> findEstudianteAsociadosAlGrupo(long idGrupo) {
        return estudianteRepository.findEstudiantesAsociadosAlGrupo(idGrupo);
    }




}
