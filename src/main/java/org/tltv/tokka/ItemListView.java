package org.tltv.tokka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tltv.tokka.data.AbstractView;
import org.tltv.tokka.data.CommonItem;
import org.tltv.tokka.data.CommonList;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.addon.touchkit.ui.Toolbar;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class ItemListView extends AbstractView {

    private VerticalComponentGroup content;

    private Button filterAll;
    private Button filterDone;
    private Button filterTodo;

    private final CommonList list;
    private FilterMode mode = FilterMode.All;

    private CommonItem newItem;

    private enum FilterMode {
        All,
        Done,
        Todo;
    }

    private Button.ClickListener filterAllListener = new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            mode = FilterMode.All;
            refresh();
        }
    };

    private Button.ClickListener filterTodoListener = new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            mode = FilterMode.Todo;
            refresh();
        }
    };

    private Button.ClickListener filterDoneListener = new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            mode = FilterMode.Done;
            refresh();
        }
    };

    private Button.ClickListener addListener = new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            list.getItems().add(newItem);
            refresh();
        }

    };

    public ItemListView(CommonList list) {
        this.list = list;

        setRightComponent(null);
        setCaption(list.getName());

        AbstractComponent back = (AbstractComponent) getLeftComponent();
        back.setCaption("");
        TouchKitIcon.list.addTo(back);

        content = new VerticalComponentGroup();
        setContent(content);

        Toolbar tools = new Toolbar();

        filterAll = new Button("All", filterAllListener);
        TouchKitIcon.asterisk.addTo(filterAll);

        filterTodo = new Button("Todo", filterTodoListener);
        TouchKitIcon.checkEmpty.addTo(filterTodo);

        filterDone = new Button("Done", filterDoneListener);
        TouchKitIcon.check.addTo(filterDone);

        tools.addComponent(filterAll);
        tools.addComponent(filterTodo);
        tools.addComponent(filterDone);

        setToolbar(tools);

        refresh();
    }

    private void refresh() {
        switch (mode) {
        case All:
            updateList(filterItems(null));
            break;
        case Todo:
            updateList(filterItems(new CompletenessFilter(false)));
            break;
        case Done:
            updateList(filterItems(new CompletenessFilter(true)));
            break;
        default:
            updateList(filterItems(null));
            break;
        }
    }

    private void updateList(Collection<CommonItem> items) {
        content.removeAllComponents();

        createNewItemEditor();

        BeanItem<CommonItem> beainItem;
        Switch s;
        for (CommonItem item : items) {
            beainItem = new BeanItem<CommonItem>(item);

            s = new Switch(item.getCaption());
            s.setPropertyDataSource(beainItem.getItemProperty("done"));
            content.addComponent(s);
        }
    }

    private void createNewItemEditor() {
        HorizontalLayout editor = new HorizontalLayout();
        editor.setSpacing(true);

        newItem = new CommonItem();
        BeanItem<CommonItem> beanItem = new BeanItem<CommonItem>(newItem);

        TextField captionField = new TextField(
                beanItem.getItemProperty("caption"));
        captionField.setInputPrompt("New Item");
        captionField.setNullRepresentation("");
        editor.addComponent(captionField);

        Button addButton = new Button("", addListener);
        addButton.setClickShortcut(KeyCode.ENTER);
        TouchKitIcon.plus.addTo(addButton);
        editor.addComponent(addButton);

        content.addComponent(editor);
    }

    private Collection<CommonItem> filterItems(ItemFilter filter) {
        if (filter == null) {
            return list.getItems();
        }
        List<CommonItem> items = new ArrayList<CommonItem>();
        for (CommonItem item : list.getItems()) {
            if (filter.passesFilter(item)) {
                items.add(item);
            }
        }

        return items;
    }

    public interface ItemFilter {
        boolean passesFilter(CommonItem item);
    }

    public class CompletenessFilter implements ItemFilter {

        private final boolean complete;

        public CompletenessFilter(boolean complete) {
            this.complete = complete;
        }

        @Override
        public boolean passesFilter(CommonItem item) {
            return complete == item.isDone();
        }

    }
}
