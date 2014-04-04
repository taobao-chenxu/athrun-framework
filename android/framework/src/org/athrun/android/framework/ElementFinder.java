package org.athrun.android.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.athrun.android.framework.special.taobaoview.SkuOptionElement;
import org.athrun.android.framework.utils.SleepUtils;
import org.athrun.android.framework.utils.ViewFinder;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ViewElement;
import org.athrun.android.framework.viewelement.ViewUtils;

import android.app.Instrumentation;
import android.view.View;
import android.widget.TextView;

public class ElementFinder {
	private final Logger logger = LogConfigure.getLogger(getClass());

	private static int timeout = IViewElement.ANR_TIME;

	private ViewFinder viewFinder;
	private Instrumentation inst;

	ElementFinder(int timeout, Instrumentation inst) {
		this.viewFinder = new ViewFinder();
		this.inst = inst;
	}

	void setTimeout(int timeout) {
		ElementFinder.timeout = timeout;
	}

	<T> T findElementById(int id, int index,
			Class<T> returnType) throws Exception {
		ArrayList<View> views = getSuitableViews(this.viewFinder
				.findViewsById(id, timeout));
		if (views.isEmpty()) {
			logger.error("findElementById(" + id + ", " + index + ", "
					+ returnType.getName() + ") return null.");
			return null;
		}

		return createInstance(views.get(index), returnType);
	}
	
	<T> T findWebElementById(int id, int index,
              Class<T> returnType, ActivityUtils activityUtils) throws Exception {
	    ArrayList<View> views = getSuitableViews(this.viewFinder
	            .findViewsById(id, timeout));
	    if (views.isEmpty()) {
	        logger.error("findElementById(" + id + ", " + index + ", "
	            + returnType.getName() + ") return null.");
	        return null;
        }

	    return createWebInstance(views.get(index), returnType, activityUtils);
    }

	<T extends TextViewElement> T findElementByText(String text, int index,
			Boolean isEqual, Class<T> returnType) throws Exception {
		ArrayList<TextView> textViews = getSuitableViews(this.viewFinder
				.findViewsByText(text, isEqual, timeout));
		if (textViews.isEmpty()) {
			logger.error("findElementByText(" + text + ", " + index + ", "
					+ isEqual + ", " + returnType.getName() + ") return null.");
			return null;
		}

		return createInstance(textViews.get(index), returnType);
	}

	<T extends ViewElement> T findElementByIndex(int index, Class<?> view,
			Class<T> returnType) throws Exception {
		ArrayList<View> views = getSuitableViews(this.viewFinder
				.findViewsByType(view, timeout));
		if (views.isEmpty()) {
			logger.error("findElementByIndex(" + index + ", " + view.getName()
					+ ", " + returnType.getName() + ") return null.");
			return null;
		}

		return createInstance(views.get(index), returnType);

	}

	SkuOptionElement findSkuOptionByText(String text) throws Exception {
		Class<?> skuSelectOption = Class
				.forName("com.taobao.tao.component.skunative.SkuSelectOption");

		ArrayList<View> skuViews = this.viewFinder
				.findViewsByType(skuSelectOption, timeout);

		for (View view : skuViews) {
			Method getText = view.getClass().getDeclaredMethod("getText");

			if (((String) getText.invoke(view)).equalsIgnoreCase(text)) {
				return createInstance(view, SkuOptionElement.class);
			}
		}
		logger.error("findSkuSelectOptionByText(" + text + ") return null.");

		return null;
	}
	
	boolean waitForText(String text, int timeout) {
		ArrayList<TextView> textViews = this.viewFinder.findViewsByText(text, true, timeout);
		return textViews.isEmpty() ? false : true;
	}

	private <T extends View> ArrayList<T> getSuitableViews(ArrayList<T> matches) {
		ArrayList<T> suitableViews = new ArrayList<T>();
		if (null == matches) {
			return suitableViews;
		}
		
		int matchedCounts = matches.size();

		if (1 == matchedCounts) {
			logger.info("There is 1 matched view.");
			View view = matches.get(0);
			// add view.hasWindowFocus() to ensure the view is on the top layer
			//if (view.hasWindowFocus() && view.getVisibility() == View.VISIBLE && view.isShown()) {
			//add by xiaoliang.chenxl: resolve the problem of webview is not Visible right now  
			final long startTime = System.currentTimeMillis();
	        while (System.currentTimeMillis() < startTime + timeout) {
	            if (view.getVisibility() == View.VISIBLE && view.isShown()) {
	                suitableViews = matches;
	                SleepUtils.sleep(500);
	                break;
	            } else {
	                SleepUtils.sleep(500);
	            }
	        }
		} else if (matchedCounts > 1) {

			ArrayList<T> shown = ViewUtils.removeUnshownViews(matches);
			logger.info("There are " + shown.size()
					+ " matched views is shown.");
			ArrayList<T> inScreen = new ArrayList<T>();

			for (T view : matches) {
				if (!ViewUtils.isViewOutOfScreen(view)) {
					inScreen.add(view);
				}
			}
			logger.info("There are " + inScreen.size()
					+ " matched views in screen.");

			ArrayList<T> inScreenShown = ViewUtils.removeUnshownViews(inScreen);

			if (0 != inScreenShown.size()) {
				suitableViews = inScreenShown;

			} else if (0 != shown.size()) {
				suitableViews = shown;
			}
		}

		return suitableViews;
	}	
	// updated by bichai@taobao.com
	private <T> T createInstance(View view, Class<T> returnType){
	      Constructor<?> constructor = returnType.getDeclaredConstructors()[0];
	      constructor.setAccessible(true);
	      Object obj = null;
	      try {
	         obj = constructor.newInstance(inst, view);
	      } catch (IllegalArgumentException e) {
	          //if catching IllegalArgumentException, check the element type and put the message into the new exception
	         throw new RuntimeException(ActivityElementType.TypeComparison(view, returnType));
	         
	      } catch (InstantiationException e) {
	         e.printStackTrace();
	      } catch (IllegalAccessException e) {
	         e.printStackTrace();
	      } catch (InvocationTargetException e) {
	         e.printStackTrace();
	      }
	      return returnType.cast(obj);
      }
	   private <T> T createWebInstance(View view, Class<T> returnType, ActivityUtils activityUtils){
          Constructor<?> constructor = returnType.getDeclaredConstructors()[0];
          constructor.setAccessible(true);
          Object obj = null;
          try {
             obj = constructor.newInstance(inst, view, activityUtils);
          } catch (IllegalArgumentException e) {
              //if catching IllegalArgumentException, check the element type and put the message into the new exception
        	             throw new RuntimeException(ActivityElementType.TypeComparison(view, returnType));
    	             
	          } catch (InstantiationException e) {
	             e.printStackTrace();
	          } catch (IllegalAccessException e) {
	             e.printStackTrace();
	          } catch (InvocationTargetException e) {
	             e.printStackTrace();
	          }
          return returnType.cast(obj);
      }
    }