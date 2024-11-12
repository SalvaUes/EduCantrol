import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.educantrol.models.Estudiante;
import com.educantrol.services.EstudianteService;

@Route("estudiantes") // Define la URL de la vista
public class EstudianteView extends VerticalLayout {

    private final EstudianteService estudianteService;
    private final Grid<Estudiante> grid = new Grid<>(Estudiante.class);

    private TextField nombre = new TextField("Nombre");
    private TextField apellido = new TextField("Apellido");
    private TextField telefono = new TextField("Teléfono");
    private TextField email = new TextField("Email");
    private Button saveButton = new Button("Guardar");
    private Button deleteButton = new Button("Eliminar");

    private Estudiante estudianteSeleccionado;

    @Autowired
    public EstudianteView(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
        
        // Configurar el Grid
        grid.setColumns("idEstudiante", "nombre", "apellido", "telefono", "email");
        grid.setItems(estudianteService.findAll());
        grid.asSingleSelect().addValueChangeListener(event -> editarEstudiante(event.getValue()));

        // Configurar botones
        saveButton.addClickListener(e -> guardarEstudiante());
        deleteButton.addClickListener(e -> eliminarEstudiante());

        // Layout de formulario
        HorizontalLayout formLayout = new HorizontalLayout(nombre, apellido, telefono, email, saveButton, deleteButton);

        // Agregar componentes al layout
        add(formLayout, grid);

        actualizarLista();
    }

    private void actualizarLista() {
        grid.setItems(estudianteService.findAll());
    }

    private void editarEstudiante(Estudiante estudiante) {
        if (estudiante != null) {
            estudianteSeleccionado = estudiante;
            nombre.setValue(estudiante.getNombre());
            apellido.setValue(estudiante.getApellido());
            telefono.setValue(estudiante.getTelefono());
            email.setValue(estudiante.getEmail());
        } else {
            estudianteSeleccionado = null;
            nombre.clear();
            apellido.clear();
            telefono.clear();
            email.clear();
        }
    }

    private void guardarEstudiante() {
        if (estudianteSeleccionado == null) {
            estudianteSeleccionado = new Estudiante();
        }
        estudianteSeleccionado.setNombre(nombre.getValue());
        estudianteSeleccionado.setApellido(apellido.getValue());
        estudianteSeleccionado.setTelefono(telefono.getValue());
        estudianteSeleccionado.setEmail(email.getValue());

        estudianteService.save(estudianteSeleccionado);
        Notification.show("Estudiante guardado");
        actualizarLista();
        editarEstudiante(null);
    }

    private void eliminarEstudiante() {
        if (estudianteSeleccionado != null) {
            estudianteService.deleteById(estudianteSeleccionado.getIdEstudiante());
            Notification.show("Estudiante eliminado");
            actualizarLista();
            editarEstudiante(null);
        } else {
            Notification.show("Seleccione un estudiante para eliminar");
        }
    }
}
