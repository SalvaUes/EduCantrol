package com.educantrol.educantrol_app.view;

import com.educantrol.educantrol_app.model.Estudiante;
import com.educantrol.educantrol_app.model.Grupo;
import com.educantrol.educantrol_app.model.Horario;
import com.educantrol.educantrol_app.model.Materia;
import com.educantrol.educantrol_app.model.Profesor;
import com.educantrol.educantrol_app.model.DetalleGrupo;
import com.educantrol.educantrol_app.service.EstudianteService;
import com.educantrol.educantrol_app.service.GrupoService;
import com.educantrol.educantrol_app.service.HorarioService;
import com.educantrol.educantrol_app.service.MateriaService;
import com.educantrol.educantrol_app.service.ProfesorService;
import com.educantrol.educantrol_app.service.DetalleGrupoService;
import com.educantrol.educantrol_app.utils.PDFGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Route(value = "grupos", layout = MainLayout.class)
public class GrupoView extends VerticalLayout {

    private final GrupoService grupoService;
    private final MateriaService materiaService;
    private final HorarioService horarioService;
    private final ProfesorService profesorService;
    private final EstudianteService estudianteService;
    private final DetalleGrupoService detalleGrupoService;
    private final TextField descripcion = new TextField("DEscripcion");
    private ComboBox<Materia> materiaComboBox;
    private ComboBox<Horario> horarioComboBox;
    private ComboBox<Profesor> profesorComboBox;
    private Button saveButton;
    private Button cancelButton;

    private Grid<Grupo> grid;
    private Grupo grupoSeleccionado;

    @Autowired
    public GrupoView(GrupoService grupoService, MateriaService materiaService, HorarioService horarioService, ProfesorService profesorService, EstudianteService estudianteService, DetalleGrupoService detalleGrupoService) {
        this.grupoService = grupoService;
        this.materiaService = materiaService;
        this.horarioService = horarioService;
        this.profesorService = profesorService;
        this.estudianteService = estudianteService;
        this.detalleGrupoService = detalleGrupoService;

        // Configurar layout principal
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        configurarFormulario();
        configurarGrid();

        add(new H3("Gestión de Grupos"), crearFormulario(), grid);

        actualizarGrid();
    }

    private void configurarFormulario() {
        materiaComboBox = new ComboBox<>("Materia");
        materiaComboBox.setItems(materiaService.findAll());
        materiaComboBox.setItemLabelGenerator(Materia::getNombre);

        horarioComboBox = new ComboBox<>("Horario");
        horarioComboBox.setItems(horarioService.findAll());
        horarioComboBox.setItemLabelGenerator(horario -> horario.getDiaSemana() + " " + horario.getHoraInicio() + " - " + horario.getHoraFin());

        profesorComboBox = new ComboBox<>("Profesor");
        profesorComboBox.setItems(profesorService.findAll());
        profesorComboBox.setItemLabelGenerator(profesor -> profesor.getNombre() + " " + profesor.getApellido());

        saveButton = new Button("Guardar", event -> guardarGrupo());
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

        formLayout.add(materiaComboBox, horarioComboBox, profesorComboBox,descripcion, saveButton, cancelButton);
        return formLayout;
    }

    private void configurarGrid() {
        grid = new Grid<>(Grupo.class, false);
        grid.removeAllColumns();
        grid.addColumn(Grupo::getIdGrupo).setHeader("ID").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(Grupo::getDescripcion).setHeader("Descripcion").setWidth("200px").setFlexGrow(0);
        grid.addColumn(grupo -> grupo.getMateria().getNombre()).setHeader("Materia").setWidth("150px").setFlexGrow(0);
        grid.addColumn(grupo -> grupo.getHorario().getDiaSemana() + " " + grupo.getHorario().getHoraInicio() + " - " + grupo.getHorario().getHoraFin()).setHeader("Horario").setWidth("300px").setFlexGrow(0);;
        grid.addColumn(grupo -> grupo.getProfesor().getNombre()+ " " + grupo.getProfesor().getApellido()).setHeader("Profesor").setWidth("150px").setFlexGrow(0);

        // Nueva columna para mostrar el número de estudiantes
        grid.addColumn(grupo -> detalleGrupoService.countByGrupo(grupo))
                .setHeader("Número de Estudiantes").setWidth("150px").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);

        grid.addComponentColumn(this::crearBotonesAccion).setHeader("Acciones");

