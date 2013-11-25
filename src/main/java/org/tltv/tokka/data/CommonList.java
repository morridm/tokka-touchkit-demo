package org.tltv.tokka.data;

import java.util.LinkedHashSet;
import java.util.Set;

public class CommonList {

    private String name;
    private final Set<CommonItem> items = new LinkedHashSet<CommonItem>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CommonItem> getItems() {
        return items;
    }
}
