/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIASlider extends UIAElement {

	public UIASlider() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIASlider(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Drags the slider to the specified value
	 * @param d The desired decimal value from 0 to 1, inclusive. A 0 value represents far left and a value of 1 represents far right.
	 * @throws Exception
	 */
	public void dragToValue(double d) throws Exception {
		MySocket.getVoid(this.guid + ".dragToValue(" + d + ")");
	}

}
