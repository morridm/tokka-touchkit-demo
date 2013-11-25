package org.tltv.tokka.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataService {

    public static final List<CommonList> lists = new ArrayList<CommonList>();

    static {
        CommonList c = new CommonList();
        c.setName("List 1");
        lists.add(c);
    }

    public void saveList(CommonList list) {
        if (!lists.contains(list)) {
            lists.add(list);
        }
    }

    public void deleteList(CommonList list) {
        lists.remove(list);
    }

    public List<CommonList> getLists() {
        return Collections.unmodifiableList(lists);
    }

}
