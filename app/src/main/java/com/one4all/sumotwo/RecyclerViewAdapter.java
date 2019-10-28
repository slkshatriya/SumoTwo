package com.one4all.sumotwo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<>();
    private Context context ;
    private Users users;


    public RecyclerViewAdapter(ArrayList<String> images, ArrayList<String> imageNames,ArrayList<String> uid, Context context) {
        this.images = images;
        this.imageNames = imageNames;
        this.context = context;
        this.uid = uid;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_view_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Picasso.get().load(images.get(position)).into(holder.circleImageView);
        holder.textView.setText(imageNames.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatLogActivity.class);
                intent.putExtra("userName",imageNames.get(position));
                intent.putExtra("userUrl",images.get(position));
                intent.putExtra("userUid",uid.get(position));
//                intent.putParcelableArrayListExtra("userObject",Users.class);
                context.startActivity(intent);


                

            }
        });

    }

    @Override
    public int getItemCount() {
        return uid.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;
        ConstraintLayout parentLayout;
        public ViewHolder(View itemView){
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user);
            textView = itemView.findViewById(R.id.user_name_from_user_list);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
