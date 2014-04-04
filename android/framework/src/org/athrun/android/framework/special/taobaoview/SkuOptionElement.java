package org.athrun.android.framework.special.taobaoview;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.athrun.android.framework.viewelement.ViewElement;

import android.app.Instrumentation;
import android.view.View;

/**
 * 
 * @author bingyang.djj
 *
 */
public class SkuOptionElement extends ViewElement {
	private View skuView;

	protected SkuOptionElement(Instrumentation inst, View skuView) {
		super(inst, skuView);
		this.skuView = skuView;
	}
	
	private Object method(String methodName) {
		Method method;
		try {
			method = skuView.getClass().getDeclaredMethod(methodName);
			return method.invoke(skuView);
			
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
	
	@Override
	public boolean isSelected() {
		inst.waitForIdleSync();
		return (Boolean) method("isSelected");
	}
	
	public boolean isCanSelect() {
		inst.waitForIdleSync();
		return (Boolean) method("isCanSelect");
	}
}
