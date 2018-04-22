package test.de.recrutement.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.de.recrutement.R;
import test.de.recrutement.parser.JsonParserIntroSliderNew;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.IntroSliderService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.UserSession;

import static test.de.recrutement.R.id.btn_next;

@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = WelcomeActivity.class.getSimpleName();
    @ViewById(R.id.view_pager)
    ViewPager view_pager;
    @ViewById(btn_next)
    Button btnNext;
    @ViewById(R.id.btn_skip)
    Button btnSkip;
    /**
     * User Session
     */
    private UserSession userSession;
    private MyViewPagerAdapter myViewPagerAdapter;
    List<JsonParserIntroSliderNew.result> introSliderList;
    JsonParserIntroSliderNew jsonParserIntroSliderNew;

    @AfterViews
    public void init() {
        basicInitialization();
        if (AndroidUtils.isNetworkAvailable(WelcomeActivity.this)) {
            callIntroSliderAPI();
        }else {
            Toast.makeText(WelcomeActivity.this, getApplicationContext().getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Click
    public void btn_next() {
        int current = getItem(+1);
        if (current < introSliderList.size()) {
            // move to next screen
            view_pager.setCurrentItem(current);
            btnNext.setText(introSliderList.get(current).getButtonText());

        } else {
            launchHomeScreen();
        }
    }

    @Click
    public void btn_skip() {
        int current = getItem(-1);
        if (current >= 0) {
            view_pager.setCurrentItem(current);
        }
    }

    /**
     * Initialization basic componets
     */
    private void basicInitialization() {
        userSession = new UserSession(WelcomeActivity.this);
        introSliderList = new ArrayList<>();
        // Checking for first time launch - before calling setContentView()
        if (!userSession.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    /**
     * Redirect On Test Type Screen
     */
    private void launchHomeScreen() {
       // userSession.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, TestTypeActivity_.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            //addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            btnNext.setText(introSliderList.get(position).getButtonText());
            Log.d(TAG,"Button Text--->" + introSliderList.get(position).getButtonText());
            if (position == introSliderList.size() - 1) {
                // last page. make button text to GOT IT
                //btnNext.setText(getString(R.string.start));

                btnSkip.setVisibility(View.GONE);

            } else {
                // still pages are left
                //btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        List<JsonParserIntroSliderNew.result> introSliderList;
        public MyViewPagerAdapter(List<JsonParserIntroSliderNew.result> introSliderList) {
            layoutInflater = LayoutInflater.from(getApplicationContext());
            this.introSliderList = introSliderList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View view = layoutInflater.inflate(layouts[position], container, false);
            View view = layoutInflater.inflate(R.layout.row_slider, container, false);
            JsonParserIntroSliderNew.result item = introSliderList.get(position);
            TextView tv_row_file = (TextView) view.findViewById(R.id.tv_row_file);
            //tv_row_file.setText(/*msg[position]*/btnTextList.get(position));
            tv_row_file.setText(Html.fromHtml(item.getScreenText()));
            tv_row_file.setTextColor(Color.parseColor(item.getFontColor()));
            view.setBackgroundColor(Color.parseColor(item.getBackgroundColor())/*colors[position]*/);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return introSliderList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * Call IntroSlider API
     */
    public void callIntroSliderAPI() {
        IntroSliderService.getIntroSlider(WelcomeActivity.this, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Intro Slider Response-->" + response.toString());
               /* List<JsonParserIntroSlider> tempList = JsonParserIntroSlider.parseIntroSliderResponse(response).getJsonParserIntroSliderList();
                if (tempList != null && tempList.size() > 0) {
                    introSliderList.addAll(tempList);
                }*/

                jsonParserIntroSliderNew = JsonParserIntroSliderNew.parseIntroSliderData(response);
                List<JsonParserIntroSliderNew.result> tempList = jsonParserIntroSliderNew.resultList;
                if(tempList != null && tempList.size() > 0) {
                    introSliderList.addAll(tempList);
                }
                changeStatusBarColor();
                myViewPagerAdapter = new MyViewPagerAdapter(introSliderList);
                view_pager.setAdapter(myViewPagerAdapter);
                view_pager.addOnPageChangeListener(viewPagerPageChangeListener);
            }
        }, new APIService.Error<JSONObject>() {
            @Override
            public void onError(JSONObject error) {
            }
        });
    }
}

