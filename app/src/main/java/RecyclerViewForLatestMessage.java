import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.one4all.sumotwo.R;
import com.one4all.sumotwo.Users;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewForLatestMessage extends RecyclerView.Adapter<RecyclerViewForLatestMessage.ItemViewHoledr> {
    ConstraintLayout parentLayout;
    Users users;
    String fromId;
    String toid;
    String message;
    ArrayList<String> uidList;
    ArrayList<String> userNameList;
    ArrayList<String> url;
    public RecyclerViewForLatestMessage(ConstraintLayout parentLayout,Users users,String message,String fromId,String toid){
        this.parentLayout = parentLayout;
        this.users = users;
        this.fromId = fromId;
        this.toid = toid;
        this.message = message;
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHoledr holder, int position) {
        uidList = new ArrayList<>();
        userNameList = new ArrayList<>();
        url = new ArrayList<>();
        final String chatPartner;
        if (fromId.equals(FirebaseAuth.getInstance().getUid())){
            chatPartner = toid;

        }else {
            chatPartner =fromId;
        }
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userList/"+chatPartner);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Users users = snapshot.getValue(Users.class);



                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
//
//
    }

    @NonNull
    @Override
    public ItemViewHoledr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_message, parent, false);
        ItemViewHoledr holedr = new ItemViewHoledr(view);
        return holedr;
    }


        @Override
    public int getItemCount() {
        return uidList.size();
    }

    public class ItemViewHoledr extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView userNameTextView;
        TextView userMessageTextView;
        public ItemViewHoledr(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImageViewForLatestMessage);
            userNameTextView = itemView.findViewById(R.id.textView41);
            userMessageTextView = itemView.findViewById(R.id.textView2);
        }
    }
}
