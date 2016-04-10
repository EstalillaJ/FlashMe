package com.cs380.flashme.flashme.util;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 4/6/16.
 */
public class ExpandableListItem {
    // group is the unexpanded title
    // children are what we will see when we expand the group
    private String group;
    private List<String> children;

    public ExpandableListItem(String group,  List<String> children){
        this.group = group;
        if (children == null){
            this.children = new ArrayList<>();
        }
        else {
            this.children = children;
        }
    }

    public String getGroup(){
        return group;
    }

    public List<String> getChildren(){
        return new ArrayList<String>(children);

    }

    public int getChildCount(){
        return children.size();
    }


}

