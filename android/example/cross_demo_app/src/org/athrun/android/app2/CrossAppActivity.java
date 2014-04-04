package org.athrun.android.app2;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CrossAppActivity extends Activity {
	private Button mButton;
	private static Map<String, Class<?>> sClassMap = new HashMap<String, Class<?>>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		mButton = (Button) findViewById(R.id.mbutton);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(CrossAppActivity.this,
						"Button in another app is pressed!", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

//	private static Class<?> getIdClass(String packageName, String sourceDir)
//			throws RemoteException, ClassNotFoundException {
//		// This kind of reflection is expensive, so let's only do it
//		// if we need to
//		Class<?> klass = sClassMap.get(packageName);
//		if (klass == null) {
//			DexClassLoader classLoader = new DexClassLoader(sourceDir,
//					"/data/local/tmp", null, ClassLoader.getSystemClassLoader());
//			klass = classLoader.loadClass(packageName + ".R$id");
//			sClassMap.put(packageName, klass);
//		}
//		return klass;
//	}
}