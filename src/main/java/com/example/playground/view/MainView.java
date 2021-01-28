package com.example.playground.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Route("/index.html")
public class MainView extends VerticalLayout {

    private final ExampleService service;
    private final Grid<ExampleModel> grid;
    private final ExampleModelEditor modelEditor;

    private String messageFilter = "";
    private LocalDateTime timestampFilter = LocalDateTime.MIN;
    private String zoneId = "UTC";

    MainView(ExampleService service, ExampleModelEditor modelEditor) {
        this.service = service;
        this.modelEditor = modelEditor;
        UI.getCurrent().getPage().retrieveExtendedClientDetails(extendedClientDetails -> {
            this.zoneId = extendedClientDetails.getTimeZoneId();
        });
        this.modelEditor.setZoneId(this.zoneId);
        this.modelEditor.setChangeHandler(() -> {
            modelEditor.setVisible(false);
            getFilteredList();
        });
        this.grid = createGrid();

        add(createFilterBar());
        add(grid);
        add(this.modelEditor);
    }

    private Grid<ExampleModel> createGrid() {
        Grid<ExampleModel> grid = new Grid<>(ExampleModel.class);

        grid.setHeight("300px");
        grid.setColumns("id", "message", "timeStamp", "date");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        // edit
        grid.asSingleSelect().addValueChangeListener(e -> modelEditor.editModel(e.getValue()));

        grid.setItems(service.findAll(zoneId));
        return grid;
    }

    private HorizontalLayout createFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setAlignItems(Alignment.START);
        bar.add(createMessageFilter());
        bar.add(createDateTimePicker());
        bar.add(createAddModelButton());
        return bar;
    }

    private TextField createMessageFilter() {
        TextField messageFilter = new TextField();
        messageFilter.setLabel("Models which contains:");
        messageFilter.setPlaceholder("Filter to message");
        messageFilter.setValueChangeMode(ValueChangeMode.EAGER);
        messageFilter.addValueChangeListener(e -> updateListOnMessage(e.getValue()));
        return messageFilter;
    }

    private void updateListOnMessage(String messageFilter) {
        this.messageFilter = messageFilter;
        if (this.messageFilter != null) {
            grid.setItems(getFilteredList());
        }
    }

    private List<ExampleModel> getFilteredList() {
        List<ExampleModel> exampleModelList = timestampFilter.isAfter(LocalDateTime.MIN)
                ? service.findAllAfter(timestampFilter, zoneId)
                : service.findAll(zoneId);
        return exampleModelList.stream()
                .filter(model -> model.getMessage().contains(messageFilter))
                .collect(Collectors.toList());
    }

    private DateTimePicker createDateTimePicker() {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setLabel("Models after timestamp (UTC):");
        dateTimePicker.addValueChangeListener(e -> updateListOnDate(e.getValue()));
        return dateTimePicker;
    }

    private void updateListOnDate(LocalDateTime dateTime) {
        if (dateTime != null) {
            timestampFilter = dateTime;
            grid.setItems(getFilteredList());
        }
    }

    private Div createAddModelButton() {
        Div container = new Div();
        Label label = new Label();
        label.setText("To create and edit an model");
        Button button = new Button("New model", VaadinIcon.PLUS.create());
        button.addClickListener(e -> modelEditor.addNewModel());
        container.add(label, button);
        return container;
    }

}
