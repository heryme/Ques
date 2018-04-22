package test.de.recrutement.utils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;



import java.io.IOException;
import java.util.List;

import test.de.recrutement.R;

/**
 * Created by Rahul Padaliya on 3/9/2017.
 */
public class NewCameraPreview implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Context context;
    private SurfaceView mPreview;


    public NewCameraPreview(Camera mCamera, Context context, SurfaceHolder mHolder, SurfaceView surfaceView) {
        this.context = context;
        this.mCamera = mCamera;
        this.mHolder = mHolder;
        mPreview = surfaceView;
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        ;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            Camera.Size selected = sizes.get(0);
            params.setPreviewSize(selected.width, selected.height);
            mCamera.setParameters(params);
            //mCamera.setDisplayOrientation(90);

            //SurfaceTexture This line Of Code Only For Lollipop
            if (!AndroidUtils.checkBuildVersionKitkat() && !AndroidUtils.checkBuildVersionJellyBean()) {
                SurfaceTexture st = new SurfaceTexture(context.MODE_PRIVATE);
                try {
                    mCamera.setPreviewTexture(st);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
