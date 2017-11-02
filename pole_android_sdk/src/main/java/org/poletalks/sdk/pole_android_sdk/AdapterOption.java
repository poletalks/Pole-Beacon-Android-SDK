package org.poletalks.sdk.pole_android_sdk;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anjal 31/10/2017
 */
public class AdapterOption extends RecyclerView.Adapter<AdapterOption.MyViewHolder> {
    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<String> options = new ArrayList<>();

    public interface TagActivityInteractionListener {
        void onTagSelected(String tag);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public AdapterOption(Activity context, ArrayList<String> options) {
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.options = options;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        holder.tag.setText(tag.get(position));
//        holder.tag.setSelected(true);
//        holder.tag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        holder.tag.setSingleLine(true);
//
//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                tagActivityInteractionListener.onTagSelected(holder.tag.getText().toString());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tag;
        public  RelativeLayout rootView;

        public MyViewHolder(View view) {
            super(view);
//            rootView = (RelativeLayout) view.findViewById(R.id.rootView);
//            tag = (TextView) view.findViewById(R.id.tag);
        }
    }
}
