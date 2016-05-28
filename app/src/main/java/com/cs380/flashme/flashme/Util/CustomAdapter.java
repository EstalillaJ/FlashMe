package com.cs380.flashme.flashme.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cs380.flashme.flashme.CardsFromDatabase;
import com.cs380.flashme.flashme.R;

import java.util.ArrayList;

/*  Created By: Jayce Poole
*
 */

//custom adapter class for list with checkboxes
public class CustomAdapter extends ArrayAdapter{

    //fields
    private Context context;
    private int listItemRes;
    private ArrayList<String> cardFront;
    private CheckBox cb;
    private Typeface fontAwesome;
    private ArrayList<Boolean> checked;
    public CustomAdapter(Context c, int resource, ArrayList<String> cardFronts, Typeface awesomeFont){
        super(c,resource,cardFronts);

        checked = new ArrayList<Boolean>(cardFronts.size());

        for(String front : cardFronts){
            checked.add(false);
        }
        fontAwesome = awesomeFont;
        context = c;
        listItemRes = resource;
        cardFront = cardFronts;


    }

    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        //LinearLayout layout = (LinearLayout) view;
        convertView =  inflater.inflate(listItemRes, parent, false);
        //layout = (LinearLayout) inflater.inflate(listItemRes, parent, false);
        TextView displayFront = (TextView) convertView.findViewById(R.id.cardFront);
        cb = (CheckBox) convertView.findViewById(R.id.cardCheckBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean allFalse = true;
                checked.set(position, buttonView.isChecked());
                if(isChecked) {
                    ((CardsFromDatabase) context).updateCard();
                }
                for(boolean check : checked) {
                    if (check) {
                        allFalse = false;
                    }
                }
                if(allFalse){
                    ((CardsFromDatabase) context).disableAddButton();
                }
            }

        });

        cb.setChecked(checked.get(position));

        //displayFront.setTypeface(fontAwesome);
        displayFront.setTypeface(fontAwesome);
        displayFront.setText(cardFront.get(position));

        return convertView;
    }
    public ArrayList<Boolean> isChecked(){
        return checked;
    }
}
