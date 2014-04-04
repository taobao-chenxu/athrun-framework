package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 * 
 */
public class Rect {

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Origin origin;
	public Size size;
}