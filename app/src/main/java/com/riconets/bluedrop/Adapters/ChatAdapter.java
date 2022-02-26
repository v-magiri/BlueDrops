package com.riconets.bluedrop.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.riconets.bluedrop.R;
import com.riconets.bluedrop.model.ChatModel;

import java.util.List;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    Context context;
    List<ChatModel> chatList;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    String userName;

    public ChatAdapter(Context context, List<ChatModel> chatList,String UserName) {
        this.context = context;
        this.chatList = chatList;
        mAuth=FirebaseAuth.getInstance();
        this.userName=UserName;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
//        String userID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        if(!chatList.get(position).getSender().equals(userName)){
            holder.chatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.LEFT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.START;
            holder.chatMessageView.setLayoutParams(layoutParams);
            holder.linearLayoutMessage.setGravity(Gravity.START);
        }
//        else{
//
//            holder.chatMessageView.setArrowPosition(ChatMessageView.ArrowPosition.LEFT);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.gravity = Gravity.START;
//            holder.chatMessageView.setLayoutParams(layoutParams);
//            holder.linearLayoutMessage.setGravity(Gravity.START);
//        }

            String message=chatList.get(position).getMessage();
            String timeStamp=chatList.get(position).getTimeStamp();
            holder.messageTxt.setText(message);
            holder.TimeTxt.setText(timeStamp);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView messageTxt,TimeTxt;
        ChatMessageView chatMessageView;
        LinearLayout linearLayoutMessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTxt=itemView.findViewById(R.id.textViewMessage);
            TimeTxt=itemView.findViewById(R.id.textViewDate);
            linearLayoutMessage=itemView.findViewById(R.id.linearLayoutMessage);
            chatMessageView=itemView.findViewById(R.id.chatMessageView);
        }
    }

}
