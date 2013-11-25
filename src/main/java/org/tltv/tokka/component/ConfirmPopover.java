package org.tltv.tokka.component;

import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ConfirmPopover extends Popover {

    private VerticalLayout content = new VerticalLayout();

    public ConfirmPopover(String text, final ConfirmListener listener) {
        setContent(content);
        content.setMargin(true);
        content.setSpacing(true);

        Label label = new Label(text);
        content.addComponent(label);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        Button confirm = new Button("Yes", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                listener.onConfirm();
                close();
            }
        });
        Button close = new Button("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        buttons.addComponent(confirm);
        buttons.addComponent(close);
        content.addComponent(buttons);
    }

    public interface ConfirmListener {
        void onConfirm();
    }
}
