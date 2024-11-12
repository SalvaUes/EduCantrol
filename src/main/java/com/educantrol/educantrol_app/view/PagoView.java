package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Pago;
import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.service.PagoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.time.LocalDate;

@Route("pago")
public class PagoView extends VerticalLayout {

    private final PagoService pagoService;
    private final Grid<Pago> pagoGrid;
    private final BigDecimalField montoField;
    private final DatePicker fechaPagoField;
    private final TextField conceptoField;
    private final TextField estudianteField;

    public PagoView(PagoService pagoService) {
        this.pagoService = pagoService;

        pagoGrid = new Grid<>(Pago.class);
        pagoGrid.setColumns("idPago", "estudiante.nombre", "monto", "fechaPago", "concepto");
        pagoGrid.setItems(pagoService.findAll());

        montoField = new BigDecimalField("Monto");
        montoField.setPlaceholder("Ingrese el monto");
        fechaPagoField = new DatePicker("Fecha de Pago");
        fechaPagoField.setValue(LocalDate.now());
        conceptoField = new TextField("Concepto");
        estudianteField = new TextField("ID Estudiante");

        Button addButton = new Button("Agregar Pago", event -> agregarPago());

        add(pagoGrid, montoField, fechaPagoField, conceptoField, estudianteField, addButton);
        setAlignItems(Alignment.START);
    }

    private void agregarPago() {
        
        LocalDate fechaPago = fechaPagoField.getValue();
        String concepto = conceptoField.getValue();
        Long idEstudiante;
        try {
            idEstudiante = Long.parseLong(estudianteField.getValue());
        } catch (NumberFormatException e) {
            Notification.show("ID de estudiante no válido", 3000, Notification.Position.MIDDLE);
            return;
        }

        Estudiante estudiante = new Estudiante(); 
        estudiante.setId_estudiante(idEstudiante);

        Pago nuevoPago = new Pago();
        
        nuevoPago.setFechaPago(fechaPago);
        nuevoPago.setConcepto(concepto);
        nuevoPago.setEstudiante(estudiante);

        pagoService.save(nuevoPago);
        pagoGrid.setItems(pagoService.findAll());

        montoField.clear();
        fechaPagoField.clear();
        conceptoField.clear();
        estudianteField.clear();
    }
}
