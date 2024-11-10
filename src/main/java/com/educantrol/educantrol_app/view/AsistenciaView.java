package main.java.com.educantrol.educantrol_app.view;

import main.java.com.educantrol.educantrol_app.model.Asistencia;
import main.java.com.educantrol.educantrol_app.model.Clase;
import main.java.com.educantrol.educantrol_app.model.Estudiante;

import main.java.com.educantrol.educantrol_app.service.AsistenciaService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("asistencias")
public class AsistenciaView extends VerticalLayout {

    private final AsistenciaService asistenciaService;
    private final Grid<Asistencia> grid = new Grid<>(Asistencia.class);

    // Campos de formulario
    private TextField estudianteField = new TextField("ID Estudiante");
    private TextField claseField = new TextField("ID Clase");
    private DatePicker fechaField = new DatePicker("Fecha");
    private TextField presenteField = new TextField("Presente (true/false)");

    private Button addButton = new Button("Añadir Asistencia");
    private Button updateButton = new Button("Actualizar Asistencia");
    private Button deleteButton = new Button("Eliminar Asistencia");

    @Autowired
    public AsistenciaView(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;

        configurarVista();
        configurarEventos();
        actualizarGrid();
    }

    private void configurarVista() {
        add(estudianteField, claseField, fechaField, presenteField, addButton, updateButton, deleteButton, grid);
        grid.setColumns("idAsistencia", "estudiante.idEstudiante", "clase.idClase", "fecha", "presente");
        grid.setSizeFull();
    }

    private void configurarEventos() {
        addButton.addClickListener(e -> agregarAsistencia());
        updateButton.addClickListener(e -> actualizarAsistencia());
        deleteButton.addClickListener(e -> eliminarAsistencia());
        grid.asSingleSelect().addValueChangeListener(e -> cargarDatosFormulario(e.getValue()));
    }

    private void agregarAsistencia() {
        try {
            Asistencia asistencia = new Asistencia();
            asistencia.setEstudiante(new Estudiante(Long.parseLong(estudianteField.getValue())));
            asistencia.setClase(new Clase(Long.parseLong(claseField.getValue())));
            asistencia.setFecha(fechaField.getValue());
            asistencia.setPresente(Boolean.parseBoolean(presenteField.getValue()));
            
            asistenciaService.guardarAsistencia(asistencia);
            actualizarGrid();
            Notification.show("Asistencia añadida con éxito.");
        } catch (Exception ex) {
            Notification.show("Error al añadir asistencia: " + ex.getMessage());
        }
    }

    private void actualizarAsistencia() {
        try {
            Optional<Asistencia> optionalAsistencia = grid.asSingleSelect().getValue();
            if (optionalAsistencia.isPresent()) {
                Asistencia asistencia = optionalAsistencia.get();
                asistencia.setFecha(fechaField.getValue());
                asistencia.setPresente(Boolean.parseBoolean(presenteField.getValue()));
                asistenciaService.guardarAsistencia(asistencia);
                actualizarGrid();
                Notification.show("Asistencia actualizada con éxito.");
            }
        } catch (Exception ex) {
            Notification.show("Error al actualizar asistencia: " + ex.getMessage());
        }
    }

    private void eliminarAsistencia() {
        Optional<Asistencia> optionalAsistencia = grid.asSingleSelect().getValue();
        if (optionalAsistencia.isPresent()) {
            asistenciaService.eliminarAsistencia(optionalAsistencia.get().getIdAsistencia());
            actualizarGrid();
            Notification.show("Asistencia eliminada con éxito.");
        } else {
            Notification.show("Seleccione una asistencia para eliminar.");
        }
    }

    private void cargarDatosFormulario(Asistencia asistencia) {
        if (asistencia != null) {
            estudianteField.setValue(asistencia.getEstudiante().getIdEstudiante().toString());
            claseField.setValue(asistencia.getClase().getIdClase().toString());
            fechaField.setValue(asistencia.getFecha());
            presenteField.setValue(asistencia.getPresente().toString());
        } else {
            estudianteField.clear();
            claseField.clear();
            fechaField.clear();
            presenteField.clear();
        }
    }

    private void actualizarGrid() {
        grid.setItems(asistenciaService.obtenerAsistencias());
    }
}
