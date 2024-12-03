package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Materia;
import com.educantrol.educantrol_app.model.MateriaRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "materias", layout = MainLayout.class)
public class MateriaView extends VerticalLayout {

    private final MateriaRepository materiaRepository;
    private final Grid<Materia> grid = new Grid<>(Materia.class);
    private final TextField nombre = new TextField("Nombre");
    private final TextField descripcion = new TextField("Descripción");
    private final TextField creditos = new TextField("Créditos");
    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");

    private Binder<Materia> binder = new Binder<>(Materia.class);
    private Materia materiaSeleccionada;

    @Autowired
    public MateriaView(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;

        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        // Configurar formulario
        HorizontalLayout formLayout = configurarFormulario();

        // Configurar Grid
        configurarGrid();

        add(formLayout, grid);

        add(new H3("Gestión de materias"),formLayout, grid);

        actualizarLista();// Agregar componentes al
    }

    private HorizontalLayout configurarFormulario() {
        // Configurar validaciones con Binder
        binder.forField(nombre)
                .asRequired("El nombre es requerido")
                .bind(Materia::getNombre, Materia::setNombre);

        binder.forField(descripcion)
                .asRequired("La descripcion es requerida")
                .bind(Materia::getDescripcion, Materia::setDescripcion);

        binder.forField(creditos)
                .asRequired("Los creditos son requeridos")
                .withConverter(new StringToIntegerConverter("Debe ingresar un número válido"))
                .bind(Materia::getCreditos, Materia::setCreditos);



        // Configurar acciones de los botones
        saveButton.addClickListener(e -> guardarMateria());
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
        HorizontalLayout formLayout = new HorizontalLayout(nombre, descripcion,creditos, saveButton, cancelButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
        formLayout.setAlignItems(Alignment.BASELINE); // Alinea los controles horizontalmente
        return formLayout;
    }

    private void configurarGrid() {
        grid.removeAllColumns();
        grid.addColumn(Materia::getIdMateria).setHeader("ID").setAutoWidth(true).setFlexGrow(0);;
        grid.addColumn(Materia::getNombre).setHeader("Nombre");
        grid.addColumn(Materia::getDescripcion).setHeader("Descripción");
        grid.addColumn(Materia::getCreditos).setHeader("Créditos");

        grid.addComponentColumn(materia -> {
            Button editButton = new Button("Editar", e -> editarMateria(materia));
            Button deleteButton = new Button("Eliminar", e -> eliminarMateria(materia));
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
                editarMateria(event.getValue());
            }
        });
    }

    private HorizontalLayout crearBotonesAcciones(Materia materia) {
        Button editButton = new Button("Editar", e -> editarMateria(materia));
        Button deleteButton = new Button("Eliminar", e -> eliminarMateria(materia));

        return new HorizontalLayout(editButton, deleteButton);
    }

    private void actualizarLista() {
        grid.setItems(materiaRepository.findAll());
    }

    private void guardarMateria() {
        if (validarFormulario()) {
            try {
                int creditosValue = Integer.parseInt(creditos.getValue());
                if (materiaSeleccionada == null) {
                    materiaSeleccionada = new Materia();
                }
                materiaSeleccionada.setNombre(nombre.getValue());
                materiaSeleccionada.setDescripcion(descripcion.getValue());
                materiaSeleccionada.setCreditos(creditosValue);

                materiaRepository.save(materiaSeleccionada);
                Notification.show("Materia guardada exitosamente.");
                limpiarFormulario();
                actualizarLista();
            } catch (NumberFormatException ex) {
                Notification.show("El valor de créditos debe ser un número válido.");
            }
        }
    }

    private void eliminarMateria(Materia materia) {
        if (materia != null) {
            materiaRepository.delete(materia);
            Notification.show("Materia eliminada exitosamente.");
            actualizarLista();
            limpiarFormulario();
        } else {
            Notification.show("No se pudo eliminar la materia. Selección inválida.");
        }
    }

    private void editarMateria(Materia materia) {
        if (materia != null) {
            materiaSeleccionada = materia;
            nombre.setValue(materia.getNombre());
            descripcion.setValue(materia.getDescripcion());
            creditos.setValue(String.valueOf(materia.getCreditos()));
        } else {
            limpiarFormulario();
        }
    }

    private boolean validarFormulario() {
        if (nombre.isEmpty() || descripcion.isEmpty() || creditos.isEmpty()) {
            Notification.show("Por favor, completa todos los campos.");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        materiaSeleccionada = null;
        nombre.clear();
        descripcion.clear();
        creditos.clear();
    }
}