package org.poletalks.sdk.pole_android_sdk;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etiennelawlor.discreteslider.library.ui.DiscreteSlider;
import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;
import com.google.gson.JsonObject;
import org.poletalks.sdk.pole_android_sdk.Model.LoginResponse;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.ApiInterface;
import org.poletalks.sdk.pole_android_sdk.RetrofitSupport.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FeedbackSDKActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private RelativeLayout tickMarkLabelsRelativeLayout;
    private DiscreteSlider discreteSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk_feedback);

        context = this;
        setStatusBarColor();

        tickMarkLabelsRelativeLayout = (RelativeLayout) findViewById(R.id.tick_mark_labels_rl);
        discreteSlider = (DiscreteSlider) findViewById(R.id.slider);

        discreteSlider.setOnDiscreteSliderChangeListener(new DiscreteSlider.OnDiscreteSliderChangeListener() {
            @Override
            public void onPositionChanged(int position) {
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

                addTickMarkTextLabels();
            }
        });

        getdata("jdp_superadmin", "password");
    }

    public void getdata(String login, String pass) {
        RetrofitConfig retrofitConfig = new RetrofitConfig(context);
        Retrofit retro = retrofitConfig.getRetro();
        final ApiInterface admin = retro.create(ApiInterface.class);

        JsonObject abc = new JsonObject();
        abc.addProperty("login_id", login);
        abc.addProperty("password", pass);

        Call<LoginResponse> call = admin.adminLogin(abc);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Oops, something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addTickMarkTextLabels(){
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

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#158570"));
        }
    }

}
