package com.mekilah.codepath.todo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.IllegalFormatException;

/**
 * Created by mekilah on 1/15/15.
 */
public class AddOrEditFragment extends DialogFragment {
    public AddOrEditFragment(){}

    public interface AddOrEditFragmentAcceptedListener{
        public void onAcceptPressedInAddOrEditFragment(AddOrEditFragmentData data);
    }

    public static class AddOrEditFragmentData implements Serializable{
        public String name;
        private int year;
        private int month;
        private int dayOfMonth;
        public String dateString;

        @Override
        public String toString() {
            return name + " " + dateString;
        }

        public void setDateFromData(int year, int month, int day){
            this.year=year;
            this.month=month;
            this.dayOfMonth=day;

            StringBuilder sb = new StringBuilder();
            sb.append(this.year);
            sb.append('/');

            if(this.month < 10){
                sb.append('0');
            }

            sb.append(this.month);

            sb.append('/');

            if(this.dayOfMonth < 10){
                sb.append('0');
            }

            sb.append(this.dayOfMonth);

            this.dateString = sb.toString();
        }

        //assumes string is "YYYY/MM/DD"
        public boolean setDateFromString(String s){
            String[] strs = s.split("/");
            if(strs.length != 3){
                return false;
            }
            try {
                this.year = Integer.parseInt(strs[0].toString());
                this.month = Integer.parseInt(strs[1].toString());
                this.dayOfMonth = Integer.parseInt(strs[2].toString());
            }catch (NumberFormatException e){
                e.printStackTrace();
                return false;
            }
            this.dateString = s;
            return true;
        }

        public AddOrEditFragmentData(){}
        public AddOrEditFragmentData(String n, DatePicker datePicker){
            this.name = n;

            //date picker's month is [0,11]
            this.setDateFromData(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
        }

    }

    public static AddOrEditFragment newInstance(String title){
        AddOrEditFragment frag = new AddOrEditFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_or_edit_item, container);
        getDialog().setTitle(getArguments().getString("title", "Add or edit item"));
        final EditText etItemName = (EditText) view.findViewById(R.id.etFragAddOrEditItemName);
        etItemName.requestFocus();

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onCancel");
                getDialog().dismiss();
            }
        });

        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onAccept");
                DatePicker datePicker = (DatePicker) getDialog().findViewById(R.id.dpAddOrEditDatePicker);
                AddOrEditFragmentData data = new AddOrEditFragmentData(etItemName.getText().toString(), datePicker);
                AddOrEditFragmentAcceptedListener listener = (AddOrEditFragmentAcceptedListener) getActivity();
                listener.onAcceptPressedInAddOrEditFragment(data);
                getDialog().dismiss();
            }
        });

        return view;
    }

}
