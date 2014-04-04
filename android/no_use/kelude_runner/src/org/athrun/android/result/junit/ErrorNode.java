/**
 * 
 */
package org.athrun.android.result.junit;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author taichan
 * 
 */
@XStreamAlias("error")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = { "content" })
public class ErrorNode {
//	@XStreamAsAttribute
//	private String message;
//
//	@XStreamAsAttribute
//	private String type;
	private String content;

	/**
	 * @param message
	 * @param type
	 * @param content
	 */
	public ErrorNode(String message, String type, String content) {
		super();
//		this.message = message;
//		this.type = type;
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

//	/**
//	 * @return the message
//	 */
//	public String getMessage() {
//		return message;
//	}
//
//	/**
//	 * @return the type
//	 */
//	public String getType() {
//		return type;
//	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
