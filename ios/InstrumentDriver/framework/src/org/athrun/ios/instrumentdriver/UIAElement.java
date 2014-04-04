/**
 * 
 */
package org.athrun.ios.instrumentdriver;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author ziyu.hch
 * 
 */
public class UIAElement {

	public UIAElement() {
	}

	public UIAElement(String guid) {
		this.guid = guid;
	}

	// -------------------------Bean property----------------------

	protected String guid;
	protected Rect rect;
	protected String name;
	protected String label;
	protected String val;
	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String value) {
		this.val = value;
	}

	// -----------------------------------------------------

	/**
	 * 根据元素的显示文本查找元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @return 返回查找到满足条件的第一个元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public UIAElement findElementByText(String text) throws Exception {

		return this.findElementByText(text, UIAElement.class);

	}

	/**
	 * 根据元素的显示文本和元素类型查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @return 返回查找到满足条件的第一个元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public <T> T findElementByText(String text, Class<T> elmentType)
			throws Exception {

		return this.findElementByText(text, 0, elmentType);
	}

	/**
	 * 根据元素的显示文本和索引查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param index
	 *            满足 text、 elementType 条件的第几个元素，第一个为 0
	 * @return 返回查找到满足条件的元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	public UIAElement findElementByText(String text, int index)
			throws Exception {

		return this.findElementByText(text, index, UIAElement.class);
	}

	/**
	 * 根据元素的显示文本和元素类型查找指定元素
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @param index
	 *            满足 text、 elementType 条件的第几个元素，第一个为 0
	 * @return 返回查找到满足条件的元素实例
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回 UIAElementNil
	 *             字符串导致转换成JSON对象失败，抛出异常结束用例
	 */
	@SuppressWarnings("unchecked")
	public <T> T findElementByText(String text, int index, Class<T> elmentType)
			throws Exception {

		String eType = elmentType.getSimpleName();
		String elementJSON = MySocket.getText("findElement('" + this.guid
				+ "','" + text + "'," + index + ",'" + eType + "')");
		System.err.println();
		if (elementJSON.equals("UIAElementNil")) {
			String exception = elementJSON + ". 元素 " + this.guid
					+ " 下，根据条件: text ='" + text + "',index =" + index
					+ ",elmentType ='" + eType + "' 未能找到对应元素!";
			System.err.println(exception);
			throw new Exception(exception);
		}

		JSONObject element = JSONObject.fromObject(elementJSON);

		return (T) JSONObject.toBean(element, elmentType);
	}

	/**
	 * 根据元素的显示文本查找当前元素下所有满足条件的 element.
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @return UIAElement[] 返回查找到满足条件的元素数组
	 * @throws Exception
	 *             当根据指定的 text 未找到元素时，返回空JSON数组 "[]".
	 *             字符串导致转换成JSON数组对象失败，抛出异常结束用例
	 */
	public UIAElement[] findElementArrayByText(String text) throws Exception {
		return findElementArrayByText(text, UIAElement.class);
	}

	/**
	 * 根据元素的显示文本查找当前元素下所有满足条件的 element.
	 * 
	 * @param text
	 *            要查找的元素的name，value 或者 label值 包含的文本
	 * @param elmentType
	 *            要查找元素的类型 ，如：UIABuuton.class
	 * @return UIAElement[] 返回查找到满足条件的元素数组
	 * @throws Exception
	 *             当根据指定的 text 和 elmentType 未找到元素时，返回空JSON数组 "[]".
	 *             字符串导致转换成JSON数组对象失败，抛出异常结束用例
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] findElementArrayByText(String text, Class<T> elmentType)
			throws Exception {

		String eType = elmentType.getSimpleName();

		String elementsJSON = MySocket.getText("findElements('" + this.guid
				+ "','" + text + "','" + eType + "')");

		JSONArray jsonElementArray = JSONArray.fromObject(elementsJSON);

		return (T[]) JSONArray.toArray(jsonElementArray, elmentType);
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

	/**
	 * Get the element's parent element
	 * 
	 * @return parent element instance.
	 */
	public UIAElement parent() throws Exception {

		return UIAElementHelp.getElement(UIAElement.class, this.guid
				+ ".parent()");
	}

	/**
	 * 取得当前元素下的所有子元素
	 * 
	 * @return
	 */
	public UIAElement[] elements() throws Exception {

		return UIAElementHelp.elementArray(UIAElement.class, this.guid
				+ ".elements()");
	}

	public UIAElement[] ancestry() throws Exception {

		return UIAElementHelp.elementArray(UIAElement.class, this.guid
				+ ".ancestry()");
	}

	public UIAActivityIndicator[] activityIndicators() throws Exception {

		return UIAElementHelp.elementArray(UIAActivityIndicator.class,
				this.guid + ".activityIndicators()");
	}

	/**
	 * 取得当前元素下所有button类型的子元素
	 * 
	 * @return
	 */
	public UIAButton[] buttons() throws Exception {

		return UIAElementHelp.elementArray(UIAButton.class, this.guid
				+ ".buttons()");
	}

	// -images
	public UIAImage[] images() throws Exception {
		return UIAElementHelp.elementArray(UIAImage.class, this.guid
				+ ".images()");
	}

	public UIALink[] links() throws Exception {

		return UIAElementHelp.elementArray(UIALink.class, this.guid
				+ ".links()");
	}

	public UIANavigationBar navigationBar() throws Exception {

		return UIAElementHelp.getElement(UIANavigationBar.class, this.guid
				+ ".navigationBar()");
	}

	public UIANavigationBar[] navigationBars() throws Exception {

		return UIAElementHelp.elementArray(UIANavigationBar.class, this.guid
				+ ".navigationBars()");
	}

	// pageIndicators
	public UIAPageIndicator[] pageIndicators() throws Exception {
		return UIAElementHelp.elementArray(UIAPageIndicator.class, this.guid
				+ ".pageIndicators()");
	}

	public UIAPicker[] pickers() throws Exception {

		return UIAElementHelp.elementArray(UIAPicker.class, this.guid
				+ ".pickers()");
	}

	public UIAPopover popover() throws Exception {
		return UIAElementHelp.getElement(UIAPopover.class, this.guid
				+ ".popover()");
	}

	// progressIndicators

	public UIAScrollView[] scrollViews() throws Exception {

		return UIAElementHelp.elementArray(UIAScrollView.class, this.guid
				+ ".scrollViews()");
	}

	/**
	 * searchBars
	 * 
	 * @return UIASearchBar[]
	 */
	public UIASearchBar[] searchBars() throws Exception {

		return UIAElementHelp.elementArray(UIASearchBar.class, this.guid
				+ ".searchBars()");
	}

	public UIASecureTextField[] secureTextFields() throws Exception {

		return UIAElementHelp.elementArray(UIASecureTextField.class, this.guid
				+ ".secureTextFields()");
	}

	// segmentedControls
	// sliders

	public UIAStaticText[] staticTexts() throws Exception {

		return UIAElementHelp.elementArray(UIAStaticText.class, this.guid
				+ ".staticTexts()");
	}

	public UIASwitch[] switches() throws Exception {

		return UIAElementHelp.elementArray(UIASwitch.class, this.guid
				+ ".switches()");
	}

	public UIASlider[] sliders() throws Exception {
		return UIAElementHelp.elementArray(UIASlider.class, this.guid
				+ ".sliders()");
	}

	public UIATabBar tabBar() throws Exception {

		return UIAElementHelp.getElement(UIATabBar.class, this.guid
				+ ".tabBar()");
	}

	public UIATabBar[] tabBars() throws Exception {

		return UIAElementHelp.elementArray(UIATabBar.class, this.guid
				+ ".tabBars()");
	}

	public UIATableView[] tableViews() throws Exception {

		return UIAElementHelp.elementArray(UIATableView.class, this.guid
				+ ".tableViews()");
	}

	public UIATextField[] textFields() throws Exception {

		return UIAElementHelp.elementArray(UIATextField.class, this.guid
				+ ".textFields()");
	}

	public UIATextView[] textViews() throws Exception {

		return UIAElementHelp.elementArray(UIATextView.class, this.guid
				+ ".textViews()");
	}

	public UIAToolbar toolbar() throws Exception {

		return UIAElementHelp.getElement(UIAToolbar.class, this.guid
				+ ".toolbar()");
	}

	public UIAToolbar[] toolbars() throws Exception {

		return UIAElementHelp.elementArray(UIAToolbar.class, this.guid
				+ ".toolbars()");
	}

	public UIAWebView[] webViews() throws Exception {

		return UIAElementHelp.elementArray(UIAWebView.class, this.guid
				+ ".webViews()");
	}

	// -------------Gestures and Actions------------
	/**
	 * tap 操作
	 * 
	 * @throws Exception
	 */
	public void tap() throws Exception {

		MySocket.getVoid(this.guid + ".tap()");
	}

	public void tapWithOptions(String tapOffset) throws Exception {

		MySocket.getVoid(this.guid + ".tapWithOptions(" + tapOffset + ")");
	}

	public void doubleTap() throws Exception {
		MySocket.getVoid(this.guid + ".doubleTap()");
	}

	public void dragInsideWithOptions(String dragOptions) throws Exception {
		MySocket.getVoid(this.guid + ".dragInsideWithOptions(" + dragOptions
				+ ")");
	}

	public void touchAndHold() throws Exception {
		touchAndHold(1);
	}

	/**
	 * Touches the specified element and holds for the specified duration.
	 * 
	 * @param seconds
	 *            The length of time to hold the touch on the element, in
	 *            seconds.The default duration value for a tap is 0. The default
	 *            value for touch-and-hold gestures (such as drag, pinch open,
	 *            and pinch close) is 1.
	 * @throws Exception
	 */
	public void touchAndHold(int seconds) throws Exception {

		MySocket.getVoid(this.guid + ".touchAndHold(" + seconds + ")");
	}

	public void scrollToVisible() throws Exception {

		MySocket.getVoid(this.guid + ".scrollToVisible()");
	}

	/*
	 * -----Determining Element State-----
	 * 
	 * Use these methods to determine whether an element is still valid.
	 * 
	 * isValid checkIsValid waitForInvalid hasKeyboardFocus isEnabled isVisible
	 */

	public Boolean isValid() throws Exception {

		return MySocket.getBoolen(this.guid + ".isValid()");
	}

	public Boolean checkIsValid() throws Exception {

		return MySocket.getBoolen(this.guid + ".checkIsValid()");
	}

	public Boolean waitForInvalid() throws Exception {

		return MySocket.getBoolen(this.guid + ".waitForInvalid()");
	}

	public Boolean hasKeyboardFocus() throws Exception {

		return MySocket.getBoolen(this.guid + ".hasKeyboardFocus()");
	}

	public Boolean isEnabled() throws Exception {

		return MySocket.getBoolen(this.guid + ".isEnabled()");
	}

	public Boolean isVisible() throws Exception {

		return MySocket.getBoolen(this.guid + ".isVisible()");
	}

	// ------Identifying Elements------

	public String label() throws Exception {

		return RunType.DEBUG ? this.label : MySocket.getText(this.guid
				+ ".label()");
	}

	public String name() throws Exception {

		return RunType.DEBUG ? this.name : MySocket.getText(this.guid
				+ ".name()");
	}

	public String value() throws Exception {

		return RunType.DEBUG ? this.val : MySocket.getText(this.guid
				+ ".value()");
	}

	/**
	 * Returns a UIAElement if its name attribute matches a specified string.
	 * 
	 * @param name
	 * @return a UIAElement
	 */
	public UIAElement withName(String name) throws Exception {

		return UIAElementHelp.getElement(UIAElement.class, this.guid
				+ ".withName('" + name + "')");
	}

	// -----Logging Element Information-----

	public void logElement() throws Exception {
		MySocket.getVoid(this.guid + ".logElement()");
	}

	public void logElementTree() throws Exception {
		MySocket.getVoid(this.guid + ".logElementTree()");
	}
}