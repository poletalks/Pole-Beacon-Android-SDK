package org.poletalks.sdk.pole_android_sdk.FeedbackPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.poletalks.sdk.pole_android_sdk.Model.DummyOption;
import org.poletalks.sdk.pole_android_sdk.R;

import java.util.ArrayList;

/**
 * Created by anjal 12/11/2017
 */

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHolder> {
    private static LayoutInflater inflater = null;
    private Context context;
    private boolean isMultiple = false;
    private ArrayList<DummyOption> options = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    public OptionAdapter(Context context, ArrayList<DummyOption> options, boolean isMultiple) {
        this.context = context;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.options = options;
        this.isMultiple = isMultiple;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(options.get(position).getDummyOption());
        if (options.get(position).isSelected()){
            holder.radioButton.setImageResource(R.drawable.ic_checked_true);
        } else {
            holder.radioButton.setImageResource(R.drawable.ic_checked_false);
        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (options.get(position).isSelected()){
                    options.get(position).setSelected(false);
                } else {
                    if (isMultiple){
                        options.get(position).setSelected(true);
                    } else {
                        for (DummyOption option: options){
                            option.setSelected(false);
                        }
                        options.get(position).setSelected(true);
                    }
                }
                notifyDataSetChanged();
            }
        });
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
        public TextView title;
        public ImageView radioButton;
        private LinearLayout rootView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.option_title);
            radioButton = (ImageView) view.findViewById(R.id.option);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
        }
    }
}
