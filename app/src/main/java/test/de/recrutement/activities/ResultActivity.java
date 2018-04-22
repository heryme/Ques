package test.de.recrutement.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import test.de.recrutement.R;

@EActivity(R.layout.activity_result)
public class ResultActivity extends AppCompatActivity {
    final static String TAG = ResultActivity.class.getSimpleName();

    @ViewById(R.id.btn_result_activity_next)
    Button btn_result_activity_next;
    @ViewById(R.id.btn_result_activity_finish)
    Button btn_result_activity_finish;
    @ViewById(R.id.tv_result_activity_done_quetion)
    TextView tv_result_activity_score;
    Context context;

    @AfterViews
    public void init() {
        //Initialization
        setUPComponents();
    }

    @Click
    public void btn_result_activity_next() {
        MainActivity.getNextQuestionAndOption();
        ResultActivity.this.finish();

       /* Intent intent = new Intent(ResultActivity.this,MainActivity_.class);
        startActivity(intent);*/

    }

    @Click
    public void btn_result_activity_finish() {

    }

    /**
     * Basic Components Initialization
     */
    private void setUPComponents() {
        context = getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.have_done));
        stringBuilder.append(" ");
        stringBuilder.append(MainActivity.count);
        stringBuilder.append(" / ");
        stringBuilder.append(SplashActivity.questionnaireList.size());
        stringBuilder.append(" ");
        stringBuilder.append(context.getString(R.string.next_one));
        tv_result_activity_score.setText(stringBuilder.toString());

        //If Reached Last Question  Then Visible Finish Button
        if(MainActivity.count == SplashActivity.questionnaireList.size()) {
            tv_result_activity_score.setVisibility(View.GONE);
            btn_result_activity_finish.setVisibility(View.VISIBLE);

        }
    }
}
