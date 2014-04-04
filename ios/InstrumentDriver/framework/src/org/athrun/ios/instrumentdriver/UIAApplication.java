/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIAApplication {

	private String guid = null;

	public UIAApplication(String guid) {
		// TODO Auto-generated constructor stub
		this.guid = guid;
	}

	public UIAWindow[] windows() throws Exception {

		return UIAElementHelp.elementArray(UIAWindow.class, this.guid
				+ ".windows()");
	}

	/**
	 * mainWindow
	 */
	public UIAWindow mainWindow() throws Exception {

		return new UIAWindow("win");
		// return UIAElementHelp.getWindow(this.guid + ".mainWindow()");
	}

	public UIAActionSheet actionSheet() throws Exception {

		return UIAElementHelp.getElement(UIAActionSheet.class, this.guid
				+ ".actionSheet()");
	}

	public UIAAlert alert() throws Exception {
		return UIAElementHelp
				.getElement(UIAAlert.class, this.guid + ".alert()");
	}

	public UIAKeyboard keyboard() throws Exception {

		return UIAElementHelp.getElement(UIAKeyboard.class, this.guid
				+ ".keyboard()");
	}

	public UIANavigationBar navigationBar() throws Exception {

		return UIAElementHelp.getElement(UIANavigationBar.class, this.guid
				+ ".navigationBar()");
	}

	public UIAStatusBar statusBar() throws Exception {

		return UIAElementHelp.getElement(UIAStatusBar.class, this.guid
				+ ".statusBar()");
	}

	/**
	 * tabBar
	 */
	public UIATabBar tabBar() throws Exception {

		return UIAElementHelp.getElement(UIATabBar.class, this.guid
				+ ".tabBar()");
	}

	public UIAToolbar toolbar() throws Exception {

		return UIAElementHelp.getElement(UIAToolbar.class, this.guid
				+ ".toolbar()");
	}

	/**
	 * 树形结构打印出当前元素节点下的所有子元素
	 * 
	 * @throws Exception
	 *             C/S Socket 通讯异常
	 * 
	 */
	public void printElementTree() throws Exception {

		String elementTree = MySocket.getText("printElementTree('" + this.guid
				+ "')");

		System.out
				.println("-----------------------------------------------------");
		System.out.println("LogElementTree:");
		System.out.println(elementTree.replaceAll("###", "\n"));
		System.out
				.println("-----------------------------------------------------");
	}
}
