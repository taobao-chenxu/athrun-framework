/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIATextView extends UIAElement {

	/**
	 * @param guid
	 */
	public UIATextView(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIATextView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置输入框的文本值。
	 * 
	 * @param value
	 *            要设置的文本
	 */
	public void setValue(String value) throws Exception {
		MySocket.getVoid(this.guid + ".setValue('" + value + "')");
	}

}
