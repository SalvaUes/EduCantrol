package com.educantrol.educantrol_app.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class DashboardView extends AppLayout {

    public DashboardView() {
        createHeader();
        createContent();
    }

    private void createHeader() {
        // Crear un encabezado superior
        H1 title = new H1("Dashboard - EduControl");
        title.getStyle()
                .set("font-size", "1.5em")
                .set("margin", "0");

        DrawerToggle toggle = new DrawerToggle();

        HorizontalLayout header = new HorizontalLayout(toggle, title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setPadding(true);
        header.getStyle().set("background-color", "#007BFF").set("color", "white");

        addToNavbar(header);
    }

    private void createContent() {
        // Crear las tarjetas de resumen
        HorizontalLayout stats = createStats();

        // Crear gráficos
        HorizontalLayout charts = createCharts();

        // Contenedor principal
        VerticalLayout mainContent = new VerticalLayout(stats, charts);
        mainContent.setPadding(true);
        mainContent.setSpacing(true);

        setContent(mainContent);
    }

    private HorizontalLayout createStats() {
        // Tarjeta de "Estudiantes Activos"
        Div studentsCard = createCard("Estudiantes Activos", "120", "green");

        // Tarjeta de "Profesores Activos"
        Div teachersCard = createCard("Profesores Activos", "25", "blue");

        // Tarjeta de "Clases Programadas"
        Div classesCard = createCard("Clases Programadas", "15", "orange");

        // Añadir todas las tarjetas a un layout horizontal
        HorizontalLayout statsLayout = new HorizontalLayout(studentsCard, teachersCard, classesCard);
        statsLayout.setSpacing(true);
        statsLayout.setWidthFull();
        return statsLayout;
    }

    private Div createCard(String title, String value, String color) {
        // Título de la tarjeta
        H2 cardTitle = new H2(title);
        cardTitle.getStyle().set("margin", "0").set("color", color);

        // Valor de la tarjeta
        Paragraph cardValue = new Paragraph(value);
        cardValue.getStyle()
                .set("font-size", "1.5em")
                .set("margin", "0");

        // Contenedor de la tarjeta
        Div card = new Div(cardTitle, cardValue);
        card.getStyle()
                .set("border", "1px solid #E0E0E0")
                .set("border-radius", "8px")
                .set("padding", "16px")
                .set("background-color", "#F8F9FA")
                .set("width", "30%");
        return card;
    }

    private HorizontalLayout createCharts() {
        // Gráfico de barras
        Chart barChart = createBarChart();

        // Gráfico circular
        Chart pieChart = createPieChart();

        // Añadir los gráficos a un layout horizontal
        HorizontalLayout chartsLayout = new HorizontalLayout(barChart, pieChart);
        chartsLayout.setSpacing(true);
        chartsLayout.setWidthFull();
        return chartsLayout;
    }

    private Chart createBarChart() {
        // Crear un gráfico de barras
        Chart chart = new Chart(ChartType.BAR);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Estudiantes por Grado");

        // Series de datos
        conf.addSeries(new ListSeries("Grado 1", 30));
        conf.addSeries(new ListSeries("Grado 2", 25));
        conf.addSeries(new ListSeries("Grado 3", 40));

        chart.setSizeFull();
        return chart;
    }

    private Chart createPieChart() {
        // Crear un gráfico circular
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Distribución de Profesores");

        // Series de datos
        //conf.addSeries(new ListSeries("Primaria", 10, "Secundaria", 15));

        chart.setSizeFull();
        return chart;
    }
}

