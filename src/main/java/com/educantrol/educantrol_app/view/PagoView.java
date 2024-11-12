import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.educantrol.models.Pago;
import com.educantrol.services.PagoService;

import java.util.List;

@Route("pagos") // URL path for this view
public class PagoView extends VerticalLayout {

    private final PagoService pagoService;
    private final Grid<Pago> grid = new Grid<>(Pago.class);

    private TextField estudianteId = new TextField("ID Estudiante");
    private TextField monto = new TextField("Monto");
    private TextField fechaPago = new TextField("Fecha de Pago (YYYY-MM-DD)");
    private TextField concepto = new TextField("Concepto");
    private Button saveButton = new Button("Guardar");
    private Button deleteButton = new Button("Eliminar");

    private Pago pagoSeleccionado;

    @Autowired
    public PagoView(PagoService pagoService) {
        this.pagoService = pagoService;

        // Configure the grid
        grid.setColumns("idPago", "estudiante.idEstudiante", "monto", "fechaPago", "concepto");
        grid.getColumnByKey("estudiante.idEstudiante").setHeader("ID Estudiante");
        grid.setItems(pagoService.findAll());
        grid.asSingleSelect().addValueChangeListener(event -> editarPago(event.getValue()));

        // Configure buttons
        saveButton.addClickListener(e -> guardarPago());
        deleteButton.addClickListener(e -> eliminarPago());

        // Form layout
        HorizontalLayout formLayout = new HorizontalLayout(estudianteId, monto, fechaPago, concepto, saveButton, deleteButton);

        // Add components to the view
        add(formLayout, grid);

        actualizarLista();
    }

    private void actualizarLista() {
        List<Pago> pagos = pagoService.findAll();
        grid.setItems(pagos);
    }

    private void editarPago(Pago pago) {
        if (pago != null) {
            pagoSeleccionado = pago;
            estudianteId.setValue(pago.getEstudiante() != null ? pago.getEstudiante().getIdEstudiante().toString() : "");
            monto.setValue(pago.getMonto().toString());
            fechaPago.setValue(pago.getFechaPago() != null ? pago.getFechaPago().toString() : "");
            concepto.setValue(pago.getConcepto());
        } else {
            pagoSeleccionado = null;
            estudianteId.clear();
            monto.clear();
            fechaPago.clear();
            concepto.clear();
        }
    }

    private void guardarPago() {
        if (pagoSeleccionado == null) {
            pagoSeleccionado = new Pago();
        }
        try {
            Integer estudianteIdInt = Integer.parseInt(estudianteId.getValue());
            Double montoDouble = Double.parseDouble(monto.getValue());
            
            pagoSeleccionado.setEstudiante(pagoService.findEstudianteById(estudianteIdInt)); // assuming pagoService has a method to fetch Estudiante by ID
            pagoSeleccionado.setMonto(montoDouble);
            pagoSeleccionado.setFechaPago(java.sql.Date.valueOf(fechaPago.getValue())); // assumes valid date format YYYY-MM-DD
            pagoSeleccionado.setConcepto(concepto.getValue());

            pagoService.save(pagoSeleccionado);
            Notification.show("Pago guardado");
            actualizarLista();
            editarPago(null);
        } catch (Exception e) {
            Notification.show("Error al guardar el pago: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void eliminarPago() {
        if (pagoSeleccionado != null) {
            pagoService.deleteById(pagoSeleccionado.getIdPago());
            Notification.show("Pago eliminado");
            actualizarLista();
            editarPago(null);
        } else {
            Notification.show("Seleccione un pago para eliminar");
        }
    }
}
