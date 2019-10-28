package com.one4all.sumotwo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        textView = findViewById(R.id.user_name_from_user_list);
        getSupportActionBar().setTitle("Users");
        recyclerView = findViewById(R.id.recycler_view);
//        progressDialog = new ProgressDialog(UserListActivity.this);
//        progressDialog.setTitle("Please wait");
//        progressDialog.setMessage("User list is downloading");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        progressBar = findViewById(R.id.progressBar_2);
        progressBar.setVisibility(View.VISIBLE);

        fetchUser();

    }

    private void fetchUser() {
        String userUid = FirebaseAuth.getInstance().getUid();

        DatabaseReference fireBaseReference = FirebaseDatabase.getInstance().getReference().child("/userList");
        fireBaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                GroupAdapter groupA = new GroupAdapter<ViewHolder>();
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(imageUrl,names,uid,UserListActivity.this);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.d("userList", snapshot.toString());
//                    progressDialog.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);


//


                    for (DataSnapshot sn: snapshot.getChildren()) {
                        Users string = sn.getValue(Users.class);
                        if (!string.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                            imageUrl.add(string.getUri());
                            names.add(string.getMdisplayName());
                            uid.add(string.getUid());
                        }
                    }


                }
//                LinearLayoutManager llm = new LinearLayoutManager(UserListActivity.this);
//                llm.setOrientation(LinearLayoutManager.VERTICAL);
//                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(UserListActivity.this));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

//    class  UserItem extends Item<ViewHolder> {
//    CircleImageView circleImageView;
//    TextView textView;
//
//    Users users = new Users();
//    public UserItem(Users users){
//
//    }
//
//
//
//
//    @Override
//    public void bind(@NonNull ViewHolder viewHolder, int position) {
//         circleImageView = circleImageView.findViewById(R.id.user);
//         textView = textView.findViewById(R.id.user_name_from_user_list);
//        Item data = getItem(position);
////        viewHolder.itemView.text
////        Picasso.get().load(users.getUri()).into(circleImageView);
//
//
//    }
//
//    @Override
//    public int getLayout() {
//        return R.layout.user_list_view_layout;
//    }
//}

