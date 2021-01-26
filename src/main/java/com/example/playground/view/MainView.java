package com.example.playground.view;

import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Route("/index.html")
public class MainView extends VerticalLayout {

    private final ExampleRepository repository;
    private final Grid<ExampleEntry> grid;

    private String messageFilter = "";

    MainView(ExampleRepository repository) {
        this.repository = repository;
        this.grid = createGrid();

        add(createMessageFilter());
        add(grid);
    }

    private Grid<ExampleEntry> createGrid() {
        Grid<ExampleEntry> grid = new Grid<>(ExampleEntry.class);
        grid.setItems(repository.findAll());
        return grid;
    }

    private TextField createMessageFilter() {
        TextField messageFilter = new TextField();
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
        return repository.findAll().stream()
                .filter(entry -> entry.getMessage().contains(messageFilter))
                .collect(Collectors.toList());
    }

}
