/**
 * 
 */
package org.athrun.android.result.junit;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author taichan
 * 
 */
@XStreamAlias("testsuite")
public class Testsuite {
	public void setSystemOut(String systemOut) {
		this.systemOut = systemOut;
	}

	public void setSystemErr(String systemErr) {
		this.systemErr = systemErr;
	}

	public String getSystemOut() {
		return systemOut;
	}

	public String getSystemErr() {
		return systemErr;
	}

	private String properties;
	@XStreamAlias("system-out")
	private String systemOut;
	@XStreamAlias("system-err")
	private String systemErr;
	@XStreamAsAttribute
	private String name;
	@XStreamImplicit
	private List<Testcase> testcases = new ArrayList<Testcase>();

	public void add(Testcase tc) {
		testcases.add(tc);
	}

	/**
	 * @param name
	 */
	public Testsuite(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the testcases
	 */
	public List<Testcase> getTestcases() {
		return testcases;
	}

}
