package com.example.humax.albumdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.humax.albumdemo.model.RecordingAsset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;


public class SdUtils {

    public static String fileName = "recording";
    public static File sdCardDir = Environment.getExternalStorageDirectory();
    public static File sdFile  = new File(sdCardDir, fileName);

    public static boolean writeListIntoSDcard(Context context, List<RecordingAsset> list){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 400);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);
                fos.close();
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }
    public static List<RecordingAsset> readListFromSdCard(Context context){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 400);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            List<RecordingAsset> list;
            try {
                FileInputStream fis = new FileInputStream(sdFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                list = (List<RecordingAsset>) ois.readObject();
                fis.close();
                ois.close();
                return list;
            } catch (StreamCorruptedException e) {
                Log.i("110","1");
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                Log.i("110","2");
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                Log.i("110","3");
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.i("110","4");
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                Log.i("110","5");
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
}
