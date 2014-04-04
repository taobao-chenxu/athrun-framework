package org.athrun.ios.instrumentdriver;

public class UIASegmentedControl extends UIAElement {

	public UIASegmentedControl() {
		// TODO Auto-generated constructor stub
	}

	public UIASegmentedControl(String guid) {
		super(guid);

	}

	public UIAButton selectedButton() throws Exception {

		return UIAElementHelp.getElement(UIAButton.class, this.guid
				+ ".selectedButton()");
	}

}
