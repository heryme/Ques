package test.de.recrutement.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import test.de.recrutement.R;
import test.de.recrutement.adapter.OptionsAdapter;
import test.de.recrutement.background_services.UploadData;
import test.de.recrutement.constant.Constant;
import test.de.recrutement.listeners.OnPictureCapturedListener;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.PictureService;
import test.de.recrutement.service.QuestionnaireService;
import test.de.recrutement.service.SendAudioService;
import test.de.recrutement.service.SendImageService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.AudioUtility;
import test.de.recrutement.utils.DialogUtility;
import test.de.recrutement.utils.NewCameraPreview;
import test.de.recrutement.utils.UserSession;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static test.de.recrutement.utils.AndroidUtils.deleteAllFile;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements Camera.ShutterCallback,
        Camera.PictureCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tv_main_activity_timer)
    public static
    TextView tv_main_activity_timer;

    @ViewById(R.id.tv_main_activity_ques)
    static
    TextView tv_main_activity_ques;

    @ViewById(R.id.tv_main_activity_count)
    static
    TextView tv_main_activity_count;

    @ViewById(R.id.btn_main_activity_submit)
    static
    Button btn_main_activity_submit;

    @ViewById(R.id.lv_options)
    ListView lv_options;

    @ViewById(R.id.preview)
    SurfaceView mPreview;

    Camera mCamera;

    /**
     * Contex
     */
    Context context = MainActivity.this;

    /**
     * Options Adapter
     */
    static OptionsAdapter optionsAdapter;

    /**
     * Counter
     */
    static CountDownTimer countdowntimer;

    /**
     * ExecutorService For Capture Images
     */
    ExecutorService es;

    /**
     * User Session
     */
    private UserSession userSession;

    //private String deviceId;

    public static int nxtQuestion = 0, count = 0;

    private static long millis;

    private long imageCaptureTime = 0;

    public static boolean examType;

    /**
     * Hashmap Question And Ans
     */
    private List<HashMap<String, List<String>>> queAnsList;

    private List<Double> resultList;

    /*camera 2 api below control use */
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    private ImageView uploadBackPhoto;
    private ImageView uploadFrontPhoto;
    OnPictureCapturedListener listener;
    private Handler handler = null;
    private Runnable runnablePhotoCapture = null;


    @AfterViews
    public void init() {

        if (handler == null){
            handler = new Handler(Looper.getMainLooper());
        }

        Intent intent = getIntent();
        examType = intent.getBooleanExtra(Constant.EXAM_TYPE, false);
        userSession = new UserSession(MainActivity.this);
        if (examType) {
            setUPComponents();
        } else {
            setUPComponentsDemoTest();
        }
    }

    @Click
    public void btn_main_activity_submit() {
        //Store selected question and answer
        selectedQuestionAndOptions();
        // Create custom dialog object
        countdowntimer.cancel();
        //Show dialog for next question
        showDialog();
    }

    /**
     * Save Question's And Answer Selected By User
     */
    private void selectedQuestionAndOptions() {

        if (examType) {

            if (SplashActivity.questionnaireList != null && SplashActivity.questionnaireList.size() > 0) {

                HashMap<String, List<String>> map = new HashMap<>();
                map.put(SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).get_id(),
                        optionsAdapter.getCheckedOptionList());
                 queAnsList.add(map);
                //Add Final Result Of The Every Question.
                resultList.add(optionsAdapter.calculationResult());
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }

        } else {

            if (SplashActivity.demoQuestionnaireList != null && SplashActivity.demoQuestionnaireList.size() > 0) {

                HashMap<String, List<String>> map = new HashMap<>();
                map.put(SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).get_id(),
                        optionsAdapter.getCheckedOptionList());
                queAnsList.add(map);

            } else {
                Toast.makeText(MainActivity.this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Basic Components Initialization For Real Test
     */
    private void setUPComponents() {

        //Set Image Capture Time
        //imageCaptureTime = AndroidUtils.timeConvertTomills(SplashActivity.settingsList.get(0).getSettingModelInnerList().get(0).getValue());
        imageCaptureTime = AndroidUtils.timeConvertTomills(AndroidUtils.getImageCapture());
        queAnsList = new ArrayList<>();
        resultList = new ArrayList<>();
        //Device Id
        //deviceId = SplashActivity.deviceId;
        AndroidUtils.createAppDirectory();
        /***
         *Delete All Images And Audio Delete Loading Time Because  We Don't  Need To Images Already Zip Created Of The Image And Audio
         */
        AndroidUtils.deleteAllFile(Constant.IMAGE_PATH);
        AndroidUtils.deleteAllFile(Constant.AUDIO_PATH);


        if (Build.VERSION.SDK_INT >= 21) {

            checkPermissions();

            listener = new OnPictureCapturedListener() {
                @Override
                public void onCaptureDone(final String pictureUrl, final byte[] pictureData) {
                    if (pictureData != null && pictureUrl != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

                                final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                                final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);


                                /*set image into imageview*/
                                /*if (pictureUrl.contains("0_pic.jpg")) {
                                    uploadBackPhoto.setImageBitmap(scaled);
                                } else if (pictureUrl.contains("1_pic.jpg")) {
                                    uploadFrontPhoto.setImageBitmap(scaled);
                                }*/

                               /* Toast.makeText(getApplicationContext(),"Picture saved to " + pictureUrl,
                                        Toast.LENGTH_LONG).show();*/

                            }
                        });
                    }

                }

                @Override
                public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
                    if (picturesTaken != null && !picturesTaken.isEmpty()) {
                        /*handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(),
                                            "Done capturing all photos! " ,
                                            Toast.LENGTH_LONG).show();
                                    Log.d("RRRR"," : : Done 1 Photos calls onDoneCapturingAllPhotos");
                                    new PictureService().startCapturing(MainActivity.this, listener);
                                } catch (Exception e) {
                                    Log.d("RRR",": :"+e.getMessage());
                                }

                            }
                        },imageCaptureTime);*/

                        runnablePhotoCapture = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    /*Toast.makeText(getApplicationContext(),
                                            "Done capturing all photos! ",
                                            Toast.LENGTH_LONG).show();*/
                                    // Log.d("RRRR"," : : Done 1 Photos calls onDoneCapturingAllPhotos");

                                    new PictureService().startCapturing(MainActivity.this, listener);

                                } catch (Exception e) {
                                    Log.d("RRR", ": :" + e.getMessage());
                                }
                            }
                        };
                        if (handler != null){
                            handler.postDelayed(runnablePhotoCapture, imageCaptureTime);
                        }


                    }

                }
            };

            new PictureService().startCapturing(MainActivity.this, listener);


        } else {
            startCamera();

        }


        AudioUtility.startAudioRec(MainActivity.this);
        //Set Question count
        setQuestionCount();
        //Set question first time
        if (SplashActivity.questionnaireList != null && SplashActivity.questionnaireList.size() > 0) {

            tv_main_activity_ques.setText(Html.fromHtml(SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion()));
            //Set Adapter
            optionsAdapter = new OptionsAdapter(MainActivity.this,
                    R.id.lv_options
                    , SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getOptionsList(),
                    SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).get_id());
            lv_options.setAdapter(optionsAdapter);
            //Set Counter Time
            String time = SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion_time();
            countdowntimer = new CountDownTimerClass(AndroidUtils.timeConvertTomills(time), 1000);
            countdowntimer.start();
        }
    }

    /**
     * Basic Components Initialization For Demo Test
     */
    private void setUPComponentsDemoTest() {

        queAnsList = new ArrayList<>();
        //Device Id
        //deviceId = SplashActivity.deviceId;
        //Set Question count
        setQuestionCount();
        //Set question first time
        if (SplashActivity.questionnaireList != null && SplashActivity.demoQuestionnaireList.size() > 0) {

            tv_main_activity_ques.setText(Html.fromHtml(SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion()));

            //Set Adapter
            optionsAdapter = new OptionsAdapter(MainActivity.this,
                    R.id.lv_options
                    , SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getOptionsList(),
                    SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).get_id());
            lv_options.setAdapter(optionsAdapter);
            Log.d(TAG, "Question-->" + tv_main_activity_ques.getText().toString());

            //Set Counter Time
            String time = SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion_time();
            countdowntimer = new CountDownTimerClass(AndroidUtils.timeConvertTomills(time), 1000);
            countdowntimer.start();
        }
    }

    /**
     * Set Question Count And Increase Click On Next Button
     */
    private static void setQuestionCount() {
        count = nxtQuestion;
        if (count == 0) {
            count = 1;
        } else {
            count++;
        }
        if (examType) {
            tv_main_activity_count.setText("Que:" + count + "/" + SplashActivity.questionnaireList.size());
        } else {
            tv_main_activity_count.setText("Que:" + count + "/" + SplashActivity.demoQuestionnaireList.size());
        }
    }

    /**
     * Get Next Question And Option
     */
    public static void getNextQuestionAndOption() {

        nxtQuestion++;

        if (examType) {

            if (nxtQuestion < SplashActivity.questionnaireList.size()) {

                //Set Counter
                String time = SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion_time();
                countdowntimer = new CountDownTimerClass(AndroidUtils.timeConvertTomills(time), 1000);
                countdowntimer.start();

                //Set Question Count
                setQuestionCount();
                //Set Question
                tv_main_activity_ques.setText(Html.fromHtml(SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion()));
                Log.d(TAG, "Question-->" + tv_main_activity_ques.getText().toString());
                //Set Options
                optionsAdapter.upadteQuestion(SplashActivity.questionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getOptionsList());
            }

        } else {

            if (nxtQuestion < SplashActivity.demoQuestionnaireList.size()) {

                //Set Counter Time
                String time = SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion_time();
                countdowntimer = new CountDownTimerClass(AndroidUtils.timeConvertTomills(time), 1000);
                countdowntimer.start();

                setQuestionCount();
                //Set Question
                tv_main_activity_ques.setText(Html.fromHtml(SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getQuestion()));
                Log.d(TAG, "Question-->" + tv_main_activity_ques.getText().toString());
                //Set Options
                optionsAdapter.upadteQuestion(SplashActivity.demoQuestionnaireList.get(nxtQuestion).getQuestionsList().get(nxtQuestion).getOptionsList());
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            FileOutputStream fileOutputStream = new FileOutputStream(AndroidUtils.getOutputMediaFile());
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constant.COMPRESS, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //mCamera.startPreview();

               /* FileOutputStream fos = new FileOutputStream(AndroidUtils.getOutputMediaFile());
                fos.write(data);
                fos.close();*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //camera.startPreview();

        //Check Os Version
        if (AndroidUtils.checkBuildVersionLollipop() ||
                AndroidUtils.checkBuildVersionKitkat() ||
                AndroidUtils.checkBuildVersionJellyBean()) {
            camera.startPreview();
        }
    }

    @Override
    public void onShutter() {
        //Toast.makeText(this, "Capture1!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Capture1!");
    }

    /**
     * Timer
     */
    static class CountDownTimerClass extends CountDownTimer {
        long progress;

        public CountDownTimerClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            millis = millisUntilFinished;
            //Log.d(TAG, "Left Seconds-->" + millis);
            progress = (long) (millisUntilFinished / 1000);
            String hms = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            tv_main_activity_timer.setText(hms + " Min(s)");
        }

        @Override
        public void onFinish() {
            onTick(0);
            btn_main_activity_submit.performClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*CountDownTimer downTimer = new CountDownTimerClass(millis,1000);
         downTimer.start();*/
        //countdowntimer = new CountDownTimerClass(millis,1000);
        countdowntimer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        countdowntimer.cancel();
    }

    /**
     * Show Dialog For Next Question
     */
    private void showDialog() {
        /* RRR remove thread when dailo for save image continiusely */
        //handler.removeMessages(0);

        final Dialog dialog = new Dialog(MainActivity.this, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_next_question);
        dialog.setCancelable(false);
        dialog.show();
        TextView tv_dialog_done_question = (TextView) dialog.findViewById(R.id.tv_dialog_done_question);
        tv_dialog_done_question.setText(AndroidUtils.setData(MainActivity.this));
        final Button btn_dialog_next = (Button) dialog.findViewById(R.id.btn_dialog_next);

        final Button btn_dialog_finish = (Button) dialog.findViewById(R.id.btn_dialog_finish);

        btn_dialog_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*RESTART EVERY 5 SECEOND SAVE FRONT PICATURE*/
                // new PictureService().startCapturing(MainActivity.this,listener);

                dialog.dismiss();
                getNextQuestionAndOption();
                dialog.hide();


            }
        });

        btn_dialog_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Json Object For Submitting Question And Answer
                if (AndroidUtils.isNetworkAvailable(MainActivity.this)) {
                    btn_dialog_finish.setVisibility(View.GONE);
                    btn_dialog_next.setVisibility(View.GONE);
                    if (examType) {
                        /*if sdk version is below 21 then close shoutdown execute service*/
                        if (Build.VERSION.SDK_INT >= 21) {
                            Log.d(TAG, "--->above 20<---");
                            if (runnablePhotoCapture != null) {
                                Log.d(TAG, "--->thread stop");
                                handler.removeMessages(0);
                                handler.removeCallbacks(runnablePhotoCapture);
                                runnablePhotoCapture = null;
                                handler = null;
                            }
                        } else {
                            es.shutdownNow();
                        }
                    }

                    createJson();

                } else {

                    Toast.makeText(MainActivity.this,
                            getApplicationContext().getString(R.string.internet_connection),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //If Reached Last Question  Then Visible Finish Button
        if (examType) {

            if (count == SplashActivity.questionnaireList.size()) {
                tv_dialog_done_question.setVisibility(View.GONE);
                btn_dialog_finish.setVisibility(View.VISIBLE);
            }

        } else {

            if (count == SplashActivity.demoQuestionnaireList.size()) {
                tv_dialog_done_question.setVisibility(View.GONE);
                btn_dialog_finish.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Crate json object
     */
    private void createJson() {
        try {

            Log.d(TAG, "queAnsList.size() >> " + queAnsList.size());
//            Log.d(TAG, "resultList.size() >> " + resultList.size());
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < queAnsList.size(); i++) {

                JSONObject jsonObj = new JSONObject();
                HashMap<String, List<String>> map = queAnsList.get(i);
                String key = queAnsList.get(i).keySet().toArray()[0].toString();
                jsonObj.put("question_id", key);

                List<String> tempList = map.get(key);
                // Option array
                JSONArray jArray = new JSONArray();
                if (tempList != null && tempList.size() > 0) {
                    for (int j = 0; j < tempList.size(); j++) {
                        jArray.put(tempList.get(j));
                    }
                }
                jsonObj.put("question_id", queAnsList.get(i).keySet().toArray()[0]);
                jsonObj.put("option_ids", jArray);
                if(examType) {
                    jsonObj.put("user_result",resultList.get(i));
                }

                jsonArray.put(jsonObj);
            }

            Log.d(TAG, "jsonArray >> " + jsonArray);

            HashMap<String, String> param = new HashMap<>();

            if (examType) {

                //param.put("questionnaire_id", SplashActivity.questionnaireModel.getId());
                param.put("questionnaire_id",userSession.getQuestionnaireID()/*Constant.QUESTIONNAIRE_ID*/);
                param.put("choices", jsonArray.toString());
                param.put("device_id",userSession.getDeviceID() /*deviceId*/ /*randomNumber()*/);
                param.put("phone_number", "1234567890");


            } else {

                //Set Parameter When getIsDemo is 1 then set Yes Other Wise 0
                if (SplashActivity.demoQuestionnaireList.get(0).getIsDemo().equalsIgnoreCase("1")) {
                    param.put("is_demo", "yes");

                } else {
                    param.put("is_demo", "no");
                }

                //param.put("is_demo", SplashActivity.questionnaireModel.getIsDemo());
                param.put("questionnaire_id", AndroidUtils.base64(SplashActivity.demoQuestionnaireList.get(0).getId()));
                param.put("choices", jsonArray.toString());
                param.put("device_id",userSession.getDeviceID()/*deviceId*/);
                param.put("phone_number", "1234567890");
            }

            //Call Send Ans API
            sendAnswerAPI(param);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    /**
     * Json Parser For Send Answer Service
     *
     * @param jsonObject
     */
    private void jsonParserSendAnswer(JSONObject jsonObject) {

        try {

            if (jsonObject.getString("message") != null && jsonObject.getString("message").length() > 0) {
                //Show Finish Dialog
                //showFinishDialog(jsonObject.getString("message"));
                if (examType) {
                    //Send All Data To The Server Like Images And Audio
                    showFinishDialog(jsonObject.getString("message"));
                    //sendAllDataToServer();
                } else {
                    showFinishDialog(jsonObject.getString("message"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show Finish Dialog When Exam Over
     *
     * @param msg
     */
    private void showFinishDialog(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        // Setting OK Button
        alertDialog.setButton(context.getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (examType) {
                    sendAllDataToServer();
                    //finish();
                } else {

                    if (count == SplashActivity.demoQuestionnaireList.size()) {
                        nxtQuestion = 0;
                    }

                    Intent intent = new Intent(MainActivity.this, TestTypeActivity_.class);
                    startActivity(intent);
                    finish();
                }
               /* if(examType) {

                    //Stop Capture Images
                    es.shutdownNow();
                    //Stop Recording
                     AudioUtility.stopAudioRec(MainActivity.this);
                    //Zip Create For Images
                    ZipAsyncTask();

                    if (count == SplashActivity.questionnaireList.size()) {
                        nxtQuestion = 0;
                    }
                    //finish();
                }else {

                    if (count == SplashActivity.demoQuestionnaireList.size()) {
                        nxtQuestion = 0;
                    }

                    Intent intent = new Intent(MainActivity.this,TestTypeActivity_.class);
                    startActivity(intent);
                    finish();
                }*/
            }

        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Call Api For SendAnswer
     *
     * @param param
     */
    private void sendAnswerAPI(HashMap<String, String> param) {

        QuestionnaireService.sendAnswer(MainActivity.this, param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "response ans >> " + response);
                jsonParserSendAnswer(response);
            }
        }, new APIService.Error<JSONObject>() {
            @Override
            public void onError(JSONObject error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void startCamera() {

        // mPreview.getHolder().addCallback(this);
        //mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        /* Edit by Vishal G: 18-04-2017 */
       /*if(AndroidUtils.checkBuildVersionKitkat()) {
            //for kitket
            mCamera = Camera.open(0);
        }else {*/
        mCamera = AndroidUtils.openFrontFacingCameraGingerbread();//Camera.open();
        //}
        NewCameraPreview newCameraPreview = new NewCameraPreview(mCamera, MainActivity.this,
                mPreview.getHolder(), mPreview);
        capturePhotoEveryFiveSec();
    }


    /**
     * Capture Images Every Five Seconds
     */
    private void capturePhotoEveryFiveSec() {

        es = Executors.newCachedThreadPool();
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                es.submit(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void run() {
                        //mCamera.enableShutterSound(false);
                        Log.d("TAG", "--->Called 5 seconds<---");
                        Log.d("TAG", "--->Camera<--" + mCamera);
                        mCamera.takePicture(MainActivity.this, null, null, MainActivity.this);
                    }
                });
            }
        }, 0, imageCaptureTime, TimeUnit.MILLISECONDS);
    }


    /**
     * Call Send Audio API
     */
    private void sendAudioFile() {

        HashMap<String, Object> params = new HashMap();
        params.put("questionnaire_id",userSession.getQuestionnaireID() /*Constant.QUESTIONNAIRE_ID*/);
        params.put("device_id",userSession.getDeviceID()/*deviceId*//*randomNumber()*/);
        params.put("audio_file", AndroidUtils.getAudioFile());

        SendAudioService.sendAudioFile(MainActivity.this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Audio Response-->" + response.toString());
                showFinishDialog(getString(R.string.submit_sucessfully));
                //finish();
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });
    }

    /**
     * Call Send Image ZIP API
     */
    public void sendImageZIPFile() {

        HashMap<String, Object> params = new HashMap();
        params.put("questionnaire_id",userSession.getQuestionnaireID() /*Constant.QUESTIONNAIRE_ID*/);
        params.put("device_id",userSession.getDeviceID()/* deviceId*//*randomNumber()*/);
        params.put("zip_file", AndroidUtils.getImageZipFile());

        SendImageService.sendImageZipFile(MainActivity.this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Image Zip  Response-->" + response.toString());
                //Send Audio
                sendAudioFile();
            }
        });
    }

    /**
     * ZipAsyncTask For Create Zip File And After Send To Server
     */
    private void ZipAsyncTask() {

        final Dialog dialog = DialogUtility.processDialog(MainActivity.this, "Please wait...", false);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                dialog.show();
                //Here zip Create In Background
                AndroidUtils.zipFile(context);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "--->Zip Completed<---");
                if (AndroidUtils.isNetworkAvailable(MainActivity.this)) {
                    dialog.hide();
                    if (!AndroidUtils.checkBuildVersionKitkat()) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            Log.d(TAG, "--->below 21<---");
                            // handler.removeMessages(0);
                            // handler.removeCallbacksAndMessages(null);
                        } else {
                            mCamera.stopPreview();
                            mCamera.release();
                            mCamera = null;
                        }

                    }
                    //Send Images Zip
                    startService();
                    //sendImageZIPFile();
                    //sendAudioFile();
                    finish();
                    // System.exit(0);


                } else {
                    dialog.hide();
                    Toast.makeText(MainActivity.this, getApplicationContext().getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }.execute();
    }

    /**
     * Send Data like Images And Audio Here.
     */
    private void sendAllDataToServer() {

        //Stop Capture Images
        //es.shutdownNow();
        //Stop Recording
        AudioUtility.stopAudioRec(MainActivity.this);


        //Zip Create For Images
        ZipAsyncTask();

        if (count == SplashActivity.questionnaireList.size()) {
            nxtQuestion = 0;
        }
    }


    /**
     * Start Upload Data Service
     */
    public void startService() {
        startService(new Intent(getBaseContext(), UploadData.class));
    }


    /*check permiswsion for camera*/
    private void checkPermissions() {
        final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        final List<String> neededPermissions = new ArrayList<>();
        for (final String p : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    p) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(p);
            }
        }
        if (!neededPermissions.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(neededPermissions.toArray(new String[]{}),
                        MY_PERMISSIONS_REQUEST_ACCESS_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
    }
}
