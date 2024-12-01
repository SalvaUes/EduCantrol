package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Horario;
import com.educantrol.educantrol_app.model.Periodo;
import com.educantrol.educantrol_app.service.HorarioService;
import com.educantrol.educantrol_app.service.PeriodoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.TimePicker; //Pack para la seleccion de horas
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalTime;

@Route(value = "horarios", layout = MainLayout.class)
public class HorarioView extends VerticalLayout {

    private final HorarioService horarioService;
    private final PeriodoService periodoService;
    private final Grid<Horario> grid = new Grid<>(Horario.class);
    private final Button addButton = new Button("Agregar Horario");

    @Autowired
    public HorarioView(HorarioService horarioService, PeriodoService periodoService) {
        this.horarioService = horarioService;
        this.periodoService = periodoService;

        grid.removeAllColumns();
        grid.addColumn(Horario::getIdHorario).setHeader("ID");
        grid.addColumn(Horario::getDiaSemana).setHeader("Día de la Semana");
        grid.addColumn(Horario::getHoraInicio).setHeader("Hora Inicio");
        grid.addColumn(Horario::getHoraFin).setHeader("Hora Fin");
        grid.addColumn(horario -> horario.getPeriodo().getNombre()).setHeader("Período");

        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(event -> openHorarioDialog(event.getValue()));

        updateGrid();

        addButton.addClickListener(e -> openHorarioDialog(null));

        add(new H3("Gestión de Horarios"), addButton, grid);
    }

    private void updateGrid() {
        grid.setItems(horarioService.findAll());
    }

    private void openHorarioDialog(Horario horario) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField diaSemanaField = new TextField("Día de la Semana");
        TimePicker horaInicioField = new TimePicker("Hora Inicio");
        TimePicker horaFinField = new TimePicker("Hora Fin");
        ComboBox<Periodo> periodoComboBox = new ComboBox<>("Período"); //para seleccionar el periodo
        periodoComboBox.setItems(periodoService.findAll());
        periodoComboBox.setItemLabelGenerator(Periodo::getNombre);

        if (horario != null) {
            diaSemanaField.setValue(horario.getDiaSemana());
            horaInicioField.setValue(horario.getHoraInicio());
            horaFinField.setValue(horario.getHoraFin());
            periodoComboBox.setValue(horario.getPeriodo());
        }

        formLayout.add(diaSemanaField, horaInicioField, horaFinField, periodoComboBox);

        Button saveButton = new Button("Guardar", event -> {
            String diaSemana = diaSemanaField.getValue();
            LocalTime horaInicio = horaInicioField.getValue();
            LocalTime horaFin = horaFinField.getValue();
            Periodo periodo = periodoComboBox.getValue();

            if (diaSemana.isEmpty() || horaInicio == null || horaFin == null || periodo == null) {
                Notification.show("Todos los campos son obligatorios");
                return;
            }

            if (horaFin.isBefore(horaInicio)) {
                Notification.show("La hora fin debe ser posterior a la hora inicio");
                return;
            }

            if (horario == null) {
                Horario nuevoHorario = new Horario();
                nuevoHorario.setDiaSemana(diaSemana);
                nuevoHorario.setHoraInicio(horaInicio);
                nuevoHorario.setHoraFin(horaFin);
                nuevoHorario.setPeriodo(periodo);
                horarioService.save(nuevoHorario);
                Notification.show("Horario creado exitosamente");
            } else {
                horario.setDiaSemana(diaSemana);
                horario.setHoraInicio(horaInicio);
                horario.setHoraFin(horaFin);
                horario.setPeriodo(periodo);
                horarioService.save(horario);
                Notification.show("Horario actualizado exitosamente");
            }

            dialog.close();
            updateGrid();
        });

        Button deleteButton = new Button("Eliminar", event -> {
            if (horario != null) {
                horarioService.deleteById(horario.getIdHorario());
                Notification.show("Horario eliminado exitosamente");
                dialog.close();
                updateGrid();
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout, new HorizontalLayout(saveButton, deleteButton, cancelButton));
        dialog.open();
    }
}
