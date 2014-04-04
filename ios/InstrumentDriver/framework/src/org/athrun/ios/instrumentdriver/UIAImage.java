package org.athrun.ios.instrumentdriver;

public class UIAImage extends UIAElement {

	public UIAImage() {
		super();
	}

	public UIAImage(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}
	
	public String url() throws Exception {
		return MySocket.getText(this.guid + ".url()");
	}

}
