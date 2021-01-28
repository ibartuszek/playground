package com.example.playground.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

@SpringComponent
@UIScope
public class ExampleModelEditor extends VerticalLayout implements KeyNotifier {

    private final ExampleService service;

    private ExampleModel model;

    private TextField id = new TextField("id");
    private TextField message = new TextField("message");
    private DateTimePicker timeStamp = new DateTimePicker("timeStamp");
    private DateTimePicker date = new DateTimePicker("date");

    private Button save = new Button("save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("cancel");
    private Button delete = new Button("delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<ExampleModel> binder = new Binder<>(ExampleModel.class);
    private ChangeHandler changeHandler;
    private String zoneId = "";

    @Autowired
    ExampleModelEditor(ExampleService service) {
        this.service = service;

        add(id, message, timeStamp, date, actions);
        binder.bindInstanceFields(this);

        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editModel(model));

        setVisible(false);
    }

    private void save() {
        service.save(model, zoneId);
        changeHandler.onChange();
    }

    private void delete() {
        service.delete(model, zoneId);
        changeHandler.onChange();
    }

    public void addNewModel() {
        editModel(ExampleModel.builder().build());
    }

    public void editModel(final ExampleModel exampleModel) {
        if (exampleModel == null) {
            setVisible(false);
            return;
        }
        boolean persisted = exampleModel.getId() != null;
        setModel(exampleModel, persisted);
        cancel.setVisible(persisted);
        binder.setBean(model);
        setVisible(true);
    }

    private void setModel(ExampleModel model, boolean persisted) {
        if (persisted) {
            this.model = service.findById(model.getId(), zoneId).orElseThrow(() ->
                    new NoSuchElementException("Model with id=" + model.getId() + "not found!"));
        } else {
            this.model = model;
        }
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        this.changeHandler = changeHandler;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public interface ChangeHandler {
        void onChange();
    }

}
