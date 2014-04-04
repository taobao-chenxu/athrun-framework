/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAAlert extends UIAElement {

	/**
	 * 
	 */
	public UIAAlert() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAAlert(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIAButton defaultButton() throws Exception {
		return UIAElementHelp.getElement(UIAButton.class, this.guid
				+ ".defaultButton()");
	}

	public UIAButton cancelButton() throws Exception {
		return UIAElementHelp.getElement(UIAButton.class, this.guid
				+ ".cancelButton()");
	}
}
