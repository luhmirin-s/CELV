package com.github.siga111.customELV.app.widget;


import java.util.ArrayList;
import java.util.List;

/**
 * Container for top level item information and its children.
 */
public class TopLevelGroup {

    private Object mItem;

    private List<Object> mChildren;

    public TopLevelGroup(Object item) {
        this.mItem = item;
        mChildren = new ArrayList<Object>();
    }

    public Object getItem() {
        return mItem;
    }

    public void add(Object child) {
        mChildren.add(child);
    }

    public void remove(Object child) {
        mChildren.remove(child);
    }

    public boolean hasChildren() {
        return mChildren.size() != 0;
    }

    public List<Object> getChildren() {
        return mChildren;
    }

}
