/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAPickerWheel extends UIAPicker {

	public UIAPickerWheel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAPickerWheel(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public void selectValue(String value) throws Exception {
		MySocket.getVoid(this.guid + ".selectValue('" + value + "')");

	}

	public void selectValue(int value) throws Exception {
		MySocket.getVoid(this.guid + ".selectValue(" + value + ")");

	}
}
