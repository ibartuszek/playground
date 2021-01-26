package com.example.playground.view;

import com.example.playground.dal.mongo.ExampleEntry;
import com.example.playground.dal.mongo.repository.ExampleRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("/index.html")
public class MainView extends VerticalLayout {

    private final ExampleRepository repository;
    private final Grid<ExampleEntry> grid;

    public MainView(ExampleRepository repository) {
        this.repository = repository;
        this.grid = new Grid<>(ExampleEntry.class);
        add(grid);
        listEntries();
    }

    private void listEntries() {
        grid.setItems(repository.findAll());
    }

}
