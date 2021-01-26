package com.example.playground.view;

import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;


@Route("/index.html")
public class MainView extends VerticalLayout {

    private final ExampleRepository repository;
    private final Grid<ExampleEntry> grid;

    private String messageFilter = "";
    private Instant timestampFilter = Instant.EPOCH;

    MainView(ExampleRepository repository) {
        this.repository = repository;
        this.grid = createGrid();

        add(createFilterBar());
        add(grid);
    }

    private Grid<ExampleEntry> createGrid() {
        Grid<ExampleEntry> grid = new Grid<>(ExampleEntry.class);
        grid.setItems(repository.findAll());
        return grid;
    }

    private HorizontalLayout createFilterBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setAlignItems(Alignment.START);
        bar.add(createMessageFilter());
        bar.add(createDateTimePicker());
        return bar;
    }

    private TextField createMessageFilter() {
        TextField messageFilter = new TextField();
        messageFilter.setLabel("Entries which contains:");
        messageFilter.setPlaceholder("Filter to message");
        messageFilter.setValueChangeMode(ValueChangeMode.EAGER);
        messageFilter.addValueChangeListener(e -> updateListOnMessage(e.getValue()));
        return messageFilter;
    }

    private void updateListOnMessage(String messageFilter) {
        this.messageFilter = messageFilter;
        if (!StringUtils.isEmpty(messageFilter)) {
            grid.setItems(getFilteredList());
        }
    }

    private List<ExampleEntry> getFilteredList() {
        List<ExampleEntry> exampleEntryList = timestampFilter.isAfter(Instant.EPOCH)
                ? repository.findAllByTimeStampAfter(timestampFilter)
                : repository.findAll();
        return exampleEntryList.stream()
                .filter(entry -> entry.getMessage().contains(messageFilter))
                .collect(Collectors.toList());
    }

    private DateTimePicker createDateTimePicker() {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setLabel("Entries after timestamp (UTC):");
        dateTimePicker.addValueChangeListener(e -> updateListOnDate(e.getValue()));
        return dateTimePicker;
    }

    private void updateListOnDate(LocalDateTime dateTime) {
        if (dateTime != null) {
            timestampFilter = dateTime.toInstant(ZoneOffset.UTC);
            grid.setItems(getFilteredList());
        }
    }

}
