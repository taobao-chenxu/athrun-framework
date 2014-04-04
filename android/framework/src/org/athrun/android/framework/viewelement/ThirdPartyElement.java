package org.athrun.android.framework.viewelement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Instrumentation;
import android.view.View;

public class ThirdPartyElement extends ViewElement {
	private View thirdPartyView;

	protected ThirdPartyElement(Instrumentation inst, View thirdPartyView) {
		super(inst, thirdPartyView);
		this.thirdPartyView = thirdPartyView;
	}
	
	private Object invokeMethod(String methodName, Object... args) {
		Method method;
		try {
			method = thirdPartyView.getClass().getDeclaredMethod(methodName);
			return method.invoke(thirdPartyView, args);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Object getMethod(String methodName, Object... args) {
		inst.waitForIdleSync();
		return invokeMethod(methodName, args);
	}
	
	public void setMethod(final String methodName, final Object... args) {
		inst.waitForIdleSync();
		inst.runOnMainSync(new Runnable() {
			
			@Override
			public void run() {
				invokeMethod(methodName, args);
			}
		});
	}

}
