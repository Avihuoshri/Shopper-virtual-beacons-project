package com.arielu.shopper.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class messageDialog extends AppCompatDialogFragment {

    private EditText msg_title ;
    private EditText msg_body  ;
    private MessageBoardActivity messageBoardActivity;

    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity()) ;
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.layout_dialog , null) ;
        messageBoardActivity = new MessageBoardActivity() ;
        dialogBuilder.setView(view)
                .setTitle("Message for customers")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Step 1: get values for constructing a message.

                        msg_title = view.findViewById(R.id.msg_title);
                        msg_body = view.findViewById(R.id.msg_body);
                        String titleStr = msg_title.getText().toString() ;
                        String msgBodyStr = msg_body.getText().toString() ;
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy") ;
//                        String date =  sdf.format(new Date());
                        String date =  Long.toString(new Date().getTime());
                        // Step 2: construct a message from those values.
                        Message message = new Message(titleStr, msgBodyStr, date);
                        // messageBoardActivity.sendMessage(message);

                        Toast.makeText(getActivity() , date , Toast.LENGTH_LONG).show();
                        dialogListener.addMessage(message);
                    }
                }) ;

        msg_title = view.findViewById(R.id.msg_title);
        msg_body = view.findViewById(R.id.msg_body);

        return dialogBuilder.create() ;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogListener = (messageDialog.DialogListener) context;
    }

    interface DialogListener{
        void addMessage(Message msg);
    }
}
