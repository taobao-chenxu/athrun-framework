/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIALink extends UIAElement {

	public UIALink() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIALink(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public String url() throws Exception {
		return MySocket.getText(this.guid + ".url()");
	}
}
