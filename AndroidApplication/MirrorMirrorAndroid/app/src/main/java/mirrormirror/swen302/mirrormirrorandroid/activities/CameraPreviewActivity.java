package mirrormirror.swen302.mirrormirrorandroid.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Date;

import mirrormirror.swen302.mirrormirrorandroid.utilities.CameraDispatcher;
import mirrormirror.swen302.mirrormirrorandroid.utilities.CameraPreview;
import mirrormirror.swen302.mirrormirrorandroid.R;
import mirrormirror.swen302.mirrormirrorandroid.utilities.DateTimeManager;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ImageStorageManager;
import mirrormirror.swen302.mirrormirrorandroid.utilities.PermissionRequester;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ServerController;

public class CameraPreviewActivity extends AppCompatActivity {

    private Camera frontCamera;
    private CameraPreview frontCameraPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        beginCameraSession();
    }

    private void beginCameraSession() {

        PermissionRequester.requestInternetPermission(this);
        frontCamera = CameraDispatcher.getCameraInstance();
        frontCameraPreview = new CameraPreview(this, frontCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(frontCameraPreview, 0);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //frontCamera.takePicture(null, null, picture);
                beginCameraCountdown();
            }
        });

    }


    private void beginCameraCountdown(){
        CountDownTimer timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView countdown = (TextView) findViewById(R.id.countdown_display);
                String remaining = String.valueOf(millisUntilFinished/1000);
                countdown.setText(remaining);
            }
            @Override
            public void onFinish() {
                TextView countdown = (TextView) findViewById(R.id.countdown_display);
                countdown.setText("");
                frontCamera.takePicture(null, null, picture);
            }
        };
        timer.start();
    }

    private Camera.PictureCallback picture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String dateTime = DateTimeManager.getDatetimeAsString();
            ServerController.sendImageAsBytes(data, dateTime, getApplicationContext());
            //request permissions to write to external storage
            //Save image in external storage maybe interanl?
            result(data, dateTime);

            //display image on in view
        }
    };

    private void result(byte[] data, String dateTime) {
        ImageStorageManager.storeImage(dateTime, data, this);
        Intent resultData = new Intent();
        setResult(RESULT_OK, resultData);
        finish();
    }



}
