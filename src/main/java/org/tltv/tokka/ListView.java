package org.tltv.tokka;

import java.util.Collection;

import org.tltv.tokka.component.ConfirmPopover;
import org.tltv.tokka.component.ConfirmPopover.ConfirmListener;
import org.tltv.tokka.data.AbstractView;
import org.tltv.tokka.data.CommonItem;
import org.tltv.tokka.data.CommonList;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ListView extends AbstractView {

    private Button topLeftButton;
    final VerticalComponentGroup content;
    private boolean editMode = false;
    private Popover addPopover;

    private CommonList deleteTarget;

    private Button.ClickListener editClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            if (editMode) {
                swichToListMode();
            } else {
                switchToEditMode();
            }
        }
    };

    private Button.ClickListener addClickListener = new Button.ClickListener() {
        @Override
        public void buttonClick(ClickEvent event) {
            @SuppressWarnings("unchecked")
            BeanItem<CommonList> item = (BeanItem<CommonList>) event
                    .getButton().getData();
            getDataService().saveList(item.getBean());
            addPopover.close();
            switchToEditMode();
        }
    };

    private ConfirmListener confirmDeleteListener = new ConfirmListener() {

        @Override
        public void onConfirm() {
            getDataService().deleteList(deleteTarget);
            switchToEditMode();
        }
    };

    public ListView() {
        setCaption("Tokka");

        topLeftButton = new Button("", editClickListener);
        setRightComponent(topLeftButton);

        content = new VerticalComponentGroup();
        setContent(content);

        swichToListMode();
    }

    @Override
    protected void onBecomingVisible() {
        swichToListMode();
    }

    public void switchToEditMode() {
        editMode = true;
        content.removeAllComponents();
        topLeftButton.setCaption("Done");

        Button addButton = new Button("+ New List");

        content.addComponent(addButton);
        addPopover = createNamePopover(new CommonList());

        addButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                addPopover.showRelativeTo(event.getButton());
            }
        });

        for (CommonList list : getDataService().getLists()) {
            content.addComponent(createDeleteButton(list));
        }
    }

    public void swichToListMode() {
        editMode = false;
        content.removeAllComponents();
        topLeftButton.setCaption("Edit");

        for (CommonList list : getDataService().getLists()) {
            content.addComponent(createNavigationButton(list));
        }
    }

    private NavigationButton createNavigationButton(final CommonList list) {
        NavigationButton button = new NavigationButton(list.getName());
        int doneItems = calculateDoneItems(list.getItems());
        button.setDescription(doneItems + "/" + list.getItems().size());
        if (doneItems > 0 && doneItems == list.getItems().size()) {
            button.addStyleName("done");
        }
        button.addClickListener(new NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButtonClickEvent event) {
                getNavigationManager().navigateTo(new ItemListView(list));
            }
        });
        return button;
    }

    private Button createDeleteButton(final CommonList list) {
        Button button = new Button(list.getName());
        TouchKitIcon.remove.addTo(button);
        button.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                deleteTarget = list;
                ConfirmPopover confirm = new ConfirmPopover(
                        "List will be removed. Are you sure?",
                        confirmDeleteListener);
                confirm.showRelativeTo(event.getButton());
            }
        });
        return button;
    }

    private Popover createNamePopover(CommonList list) {
        Popover popover = new Popover();
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);
        popover.setContent(content);

        BeanItem<CommonList> item = new BeanItem<CommonList>(list);
        TextField name = new TextField(item.getItemProperty("name"));
        name.setNullRepresentation("");
        name.setInputPrompt("List name");

        Button addButton = new Button("Add", addClickListener);
        addButton.setData(item);

        content.addComponent(name);
        content.addComponent(addButton);
        return popover;
    }

    private int calculateDoneItems(Collection<CommonItem> items) {
        int done = 0;
        for (CommonItem i : items) {
            if (i.isDone()) {
                done++;
            }
        }
        return done;
    }
}
