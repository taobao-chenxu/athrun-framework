/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAActionSheet extends UIAElement {

	public UIAActionSheet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAActionSheet(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIAButton cancelButton() throws Exception {

		return UIAElementHelp.getElement(UIAButton.class, this.guid
				+ ".cancelButton()");
	}

}
