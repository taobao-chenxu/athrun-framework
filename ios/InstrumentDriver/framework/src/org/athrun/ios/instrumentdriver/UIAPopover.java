package org.athrun.ios.instrumentdriver;

/**
 * 
 * @author yuanyao
 * 
 */
public class UIAPopover extends UIAElement {
	public UIAPopover() {
		super();
	}

	public UIAPopover(String guid) {
		super(guid);
	}

	public UIAActionSheet actionSheet() throws Exception {
		return UIAElementHelp.getElement(UIAActionSheet.class, this.guid
				+ ".actionSheet()");
	}

	public void dismiss() throws Exception {
		MySocket.getVoid(this.guid + ".dismiss()");
	}
}
