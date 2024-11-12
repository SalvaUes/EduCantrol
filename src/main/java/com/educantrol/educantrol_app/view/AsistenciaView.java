package com.educantrol.educantrol_app.view;



import com.educantrol.educantrol_app.model.Asistencia;
import com.educantrol.educantrol_app.service.AsistenciaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("asistencias")
public class AsistenciaView extends VerticalLayout {

    private final AsistenciaService asistenciaService;

    private Grid<Asistencia> grid = new Grid<>(Asistencia.class);
    private DatePicker fechaField = new DatePicker("Fecha");  // Cambiado a DatePicker
    private TextField presenteField = new TextField("Presente (true/false)");

    private Button saveButton = new Button("Guardar");
    private Button deleteButton = new Button("Eliminar");

    private Asistencia asistenciaSeleccionada;

    public AsistenciaView(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
        
        setSizeFull();
        configureGrid();
        configureForm();

        add(getContent());

        updateList();
    }

    private void configureGrid() {
        grid.setColumns("idAsistencia", "clase", "estudiante", "fecha", "presente");
        grid.asSingleSelect().addValueChangeListener(event -> editAsistencia(event.getValue()));
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, getFormLayout());
        content.setSizeFull();
        return content;
    }

    private VerticalLayout getFormLayout() {
        VerticalLayout formLayout = new VerticalLayout(fechaField, presenteField, createButtonsLayout());
        formLayout.setSpacing(false);
        return formLayout;
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        saveButton.addClickListener(e -> saveAsistencia());
        deleteButton.addClickListener(e -> deleteAsistencia());

        return new HorizontalLayout(saveButton, deleteButton);
    }

    private void configureForm() {
        fechaField.setPlaceholder("Selecciona una fecha");  // Mensaje más claro
    }

    private void saveAsistencia() {
        if (asistenciaSeleccionada == null) {
            asistenciaSeleccionada = new Asistencia();
        }

        // Asignar los valores del formulario a la asistencia seleccionada
        asistenciaSeleccionada.setFecha(fechaField.getValue());
        asistenciaSeleccionada.setPresente(Boolean.parseBoolean(presenteField.getValue()));

        asistenciaService.save(asistenciaSeleccionada);
        updateList();
        Notification.show("Asistencia guardada");
        clearForm();
    }

    private void deleteAsistencia() {
        if (asistenciaSeleccionada != null) {
            asistenciaService.delete(asistenciaSeleccionada);
            updateList();
            Notification.show("Asistencia eliminada");
            clearForm();
        }
    }

    private void editAsistencia(Asistencia asistencia) {
        if (asistencia == null) {
            clearForm();
        } else {
            asistenciaSeleccionada = asistencia;
            fechaField.setValue(asistencia.getFecha());
            presenteField.setValue(asistencia.getPresente() != null ? asistencia.getPresente().toString() : "");
        }
    }

    private void clearForm() {
        asistenciaSeleccionada = null;
        fechaField.clear();
        presenteField.clear();
    }

    private void updateList() {
        grid.setItems(asistenciaService.findAll());
    }
}
