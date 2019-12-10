package mirrormirror.swen302.mirrormirrorandroid.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by bondkyal on 10/08/17.
 */

public class PermissionRequester {

    public static int CAMERA_PERMISSION_CONSTANT = 0;
    public static int INTERNET_PERMISSION_CONSTANT = 1;

    public static boolean requestCameraPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CONSTANT);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            return false;
        }else{
            return true;
        }

    }

    public static void requestInternetPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CONSTANT);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


}
