package org.poletalks.sdk.pole_android_sdk.FeedbackPage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;

import org.poletalks.sdk.pole_android_sdk.Model.CommonResponse;
import org.poletalks.sdk.pole_android_sdk.Model.DummyOption;
import org.poletalks.sdk.pole_android_sdk.Model.FeedbackQuestion;
import org.poletalks.sdk.pole_android_sdk.Model.FeedbackResponse;
import org.poletalks.sdk.pole_android_sdk.Model.QuestionResponse;
import org.poletalks.sdk.pole_android_sdk.R;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;
import org.poletalks.sdk.pole_android_sdk.Utils.CheckNetwork;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackSDKActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private RelativeLayout submitLoader;
    private DiscreteSlider discreteSlider;
    private ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
    private QuestionAdapter qAdapter;
    private RecyclerView recyclerView;
    private TextView programmTitle;
    private RelativeLayout loader;
    private NestedScrollView nestedScrollView;
    private Button button;
    private SharedPreferences pref;
    private String item_type, item_id;

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk_feedback);

        context = this;
        setStatusBarColor();
        pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        programmTitle = (TextView) findViewById(R.id.programm_title);
        loader = (RelativeLayout) findViewById(R.id.loader);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        button = (Button) findViewById(R.id.submit);
        submitLoader = (RelativeLayout) findViewById(R.id.submit_loader);

        qAdapter = new QuestionAdapter(context, feedbackQuestions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(qAdapter);

        item_id = getIntent().getStringExtra("item_id");
        item_type = getIntent().getStringExtra("item_type");

        if (item_id == null || item_type == null){
//            item_id = "59f9e72f65db3e1d9cbeaa02";
//            item_type = "PROGRAM";
            finish();
        }

        getdata(item_id, item_type);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackData();
            }
        });
    }

    private void sendFeedbackData() {
        if (CheckNetwork.isInternetAvailable(context)){
            button.setVisibility(View.GONE);
            submitLoader.setVisibility(View.VISIBLE);
            RetrofitConfig retrofitConfig = new RetrofitConfig(context);
            Retrofit retro = retrofitConfig.getRetro();
            ApiInterface apiInterface = retro.create(ApiInterface.class);

            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setUser_id(pref.getString("pole_uid", "none"));
            ArrayList<QuestionResponse> questionResponses = new ArrayList<>();
            for (FeedbackQuestion item :feedbackQuestions){
                QuestionResponse response = new QuestionResponse();
                response.setFeedback_question_id(item.get_id());
                response.setType(item.getType());
                response.setResponse(item.getResponse());
                if (response.getResponse() != null){
                    questionResponses.add(response);
                }
            }
            feedbackResponse.setResponses(questionResponses);

            if (questionResponses.size() == 0){
                button.setVisibility(View.VISIBLE);
                submitLoader.setVisibility(View.GONE);

                Toast.makeText(context, "Please answer at least one question.", Toast.LENGTH_SHORT).show();
                return;
            }

            Call<CommonResponse> call = apiInterface.sendFeedback(feedbackResponse);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    button.setVisibility(View.VISIBLE);
                    submitLoader.setVisibility(View.GONE);
                    if (response.code() == 200) {
                        successPopup();
                    } else {
                        Log.e("Send feedback error", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    button.setVisibility(View.VISIBLE);
                    submitLoader.setVisibility(View.GONE);
                    Log.e("Send feedback error", t.toString());
                }
            });
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void getdata(String item_id, String item_type) {
        loader.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.GONE);

        RetrofitConfig retrofitConfig = new RetrofitConfig(context);
        Retrofit retro = retrofitConfig.getRetro();
        final ApiInterface admin = retro.create(ApiInterface.class);
        Call<CommonResponse> call = admin.getFeedbackQuestions(item_id, item_type);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.code() == 200) {
                    feedbackQuestions.addAll(response.body().getFeedbackQuestions());

                    for (FeedbackQuestion item : feedbackQuestions){
                        for (String optionString : item.getOptions()){
                            DummyOption dummyOption = new DummyOption();
                            dummyOption.setDummyOption(optionString);
                            dummyOption.setSelected(false);
                            item.getDummyOptions().add(dummyOption);
                        }
                    }
                    loader.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);

                    qAdapter.notifyDataSetChanged();
                    if (response.body().getItem() != null && response.body().getItem().getName() != null){
                        programmTitle.setText(response.body().getItem().getName());
                    }
                } else {
                    Toast.makeText(context, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(context, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void successPopup() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback_success, null);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        final Button submit;

        submit = (Button) view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setLayout(dpToPixels(context, 360), dpToPixels(context, 360));
    }

    public static int dpToPixels(Context context, int dpValue) {
        float pixels = 0;
        if (context != null) {
            pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        }

        return (int) pixels;
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#158570"));
        }
    }

}
