package com.github.siga111.customELV.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.List;

/**
 * Upgraded version of ExpandableListView.
 *
 * Features:
 *  > Both group and children clicks are processed in same way, that allows us to store data in both of them.
 *  > Collapse/expand happens only on icon click.
 *  > Selection is sticky (and saves its position)
 *  > If group with selected child is collapsed, selection moves to group header and returns to item
 *      after that group is expanded back.
 *  > Several clicks on same item will be callback only once.
 *  > Selection can be set from outside. If selection is set to child, corresponding group will expand.
 *
 * View creation is handled in Adapter class, so it is also responsible for icon visibility.
 *
 * To do:
 *  > Collapse/Expand icon animation
 *  > Some clever abstractions to remove casts
 *  > Some configuration options.
 *
 */
public class CustomELV extends ExpandableListView {

    public static final int NO_CHILD_SELECTED = -1;

    private Context mContext;
    private BaseCustomELVAdapter mAdapter;
    private List<TopLevelGroup> mTopLevelItems;

    private OnListItemClickedListener mListener;

    private int mSelectedGroup = 0;
    private int mSelectedChild = NO_CHILD_SELECTED;


    public CustomELV(Context context) {
        super(context);
        mContext = context;
    }

    public CustomELV(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomELV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    /**
     * Sets list item click callback listener.
     * @param listener -
     */
    public void setOnListItemClickListener(OnListItemClickedListener listener) {
        mListener = listener;
    }

    /**
     * Initializes CustomELV with some data and custom adapter.
     *
     * @param items - list of top level list view items that may or may not contain any number of children.
     * @param adapter - custom list adapter that handles view creation.
     */
    public void init(List<TopLevelGroup> items, BaseCustomELVAdapter adapter) {
        mTopLevelItems = items;
        mAdapter = adapter;

        // preparing adapter
        mAdapter.setGroups(mTopLevelItems);
        mAdapter.setOnGroupIconClickListener(onIconClick);
        setAdapter(mAdapter);

        // preparing custom listeners
        setOnGroupClickListener(onGroupClick);
        setOnChildClickListener(onChildClick);
        setOnGroupExpandListener(onExpandListener);
        setOnGroupCollapseListener(onCollapseListener);
    }

    /**
     * Handles click on expand/collapse icon.
     */
    private BaseCustomELVAdapter.OnGroupIconClickListener onIconClick = new BaseCustomELVAdapter.OnGroupIconClickListener() {
        @Override
        public void onClick(View v, int groupPosition, boolean isExpanded) {
            if (isExpanded) {
                collapseGroup(groupPosition);
            } else {
                expandGroup(groupPosition);
            }
        }
    };

    /**
     * Handles clicks on group headers.
     * Click is processed only if it happened on item other than already selected.
     */
    private OnGroupClickListener onGroupClick = new OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // not same group OR previous selection was child
            if (groupPosition != mSelectedGroup || mSelectedChild != NO_CHILD_SELECTED) {
                registerClick(mTopLevelItems.get(groupPosition).getItem());
                setSelectedItem(groupPosition, NO_CHILD_SELECTED);
            }
            return true;
        }
    };

    /**
     * Handles click on children in some group.
     * Click is processed only if it happened on item other than already selected.
     */
    private OnChildClickListener onChildClick = new OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            // not same group OR another child
            if (groupPosition != mSelectedGroup || childPosition != mSelectedChild) {
                registerClick(mTopLevelItems.get(groupPosition).getChildren().get(childPosition));
                setSelectedItem(groupPosition, childPosition);
            }
            return true;
        }
    };

    /**
     * When collapsing group with selected child. Visually selection changes to group header,
     * but actual saved position still remains.
     */
    private OnGroupCollapseListener onCollapseListener = new OnGroupCollapseListener() {
        @Override
        public void onGroupCollapse(int groupPosition) {
            if (groupPosition == mSelectedGroup) {
                setSelectedItemLocally(mSelectedGroup, NO_CHILD_SELECTED);
            }
        }
    };

    /**
     * When expanding visually selected group, selection goes to saved position, if
     * there was any.
     */
    private OnGroupExpandListener onExpandListener = new OnGroupExpandListener() {
        @Override
        public void onGroupExpand(int groupPosition) {
            if (groupPosition == mSelectedGroup) {
                setSelectedItemLocally(mSelectedGroup, mSelectedChild);
            }
        }
    };

    /**
     * Sets current selection to specific item. And saves this selection.
     *
     * @param groupPosition - 0 based index of selected items group.
     * @param childPosition - 0 based index of selected item within group or
     *                      NO_CHILD_SELECTED if group header should be selected.
     */
    public void setSelectedItem(int groupPosition, int childPosition) {
        expandGroup(groupPosition);
        setSelectedItemLocally(groupPosition, childPosition);

        mSelectedGroup = groupPosition;
        mSelectedChild = childPosition;
    }

    /**
     * Visually changes selection, but doesn't save new position.
     *
     * @param groupPosition - 0 based index of selected items group.
     * @param childPosition - 0 based index of selected item within group or
     *                      NO_CHILD_SELECTED if group header should be selected.
     */
    private void setSelectedItemLocally(int groupPosition, int childPosition) {
        int index = getFlatPosition(groupPosition, childPosition);
        setItemChecked(index, true);
    }

    /**
     * Returns position of item as if list had only one level.
     * index is a 0 based index of the child item calculated as follows:
     *
     *    Group 1 [index = 0]
     *    Child item 1
     *    Child item 2
     *    Child item 3 [index = 3]
     *    Group 2 [index = 4]
     *    Child item 1
     *    Child item 2
     *    Child item 3 [index = 7]
     *
     * @param groupPosition - 0 based index of selected items group.
     * @param childPosition - 0 based index of selected item within group or
     *                      NO_CHILD_SELECTED if group header should be selected.
     * @return - items position in flat list.
     */
    private int getFlatPosition(int groupPosition, int childPosition) {
        return (childPosition == NO_CHILD_SELECTED) ?
                getFlatListPosition(getPackedPositionForGroup(groupPosition)):
                getFlatListPosition(getPackedPositionForChild(groupPosition, childPosition));
    }

    private void registerClick(Object data) {
        if (mListener != null) {
            mListener.onListItemClicked(data);
        }
    }

    /**
     * ELV item click callback.
     */
    public static interface OnListItemClickedListener {

        /**
         * Returns data that was kept in clicked list item (at any level).
         *
         * @param listItem - object with data.
         */
        void onListItemClicked(Object listItem);

    }
}
