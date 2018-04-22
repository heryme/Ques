package test.de.recrutement.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;


import test.de.recrutement.R;
import test.de.recrutement.activities.MainActivity;
import test.de.recrutement.activities.SplashActivity;
import test.de.recrutement.constant.Constant;
import test.de.recrutement.model.SettingModel;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static test.de.recrutement.constant.Constant.ZIP_PATH;

/**
 * Created by Rahul Padaliya on 3/7/2017.
 */
public class AndroidUtils {

    public static final String TAG = AndroidUtils.class.getSimpleName();

    /**
     * Set data for tv_dialog_done_question (In for next question dialog)
     *
     * @return
     */
    public static String setData(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(R.string.have_done));
        stringBuilder.append(" ");
        stringBuilder.append(MainActivity.count);
        stringBuilder.append(" / ");
        if (MainActivity.examType) {
            stringBuilder.append(SplashActivity.questionnaireList.size());
        } else {
            stringBuilder.append(SplashActivity.demoQuestionnaireList.size());
        }
        stringBuilder.append(" ");
        stringBuilder.append(context.getString(R.string.next_one));
        return stringBuilder.toString();
    }

    /**
     * Get Image Capture Value
     *
     * @return
     */
    public static String getImageCapture() {
        final String KEY_NAME = "image_capture";

        if (SplashActivity.settingsList != null && SplashActivity.settingsList.size() > 0) {
            for (SettingModel settingModel : SplashActivity.settingsList) {
                if (settingModel.getSettingModelInnerList() != null && settingModel.getSettingModelInnerList().size() > 0) {
                    for (SettingModel.SettingModelInner settingModelInner : settingModel.getSettingModelInnerList()) {
                        if (settingModelInner.getKey().equalsIgnoreCase(KEY_NAME))
                            return settingModelInner.getValue();
                    }
                }
            }
        }
        return null;
    }


    /**
     * Check Current OS Lollipop or not  Build Version Of The Device
     *
     * @return
     */
    public static Boolean checkBuildVersionLollipop() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion == Build.VERSION_CODES.LOLLIPOP_MR1) {
            // Do something for lollipop and above versions
            return true;
        } else {
            // do something for phones running an SDK before lollipop
            return false;
        }
    }

    /**
     * Check Current OS KITKAT or not  Build Version Of The Device
     *
     * @return
     */
    public static Boolean checkBuildVersionKitkat() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion == Build.VERSION_CODES.KITKAT) {
            // Do something for lollipop and above versions
            return true;
        } else {
            // do something for phones running an SDK before lollipop
            return false;
        }
    }


    /**
     * Check Current OS JELLY_BEAN or not  Build Version Of The Device
     *
     * @return
     */
    public static Boolean checkBuildVersionJellyBean() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion == Build.VERSION_CODES.JELLY_BEAN) {
            // Do something for lollipop and above versions
            return true;
        } else {
            // do something for phones running an SDK before lollipop
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static long timeConvertTomills(String time) {
        try {
            if (time != null && time.length() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = null;
                date = sdf.parse(time);
                System.out.println("in milliseconds: " + date.getTime());
                return date.getTime();
            } else {
                Log.d(TAG, "Photo capture duration not available");
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Create App Directory
     */
    public static void createAppDirectory() {
        File mydir = new File(Constant.APP_DIR + Constant.SUB_DIR);
        if (!mydir.exists())
            mydir.mkdirs();
    }

    /**
     * Create Image directory
     *
     * @return
     */
    public static File getOutputMediaFile() {

        File file = new File(Constant.APP_DIR + Constant.SUB_DIR + Constant.IMAGES_DIR);
        if (!file.exists())
            file.mkdirs();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        return new File(file.getPath() + File.separator + formatter.format(now) + ".jpg");
    }

    /**
     * Check Camera Or Not
     *
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check permission
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check Front Camera In Device Or Not
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    Log.d(TAG,"camIdx >> "+ camIdx);
                    cam = Camera.open(camIdx);
                    cam.enableShutterSound(false);
                } catch (RuntimeException e) {
                    Log.e("Camera", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    /**
     * Create Image zip file
     */
    public static void zipFile(Context context) {
        UserSession userSession = new UserSession(context);
        try {
            CompressFile.zipDir(Constant.ZIP_PATH + userSession.getQuestionnaireID() + ".zip",
                    Constant.APP_DIR + Constant.SUB_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Audio File
     *
     * @return
     */
    public static File getAudioFile() {
        String path = Constant.AUDIO_PATH;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {       //Log.d("AudioFile", "--->getZipFile:" + files[i].getAbsoluteFile());
            return files[i].getAbsoluteFile();
        }
        return null;
    }


    /**
     * Get Image zip File
     *
     * @return
     */
    public static File getImageZipFile() {
        String path = Constant.ZIP_PATH;
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".zip")) {
                    return files[i].getAbsoluteFile();
                }
            }
        }
        return null;
    }

    /**
     * Delete All Image File From The Image Directory When Test Start
     */
    public static void deleteAllFile(String path) {
        //String path = Constant.IMAGE_PATH;
        //Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            //Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                //Log.d("Test Files", "--->FileName:" + files[i].getName());
                files[i].getAbsoluteFile().delete();
                //Log.d("getZipFile", "--->getZipFile:" + files[i].getAbsoluteFile());
            }
        }
    }

    /**
     * Delete All Audio File From The Audio Directory When Recording Start
     */
    public static void deleteAllAudioFile() {
        String path = Constant.AUDIO_PATH;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Test Files", "--->FileName:" + files[i].getName());
                files[i].getAbsoluteFile().delete();
                Log.d("getZipFile", "--->getZipFile:" + files[i].getAbsoluteFile());
            }
        }
    }

    /**
     * Convert Questionnaire ID In Base64
     *
     * @param arg
     * @return
     */
    public static String base64(String arg) {
        try {
            // Sending side
            byte[] data = arg.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            System.out.println("Base 64---> " + base64);
            return base64;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateNoteOnSD(Context context,
                                        String sFileName,
                                        String data) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String dateTime = format.format(new Date()).toString();
            File root = new File(Environment.getExternalStorageDirectory(), "Questionnaire Intent Data");
            if (!root.exists()) {
                root.mkdirs();
            }
            File log = new File(root, sFileName);
            try {
                if (log.exists() == false) {
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
                }
                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                out.append(" date & time :" + dateTime + "Data--->" + data + "\n" );
                out.close();
            } catch (IOException e) {
                System.out.println("COULD NOT LOG!!");
            }
            /*Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Device ID
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID).toString().trim();
    }
}

