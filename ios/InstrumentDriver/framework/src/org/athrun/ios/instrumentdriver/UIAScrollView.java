/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAScrollView extends UIAElement {

	public UIAScrollView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIAScrollView(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public void scrollUp() throws Exception {
		MySocket.getVoid(this.guid + ".scrollUp()");
	}

	public void scrollDown() throws Exception {
		MySocket.getVoid(this.guid + ".scrollDown()");
	}

	public void scrollLeft() throws Exception {
		MySocket.getVoid(this.guid + ".scrollLeft()");
	}

	public void scrollRight() throws Exception {
		MySocket.getVoid(this.guid + ".scrollRight()");
	}

	public void scrollToElementWithName(String name) throws Exception {

		MySocket.getVoid(this.guid + ".scrollToElementWithName('" + name + "')");
	}
}
