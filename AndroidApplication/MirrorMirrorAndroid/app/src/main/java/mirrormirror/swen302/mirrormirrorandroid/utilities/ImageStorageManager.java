package mirrormirror.swen302.mirrormirrorandroid.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import mirrormirror.swen302.mirrormirrorandroid.R;

/**
 * Created by bondkyal on 10/08/17.
 */

public class ImageStorageManager {
    public static void storeImage(String dateTime, byte[] data, Context context) {
        try {
            saveImageReference(context, dateTime);
            FileOutputStream fos = context.openFileOutput(dateTime, Context.MODE_PRIVATE);
            fos.write(data);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadImageByName(String fileName, Context context){
        try{
            FileInputStream fis = context.openFileInput(fileName);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return bytes;

        }catch(Exception e){

        }
        return null;
    }

    public static void saveImageReference(Context context, String dateTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.images_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sharedPreferences.getAll().size()+"",dateTime );

//        String image1 = sharedPreferences.getString("image1", null);
//        String image2 = sharedPreferences.getString("image2", null);
//        String image3 = sharedPreferences.getString("image3", null);
//        String image4 = sharedPreferences.getString("image4", null);
//
//        if(image1 == null){
//            editor.putString("image1", dateTime);
//        }else if(image2 == null){
//            editor.putString("image2", dateTime);
//        }else if(image3 == null){
//            editor.putString("image3", dateTime);
//        }else if(image4 == null){
//            editor.putString("image4", dateTime);
//        }
//        else{
//            //delete oldest image
//            String toDelete = sharedPreferences.getString("image4", null);
//            String filesDirPath = context.getFilesDir().getAbsolutePath();
//            File f = new File(filesDirPath+"/"+toDelete);
//            if(f.exists()){
//                f.delete();
//            }
//
//            editor.putString("image4", sharedPreferences.getString("image3",null));
//            editor.putString("image3", sharedPreferences.getString("image2",null));
//            editor.putString("image2", sharedPreferences.getString("image1",null));
//            editor.putString("image1", dateTime);
//
//        }

        editor.apply();
    }
}