        //grid.setHeight("400px");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editarGrupo(event.getValue());
            }
        });
    }

    private HorizontalLayout crearBotonesAccion(Grupo grupo) {
        Button editarButton = new Button("Editar", click -> editarGrupo(grupo));
        editarButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        editarButton.getStyle().set("background-color", "#17A2B8");
        editarButton.getStyle().set("color", "white");

        Button eliminarButton = new Button("Eliminar", click -> eliminarGrupo(grupo));
        eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        eliminarButton.getStyle().set("background-color", "#DC3545");
        eliminarButton.getStyle().set("color", "white");

        Button agregarAlumnosButton = new Button("Gestionar Estudiantes", click -> mostrarModalGestionarAlumnos(grupo));
        agregarAlumnosButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        agregarAlumnosButton.getStyle().set("background-color", "#0D6EFD");
        agregarAlumnosButton.getStyle().set("color", "white");

        // Botón para generar PDF con el detalle de alumnos
        Button detalleAlumnosButton = new Button("PDF Estudiantes", click -> generarPDFDetalleAlumnos(grupo));
        detalleAlumnosButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        return new HorizontalLayout(editarButton, eliminarButton, agregarAlumnosButton, detalleAlumnosButton);
    }

    private void generarPDFDetalleAlumnos(Grupo grupo) {
        // Obtener la lista de estudiantes asociados al grupo
        List<Estudiante> estudiantes = estudianteService.findEstudianteAsociadosAlGrupo(grupo.getIdGrupo());

        String pdfFileName = "detalle_alumnos_grupo_" + grupo.getIdGrupo() + ".pdf";

        try {
            // Generar el archivo PDF en un StreamResource
            PDFGenerator pdfGenerator = new PDFGenerator();
            ByteArrayInputStream pdfStream = pdfGenerator.generateEstudiantesPDF(estudiantes, grupo.getDescripcion());

            // Crear un recurso de flujo
            StreamResource streamResource = new StreamResource(pdfFileName, () -> pdfStream);

            // Crear un enlace para descargar o visualizar el PDF
            Anchor pdfLink = new Anchor(streamResource, "Abrir PDF");
            pdfLink.setTarget("_blank"); // Abrir en una nueva ventana
            pdfLink.getStyle().set("display", "none"); // Ocultar visualmente el enlace

            // Agregar el enlace al layout
            add(pdfLink);

            // Simular clic para abrir el PDF automáticamente
            pdfLink.getElement().callJsFunction("click");

            Notification.show("PDF generado exitosamente: " + pdfFileName);
        } catch (Exception e) {
            Notification.show("Error al generar el PDF: " + e.getMessage());
        }
    }



    private void mostrarModalGestionarAlumnos(Grupo grupo) {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        dialog.setHeight("600px");

        // Título del modal con el nombre del grupo
        H3 titulo = new H3("Gestionar Alumnos para el Grupo: " + grupo.getDescripcion());
        titulo.getStyle().set("text-align", "center");

        // Obtener estudiantes asociados y no asociados
        List<Estudiante> estudiantesAsociados = estudianteService.findEstudianteAsociadosAlGrupo(grupo.getIdGrupo());
        List<Estudiante> estudiantesNoAsociados = estudianteService.findEstudianteNoAsociadosAlGrupo(grupo.getIdGrupo());

        // Combinar ambas listas para el Grid
        List<Estudiante> todosEstudiantes = new ArrayList<>();
        todosEstudiantes.addAll(estudiantesAsociados);
        todosEstudiantes.addAll(estudiantesNoAsociados);

        // Declarar el Grid
        Grid<Estudiante> estudiantesGrid = new Grid<>(Estudiante.class, false);

        // Barra de búsqueda
        TextField searchField = new TextField("Buscar estudiante");
        searchField.setWidthFull();
        searchField.setPlaceholder("Ingrese nombre o apellido...");
        searchField.addValueChangeListener(event -> {
            String filtro = event.getValue().trim().toLowerCase();
            List<Estudiante> estudiantesFiltrados = todosEstudiantes.stream()
                    .filter(e -> e.getNombre().toLowerCase().contains(filtro) || e.getApellido().toLowerCase().contains(filtro))
                    .toList();
            estudiantesGrid.setItems(estudiantesFiltrados);
        });

        // Configurar el Grid
        estudiantesGrid.addColumn(Estudiante::getidEstudiante).setHeader("ID").setWidth("70px").setTextAlign(ColumnTextAlign.CENTER);
        estudiantesGrid.addColumn(Estudiante::getNombre).setHeader("Nombre").setAutoWidth(true);
        estudiantesGrid.addColumn(Estudiante::getApellido).setHeader("Apellido").setAutoWidth(true);

        // Botón dinámico en cada fila
        estudiantesGrid.addComponentColumn(estudiante -> {
            boolean esAsociado = estudiantesAsociados.contains(estudiante);

            Button actionButton = new Button(esAsociado ? "Eliminar" : "Agregar");
            actionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            actionButton.getStyle().set("background-color", esAsociado ? "#DC3545" : "#0D6EFD");
            actionButton.getStyle().set("color", "white");

            actionButton.addClickListener(click -> {
                if ("Agregar".equals(actionButton.getText())) {
                    // Agregar estudiante al grupo
                    DetalleGrupo detalleGrupo = new DetalleGrupo();
                    detalleGrupo.setGrupo(grupo);
                    detalleGrupo.setEstudiante(estudiante);
                    detalleGrupoService.save(detalleGrupo);

                    Notification.show("Alumno agregado exitosamente.");
                    estudiantesAsociados.add(estudiante);
                    estudiantesNoAsociados.remove(estudiante);

                    // Cambiar el botón a "Eliminar"
                    actionButton.setText("Eliminar");
                    actionButton.getStyle().set("background-color", "#DC3545");
                    actionButton.getStyle().set("color", "white");
                } else {
                    // Eliminar estudiante del grupo
                    DetalleGrupo detalleGrupo = detalleGrupoService.findByGrupoAndEstudiante(grupo, estudiante);
                    if (detalleGrupo != null) {
                        detalleGrupoService.delete(detalleGrupo);
                        Notification.show("Alumno eliminado del grupo.");
                        estudiantesAsociados.remove(estudiante);
                        estudiantesNoAsociados.add(estudiante);

                        // Cambiar el botón a "Agregar"
                        actionButton.setText("Agregar");
                        actionButton.getStyle().set("background-color", "#0D6EFD");
                        actionButton.getStyle().set("color", "white");
                    }
                }

                // Actualizar Grid
                todosEstudiantes.clear();
                todosEstudiantes.addAll(estudiantesAsociados);
                todosEstudiantes.addAll(estudiantesNoAsociados);
                estudiantesGrid.setItems(todosEstudiantes);
                actualizarGrid();
            });

            return actionButton;
        }).setHeader("Acción").setWidth("120px").setTextAlign(ColumnTextAlign.CENTER);

        // Establecer estudiantes iniciales en el Grid
        estudiantesGrid.setItems(todosEstudiantes);

        // Layout del diálogo
        VerticalLayout dialogLayout = new VerticalLayout(titulo, searchField, estudiantesGrid);
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        dialog.add(dialogLayout);
        dialog.open();
     }


    private void guardarGrupo() {
        if (!validarFormulario()) {
            return;
        }

        if (grupoSeleccionado == null) {
            grupoSeleccionado = new Grupo();
        }

        grupoSeleccionado.setMateria(materiaComboBox.getValue());
        grupoSeleccionado.setHorario(horarioComboBox.getValue());
        grupoSeleccionado.setProfesor(profesorComboBox.getValue());
        grupoSeleccionado.setDescripcion(descripcion.getValue());

        grupoService.save(grupoSeleccionado);
        Notification.show("Grupo guardado exitosamente");

        limpiarFormulario();
        actualizarGrid();
        grupoSeleccionado = null;
    }

    private void editarGrupo(Grupo grupo) {
        if (grupo != null) {
            grupoSeleccionado = grupo;

            materiaComboBox.setValue(grupo.getMateria());
            horarioComboBox.setValue(grupo.getHorario());
            profesorComboBox.setValue(grupo.getProfesor());

            saveButton.setText("Actualizar");
        }
    }

    private void eliminarGrupo(Grupo grupo) {
        grupoService.deleteById(grupo.getIdGrupo());
        Notification.show("Grupo eliminado exitosamente");
        actualizarGrid();
    }

    private void cancelarEdicion() {
        limpiarFormulario();
        grupoSeleccionado = null;
    }

    private void limpiarFormulario() {
        materiaComboBox.clear();
        horarioComboBox.clear();
        profesorComboBox.clear();

        saveButton.setText("Guardar");
    }

    private void actualizarGrid() {
        List<Grupo> grupos = grupoService.findAll();
        grid.setItems(grupos);
    }

    private boolean validarFormulario() {
        Materia materia = materiaComboBox.getValue();
        Horario horario = horarioComboBox.getValue();
        Profesor profesor = profesorComboBox.getValue();

        if (materia == null || horario == null || profesor == null) {
            Notification.show("Todos los campos son obligatorios");
            return false;
        }

        return true;
    }
}
