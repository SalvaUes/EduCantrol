package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.*;
import com.educantrol.educantrol_app.service.*;
import com.educantrol.educantrol_app.utils.PDFGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "asistencias", layout = MainLayout.class)
public class AsistenciaView extends VerticalLayout {

    private final AsistenciaService asistenciaService;
    private final GrupoService grupoService;
    private final EstudianteService estudianteService;
    private final DetalleAsistenciaService detalleAsistenciaService;

    private final Grid<Asistencia> grid = new Grid<>(Asistencia.class, false);
    private ComboBox<Grupo> grupoComboBox;
    private DatePicker fechaPicker;
    private Button saveButton;
    private Button cancelButton;

    public AsistenciaView(AsistenciaService asistenciaService, GrupoService grupoService, EstudianteService estudianteService, DetalleAsistenciaService detalleAsistenciaService) {
        this.asistenciaService = asistenciaService;
        this.grupoService = grupoService;
        this.estudianteService = estudianteService;
        this.detalleAsistenciaService = detalleAsistenciaService;
        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        configurarGrid();
        configurarFormulario();

        add(new H3("Gestion Asistencias"), crearFormulario(), grid);

        actualizarGrid();
    }

    private void configurarGrid() {
        grid.addColumn(asistencia -> asistencia.getGrupo().getDescripcion()).setHeader("Grupo");
        grid.addColumn(Asistencia::getFecha).setHeader("Fecha");

        // Botones de acción (Editar y Eliminar)
        grid.addComponentColumn(asistencia -> {
            Button agregarButton = new Button("Agregar Asistencia", click -> mostrarModalAgregarAsistencia(asistencia));
            agregarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Button editarButton = new Button("Editar", click -> editarAsistencia(asistencia));
            editarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editarButton.getStyle().set("background-color", "#17A2B8");
            editarButton.getStyle().set("color", "white");

            Button eliminarButton = new Button("Eliminar", click -> eliminarAsistencia(asistencia));
            eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            eliminarButton.getStyle().set("background-color", "#DC3545");
            eliminarButton.getStyle().set("color", "white");

            Button imprimirButton = new Button("Imprimir", click -> generarPDFDetalleAsistencia(asistencia));
            imprimirButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);


            return new HorizontalLayout(agregarButton, editarButton, eliminarButton, imprimirButton);
        }).setHeader("Acciones");
    }


    private void configurarFormulario() {
        grupoComboBox = new ComboBox<>("Grupo");
        grupoComboBox.setItems(grupoService.findAll());
        grupoComboBox.setItemLabelGenerator(Grupo::getDescripcion);
        fechaPicker = new DatePicker("Fecha");
        saveButton = new Button("Guardar", event -> guardarAsistencia());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("background-color", "#0D6EFD");
        saveButton.getStyle().set("color", "white");

        cancelButton = new Button("Cancelar", event -> cancelarEdicion());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.getStyle().set("background-color", "#DC3545");
        cancelButton.getStyle().set("color", "white");

    }

    private void mostrarModalAgregarAsistencia(Asistencia asistencia) {
        Dialog dialog = new Dialog();
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        H3 titulo = new H3("Registrar Asistencia para el Grupo: " + asistencia.getGrupo().getDescripcion());
        titulo.getStyle().set("text-align", "center");

        // Obtener los estudiantes del grupo asociado
        List<Estudiante> estudiantes = estudianteService.findEstudianteAsociadosAlGrupo(asistencia.getGrupo().getIdGrupo());

        // Map para almacenar las asistencias seleccionadas
        Map<Estudiante, Boolean> asistenciaMap = new HashMap<>();

        // Grid para mostrar los estudiantes
        Grid<Estudiante> estudiantesGrid = new Grid<>(Estudiante.class, false);
        estudiantesGrid.addColumn(Estudiante::getidEstudiante).setHeader("ID").setWidth("70px");
        estudiantesGrid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
        estudiantesGrid.addColumn(Estudiante::getApellido).setHeader("Apellido").setAutoWidth(true);

        estudiantesGrid.addComponentColumn(estudiante -> {
            ComboBox<Boolean> asistenciaComboBox = new ComboBox<>();
            asistenciaComboBox.setItems(true, false); // Valores booleanos: Sí y No
            asistenciaComboBox.setItemLabelGenerator(asiste -> asiste ? "Sí" : "No"); // Mostrar etiquetas "Sí" o "No"

            // Establecer "Sí" como valor predeterminado
            asistenciaComboBox.setValue(true); // Mostrar "Sí" por defecto

            // Registrar el valor inicial en el mapa
            asistenciaMap.put(estudiante, true);

            // Listener para actualizaciones
            asistenciaComboBox.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    asistenciaMap.put(estudiante, event.getValue());
                }
            });

            return asistenciaComboBox;
        }).setHeader("Asistencia").setWidth("150px").setTextAlign(ColumnTextAlign.CENTER);


        // Establecer los estudiantes en el Grid
        estudiantesGrid.setItems(estudiantes);

        // Botón para guardar la asistencia
        Button guardarButton = new Button("Guardar Asistencia", event -> {
            List<DetalleAsistencia> detalles = new ArrayList<>();

            for (Estudiante estudiante : asistenciaMap.keySet()) {
                Boolean asiste = asistenciaMap.get(estudiante);
                if (asiste != null) {
                    DetalleAsistencia detalle = new DetalleAsistencia();
                    detalle.setAsistenciaPresente(asiste); // Asegúrate de que este método esté definido en la clase DetalleAsistencia
                    detalle.setEstudiante(estudiante);
                    detalle.setAsistencia(asistencia); // Este es redundante y puede eliminarse
                    detalles.add(detalle);
                }
            }

            if (!detalles.isEmpty()) {
                 detalleAsistenciaService.saveAllByAsistencia(detalles, asistencia.getIdAsistencia());
                Notification.show("Asistencia registrada correctamente.");
                dialog.close();
            } else {
                Notification.show("Debe marcar la asistencia para al menos un estudiante.");
            }
        });

        VerticalLayout layout = new VerticalLayout(titulo, estudiantesGrid, guardarButton);
        layout.setSpacing(true);
        layout.setPadding(true);

        dialog.add(layout);
        dialog.open();
    }




    private HorizontalLayout crearFormulario() {
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setAlignItems(Alignment.BASELINE);
        formLayout.add(grupoComboBox,fechaPicker,saveButton, cancelButton);
        return formLayout;
    }

    private void actualizarGrid() {
        List<Asistencia> asistencias = asistenciaService.findAll();
        grid.setItems(asistencias);
    }
    private void guardarAsistencia() {
        if (fechaPicker.isEmpty()) {
            Notification.show("Por favor, selecciona una fecha.");
            return;
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setFecha(java.sql.Date.valueOf(fechaPicker.getValue()));

        // Puedes modificar esto para permitir que el usuario seleccione un grupo
        Grupo grupoSeleccionado = grupoService.findAll().get(0); // Ajustar según implementación
        asistencia.setGrupo(grupoSeleccionado);

        asistenciaService.save(asistencia);

        Notification.show("Asistencia guardada correctamente.");
        limpiarFormulario();
        actualizarGrid();
    }

    private void generarPDFDetalleAsistencia(Asistencia asistencia) {
        // Obtener los detalles de asistencia para la asistencia seleccionada
        List<DetalleAsistencia> detalles = detalleAsistenciaService.findAllByAsistencia(asistencia.getIdAsistencia());

        // Generar el PDF
        String pdfFileName = "asistencia_grupo_" + asistencia.getGrupo().getIdGrupo() + "_fecha_" + asistencia.getFecha() + ".pdf";
        try {
            PDFGenerator pdfGenerator = new PDFGenerator();
            ByteArrayInputStream pdfStream = pdfGenerator.generateAsistenciaPDF(detalles, asistencia);
            // Crear un recurso de flujo
            StreamResource streamResource = new StreamResource(pdfFileName, () -> pdfStream);

            // Crear un enlace dinámico para abrir el PDF
            Anchor pdfLink = new Anchor(streamResource, "Abrir PDF");
            pdfLink.setTarget("_blank"); // Abrir en una nueva ventana
            pdfLink.getStyle().set("display", "none"); // Ocultar el enlace visualmente

            // Agregar el enlace temporalmente al layout
            add(pdfLink);

            // Simular un clic para abrir el archivo en la nueva ventana
            pdfLink.getElement().callJsFunction("click");

            // Notificación de éxito
            Notification.show("PDF generado exitosamente: " + pdfFileName);
        } catch (Exception e) {
            Notification.show("Error al generar el PDF: " + e.getMessage());
        }
    }


    private void editarAsistencia(Asistencia asistencia) {
        if (asistencia != null) {
            fechaPicker.setValue(LocalDate.parse(asistencia.getFecha().toString()));


            saveButton.setText("Actualizar");
            saveButton.addClickListener(event -> {
                asistencia.setFecha(Date.valueOf(fechaPicker.getValue()));
                asistenciaService.save(asistencia);
                Notification.show("Asistencia actualizada correctamente.");
                limpiarFormulario();
                actualizarGrid();
            });
        } else {
            Notification.show("Selecciona una asistencia para editar.");
        }
    }

    private void eliminarAsistencia(Asistencia asistencia) {
        if (asistencia != null) {
            asistenciaService.deleteById(asistencia.getIdAsistencia());
            Notification.show("Asistencia eliminada correctamente.");
            actualizarGrid();
        } else {
            Notification.show("Selecciona una asistencia para eliminar.");
        }
    }

    private void limpiarFormulario() {
        fechaPicker.clear();
        saveButton.setText("Guardar");
    }
    private void cancelarEdicion() {
        limpiarFormulario();
        //asistencia = null;
    }



}

