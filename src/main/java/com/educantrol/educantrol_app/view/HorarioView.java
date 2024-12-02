package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Horario;
import com.educantrol.educantrol_app.model.Periodo;
import com.educantrol.educantrol_app.service.HorarioService;
import com.educantrol.educantrol_app.service.PeriodoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Route(value = "horarios", layout = MainLayout.class)
public class HorarioView extends VerticalLayout {

    private final HorarioService horarioService;
    private final PeriodoService periodoService;

    private ComboBox<String> diaSemanaComboBox;
    private TimePicker horaInicioField;
    private TimePicker horaFinField;
    private ComboBox<Periodo> periodoComboBox;
    private Button saveButton;
    private Button cancelButton;

    private Grid<Horario> grid;
    private Horario horarioSeleccionado;

    @Autowired
    public HorarioView(HorarioService horarioService, PeriodoService periodoService) {
        this.horarioService = horarioService;
        this.periodoService = periodoService;

        configurarFormulario();
        configurarGrid();

        add(new H3("Gestión de Horarios"), crearFormulario(), grid);

        actualizarGrid();
    }

    private void configurarFormulario() {
        diaSemanaComboBox = new ComboBox<>("Día de la Semana");
        diaSemanaComboBox.setItems(Arrays.asList(
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        ));

        horaInicioField = new TimePicker("Hora Inicio");
        horaFinField = new TimePicker("Hora Fin");
        periodoComboBox = new ComboBox<>("Período");
        periodoComboBox.setItems(periodoService.obtenerTodosLosPeriodos());
        periodoComboBox.setItemLabelGenerator(Periodo::getNombre);

        saveButton = new Button("Guardar", event -> guardarHorario());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("background-color", "#0D6EFD");
        saveButton.getStyle().set("color", "white");

        cancelButton = new Button("Cancelar", event -> cancelarEdicion());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.getStyle().set("background-color", "#DC3545");
        cancelButton.getStyle().set("color", "white");
    }

    private HorizontalLayout crearFormulario() {
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setAlignItems(Alignment.BASELINE);

        formLayout.add(diaSemanaComboBox, horaInicioField, horaFinField, periodoComboBox, saveButton, cancelButton);
        return formLayout;
    }

    private void configurarGrid() {
        grid = new Grid<>(Horario.class, false);
        grid.addColumn(Horario::getIdHorario).setHeader("ID").setWidth("50px");
        grid.addColumn(Horario::getDiaSemana).setHeader("Día de la Semana");
        grid.addColumn(Horario::getHoraInicio).setHeader("Hora Inicio");
        grid.addColumn(Horario::getHoraFin).setHeader("Hora Fin");
        grid.addColumn(horario -> horario.getPeriodo().getNombre()).setHeader("Período");

        grid.addComponentColumn(this::crearBotonesAccion).setHeader("Acciones");

        grid.setHeight("400px");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editarHorario(event.getValue());
            }
        });
    }

    private HorizontalLayout crearBotonesAccion(Horario horario) {
        Button editarButton = new Button("Editar", click -> editarHorario(horario));
        editarButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        editarButton.getStyle().set("background-color", "#17A2B8");
        editarButton.getStyle().set("color", "white");

        Button eliminarButton = new Button("Eliminar", click -> eliminarHorario(horario));
        eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminarButton.getStyle().set("background-color", "#DC3545");
        eliminarButton.getStyle().set("color", "white");

        return new HorizontalLayout(editarButton, eliminarButton);
    }

    private void guardarHorario() {
        if (!validarFormulario()) {
            return;
        }

        if (horarioSeleccionado == null) {
            horarioSeleccionado = new Horario();
        }

        horarioSeleccionado.setDiaSemana(diaSemanaComboBox.getValue());
        horarioSeleccionado.setHoraInicio(horaInicioField.getValue());
        horarioSeleccionado.setHoraFin(horaFinField.getValue());
        horarioSeleccionado.setPeriodo(periodoComboBox.getValue());

        horarioService.save(horarioSeleccionado);

        Notification.show("Horario guardado exitosamente");

        limpiarFormulario();
        actualizarGrid();
        horarioSeleccionado = null;
    }

    private void editarHorario(Horario horario) {
        if (horario != null) {
            horarioSeleccionado = horario;

            diaSemanaComboBox.setValue(horario.getDiaSemana());
            horaInicioField.setValue(horario.getHoraInicio());
            horaFinField.setValue(horario.getHoraFin());
            periodoComboBox.setValue(horario.getPeriodo());

            saveButton.setText("Actualizar");
        }
    }

    private void eliminarHorario(Horario horario) {
        horarioService.deleteById(horario.getIdHorario());
        Notification.show("Horario eliminado exitosamente");
        actualizarGrid();
    }

    private void cancelarEdicion() {
        limpiarFormulario();
        horarioSeleccionado = null;
    }

    private void limpiarFormulario() {
        diaSemanaComboBox.clear();
        horaInicioField.clear();
        horaFinField.clear();
        periodoComboBox.clear();

        saveButton.setText("Guardar");
    }

    private void actualizarGrid() {
        List<Horario> horarios = horarioService.findAll();
        grid.setItems(horarios);
    }

    private boolean validarFormulario() {
        String diaSemana = diaSemanaComboBox.getValue();
        LocalTime horaInicio = horaInicioField.getValue();
        LocalTime horaFin = horaFinField.getValue();
        Periodo periodo = periodoComboBox.getValue();

        if (diaSemana == null || diaSemana.isEmpty() ||
            horaInicio == null || horaFin == null || periodo == null) {
            Notification.show("Todos los campos son obligatorios");
            return false;
        }

        if (horaFin.isBefore(horaInicio)) {
            Notification.show("La hora fin debe ser posterior a la hora inicio");
            return false;
        }

        return true;
    }
}
