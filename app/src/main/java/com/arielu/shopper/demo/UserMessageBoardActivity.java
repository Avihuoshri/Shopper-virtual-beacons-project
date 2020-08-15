package com.arielu.shopper.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.database.Firebase;
import com.arielu.shopper.demo.database.Firebase2;
import com.arielu.shopper.demo.models.Message;
import com.arielu.shopper.demo.models.UserSessionData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserMessageBoardActivity extends AppCompatActivity {
    ListView listview;
    List<Message> messages;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        retrieveMessagesFromDB(Firebase.userData.getCompanybranchID());

        listview = findViewById(R.id.message_list);
        messages = generateFakeMessages();
        messageAdapter =  new MessageAdapter(getApplicationContext(),messages);
        listview.setAdapter(messageAdapter);
    }


//    public void sendMessage(View v){
//        // TODO: Ability to create a title.
//        // Step 1: get values for constructing a message.
//        String title = "Some random title";
//        String content = ((EditText) findViewById(R.id.msg_body)).getText().toString();
//        String date =  Long.toString(new Date().getTime());
//        // Step 2: construct a message from those values.
//        Message message = new Message(title, content, date);
//        // Step 3: store the message in DB.
//        Firebase2.pushNewStoreMessage(Firebase.userData.getCompanybranchID(), message);
//        Toast.makeText(getApplicationContext(), "sending message...", Toast.LENGTH_SHORT).show();
//        // Step 4: update UI -> add the new message to the list.
//        messages.add(message);
//        messageAdapter.notifyDataSetChanged();
//    }

 //    for testing purposes only.
    public List<Message> generateFakeMessages()
    {
        List<Message> msgs = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
            msgs.add(new Message("Title "+i,"this fake message","12/24/2019"));
        }

        return msgs;
    }

//    public void retrieveMessagesFromDB(String companybranchID)
//    {
//        Firebase2.getStoreMessages(companybranchID, (msgsObj) -> {
//            List<Message> msgs = (List<Message>) msgsObj;
//            Log.d("firebase_messages", msgs.toString());
//            messages.clear();
//            messages.addAll(msgs);
//            messageAdapter.notifyDataSetChanged();
//        });
//    }


    public void retrieveMessagesFromDB(String companybranchID)
    {
        if(!companybranchID.equals("0")) // if user is a worker, then it has a companybranchID other than "0".
        { // if worker.
            Firebase2.getStoreMessages(companybranchID, (msgsObj) -> {
                List<Message> msgs = (List<Message>) msgsObj;
                Log.d("firebase_messages", msgs.toString());
                messages.clear();
                messages.addAll(msgs);
                messageAdapter.notifyDataSetChanged();
            });
        }
        else
        { // if a regular user.
            String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Firebase2.getUserListinSession(uID,(data) -> {
                UserSessionData usd = data.get(data.size()-1);

                Firebase2.getStoreMessages(usd.getCompanybranchID(), (msgsObj) -> {
                    List<Message> msgs = (List<Message>) msgsObj;
                    Log.d("firebase_messages", msgs.toString());
                    messages.clear();
                    messages.addAll(msgs);
                    messageAdapter.notifyDataSetChanged();
                });

            });
        }
    }
}