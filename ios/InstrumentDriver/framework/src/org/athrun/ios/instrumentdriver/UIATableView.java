/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIATableView extends UIAElement {

	/**
	 * @param guid
	 */
	public UIATableView(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIATableView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UIATableCell[] cells() throws Exception {

		return UIAElementHelp.elementArray(UIATableCell.class, this.guid
				+ ".cells()");
	}

	public UIATableGroup[] groups() throws Exception {

		return UIAElementHelp.elementArray(UIATableGroup.class, this.guid
				+ ".groups()");
	}

}
