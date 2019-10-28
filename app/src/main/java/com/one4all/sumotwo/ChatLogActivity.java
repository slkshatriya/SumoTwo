package com.one4all.sumotwo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatLogActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    Uri uri;
    String userUrl;
    ImageButton sendButton;
    String imageUrl;
    EditText messageInput;
    Task<Void> databaseReference;
    String userUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_log);
        getSupportActionBar().setTitle(getIntent().getStringExtra("userName"));
        recyclerView = findViewById(R.id.chat_list_view);
        sendButton = findViewById(R.id.sendButton);
        messageInput = findViewById(R.id.messageInput);
         imageUrl = getIntent().getStringExtra("userUrl");
         userUid = getIntent().getStringExtra("userUid");



        fetchDataFromUser();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageInput.getText().equals("")) {
                    String fromTo = FirebaseAuth.getInstance().getUid();
                    Long ts = System.currentTimeMillis() / 1000;
                    String timeStamp = ts.toString();
//

                    Messages messages = new Messages(userUid, fromTo, timeStamp, messageInput.getText().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("messages/" + fromTo + "/" + userUid).push().setValue(messages);
                    FirebaseDatabase.getInstance().getReference().child("messages/" + userUid + "/" + fromTo).push().setValue(messages);
                    FirebaseDatabase.getInstance().getReference().child("latest/" + fromTo + "/" + userUid).push().setValue(messages);
                    FirebaseDatabase.getInstance().getReference().child("latest/" + userUid + "/" + fromTo).push().setValue(messages);
                    messageInput.setText("");
//                fetchDataFromUser();
                }



            }
        });


    }
    public  void fetchDataFromUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("/messages/"+userUid+"/"+FirebaseAuth.getInstance().getUid());
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            GroupAdapter groupAdapter2 = new GroupAdapter();
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                dataSnapshot.getValue(Messages.class);
//                for (DataSnapshot  dataSnapshot1 : dataSnapshot.getChildren()){
//                     Messages a =dataSnapshot1.getValue(Messages.class);
//                     if (a.getFromTo().equals(FirebaseAuth.getInstance().getUid())){
//                         groupAdapter2.add(new ChatTo(a.getMessage()));
//
//
//                     }else {
//                         groupAdapter2.add(new ChatFrom(imageUrl,a.getMessage()));
//                     }
//
//
//
//                }
//                groupAdapter2.notifyDataSetChanged();
//                recyclerView.setAdapter(groupAdapter2);
//                recyclerView.setLayoutManager(new LinearLayoutManager(ChatLogActivity.this));
//                recyclerView.scrollToPosition(groupAdapter2.getItemCount()-1);
//
//
//
//
//
//
//
//
//
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ChatLogActivity.this,"Sorry message didn't send!",Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            GroupAdapter groupAdapter2 = new GroupAdapter();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupAdapter2.clear();//To prevent copy message


                for (DataSnapshot  dataSnapshot1 : dataSnapshot.getChildren()){
                    Messages a =dataSnapshot1.getValue(Messages.class);
                    if (a.getFromTo().equals(FirebaseAuth.getInstance().getUid())){
                        groupAdapter2.add(new ChatTo(a.getMessage()));


                    }else {
                        groupAdapter2.add(new ChatFrom(imageUrl,a.getMessage()));
                    }



                }


                recyclerView.setAdapter(groupAdapter2);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatLogActivity.this));
                recyclerView.scrollToPosition(groupAdapter2.getItemCount()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });

    }
}
 class  ChatFrom extends Item<ViewHolder>{

     String url;
     String message;
    public ChatFrom(String url,String message){
        this.url = url;
        this.message = message;
    }

    CircleImageView circleImageViewFromFrom;
    TextView textView;


     @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
         circleImageViewFromFrom = viewHolder.itemView.findViewById(R.id.circleImageViewFromFrom);

         textView = viewHolder.itemView.findViewById(R.id.textviewFromFrom);



         Picasso.get().load(url).into(circleImageViewFromFrom);
         textView.setText(message);
 }

    @Override
    public int getLayout() {
        return R.layout.chat_from_row;
    }
}
class  ChatTo extends Item<ViewHolder>{



//    String url;
    String text;
    public ChatTo(String text){
        this.text = text;

//        this.url = url;
    }
    CircleImageView circleImageViewFromTo;
    TextView textView2;

    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        circleImageViewFromTo = viewHolder.itemView.findViewById(R.id.circleImageViewFromTo);
        textView2 = viewHolder.itemView.findViewById(R.id.button);

//        Picasso.get().load().into(circleImageViewFromTo);
        textView2.setText(text);


    }



    @Override
    public int getLayout() {
        return R.layout.chat_to_row;
    }
}

class  Messages {
    private String fromFrom;
    private String FromTo;
    private String timeStamp;
    private String message;

    public Messages(String fromFrom, String fromTo, String timeStamp, String message) {
        this.fromFrom = fromFrom;
        FromTo = fromTo;
        this.timeStamp = timeStamp;
        this.message = message;

    }

    public Messages() {
    }

    public String getFromFrom() {
        return fromFrom;
    }

    public void setFromFrom(String fromFrom) {
        this.fromFrom = fromFrom;
    }

    public String getFromTo() {
        return FromTo;
    }

    public void setFromTo(String fromTo) {
        FromTo = fromTo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



