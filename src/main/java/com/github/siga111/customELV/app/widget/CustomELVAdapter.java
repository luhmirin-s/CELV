package com.github.siga111.customELV.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siga111.customELV.app.Data;
import com.github.siga111.customELV.app.R;

/**
 * Example of simplest BaseCustomELVAdapter implementation.
 *
 */
public class CustomELVAdapter extends BaseCustomELVAdapter {

    public CustomELVAdapter(Context context) {
        super(context);
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_parent, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.parent_title);
        title.setText(((Data) getGroup(groupPosition).getItem()).title);

        ImageView arrow = (ImageView) convertView.findViewById(R.id.parent_arrow);

        if (!mGroups.get(groupPosition).hasChildren()) {
            arrow.setVisibility(View.GONE);
        } else {
            arrow.setVisibility(View.VISIBLE);

            // This part is pretty important!
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIconClick != null) {
                        mOnIconClick.onClick(v, groupPosition, isExpanded);
                    } else {
                        throw new RuntimeException("There should be onIconClickListener");
                    }
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.child_title);
        title.setText(((Data) mGroups.get(groupPosition).getChildren().get(childPosition)).title);
        return convertView;
    }

}
