package com.educantrol.educantrol_app.view;


import com.educantrol.educantrol_app.model.Profesor;
import com.educantrol.educantrol_app.service.ProfesorService;
import com.educantrol.educantrol_app.utils.ProfesorImporter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Label;

import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;
import java.io.IOException;

@Route(value = "profesores" , layout = MainLayout.class)
public class ProfesorView extends VerticalLayout {

    private final ProfesorService profesorService;
    private final Grid<Profesor> grid = new Grid<>(Profesor.class);

    private final TextField nombre = new TextField("Nombre");
    private final TextField apellido = new TextField("Apellido");
    private final TextField especialidad = new TextField("Especialidad");
    private final TextField email = new TextField("Email");
    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");

    private Binder<Profesor> binder = new Binder<>(Profesor.class);
    private Profesor profesorSeleccionado;

    @Autowired
    public ProfesorView(ProfesorService profesorService) {
        this.profesorService = profesorService;

        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        // Configurar formulario
        HorizontalLayout formLayout = configurarFormulario();

        // Configurar Grid
        configurarGrid();

        // Agregar componentes al layout principal
        add(new H3("Gestión de Profesores"),formLayout, grid);

        // Inicializar lista de profesores
        actualizarLista();


    }

    private HorizontalLayout configurarFormulario() {
        // Configurar validaciones con Binder
        binder.forField(nombre)
                .asRequired("El nombre es requerido")
                .bind(Profesor::getNombre, Profesor::setNombre);

        binder.forField(apellido)
                .asRequired("El apellido es requerido")
                .bind(Profesor::getApellido, Profesor::setApellido);

        binder.forField(especialidad)
                .asRequired("La especialidad es requerida")
                .bind(Profesor::getEspecialidad, Profesor::setEspecialidad);
        binder.forField(email)
                .asRequired("El email es requerido")
                .bind(Profesor::getEmail, Profesor::setEmail);


        // Configurar acciones de los botones
        saveButton.addClickListener(e -> guardarProfesor());
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

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".xlsx"); // Solo aceptar archivos Excel
        Label label = new Label("Importar Profesores:");

        upload.addSucceededListener(event -> {
            try (InputStream inputStream = buffer.getInputStream()) {
                // Procesar el archivo Excel
                ProfesorImporter importer = new ProfesorImporter();
                List<Profesor> profesores = importer.importarProfesores(inputStream);

                // Guardar en la base de datos
                for (Profesor profesor : profesores) {
                    profesorService.save(profesor);
                }


                NotificationUtil.showSuccess("Archivo importado correctamente.");
            } catch (Exception e) {
                NotificationUtil.showError("Error al procesar el archivo: " + e.getMessage());
            }
        });

        // Añadir el componente Upload a la vista
       // add(new Button("Importar Profesores"), upload );

        // Controles horizontales en una sola línea
        HorizontalLayout formLayout = new HorizontalLayout(nombre, apellido,email, especialidad, saveButton, cancelButton, label, upload);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
        formLayout.setAlignItems(Alignment.BASELINE); // Alinea los controles horizontalmente
        return formLayout;
    }

    private void configurarGrid() {
        // Configurar columnas del Grid
        grid.removeAllColumns();
        grid.addColumn(Profesor::getIdProfesor).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Profesor::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Profesor::getApellido).setHeader("Apellido").setAutoWidth(true);
        grid.addColumn(Profesor::getEspecialidad).setHeader("Especialidad").setAutoWidth(true);
        grid.addColumn(Profesor::getEmail).setHeader("Email").setAutoWidth(true);

        // Agregar columna de acciones (Editar y Eliminar)
        grid.addComponentColumn(profesor -> {
            Button editButton = new Button("Editar", e -> editarProfesor(profesor));
            Button deleteButton = new Button("Eliminar", e -> eliminarProfesor(profesor));
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
                editarProfesor(event.getValue());
            }
        });
    }

    private void actualizarLista() {
        grid.setItems(profesorService.findAll());
    }

    private void guardarProfesor() {
        if (profesorSeleccionado == null) {
            profesorSeleccionado = new Profesor();
        }

        try {
            // Validar y escribir en el modelo
            binder.writeBean(profesorSeleccionado);

            // Guardar el profesor en el servicio
            profesorService.save(profesorSeleccionado);

            mostrarNotificacion("Profesor guardado correctamente.", NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            limpiarFormulario();
        } catch (ValidationException e) {
            mostrarNotificacion("Por favor, corrija los errores antes de guardar.", NotificationVariant.LUMO_ERROR);
        }
    }

    private void editarProfesor(Profesor profesor) {
        profesorSeleccionado = profesor;
        binder.readBean(profesorSeleccionado); // Carga los datos del profesor seleccionado en el formulario
    }

    private void eliminarProfesor(Profesor profesor) {
        if (profesor != null) {
            profesorService.deleteById(profesor.getIdProfesor());
            mostrarNotificacion("Profesor eliminado correctamente.", NotificationVariant.LUMO_SUCCESS);
            actualizarLista();
            limpiarFormulario();
        } else {
            mostrarNotificacion("Error al eliminar el profesor.", NotificationVariant.LUMO_ERROR);
        }
    }

    private void limpiarFormulario() {
        profesorSeleccionado = null;
        binder.readBean(null); // Limpia el formulario
    }

    private void mostrarNotificacion(String mensaje, NotificationVariant variante) {
        Notification notification = new Notification(mensaje, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variante);
        notification.open();
    }

}
