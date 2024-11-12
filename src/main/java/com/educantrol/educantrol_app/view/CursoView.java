package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Curso;
import com.educantrol.educantrol_app.model.CursoRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("cursos")
public class CursoView extends VerticalLayout {

    private final CursoRepository cursoRepository;
    private final Grid<Curso> grid;
    private final TextField nombre = new TextField("Nombre");
    private final TextField descripcion = new TextField("Descripción");
    private final TextField creditos = new TextField("Créditos");
    private final Button guardarBtn = new Button("Guardar");

    public CursoView(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
        this.grid = new Grid<>(Curso.class);

        // Configuración del grid
        grid.setColumns("id", "nombre", "descripcion", "creditos");
        actualizarLista();

        // Layout para el formulario
        HorizontalLayout formulario = new HorizontalLayout(nombre, descripcion, creditos, guardarBtn);

        // Acción del botón de guardar
        guardarBtn.addClickListener(e -> guardarCurso());

        add(formulario, grid);
    }

    private void guardarCurso() {
        // Validación de campos
        if (nombre.isEmpty() || descripcion.isEmpty() || creditos.isEmpty()) {
            Notification.show("Por favor, completa todos los campos.");
            return;
        }

        try {
            int creditosValue = Integer.parseInt(creditos.getValue());
            Curso curso = new Curso(nombre.getValue(), descripcion.getValue(), creditosValue);
            cursoRepository.save(curso);
            Notification.show("Curso guardado exitosamente.");
            actualizarLista();
        } catch (NumberFormatException ex) {
            Notification.show("El valor de créditos debe ser un número.");
        }
    }

    private void actualizarLista() {
        grid.setItems(cursoRepository.findAll());
    }
}
