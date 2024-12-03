package com.educantrol.educantrol_app.service;

import com.educantrol.educantrol_app.model.Grupo;
import com.educantrol.educantrol_app.model.GrupoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GrupoService {
    @Autowired
    private GrupoRepository grupoRepository;

    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }

    public Grupo save(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public void deleteById(Long id) {
        grupoRepository.deleteById(id);
    }
}
