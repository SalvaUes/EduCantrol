package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Clase;
import com.educantrol.educantrol_app.service.ClaseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;



@Route("clases")
public class ClaseView extends VerticalLayout {

    private final ClaseService claseService;
    private final Grid<Clase> grid = new Grid<>(Clase.class);
    private final Button addButton = new Button("Agregar Clase");

    @Autowired



    public ClaseView(ClaseService claseService) {
        this.claseService = claseService;

        

        grid.setColumns("idClase", "nombre", "descripcion", "fecha");
        grid.setSizeFull();

        


        updateGrid();




        
        
        addButton.addClickListener(e -> openClaseDialog(null));

        
        
        add(new H3("Gestión de Clases"), addButton, grid);
    }

    private void updateGrid() {
        grid.setItems(claseService.getAllClases());
    }

    private void openClaseDialog(Clase clase) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nombreField = new TextField("Nombre");
        TextField descripcionField = new TextField("Descripción");
        TextField fechaField = new TextField("Fecha (YYYY-MM-DD)");

        if (clase != null) {
            nombreField.setValue(clase.getNombre());
            descripcionField.setValue(clase.getDescripcion());
            fechaField.setValue(clase.getFecha() != null ? clase.getFecha().toString() : "");
        }

        formLayout.add(nombreField, descripcionField, fechaField);

        Button saveButton = new Button("Guardar", event -> {
            String nombre = nombreField.getValue();
            String descripcion = descripcionField.getValue();
            LocalDate fecha = fechaField.getValue().isEmpty() ? null : LocalDate.parse(fechaField.getValue());

            if (nombre.isEmpty()) {
                Notification.show("Disculpe el nombre es obligatorio!");
                return;
            }

            if (clase == null) {

                

                Clase nuevaClase = new Clase(nombre, descripcion, fecha);
                claseService.createClase(nuevaClase);
                Notification.show("Clase creada exitosamente");
            } else {

                
                clase.setNombre(nombre);
                clase.setDescripcion(descripcion);
                clase.setFecha(fecha);
                claseService.updateClase(clase.getIdClase(), clase);
                Notification.show("Clase actualizada exitosamente");
            }

            dialog.close();
            updateGrid();
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout, saveButton, cancelButton);
        dialog.open();
    }
}