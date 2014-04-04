package org.athrun.android.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * This class contains the methods of catching screen shot
 * Because the screen shot file is saved on sdcard, please
 * add permission: android.permission.WRITE_EXTERNAL_STORAGE
 * in test apk's AndroidManifest.xml
 * @author zhuangfei(jiand.zhaojd@alibaba-inc.com)
 */
public class ScreenShot {  
    private ScreenShot() {
        throw new AssertionError();
    }
    // get the screen shot of Activity，save it as png  
    public static Bitmap takeScreenShot(Activity activity) {  
  
    	 // The view you will take screenshot
        View view = activity.getWindow().getDecorView(); 
        view.setDrawingCacheEnabled(true); 
        view.buildDrawingCache(); 
        Bitmap wholeBitmap = view.getDrawingCache(); 
  
        // Get the height of device's statusBar
        Rect frame = new Rect(); 
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame); 
        int statusBarHeight = frame.top; 
        Log.i("TAG", "" + statusBarHeight); 
  
        // Get the height and width of device screen
        int width = activity.getWindowManager().getDefaultDisplay().getWidth(); 
        int height = activity.getWindowManager().getDefaultDisplay().getHeight(); 
        
        // remove the area of statusbar
        Bitmap lastBitmap = Bitmap.createBitmap(wholeBitmap, 0, statusBarHeight, width, height 
                - statusBarHeight); 
        view.destroyDrawingCache(); 
        return lastBitmap; 
    }  
  
    // save screen shot to sdcard  
    public static boolean savePic(Bitmap b, String strFileName) {  
  
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(strFileName);  
            if (null != fos) {  
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);  
                fos.flush();  
                fos.close();  
                return true;
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return false;
    }  
  
    public static void shoot(Activity a, String picName) {
        String saveNameStr = "sdcard" +  File.separator + "Athrun" +  File.separator + picName + ".png";
        Bitmap btp = ScreenShot.takeScreenShot(a);
        if(btp != null)
        {
            ScreenShot.savePic(btp, saveNameStr); 
        }
        else {
            System.out.println("capture the screen fail!");
        }
    }
}  
