package test.de.recrutement.constant;

import android.os.Environment;

/**
 * Created by Hiren Kapuria on 1/4/2017.
 */

public class Constant {

    public static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SUB_DIR = "/Questionnaire/";
    public static final String VIDEO_DIR = "Video/";
    public static final String IMAGES_DIR = "images/";
    public static final String AUDIO_DIR = "audio/";
    public static final String IMAGE_PATH = Constant.APP_DIR + Constant.SUB_DIR + Constant.IMAGES_DIR;
    public static final String AUDIO_PATH = Constant.APP_DIR + Constant.SUB_DIR + Constant.AUDIO_DIR;
    public static final String ZIP_PATH = Constant.APP_DIR + Constant.SUB_DIR;

    public static final String CODE_ALREADY_GIVEN_EXAM = "101";
    public static final String CODE_INVALID_QUESTIONNAIRE_ID = "100";
    public static final String CODE_SUCCESS = "1";

    public static final int COMPRESS = 50;




    //Live for webs
    //public static final String QUESTIONNAIRE_ID = /*"NThmNWI2ZWRhMzZhM2YxNGIxMTllM2Q0"*/"NThhYWRmMjg5ZmRhMjAzMzExNmRhYzM2";

    //Client
     public static final String QUESTIONNAIRE_ID = "NTliMmE2YzczOWI4ZGMzNDFjNGYyOWEy";/*NThmYjZmYzAzOWI4ZGM1MDJhNjk1MTQz*/

    //Local
    //public static final String QUESTIONNAIRE_ID = "NTkxYzMzYjVhMzZhM2YyM2QwNjc2NzAy";

    public static final String EXAM_TYPE = "exam_type";
}
