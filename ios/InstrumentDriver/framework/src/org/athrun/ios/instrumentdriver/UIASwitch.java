/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author taichan
 * @author ziyu.hch
 * 
 */
public class UIASwitch extends UIAElement {

	public UIASwitch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param guid
	 */
	public UIASwitch(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置 switch类型元素的开、关状态 true: on , false :off
	 * 
	 * @param value A boolean value to represent the desired state, true for On, false for Off.
	 */
	public void setValue(Boolean value) throws Exception {
		int i = 0;
		if(value){
			i = 1;
		}
		MySocket.getVoid(this.guid + ".setValue(" + i + ")");
	}

}
