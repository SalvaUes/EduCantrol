package com.educantrol.educantrol_app.utils;

import com.educantrol.educantrol_app.model.Asistencia;
import com.educantrol.educantrol_app.model.DetalleAsistencia;
import com.educantrol.educantrol_app.model.Estudiante;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {
    public ByteArrayInputStream generateEstudiantesPDF(List<Estudiante> estudiantes, String grupoDescripcion) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Agregar contenido al PDF
        document.add(new Paragraph("Detalle de Alumnos del Grupo: " + grupoDescripcion));
        document.add(new Paragraph(" "));
        PdfPTable table = new PdfPTable(3); // 3 columnas: ID, Nombre, Apellido

        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Apellido");

        for (Estudiante estudiante : estudiantes) {
            table.addCell(String.valueOf(estudiante.getidEstudiante()));
            table.addCell(estudiante.getNombre());
            table.addCell(estudiante.getApellido());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateAsistenciaPDF(List<DetalleAsistencia> detalles, Asistencia asistencia) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Detalles de Asistencia"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Grupo: " + asistencia.getGrupo().getDescripcion()));
        document.add(new Paragraph("Fecha: " + asistencia.getFecha()));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        PdfPTable table = new PdfPTable(3); // ID, Nombre, Asistencia
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Asistencia");

        for (DetalleAsistencia detalle : detalles) {
            table.addCell(detalle.getEstudiante().getidEstudiante().toString());
            table.addCell(detalle.getEstudiante().getNombre() + " " + detalle.getEstudiante().getApellido());
            table.addCell(detalle.getAsistenciaPresente() ? "Presente" : "Ausente");
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

}

