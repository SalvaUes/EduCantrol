package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Horario;
import com.educantrol.educantrol_app.service.HorarioService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

@Route("horarios")
public class HorarioView extends VerticalLayout {

    private final HorarioService horarioService;
    private final Grid<Horario> grid = new Grid<>(Horario.class);
    private final TextField diaSemana = new TextField("Día de la Semana");
    private final TextField horaInicio = new TextField("Hora Inicio");
    private final TextField horaFin = new TextField("Hora Fin");
    private final Button save = new Button("Guardar");

    @Autowired
    public HorarioView(HorarioService horarioService) {
        this.horarioService = horarioService;

        save.addClickListener(event -> {
            Horario horario = new Horario();
            horario.setDiaSemana(diaSemana.getValue());
            horario.setHoraInicio(LocalTime.parse(horaInicio.getValue()));
            horario.setHoraFin(LocalTime.parse(horaFin.getValue()));
            horarioService.save(horario);
            updateGrid();
        });

        add(diaSemana, horaInicio, horaFin, save, grid);
        updateGrid();
    }

    private void updateGrid() {
        grid.setItems(horarioService.findAll());
    }
}

