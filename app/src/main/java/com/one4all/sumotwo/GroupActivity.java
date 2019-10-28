package com.one4all.sumotwo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton button;
    EditText editText;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        findViewBYId();
        getSupportActionBar().setTitle("Group Chat");
        fetchData();
        GroupAdapter groupAdapter = new GroupAdapter();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("userList/"+FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);
                    author = users.getMdisplayName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                editText.setText("");

                Long ts = System.currentTimeMillis() / 1000;
                String timeStamp = ts.toString();
//

                GroupMessages groupMessages =new GroupMessages(author,text,timeStamp,FirebaseAuth.getInstance().getUid());
                FirebaseDatabase.getInstance().getReference().child("groupMessage").push().setValue(groupMessages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("groupActivity","Message sent succesful");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("groupActivity","Message was not succesful");
                    }
                });
            }
        });


    }
    GroupAdapter groupAdapter = new GroupAdapter();

    public void findViewBYId(){
        recyclerView = findViewById(R.id.chat_list_view_from_group_activity);
        editText = findViewById(R.id.messageInputFromGroupActivity);
        button = findViewById(R.id.sendButtonFromGroupActivity);
    }
    public void fetchData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("groupMessage/");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                GroupMessages groupMessages = dataSnapshot.getValue(GroupMessages.class);
                String uid = groupMessages.getUid();
                String userUid = FirebaseAuth.getInstance().getUid();
                if (userUid.equals(uid)) {
                    groupAdapter.add(new ViewHolderTo(groupMessages.getMessage(), groupMessages.getAuthor()));

                } else {
                    groupAdapter.add(new ViewHolderFrom(groupMessages.getMessage(), groupMessages.getAuthor()));
                }

                recyclerView.setAdapter(groupAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
                recyclerView.scrollToPosition(groupAdapter.getItemCount()-1);

            }




            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                groupAdapter.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    GroupMessages groupMessages = snapshot.getValue(GroupMessages.class);
                    String uid = groupMessages.getUid();
                    if (groupMessages.getUid().equals(uid)) {
                        groupAdapter.add(new ViewHolderTo(groupMessages.getMessage(), groupMessages.getAuthor()));

                    } else {
                        groupAdapter.add(new ViewHolderFrom(groupMessages.getMessage(), groupMessages.getAuthor()));
                    }
                }
                recyclerView.setAdapter(groupAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(GroupActivity.this));

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
class ViewHolderFrom extends Item<ViewHolder>{

    TextView textView;
    Button button;
    String message;
    String author;

    public ViewHolderFrom(String message,String author) {

        this.message = message;
        this.author = author;
    }

    @Override
    public void bind(@NonNull com.xwray.groupie.ViewHolder viewHolder, int position) {
        textView = viewHolder.itemView.findViewById(R.id.textViewTo);
        button = viewHolder.itemView.findViewById(R.id.button2);


        textView.setText(author);
        button.setText(message);

    }

    @Override
    public int getLayout() {
        return R.layout.group_activity_from;
    }
}
class ViewHolderTo extends Item<ViewHolder>{
    TextView textView;
    Button button;
    String message;
    String author;
    public ViewHolderTo(String message,String author) {

        this.message = message;
        this.author = author;
    }
    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        textView = viewHolder.itemView.findViewById(R.id.textView7);
        button = viewHolder.itemView.findViewById(R.id.button3);

        textView.setText(author);
        button.setText(message);

    }

    @Override
    public int getLayout() {
        return R.layout.group_activity_to;
    }
}
class GroupMessages{
    String author;
    String message;
    String timeStamp;
    String uid;

    public GroupMessages(String author, String message, String timeStamp, String uid) {
        this.author = author;
        this.message = message;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }
    public GroupMessages(){

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
