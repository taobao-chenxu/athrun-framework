package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class UIATarget {

	private String guid = null;

	public UIATarget() {

		this.guid = "target";
	}

	/**
	 * localTarget
	 */
	public static UIATarget localTarget() {

		return new UIATarget();
	}

	/**
	 * 
	 */
	public UIAApplication frontMostApp() {

		return new UIAApplication("app");
	}

	public void delay(int second) throws Exception {
		MySocket.getVoid(this.guid + ".delay(" + second + ")");
	}

	public void tap(String xy) throws Exception {
		MySocket.getVoid(this.guid + ".tap(" + xy + ")");
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
	 * Flicks from the specified starting screen location to the specified
	 * ending screen location.
	 * 
	 * <p>
	 * 模拟手势，从一个坐标点滑动到另一个点坐标点(原点坐标为屏幕左上角)
	 * 
	 * @param fromPointObject
	 * @param toPointObject
	 * @throws Exception
	 */
	public void flickFromTo(Point fromPointObject, Point toPointObject)
			throws Exception {
		MySocket.getVoid(this.guid + ".flickFromTo(" + fromPointObject + ","
				+ toPointObject + ")");
	}

	/**
	 * 向上滑动窗口,滑动一个范围，200单位 即向上滑动大约半个屏幕的高度
	 * 
	 * @throws Exception
	 */
	public void scrollUp() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 150,y : 300}, {x : 150,y : 100}, 1)");
	}

	/**
	 * 向下滑动窗口,滑动一个范围，200单位 即向下滑动大约半个屏幕的高度
	 * 
	 * @throws Exception
	 */
	public void scrollDown() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 150,y : 100}, {x : 150,y : 300}, 1)");
	}

	/**
	 * 向左边滑动窗口,滑动一个范围，200单位 （360*480）一般是换页
	 * 
	 * @throws Exception
	 */
	public void scrollLeft() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 260,y : 200}, {x : 60,y : 200}, 1)");
	}

	/**
	 * 向右边滑动窗口,滑动一个范围，200 一般是换页
	 * 
	 * @throws Exception
	 */
	public void scrollRight() throws Exception {

		MySocket.getVoid(this.guid
				+ ".dragFromToForDuration({x : 60,y : 200}, {x : 260,y : 200}, 1)");
	}

	/**
	 * 在屏幕上从起始位置移动到结束位置
	 * 
	 * @param start
	 *            : 滑动操作的起始位置
	 * @param end
	 *            : 滑动操作的结束位置
	 * @param duration
	 *            : 滑动操作的经历的时间
	 * @throws Exception
	 */
	public void dragFromToForDuration(String start, String end, double duration)
			throws Exception {
		MySocket.getVoid(this.guid + ".dragFromToForDuration(" + start + ","
				+ end + "," + duration + ")");
	}

	public void captureScreenWithName(String imageName) throws Exception {
		MySocket.getVoid(this.guid + ".captureScreenWithName('" + imageName
				+ "')");
	}

}
