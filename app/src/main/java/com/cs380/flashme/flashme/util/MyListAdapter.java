package com.cs380.flashme.flashme.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cs380.flashme.flashme.R;

import java.util.List;

/**
 * Created by josh on 4/6/16.
 */
public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ExpandableListItem> groups;

    public MyListAdapter(Context context, List<ExpandableListItem> groups){
        this.context = context;
        this.groups = groups;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildCount();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    /**
     * This method produces unique ids for each child.
     */
    public long getChildId(int groupPosition, int childPosition) {
        return Integer.parseInt(""+groupPosition+""+childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    /**
     * This method produces the proper xml view for a group element.
     */
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //grab the particular group object
        ExpandableListItem expandableListItem = groups.get(groupPosition);

        //create a basic view for it if we dont have one
        if (convertView == null){
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.expandable_group, null);
        }

        //get the textView element within our group view and set its text
        TextView heading = (TextView) convertView.findViewById(R.id.group_title);
        heading.setText(expandableListItem.getGroup());

        return convertView;
    }

    @Override
    /**
     * This method produces the proper xml view for a child element.
     */
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = groups.get(groupPosition).getChildren().get(childPosition);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_child,null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.child_item);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
