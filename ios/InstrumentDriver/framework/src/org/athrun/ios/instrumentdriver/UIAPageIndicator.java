package org.athrun.ios.instrumentdriver;

/**
 * 
 * @author yuanyao.cx
 *
 */
public class UIAPageIndicator extends UIAElement {

	public UIAPageIndicator() {
		super();
	}

	public UIAPageIndicator(String guid) {
		super(guid);
	}

	public void goToNextPage() throws Exception {
		MySocket.getVoid(this.guid + ".goToNextPage()");
	}

	public void goToPreviousPage() throws Exception {
		MySocket.getVoid(this.guid + ".goToPreviousPage()");
	}

	public int pageCount() throws Exception {
		return MySocket.getInt(this.guid + ".pageCount()");
	}

	public int pageIndex() throws Exception {
		return MySocket.getInt(this.guid + ".pageIndex()");
	}
	
	public void selectPage(int index) throws Exception {
		MySocket.getVoid(this.guid+".selectPage("+index+")");
	}
}
