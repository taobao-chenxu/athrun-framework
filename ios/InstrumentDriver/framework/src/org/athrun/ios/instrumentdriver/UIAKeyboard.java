/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAKeyboard extends UIAElement {

	public UIAKeyboard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAKeyboard(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public void typeString(String text) throws Exception {
		MySocket.getVoid(this.guid + ".typeString('" + text + "')");
	}

}
