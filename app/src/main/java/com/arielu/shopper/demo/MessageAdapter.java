package com.arielu.shopper.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arielu.shopper.demo.models.Message;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_message, null);
        }
        ((TextView)view.findViewById(R.id.message_title)).setText(messages.get(i).getTitle());
        ((TextView)view.findViewById(R.id.message_date)).setText(messages.get(i).retrieveParsedDate());
        ((TextView)view.findViewById(R.id.message_content)).setText(messages.get(i).getContent());
        return view;
    }
}
