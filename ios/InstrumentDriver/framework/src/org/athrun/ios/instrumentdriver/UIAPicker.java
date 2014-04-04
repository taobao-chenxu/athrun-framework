/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAPicker extends UIAElement {

	public UIAPicker() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAPicker(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIAPickerWheel[] wheels() throws Exception {
		return UIAElementHelp.elementArray(UIAPickerWheel.class, this.guid
				+ ".wheels()");
	}

}
