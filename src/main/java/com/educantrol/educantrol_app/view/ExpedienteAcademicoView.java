package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.ExpedienteAcademico;
import com.educantrol.educantrol_app.service.ExpedienteAcademicoService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Date;

@Route("expedientes")
public class ExpedienteAcademicoView extends VerticalLayout {
    private final ExpedienteAcademicoService expedienteService;

    private Grid<ExpedienteAcademico> grid = new Grid<>(ExpedienteAcademico.class);
    private TextField estudianteId = new TextField("ID de Estudiante");
    private TextField asignatura = new TextField("Asignatura");
    private TextField periodo = new TextField("Periodo");
    private NumberField calificacion = new NumberField("Calificación");

    private Button guardar = new Button("Guardar");
    private Button eliminar = new Button("Eliminar");
    private ExpedienteAcademico expedienteSeleccionado;

    public ExpedienteAcademicoView(ExpedienteAcademicoService expedienteService) {
        this.expedienteService = expedienteService;
        configurarVista();
        actualizarLista();
    }

    private void configurarVista() {
        grid.setColumns("id", "estudianteId", "asignatura", "periodo", "calificacion", "fechaRegistro");
        grid.asSingleSelect().addValueChangeListener(event -> seleccionarExpediente(event.getValue()));

        guardar.addClickListener(e -> guardarExpediente());
        eliminar.addClickListener(e -> eliminarExpediente());

        HorizontalLayout formulario = new HorizontalLayout(estudianteId, asignatura, periodo, calificacion, guardar, eliminar);
        add(grid, formulario);
    }

    private void seleccionarExpediente(ExpedienteAcademico expediente) {
        if (expediente != null) {
            expedienteSeleccionado = expediente;
            estudianteId.setValue(expediente.getEstudianteId().toString());
            asignatura.setValue(expediente.getAsignatura());
            periodo.setValue(expediente.getPeriodo());
            calificacion.setValue(expediente.getCalificacion());
        } else {
            expedienteSeleccionado = null;
            estudianteId.clear();
            asignatura.clear();
            periodo.clear();
            calificacion.clear();
        }
    }

    private void guardarExpediente() {
        if (expedienteSeleccionado == null) {
            expedienteSeleccionado = new ExpedienteAcademico();
        }

        expedienteSeleccionado.setEstudianteId(Long.valueOf(estudianteId.getValue()));
        expedienteSeleccionado.setAsignatura(asignatura.getValue());
        expedienteSeleccionado.setPeriodo(periodo.getValue());
        expedienteSeleccionado.setCalificacion(calificacion.getValue());
        expedienteSeleccionado.setFechaRegistro(new Date());

        expedienteService.guardarExpediente(expedienteSeleccionado);
        actualizarLista();
        Notification.show("Expediente guardado exitosamente");
        seleccionarExpediente(null);
    }

    private void eliminarExpediente() {
        if (expedienteSeleccionado != null) {
            expedienteService.eliminarExpediente(expedienteSeleccionado.getId());
            actualizarLista();
            Notification.show("Expediente eliminado exitosamente");
            seleccionarExpediente(null);
        }
    }

    private void actualizarLista() {
        grid.setItems(expedienteService.obtenerTodos());
    }
}
