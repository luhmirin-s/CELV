package com.github.siga111.customELV.app.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Abstract adapter for CustomELV (Custom Expandable List View).
 *
 * Stores ListView data and implements almost all necessary methods for ELV correct work.
 * Subclasses may only implement View creation and recycling.
 */
public abstract class BaseCustomELVAdapter extends BaseExpandableListAdapter {

    protected final Context mContext;
    protected List<TopLevelGroup> mGroups;
    protected OnGroupIconClickListener mOnIconClick;

    public BaseCustomELVAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Sets ListView data of type T wrapped by TopLevelGroup class.
     * @param screenGroups - list of top level list view items.
     */
    public void setGroups(List<TopLevelGroup> screenGroups) {
        this.mGroups = screenGroups;
    }

    /**
     * Sets callback for top level item expand/collapse icon click.
     * Callback should be assigned to each icon in getGroupView()
     *
     * @param listener - icon click listener.
     */
    public void setOnGroupIconClickListener(OnGroupIconClickListener listener) {
        mOnIconClick = listener;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getChildren().size();
    }

    @Override
    public TopLevelGroup getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public abstract View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent);

    @Override
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    /**
     * ELV top level item expand/collapse icon click.
     */
    public static interface OnGroupIconClickListener {

        /**
         * @param v - view that was clicked
         * @param groupPosition - index of that view.
         * @param isExpanded - true if clicked group is expanded.
         */
        void onClick(View v, int groupPosition, boolean isExpanded);
    }
}
