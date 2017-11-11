package org.poletalks.sdk.pole_android_sdk.FeedbackPage;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;

import org.poletalks.sdk.pole_android_sdk.Model.FeedbackQuestion;
import org.poletalks.sdk.pole_android_sdk.R;

import java.util.ArrayList;

/**
 * Created by anjal 9/11/2017
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionGenericViewHolder> {

    private Context context;
    private ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
    private static final int ITEM_VIEW_TYPE_TEXT = 0, ITEM_VIEW_TYPE_SLIDER = 1,
                            ITEM_VIEW_TYPE_SINGLE= 2, ITEM_VIEW_TYPE_MULTIPLE = 3;


    public QuestionAdapter(Context context, ArrayList<FeedbackQuestion> feedbackQuestions) {
        this.context = context;
        this.feedbackQuestions = feedbackQuestions;
    }

    @Override
    public int getItemViewType(int position) {
        if (feedbackQuestions.get(position).getType().equals("TEXT")){
            return ITEM_VIEW_TYPE_TEXT;
        } else if (feedbackQuestions.get(position).getType().equals("SLIDER")){
            return ITEM_VIEW_TYPE_SLIDER;
        } else if (feedbackQuestions.get(position).getType().equals("SINGLE")){
            return ITEM_VIEW_TYPE_SINGLE;
        } else if (feedbackQuestions.get(position).getType().equals("MULTIPLE")){
            return ITEM_VIEW_TYPE_MULTIPLE;
        } else {
            return ITEM_VIEW_TYPE_TEXT;
        }
    }

    @Override
    public QuestionGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case ITEM_VIEW_TYPE_TEXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_pole_sdk_text, parent, false);
                return new QuestionAdapter.TextViewHolder(v);
            case ITEM_VIEW_TYPE_SLIDER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_pole_sdk_slider, parent, false);
                return new QuestionAdapter.SliderViewHolder(v);
            case ITEM_VIEW_TYPE_SINGLE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_pole_sdk_single, parent, false);
                return new QuestionAdapter.SingleViewHolder(v);
            case ITEM_VIEW_TYPE_MULTIPLE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_pole_sdk_multiple, parent, false);
                return new QuestionAdapter.MultipleViewHolder(v);
            default:
                throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(QuestionGenericViewHolder holder, int position) {
        holder.bindType(feedbackQuestions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return feedbackQuestions.size();
    }

    public class TextViewHolder extends QuestionGenericViewHolder {
        private LinearLayout rootView;
        private TextView title;
        private EditText answer;

        public TextViewHolder(View view) {
            super(view);

            answer = (EditText) view.findViewById(R.id.answer);
            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            title = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public void bindType(final FeedbackQuestion feedbackQuestion, final int position) {
            super.bindType(feedbackQuestion, position);

            title.setText(String.format("%s. %s", String.valueOf(position + 1), feedbackQuestion.getTitle()));

            answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    feedbackQuestion.setResponse(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }


    public class SliderViewHolder extends QuestionGenericViewHolder {
        private LinearLayout rootView;
        private RelativeLayout tickMarkLabelsRelativeLayout;
        private DiscreteSlider discreteSlider;
        private TextView title;

        public SliderViewHolder(View view) {
            super(view);

            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            tickMarkLabelsRelativeLayout = (RelativeLayout) view.findViewById(R.id.tick_mark_labels_rl);
            discreteSlider = (DiscreteSlider) view.findViewById(R.id.slider);
            title = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public void bindType(final FeedbackQuestion feedbackQuestion, final int position) {
            super.bindType(feedbackQuestion, position);

            title.setText(String.format("%s. %s", String.valueOf(position + 1), feedbackQuestion.getTitle()));
            feedbackQuestion.setResponse("3");

            discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
                @Override
                public void onPositionChanged(int position) {
                    feedbackQuestion.setResponse(String.valueOf(position+1));
                    int childCount = tickMarkLabelsRelativeLayout.getChildCount();
                    for(int i= 0; i<childCount; i++){
                        TextView tv = (TextView) tickMarkLabelsRelativeLayout.getChildAt(i);
                        tv.setTextSize(12);
                        if(i == position)
                            tv.setTextColor(Color.parseColor("#384950"));
                        else
                            tv.setTextColor(Color.parseColor("#919eab"));
                    }
                }
            });

            tickMarkLabelsRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tickMarkLabelsRelativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    addTickMarkTextLabels(discreteSlider, tickMarkLabelsRelativeLayout);
                }
            });

        }
    }


    public class SingleViewHolder extends QuestionGenericViewHolder {
        private LinearLayout rootView;
        private TextView title;
        private RecyclerView recyclerView;
        private OptionAdapter oAdapter;

        public SingleViewHolder(View view) {
            super(view);

            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }

        @Override
        public void bindType(final FeedbackQuestion feedbackQuestion, final int position) {
            super.bindType(feedbackQuestion, position);

            title.setText(String.format("%s. %s", String.valueOf(position + 1), feedbackQuestion.getTitle()));

            oAdapter = new OptionAdapter(context, feedbackQuestion.getDummyOptions(), false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(false);
            recyclerView.setAdapter(oAdapter);
        }
    }

    public class MultipleViewHolder extends QuestionGenericViewHolder {
        private LinearLayout rootView;
        private TextView title;
        private RecyclerView recyclerView;
        private OptionAdapter oAdapter;

        public MultipleViewHolder(View view) {
            super(view);

            rootView = (LinearLayout) view.findViewById(R.id.root_view);
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }

        @Override
        public void bindType(final FeedbackQuestion feedbackQuestion, final int position) {
            super.bindType(feedbackQuestion, position);

            title.setText(String.format("%s. %s", String.valueOf(position + 1), feedbackQuestion.getTitle()));

            oAdapter = new OptionAdapter(context, feedbackQuestion.getDummyOptions(), true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(oAdapter);

        }
    }

    private void addTickMarkTextLabels(DiscreteSlider discreteSlider, RelativeLayout tickMarkLabelsRelativeLayout){
        int tickMarkCount = discreteSlider.getTickMarkCount();
        float tickMarkRadius = discreteSlider.getTickMarkRadius();
        int width = tickMarkLabelsRelativeLayout.getMeasuredWidth();

        int discreteSliderBackdropLeftMargin = DisplayUtility.dp2px(context, 24);
        int discreteSliderBackdropRightMargin = DisplayUtility.dp2px(context, 24);
        int interval = (width - (discreteSliderBackdropLeftMargin+discreteSliderBackdropRightMargin) - ((int)(tickMarkRadius + tickMarkRadius)) )
                / (tickMarkCount-1);

        String[] tickMarkLabels = {"1", "2", "3", "4", "5"};
        int tickMarkLabelWidth = DisplayUtility.dp2px(context, 40);

        for(int i=0; i<tickMarkCount; i++) {
            TextView tv = new TextView(context);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    tickMarkLabelWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            tv.setText(tickMarkLabels[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);

            if(i == discreteSlider.getPosition())
                tv.setTextColor(Color.parseColor("#384950"));
            else
                tv.setTextColor(Color.parseColor("#919eab"));

            int left = discreteSliderBackdropLeftMargin + (int) tickMarkRadius + (i * interval) - (tickMarkLabelWidth/2);

            layoutParams.setMargins(left, 0, 0, 0);
            tv.setLayoutParams(layoutParams);

            tickMarkLabelsRelativeLayout.addView(tv);
        }
    }
}
