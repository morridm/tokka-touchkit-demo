package org.tltv.tokka.data;

import com.vaadin.addon.touchkit.ui.NavigationView;

public class AbstractView extends NavigationView {

    private final DataService dataService = new DataService();

    public DataService getDataService() {
        return dataService;
    }

}
