package com.educantrol.educantrol_app.utils;

import com.educantrol.educantrol_app.model.Profesor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfesorImporter {

    public List<Profesor> importarProfesores(InputStream inputStream) throws Exception {
        List<Profesor> profesores = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0); // Usar la primera hoja del archivo
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Saltar la fila de encabezados
                continue;
            }

            Profesor profesor = new Profesor();
            profesor.setNombre(row.getCell(0).getStringCellValue());
            profesor.setApellido(row.getCell(1).getStringCellValue());
            profesor.setEspecialidad(row.getCell(2).getStringCellValue());
            profesor.setEmail(row.getCell(3).getStringCellValue());
            profesores.add(profesor);
        }

        workbook.close();
        return profesores;
    }
}