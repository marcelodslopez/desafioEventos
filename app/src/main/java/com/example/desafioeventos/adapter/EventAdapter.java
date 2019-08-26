package com.example.desafioeventos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.desafioeventos.DetailEventActivity;
import com.example.desafioeventos.R;
import com.example.desafioeventos.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;



/**
 * Created by Marce on 09/06/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

   Context context;
    private List<Event> list;
    private LayoutInflater layoutInflater;

    public EventAdapter(Context context, List<Event> events) {
        this.list = events;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

       // holder.iv.setImageResource(R.drawable.ic_menu_camera);
        holder.tv_name.setText(list.get(position).getTitle());

        Picasso.with(context)
                .load(list.get(position).getImage())
                .placeholder(context.getResources().getDrawable(R.drawable.event))
                .error(context.getResources().getDrawable(R.drawable.event))
                .into( holder.iv);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Event> eventsList) {
        this.list = eventsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView iv;
        public TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_picture);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position= getAdapterPosition();
            Event event = list.get(position);
            Intent intent = new Intent( view.getContext(), DetailEventActivity.class);
            intent.putExtra("event",event);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);


        }
    }

}
