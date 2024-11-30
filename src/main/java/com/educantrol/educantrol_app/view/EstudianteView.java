package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.service.EstudianteService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.notification.Notification;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Route(value = "estudiante", layout = MainLayout.class) // Define la URL de la vista
public class EstudianteView extends VerticalLayout {

    private final EstudianteService estudianteService;
    private final Grid<Estudiante> grid = new Grid<>(Estudiante.class);

    private TextField nombre = new TextField("Nombre");
    private TextField apellido = new TextField("Apellido");
    private TextField telefono = new TextField("Teléfono");
    private TextField email = new TextField("Email");
    private Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");

    private Binder<Estudiante> binder = new Binder<>(Estudiante.class);
    private Estudiante estudianteSeleccionado;

    @Autowired
    public EstudianteView(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;

        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);

        // Configurar formulario
        HorizontalLayout formLayout = configurarFormulario();

        // Configurar Grid
        configurarGrid();

        // Agregar componentes al layout principal
        add(formLayout, grid);

        // Inicializar lista de estudiantes
        actualizarLista();
    }

    private MemoryBuffer buffer = new MemoryBuffer(); // Buffer para almacenar temporalmente la foto

    private HorizontalLayout configurarFormulario() {
        // Configurar alineación de controles del formulario
        nombre.setWidth("200px");
        apellido.setWidth("200px");
        telefono.setWidth("200px");
        email.setWidth("250px");
        saveButton.setWidth("120px");

        // Aplicar estilo al botón
        saveButton.getStyle()
                .set("background-color", "#007BFF")
                .set("color", "white")
                .set("border-radius", "8px");

        // Configurar validaciones con Binder
        binder.forField(nombre)
                .asRequired("El nombre es requerido")
                .bind(Estudiante::getNombre, Estudiante::setNombre);

        binder.forField(apellido)
                .asRequired("El apellido es requerido")
                .bind(Estudiante::getApellido, Estudiante::setApellido);

        binder.forField(telefono)
                .asRequired("El teléfono es requerido")
                .bind(Estudiante::getTelefono, Estudiante::setTelefono);

        binder.forField(email)
                .asRequired("El email es requerido")
                .withValidator(email -> email.contains("@"), "El email debe ser válido")
                .bind(Estudiante::getEmail, Estudiante::setEmail);

        // Configurar el buffer para manejar la carga de la foto
        buffer = new MemoryBuffer();
        // Componente de carga
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png"); // Solo acepta imágenes JPEG y PNG
        upload.setMaxFileSize(2 * 1024 * 1024); // Tamaño máximo de archivo: 2 MB

        upload.addSucceededListener(event -> {
            if (estudianteSeleccionado == null) {
                estudianteSeleccionado = new Estudiante(); // Inicializar si es nulo
            }
            try {
                // Leer los bytes del archivo cargado y asignarlos al atributo "foto"
                estudianteSeleccionado.setFoto(buffer.getInputStream().readAllBytes());
                Notification.show("Foto cargada correctamente.", 3000, Notification.Position.TOP_CENTER);
            } catch (IOException e) {
                Notification.show("Error al cargar la foto: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });

        upload.addFailedListener(event -> {
            Notification.show("Error al cargar el archivo: " + event.getReason().getMessage(), 3000, Notification.Position.TOP_CENTER);
        });



        cancelButton.getStyle()
                .set("background-color", "#DC3545")
                .set("color", "white")
                .set("border-radius", "8px");

        // Configurar acciones del botón Guardar
        saveButton.addClickListener(e -> guardarEstudiante());

        // Controles horizontales en una sola línea
        HorizontalLayout formLayout = new HorizontalLayout(nombre, apellido, telefono, email, upload, saveButton, cancelButton);
        formLayout.setSpacing(true);
        add(formLayout);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidthFull();
        formLayout.setAlignItems(Alignment.BASELINE); // Alinea todos los controles horizontalmente


        return formLayout;
    }

    private void configurarGrid() {
        // Configurar columnas del Grid
        grid.removeAllColumns();
        grid.addColumn(Estudiante::getidEstudiante).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Estudiante::getApellido).setHeader("Apellido").setAutoWidth(true);
        grid.addColumn(Estudiante::getTelefono).setHeader("Teléfono").setAutoWidth(true);
        grid.addColumn(Estudiante::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addComponentColumn(estudiante -> {
            Div container = new Div(); // Contenedor genérico

            if (estudiante.getFoto() != null) {
                // Crear un StreamResource para mostrar la foto
                StreamResource resource = new StreamResource("foto",
                        () -> new ByteArrayInputStream(estudiante.getFoto())); // Crear el InputStream a partir de los bytes

                // Crear el componente de imagen
                Image image = new Image(resource, "Foto");
                image.setWidth("50px");
                image.setHeight("50px");
                container.add(image);
            } else {
                // Agregar texto "Sin foto" al contenedor
                container.add(new Text("Sin foto"));
            }

            return container; // Retornar el contenedor
        }).setHeader("Foto");


        // Agregar columna de acciones (Editar y Eliminar)
        grid.addComponentColumn(estudiante -> {
            Button editButton = new Button("Editar", e -> editarEstudiante(estudiante));
            Button deleteButton = new Button("Eliminar", e -> eliminarEstudiante(estudiante));
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
        grid.addClassName("custom-grid");
        grid.setWidthFull();
        grid.setHeight("400px");
        grid.getStyle().set("border", "1px solid #ccc")
                .set("border-radius", "8px")
                .set("box-shadow", "2px 2px 10px rgba(0, 0, 0, 0.1)");
    }

    private void actualizarLista() {
        grid.setItems(estudianteService.findAll());
    }

    private void editarEstudiante(Estudiante estudiante) {
        estudianteSeleccionado = estudiante;
        binder.readBean(estudiante);
        binder.setValidationStatusHandler(status -> {
            // No mostrar errores al cargar datos para edición
        });
    }

    private void guardarEstudiante() {
        if (estudianteSeleccionado == null) {
            estudianteSeleccionado = new Estudiante();
        }
        try {
            // Validar y escribir en el modelo
            binder.writeBean(estudianteSeleccionado);

            estudianteService.save(estudianteSeleccionado);
            NotificationUtil.showSuccess("Estudiante guardado");
            actualizarLista();
            limpiarFormulario();
        } catch (ValidationException e) {
            NotificationUtil.showError("Por favor, complete todos los campos requeridos.");
        }
    }

    private void eliminarEstudiante(Estudiante estudiante) {
        if (estudiante != null) {
            // Crear un diálogo de confirmación
            Dialog confirmDialog = new Dialog();
            confirmDialog.setHeaderTitle("Confirmación");

            // Mensaje de confirmación
            confirmDialog.add(new Text("¿Está seguro de que desea eliminar al estudiante?"));

            // Botones de acción
            Button confirmButton = new Button("Eliminar", e -> {
                estudianteService.deleteById(estudiante.getidEstudiante());
                NotificationUtil.showSuccess("Estudiante eliminado");
                actualizarLista();
                limpiarFormulario();
                confirmDialog.close(); // Cerrar el diálogo después de la acción
            });
            confirmButton.getStyle().set("background-color", "#DC3545").set("color", "white"); // Estilo rojo para el botón

            Button cancelButton = new Button("Cancelar", e -> confirmDialog.close());
            cancelButton.getStyle().set("background-color", "#6c757d").set("color", "white"); // Estilo gris para cancelar

            // Layout para botones
            HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
            confirmDialog.add(buttons);

            // Abrir el diálogo
            confirmDialog.open();
        } else {
            NotificationUtil.showError("No se pudo eliminar el estudiante. Selección inválida.");
        }
    }

    private void limpiarFormulario() {
        estudianteSeleccionado = null;

        // Limpia los datos del formulario y los estilos de error
        binder.readBean(null);
        limpiarErroresVisuales();
    }

    private void limpiarErroresVisuales() {
        // Elimina los estilos de error de los campos
        nombre.getElement().getClassList().remove("error");
        apellido.getElement().getClassList().remove("error");
        telefono.getElement().getClassList().remove("error");
        email.getElement().getClassList().remove("error");

        // O puedes resetear los estilos directamente
        nombre.getStyle().remove("background-color");
        apellido.getStyle().remove("background-color");
        telefono.getStyle().remove("background-color");
        email.getStyle().remove("background-color");
    }


}
