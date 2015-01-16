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

/**
 * Created by mekilah on 1/15/15.
 */
public class AddOrEditFragment extends DialogFragment {
    public AddOrEditFragment(){}

    public interface AddOrEditFragmentAcceptedListener{
        public void onAcceptPressedInAddOrEditFragment(AddOrEditFragmentData data);
    }

    public class AddOrEditFragmentData implements Serializable{
        public String name;
        public int year;
        public int month;
        public int dayOfMonth;

        public AddOrEditFragmentData(){}
        public AddOrEditFragmentData(String n, DatePicker datePicker){
            this.name = n;
            this.year = datePicker.getYear();
            this.month = datePicker.getMonth();
            this.dayOfMonth = datePicker.getDayOfMonth();
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
