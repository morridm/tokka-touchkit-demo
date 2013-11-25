package org.tltv.tokka;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * The UI's "main" class
 */
@SuppressWarnings("serial")
@Widgetset("org.tltv.tokka.gwt.AppWidgetSet")
@Theme("tokka")
public class MyTouchKitUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        final NavigationManager navigationManager = new NavigationManager();

        navigationManager.setCurrentComponent(new ListView());

        setContent(navigationManager);
    }
}
