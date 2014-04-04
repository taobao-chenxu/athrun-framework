package net.srcz.android.screencast.api.injector;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

import javax.swing.SwingUtilities;

import net.srcz.android.screencast.api.AndroidDevice;
import net.srcz.android.screencast.api.recording.QuickTimeOutputStream;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class ScreenCaptureThread extends Thread {

	private BufferedImage image;
	private Dimension size;
	private IDevice device;
	private QuickTimeOutputStream qos = null;
	private boolean landscape = false;
	private ScreenCaptureListener listener = null;

	public ScreenCaptureListener getListener() {
		return listener;
	}

	public void setListener(ScreenCaptureListener listener) {
		this.listener = listener;
	}

	public interface ScreenCaptureListener {
		public void handleNewImage(Dimension size, BufferedImage image,
				boolean landscape);
	}

	public ScreenCaptureThread(IDevice device) {
		super("Screen capture");
		this.device = device;
		image = null;
		size = new Dimension();
	}

	public Dimension getPreferredSize() {
		return size;
	}

	public void run() {
		do {
			try {
				boolean ok = fetchImage();
				if (!ok)
					break;
			} catch (java.nio.channels.ClosedByInterruptException ciex) {
				break;
			} catch (IOException e) {
				System.err.println((new StringBuilder())
						.append("Exception fetching image: ")
						.append(e.toString()).toString());
			}

		} while (true);
	}

	public void startRecording(File f) {
		try {
			if (!f.getName().toLowerCase().endsWith(".mov"))
				f = new File(f.getAbsolutePath() + ".mov");
			qos = new QuickTimeOutputStream(f,
					QuickTimeOutputStream.VideoFormat.JPG);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		qos.setVideoCompressionQuality(1f);
		qos.setTimeScale(30); // 30 fps
	}

	public void stopRecording() {
		try {
			QuickTimeOutputStream o = qos;
			qos = null;
			o.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean fetchImage() throws IOException {
		if (device == null) {
			// device not ready
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}

		// RawImage rawImage = null;
		BufferedImage image;
		synchronized (device) {
			String filename = "/AppTestLog/image.jpg";

//			 File f = new File(filename);
//			 AndroidDevice d = new AndroidDevice(device);
//			 System.out.println(d.executeCommand("/data/local/gsnap /sdcard/test/2.jpg /dev/graphics/fb0 50"));
//			 d.pullFile("/sdcard/test/2.jpg", f);

		    String cmd = "instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate /Users/jerryding/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app -e UIASCRIPT /Users/jerryding/Desktop/image.js -e UIARESULTSPATH /AppTestLog/";
		    runCmd(cmd);
			//cmd = "adb pull /sdcard/test/2.jpg d:/1.jpg";
			//runCmd(cmd);
			cmd = "sips -s format jpeg -s formatOptions 60 /AppTestLog/Run 1/image.png -o /AppTestLog/image.jpg";
			runCmd(cmd);

			FileInputStream fIn = new FileInputStream(filename);
			JPEGImageDecoder jpeg_decode = JPEGCodec.createJPEGDecoder(fIn);
			image = jpeg_decode.decodeAsBufferedImage();

			// rawImage = device.getScreenshot();
			System.out.println("Time now: \t" + System.currentTimeMillis());
		}
		if (image != null) {
			display(image);
		} else {
			System.out.println("failed getting screenshot through ADB ok");
		}
		
		String deleimage1 = "rm /AppTestLog/image.png";
		String deleimage2 = "rm /AppTestLog/image.jpg";
		runCmd(deleimage1);
		runCmd(deleimage2);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			return false;
		}

		return true;
	}

	private void runCmd(String cmd) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void toogleOrientation() {
		landscape = !landscape;
	}

	public void display(BufferedImage bufferImage) {
		image = bufferImage;
		size.setSize(image.getWidth(), image.getHeight());
		landscape = false;

		try {
			if (qos != null)
				qos.writeFrame(image, 10);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (listener != null) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					listener.handleNewImage(size, image, landscape);
					// jp.handleNewImage(size, image, landscape);
				}
			});
		}

	}

	public void display(RawImage rawImage) {
		int width2 = landscape ? rawImage.height : rawImage.width;
		int height2 = landscape ? rawImage.width : rawImage.height;
		if (image == null) {
			image = new BufferedImage(width2, height2,
					BufferedImage.TYPE_INT_RGB);
			size.setSize(image.getWidth(), image.getHeight());
		} else {
			if (image.getHeight() != height2 || image.getWidth() != width2) {
				image = new BufferedImage(width2, height2,
						BufferedImage.TYPE_INT_RGB);
				size.setSize(image.getWidth(), image.getHeight());
			}
		}
		int index = 0;
		int indexInc = rawImage.bpp >> 3;
		for (int y = 0; y < rawImage.height; y++) {
			for (int x = 0; x < rawImage.width; x++, index += indexInc) {
				int value = rawImage.getARGB(index);
				if (landscape)
					image.setRGB(y, rawImage.width - x - 1, value);
				else
					image.setRGB(x, y, value);
			}
		}

		try {
			if (qos != null)
				qos.writeFrame(image, 10);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (listener != null) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					listener.handleNewImage(size, image, landscape);
					// jp.handleNewImage(size, image, landscape);
				}
			});
		}
	}

}
