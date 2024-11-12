package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Profesor;
import com.educantrol.educantrol_app.service.ProfesorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("profesores")
public class ProfesorView extends VerticalLayout {

    private final ProfesorService profesorService;
    private final Grid<Profesor> grid = new Grid<>(Profesor.class);
    private final TextField nombre = new TextField("Nombre");
    private final TextField apellido = new TextField("Apellido");
    private final TextField especialidad = new TextField("Especialidad");
    private final Button save = new Button("Guardar");

    @Autowired
    public ProfesorView(ProfesorService profesorService) {
        this.profesorService = profesorService;

        save.addClickListener(event -> {
            Profesor profesor = new Profesor();
            profesor.setNombre(nombre.getValue());
            profesor.setApellido(apellido.getValue());
            profesor.setEspecialidad(especialidad.getValue());
            profesorService.save(profesor);
            updateGrid();
        });

        add(nombre, apellido, especialidad, save, grid);
        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(profesorService.findAll());
    }
}


