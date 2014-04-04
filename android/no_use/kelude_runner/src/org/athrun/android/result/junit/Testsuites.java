/**
 * 
 */
package org.athrun.android.result.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamImplicitCollection;

/**
 * @author taichan
 * 
 */
@XStreamAlias("testsuites")
public class Testsuites {
	@XStreamImplicit
	private List<Testsuite> testsuitelist = new ArrayList<Testsuite>();

	public void add(Testsuite suite) {
		testsuitelist.add(suite);
	}

	public List<Testsuite> getTestSuites() {
		return testsuitelist;
	}

}
