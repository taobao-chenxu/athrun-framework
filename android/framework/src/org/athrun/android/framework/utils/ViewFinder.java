package org.athrun.android.framework.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.athrun.android.framework.LogConfigure;
import org.athrun.android.framework.viewelement.IViewElement;
import org.athrun.android.framework.viewelement.ViewUtils;

import android.view.View;
import android.widget.TextView;

public class ViewFinder {
	private final Logger logger = LogConfigure.getLogger(getClass());

	public ArrayList<View> findViewsById(int id, int timeout) {
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		ArrayList<View> matches = new ArrayList<View>();

		while (System.currentTimeMillis() < startTime + timeout) {

			ArrayList<View> all = ViewUtils.getAllViews(false);

			for (View view : all) {

				if (id == view.getId()) {
					matches.add(view);
				}
			}

			if (0 == matches.size()) {
				SleepUtils.sleep(IViewElement.RETRY_TIME);
				continue;
			}

			logger.info("find " + matches.size() + " view that match the id "
					+ id);
			return matches;
		}

		logger.error("findViewsById(" + id + ") return null.");
		return null;
	}

	public ArrayList<TextView> findViewsByText(String text, boolean isEqual, int timeout) {
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		ArrayList<TextView> matches = new ArrayList<TextView>();

		while (System.currentTimeMillis() < startTime + timeout) {

			ArrayList<View> all = ViewUtils.getAllViews(false);
			ArrayList<TextView> textViews = ViewUtils.filterViews(
					TextView.class, all);

			for (TextView textView : textViews) {

				String current = textView.getText().toString();
				if (current.contains(text)
						&& !(isEqual && !current.equalsIgnoreCase(text))) {
					matches.add(textView);
				}
			}

			if (0 == matches.size()) {
				SleepUtils.sleep(IViewElement.RETRY_TIME);
				continue;
			}

			logger.info("found " + matches.size()
					+ " TextView that contains the text " + text + ".");

			return matches;
		}

		logger.error("findViewByText(" + text + ") return null.");
		return null;
	}

	public ArrayList<View> findViewsByType(Class<?> clazz, int timeout) {
		logger.info("Search for " + clazz.getName());
		final long startTime = System.currentTimeMillis();
		logger.info("maxTimeToFindView is " + timeout);

		ArrayList<View> matches = new ArrayList<View>();

		while (System.currentTimeMillis() < startTime + timeout) {

			ArrayList<View> all = ViewUtils.getAllViews(false);
			matches = ViewUtils.filterViews(clazz, all);

			if (0 == matches.size()) {
				SleepUtils.sleep(IViewElement.RETRY_TIME);
				continue;
			}

			logger.info("found " + matches.size() + " instance of "
					+ clazz.getName() + ".");

			return matches;
		}

		logger.error("findViewsByType(" + clazz.getName() + ") return null.");
		return null;
	}
}
