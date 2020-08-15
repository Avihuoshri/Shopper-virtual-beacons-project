package com.arielu.shopper.demo;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

public class DialogAddProductQuantity extends DialogFragment {

    private DialogAddProductQuantity.DialogListener dialogListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_add_quantity, null);
/*
        ArrayAdapter arrAdapter = ArrayAdapter.createFromResource(getContext(),R.array.spinner_quantity,android.R.layout.simple_spinner_dropdown_item);
        arrAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner spinner_quantity = (Spinner)view.findViewById(R.id.spin_quantity);
        spinner_quantity.setAdapter(arrAdapter);
 */
        NumberPicker numberPicker = view.findViewById(R.id.picker_quantity);
        numberPicker.setMaxValue(12);
        numberPicker.setMinValue(1);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
//                        int quantity = Integer.parseInt((String) spinner_quantity.getSelectedItem());
//                        dialogListener.addQuantity(quantity);
                        dialogListener.addQuantity(numberPicker.getValue());
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogListener = (DialogAddProductQuantity.DialogListener) context;
    }

    interface DialogListener{
        void addQuantity(int quantity);
    }
}
