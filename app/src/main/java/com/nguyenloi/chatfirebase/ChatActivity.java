package com.nguyenloi.chatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    ImageView imgBackChatToMain;
    EditText edtMessage;
    FloatingActionButton btnSend;
    RecyclerView rcvChat;
    TextView tvChat;

    String userName, otherName;

    FirebaseDatabase database;
    DatabaseReference reference;

    MessageAdapter messageAdapter;
    List<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.getSupportActionBar().hide();
        setControl();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        userName=getIntent().getStringExtra("userName");
        otherName=getIntent().getStringExtra("otherName");

        rcvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        tvChat.setText(otherName);

        imgBackChatToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMessage.getText().toString();
                if(!message.equals("")){
                    sendMessage(message);
                    edtMessage.setText("");
                }
            }
        });

        getMessageDislay();
    }

    public void getMessageDislay()
    {
        reference.child("Messages").child(userName).child(otherName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ModelClass modelClass = snapshot.getValue(ModelClass.class);
                        list.add(modelClass);

                        messageAdapter.notifyDataSetChanged();
                        rcvChat.scrollToPosition(list.size()-1);
                        Log.e("Hello","world");
                        Log.e("Hello","world");
                        Log.e("Hello","world");
                        Log.e("Hello","world");
                        Log.e("Hello","world");
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        messageAdapter = new MessageAdapter(list,userName);
        rcvChat.setAdapter(messageAdapter);
    }

    public void sendMessage(String message){
        final String key = reference.child("Messages").child(userName).child(otherName).push().getKey();
        final Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("from",userName);

        reference.child("Messages").child(userName).child(otherName).child(key)
                .setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    reference.child("Messages").child(otherName).child(userName)
                            .child(key).setValue(messageMap);
                }
            }
        });
    }

    private void setControl() {
        imgBackChatToMain = findViewById(R.id.imgBackChatToMain);
        edtMessage=findViewById(R.id.edtMessage);
        btnSend=findViewById(R.id.btnSend);
        rcvChat=findViewById(R.id.rcvChat);
        tvChat=findViewById(R.id.tvChat);

    }
}