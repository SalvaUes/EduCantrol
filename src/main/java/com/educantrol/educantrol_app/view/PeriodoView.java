package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Periodo;
import com.educantrol.educantrol_app.model.PeriodoRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "periodos", layout = MainLayout.class)
public class PeriodoView extends VerticalLayout {

    private final PeriodoRepository periodoRepository;
    private final Grid<Periodo> grid = new Grid<>(Periodo.class);
    private final TextField nombre = new TextField("Nombre");
    private final TextField descripcion = new TextField("Descripción");
    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");

    private Binder<Periodo> binder = new Binder<>(Periodo.class);
    private Periodo periodoSeleccionado;

    @Autowired
    public PeriodoView(PeriodoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;

        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        // Configurar formulario
        HorizontalLayout formLayout = configurarFormulario();

        // Configurar Grid
        configurarGrid();

        add(formLayout, grid);

        actualizarLista(); // Actualiza la lista de periodos
    }

    private HorizontalLayout configurarFormulario() {
        // Configurar validaciones con Binder
        binder.forField(nombre)
                .asRequired("El nombre es requerido")
                .bind(Periodo::getNombre, Periodo::setNombre);

        binder.forField(descripcion)
                .asRequired("La descripcion es requerida")
                .bind(Periodo::getDescripcion, Periodo::setDescripcion);

        // Configurar acciones de los botones
        saveButton.addClickListener(e -> guardarPeriodo());
        cancelButton.addClickListener(e -> limpiarFormulario());

        // Estilizar botones
        saveButton.getStyle()
                .set("background-color", "#007BFF")
                .set("color", "white")
                .set("border-radius", "8px");

        cancelButton.getStyle()
                .set("background-color", "#DC3545")
                .set("color", "white")
                .set("border-radius", "8px");

        // Controles horizontales en una sola línea
        HorizontalLayout formLayout = new HorizontalLayout(nombre, descripcion, saveButton, cancelButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
        formLayout.setAlignItems(Alignment.BASELINE); // Alinea los controles horizontalmente
        return formLayout;
    }

    private void configurarGrid() {
        grid.removeAllColumns();
        grid.addColumn(Periodo::getId).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Periodo::getNombre).setHeader("Nombre");
        grid.addColumn(Periodo::getDescripcion).setHeader("Descripción");

        grid.addComponentColumn(periodo -> {
            Button editButton = new Button("Editar", e -> editarPeriodo(periodo));
            Button deleteButton = new Button("Eliminar", e -> eliminarPeriodo(periodo));
            editButton.getStyle()
                    .set("background-color", "#17A2B8")
                    .set("color", "white")
                    .set("border-radius", "5px");
            deleteButton.getStyle()
                    .set("background-color", "#DC3545")
                    .set("color", "white")
                    .set("border-radius", "5px");
            HorizontalLayout actions = new HorizontalLayout(editButton, deleteButton);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Acciones").setAutoWidth(true);

        // Configurar estilo general del Grid
        grid.setWidthFull();
        grid.setHeight("400px");
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editarPeriodo(event.getValue());
            }
        });
    }

    private void actualizarLista() {
        grid.setItems(periodoRepository.findAll());
    }

    private void guardarPeriodo() {
        if (validarFormulario()) {
            if (periodoSeleccionado == null) {
                periodoSeleccionado = new Periodo();
            }
            periodoSeleccionado.setNombre(nombre.getValue());
            periodoSeleccionado.setDescripcion(descripcion.getValue());

            periodoRepository.save(periodoSeleccionado);
            Notification.show("Periodo guardado exitosamente.");
            limpiarFormulario();
            actualizarLista();
        }
    }

    private void eliminarPeriodo(Periodo periodo) {
        if (periodo != null) {
            periodoRepository.delete(periodo);
            Notification.show("Periodo eliminado exitosamente.");
            actualizarLista();
            limpiarFormulario();
        } else {
            Notification.show("No se pudo eliminar el periodo. Selección inválida.");
        }
    }

    private void editarPeriodo(Periodo periodo) {
        if (periodo != null) {
            periodoSeleccionado = periodo;
            nombre.setValue(periodo.getNombre());
            descripcion.setValue(periodo.getDescripcion());
        } else {
            limpiarFormulario();
        }
    }

    private boolean validarFormulario() {
        if (nombre.isEmpty() || descripcion.isEmpty()) {
            Notification.show("Por favor, completa todos los campos.");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        periodoSeleccionado = null;
        nombre.clear();
        descripcion.clear();
    }
}
