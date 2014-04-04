/**
 * 
 */
package org.athrun.android.result.junit;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author taichan
 * 
 */
@XStreamAlias("testcase")
public class Testcase {

	@XStreamAsAttribute
	private String classname;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String time;
	private ErrorNode error;

	/**
	 * @param classname
	 * @param name
	 * @param time
	 */
	public Testcase(String classname, String name, String time) {
		super();
		this.classname = classname;
		this.name = name;
		this.time = time;
	}

	/**
	 * @return the error
	 */
	public ErrorNode getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(ErrorNode error) {
		this.error = error;
	}

	/**
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

}
