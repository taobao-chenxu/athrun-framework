package org.athrun.android.framework.dragon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
/**
 * This Class will helper tester take screen shot of current activity under test
 * @author huangqin 
 * added in 2013-4-19
 *
 */
public class ScreenShot {	
    /**
     *  Take screenshot of  current activity and saved with .png
     * @param activity
     * @return
     */
    private Bitmap screenShot(Activity activity) { 
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
        
        Log.i("TAG", "statusBarHeight:" + statusBarHeight +  
        		" width:" + width + "  height:" + height + 
        		" width2:" + wholeBitmap.getWidth() + " height2:" + wholeBitmap.getHeight());
        
        // remove the area of statusbar
        Bitmap lastBitmap = Bitmap.createBitmap(wholeBitmap, 0, statusBarHeight, width, (wholeBitmap.getHeight() - statusBarHeight)); 
       
        view.destroyDrawingCache(); 
     
        return lastBitmap; 
    } 
  
    /**
     *  Save the bitmap with .png to Sdcard 
     * @param bitmap
     * @param strFileName
     */
    private boolean savePic(Bitmap bitmap, String strFileName) {
        FileOutputStream fos = null; 
        try { 
            fos = new FileOutputStream(strFileName); 
            if (null != fos) { 
            	bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos); 
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
    
    /**
	 * take screenshot of current activity which under testing
	 * added by huangqin 2013-4-19
	 * @return
	 * @throws Exception
	 */
	public boolean takeScreenShot(Activity curActivity){
		Bitmap bitmap = screenShot(curActivity);
		return savePic(bitmap, getFilePath(curActivity.getLocalClassName()));
	}
	/**
	 * 获取SD卡跟路径
	 * @return
	 */
	private String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
			Log.i("TAG", "sd card path:" + sdDir);
			return sdDir.toString();
		}		
		return null;
	}
	
	private String getFilePath(String fileName) {
		String sdPath = getSDPath();
		return sdPath + "/pandaspacetest/screenshot/" + fileName;
	}
} 
