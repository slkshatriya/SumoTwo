package com.one4all.sumotwo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LatestMessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GroupAdapter groupAdapter;
    String user;
    ArrayList<String> uidList;
    ArrayList<String> userNameList;
    ArrayList<String> url;
    ProgressDialog progressDialog;
    ProgressBar progressBar;


    FloatingActionButton floatingActionButton;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

         super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_two,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.userList:
                Intent intent = new Intent(LatestMessageActivity.this,GroupActivity.class);
                startActivity(intent);
                return  true;
            case R.id.about_info:

                Intent intent1 = new Intent(LatestMessageActivity.this,AboutActivity.class);
                startActivity(intent1);
                return true;



            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(LatestMessageActivity.this,LoginActivity.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.share3:
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.putExtra(Intent.EXTRA_TEXT,"check this app at https://play.google.com/store/apps/details?id=com.one4all.SumoTwo");
                intent2.setType("text/plain");
                startActivity(intent2);
                return true;
   }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_message);
        recyclerView = findViewById(R.id.recyclerView_of_latest_message);
        floatingActionButton = findViewById(R.id.floatingActionButton);


        progressDialog = new ProgressDialog(LatestMessageActivity.this);
        progressBar = findViewById(R.id.progressBar);




//        progressDialog.setTitle("Please wait!!");
//        progressDialog.setMessage("Please wait!!");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

//        progressDialog.show();

        uidList = new ArrayList<>();
        userNameList = new ArrayList<>();
        url = new ArrayList<>();
        progressBar.getProgressDrawable();
        progressBar.setVisibility(View.VISIBLE);



        recyclerView.addItemDecoration(new DividerItemDecoration(LatestMessageActivity.this,DividerItemDecoration.VERTICAL));
        getSupportActionBar().setTitle("Sumo");

        fetchLatestMessage2();
        floatingActionButton.setBackgroundColor(Color.YELLOW);
         groupAdapter = new GroupAdapter<ViewHolder>();
         floatingActionButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(LatestMessageActivity.this,UserListActivity.class);
                 startActivity(intent);

             }
         });

    }

    HashMap<String,Messages> hashMap = new HashMap<>();




    public void refreshRecyclerView(){
        groupAdapter.clear();

//        for (final Messages messages : hashMap.values()) {
//            final ChatItemForLatestMessage chatItemForLatestMessage = new ChatItemForLatestMessage(user,messages.getMessage(),messages.getFromFrom(),messages.getFromTo());
//            groupAdapter.add(new ChatItemForLatestMessage(user,messages.getMessage(),messages.getFromFrom(),messages.getFromTo()));
//            recyclerView.setAdapter(groupAdapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(LatestMessageActivity.this));
            groupAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull Item item, @NonNull View view) {
                     Intent intent = new Intent(LatestMessageActivity.this,ChatLogActivity.class);
                     startActivity(intent);

                }
            });
//
//
        for (Messages value : hashMap.values()) {




                groupAdapter.add(new ChatItemForLatestMessage(LatestMessageActivity.this, user, value.getMessage(), value.getFromFrom(), value.getFromTo()));
                userNameList.add(user);
                uidList.add(value.getFromFrom());

//                uidList.add(value.getFromTo());
            groupAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull Item item, @NonNull View view) {
                    Log.d("onItemClick","123");
                    Intent intent = new Intent(LatestMessageActivity.this, ChatLogActivity.class);
                        Log.d("userUidFromNewMessage", uidList.get(item.getPosition(item)));

                        intent.putExtra("userUid", uidList.get(item.getPosition(item)));
                        intent.putExtra("userUrl", url.get(item.getPosition(item)));
                        intent.putExtra("userName", userNameList.get(item.getPosition(item)));
                        Log.d("latestMessage","latestMessageClicked");

                        startActivity(intent);

                }
            });




//                groupAdapter.setOnItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(@NonNull Item item, @NonNull View view) {
//                        Intent intent = new Intent(LatestMessageActivity.this, ChatLogActivity.class);
//                        Log.d("userUidFromNewMessage", uidList.get(item.getPosition(item)));
//
//                        intent.putExtra("userUid", uidList.get(item.getPosition(item)));
//                        intent.putExtra("userUrl", url.get(item.getPosition(item)));
//                        intent.putExtra("userName", userNameList.get(item.getPosition(item)));
//                        Log.d("latestMessage","latestMessageClicked");
//
//                        startActivity(intent);
//                    }
//                });
            recyclerView.setAdapter(groupAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(LatestMessageActivity.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);

            }
        }
    public void fetchLatestMessage2(){


        final String toId = FirebaseAuth.getInstance().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/latest/"+toId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                groupAdapter.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
//                    progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);


                        Messages messages = snapshot.getValue(Messages.class);
                        String key = dataSnapshot.getKey();
                        Log.d("dataSnapshotLatestUid", messages.getFromTo());
                        String a;

                        uidList.add(messages.getFromFrom());
                        hashMap.put(key, messages);

                        refreshRecyclerView();
  }
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                groupAdapter.clear();
                uidList.clear();
                userNameList.clear();


                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Messages messages = snapshot.getValue(Messages.class);
                    String mess = messages.getMessage();

                    String key = dataSnapshot.getKey();
                    uidList.add(messages.getFromFrom());

                    hashMap.put(key,messages);

                    refreshRecyclerView();




                }
//                recyclerView.setAdapter(groupAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(LatestMessageActivity.this));

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
class ChatItemForLatestMessage extends Item<ViewHolder>{
    CircleImageView circleImageView;
    TextView userName;
    TextView userMessage;
//    String url;
    String name;
    String messages;
    String fromid;
    String toid;

    Context context;
    ArrayList<String> uidList = new ArrayList<>();
    ArrayList<String> userNameList = new ArrayList<>();
    ArrayList<String> url = new ArrayList<>();
    public ChatItemForLatestMessage(Context context,String name,String messages,String fromid,String toId){

        this.context = context;
        this.name = name;
        this.messages = messages;
        this.fromid = fromid;
        this.toid = toId;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    @Override
    public void bind(@NonNull ViewHolder viewHolder, final int position) {
        circleImageView =viewHolder.itemView.findViewById(R.id.circleImageViewForLatestMessage);
        userMessage = viewHolder.itemView.findViewById(R.id.textView2);
        userName = viewHolder.itemView.findViewById(R.id.textView41);
        userName.setText(name);
        userMessage.setText(messages);

        final String chatPartner;
        if (fromid.equals(FirebaseAuth.getInstance().getUid())){
            chatPartner = toid;

        }else {
            chatPartner =fromid;
        }
        uidList.add(chatPartner);
//        if (fromid.equals(FirebaseAuth.getInstance().getUid())) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("/userList/" + chatPartner);
//
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users users = snapshot.getValue(Users.class);
                        Log.d("users", users.getMdisplayName());
                        userName.setText(users.getMdisplayName());

                        Picasso.get().load(users.getUri()).into(circleImageView);
                        userNameList.add(users.getMdisplayName());
                        Log.d("userList", users.getUid());
                        uidList.add(users.getUid());//1
                        uidList.add(fromid);
                        uidList.add(chatPartner);
                        url.add(users.getUri());
//                    }


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });




            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("onItemClick","123");
                    Intent intent = new Intent(context,ChatLogActivity.class);

                    intent.putExtra("userUrl",url.get(0));
                    intent.putExtra("userName",userNameList.get(0));
                    intent.putExtra("userUid",uidList.get(0));

                    context.startActivity(intent);
                }
            });
    }



    @Override
    public int getLayout() {
        return R.layout.latest_message;
    }
}
