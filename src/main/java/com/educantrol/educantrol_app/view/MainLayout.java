package com.educantrol.educantrol_app.view;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.LumoUtility;

import javax.swing.text.html.ListView;

//@Theme(themeFolder = "multifactorial")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }


    private void createHeader() {
        H1 logo = new H1("Sistema EduControl");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }



    private void createDrawer() {
        RouterLink estudiantesLink = new RouterLink("Estudiantes", EstudianteView.class);
        RouterLink profesoresLink = new RouterLink("Profesores", ProfesorView.class);
        RouterLink materiasLink = new RouterLink("Materias", MateriaView.class);
        RouterLink periodosLink = new RouterLink("Períodos", PeriodoView.class);
        RouterLink horariosLink = new RouterLink("Horarios", HorarioView.class);
        RouterLink gruposLink = new RouterLink("Grupos", GrupoView.class);
        RouterLink asistenciaLink = new RouterLink("Asistencia", AsistenciaView.class);
    
        VerticalLayout nav = new VerticalLayout(estudiantesLink, profesoresLink,materiasLink, periodosLink, horariosLink, gruposLink, asistenciaLink);
        nav.setSpacing(false);
        nav.setPadding(true);
        nav.setWidthFull();
        nav.getStyle().set("background-color", "#F8F9FA").set("font-size", "16px");
    
        addToDrawer(nav);
    }
    
}