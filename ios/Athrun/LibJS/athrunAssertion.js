/**
 * 校验机制
 */

var Athrun = {
	Version : "version 1.1",
	Desc : "Athrun iOS",
	/**
	 * 这里校验表达式的真假，express 为待校验的表达式，message为校验附加的日志信息
	 */
	assertTrue : function(express, message) {

		if (typeof message == "undefined") {
			message = "";
		}
		if (express) {
			UIALogger.logMessage(message + " #校验成功");
		} else {
			UIALogger.logWarning(message + " #校验失败");
		}
	},
	/**
	 * 这里校验表达式的真假，express 为待校验的表达式，message为校验附加的日志信息
	 */
	assertFalse : function(express, message) {
		this.assertTrue(!express, message)
	},

	/**
	 * Asserts that the given object is null or UIAElementNil (UIAutomation's
	 * version of a null stand-in). If the given object is not one of these, an
	 * exception is thrown with a default message or the given optional
	 * +message+ parameter.
	 */
	assertNull : function(thingie, message) {
		var defMessage = "期盼值 == null , 实际值 # " + thingie + " #";
		this.assertTrue(thingie === null
				|| thingie.toString() == "[object UIAElementNil]",
				message ? message + ": " + defMessage : defMessage);
	},

	/**
	 * Asserts that the given object is not null or UIAElementNil
	 * (UIAutomation's version of a null stand-in). If it is null, an exception
	 * is thrown with a default message or the given optional +message+
	 * parameter
	 */
	assertNotNull : function(thingie, message) {
		var defMessage = "期盼值 != null";
		this.assertTrue(thingie !== null
				&& thingie.toString() != "[object UIAElementNil]",
				message ? message + ": " + defMessage : defMessage);
	},

	/**
	 * 这里校验文本，控件的 显示文本或者value值是否与期望的相匹配。 不匹配则抛出异常，用例运行失败，匹配则写入日志信息并继续运行下一步骤
	 */
	assertEquals : function(actual, expect, message) {
		var msg = "实际值 # " + actual + " # 与期盼值 # " + expect + " # ";
		this.assertTrue(expect == actual, msg + (!message ? "" : message));
	},
	/**
	 * 这里校验文本，控件的 显示文本或者value值是否与期望的不相匹配。 匹配则抛出异常，用例运行失败，匹配则写入日志信息并继续运行下一步骤
	 */
	assertNotEquals : function(actual, expect, message) {
		var msg = "实际值 # " + actual + " # 与期盼值 # " + expect + " # ";
		this.assertTrue(expect != actual, message ? message + ": " + msg
				: msg);
	},

	/**
	 * 验证传入的控件对象是否包含期盼的文本，主要通过元素的name和label进行匹配判断
	 * obj为传入的UIAElement，expectContain为期盼包含的字符，message为验证通过时添加的日志信息
	 * 未作异常处理，参数不满足预期类型系统抛出异常被appTest捕获。
	 * 注：这里可以扩大匹配范围，遍历传入obj中包含的element，逐个进行校验返回结果。暂时不支持
	 */
	assertContainText : function(obj, expectContain, message) {

		var name = obj.name();
		var labelText = obj.label();
		var value = obj.value();
		var isTrue1 = name == null ? false
				: (name.search(expectContain) != -1 ? true : false);
		var isTrue2 = labelText == null ? false : (labelText
				.search(expectContain) != -1 ? true : false);
		var isTrue3 = value == null ? false
				: (value.search(expectContain) != -1 ? true : false);
		if (isTrue1 || isTrue2 || isTrue3) {
			var msg = "对象的文本属性中包含期盼值的字符 # " + expectContain + " #";
			msg += (!message ? "" : message + " #校验成功");
			UIALogger.logMessage(msg);
		} else {
			var errorMsg = "对象的文本属性中不包含期盼值的字符 # " + expectContain + " #";
			errorMsg += (!message ? "" : message + " #校验失败");
			UIALogger.logWarning(errorMsg);
		}
	},

	assertNotContainText : function(obj, expectContain, message) {
		var name = obj.name();
		var labelText = obj.label();
		var value = obj.value();
		var isTrue1 = name ==null? false:(name.search(expectContain) != -1 ? true : false);
		var isTrue2 = labelText==null? false:(labelText.search(expectContain) != -1 ? true : false);
		var isTrue3 = value ==null? false:(value.search(expectContain) != -1 ? true : false);
		if(!isTrue1 && !isTrue2 && !isTrue3) {
			var msg = "对象的name|label|value值#"+name+"#中不包含期盼值的字符 # " + expectContain + " #";	
			msg += (!message ? "" : message + " #校验成功");
			UIALogger.logMessage(msg);
			return 1;
		} else {
			var errorMsg = "对象的name｜label｜value值中包含期盼值的字符 # " + expectContain + " #";
			errorMsg += (!message ? "" : message + " #校验失败");
			UIALogger.logWarning(errorMsg);
			
			return 0;
		}
	},

	/**
	 * 验证元素的有效性，obj:为待验证元素， message:验证的日志信息.
	 */
	assertIsValid : function(obj, message) {

		if (obj.checkIsValid()) {
			var passMsg = "被检测元素 # " + typeof (obj) + " # 有效 "
					+ (!message ? "" : message);
			UIALogger.logMessage(passMsg);
		} else {
			var errorMsg = "被检测元素 # " + obj + " # 无效 "	+ (!message ? "" : message);
			UIALogger.logWarning(errorMsg);
		}
	},
	
	/**
	 * 验证元素的有效性，obj:为待验证元素， message:验证的日志信息.
	 */
	assertIsNotValid : function(obj, message) {
	
		if(!obj.checkIsValid()) {
			var errorMsg = "被检测元素 # " + obj + " # 是无效的. " + (!message ? "" : message);
			UIALogger.logMessage(passMsg);
			return 1;
		} else {
			var passMsg = "被检测元素 # " + typeof (obj) + " # 有效. " + (!message ? "" : message);
			UIALogger.logWarning(errorMsg);
			return 0;
		}
	},
	
	/**
	 * 正则表达式验证 第一个参数 regexp 为自己设定的正则表达式，第二个参数 expression
	 * 为要验证的文本(比如控件的name或者value值) 判断要验证的文本是否符合给定的正则表达式。 message为要写入的日志信息
	 */
	assertRegExptest : function(regExp, expression, message) {

		if (regExp.test(expression)) {
			var passMsg = expression + " 正则表达式:" + regExp + "匹配 "
					+ (!message ? "" : message + " #校验成功");
			UIALogger.logMessage(passMsg);
		} else {
			var errorMsg = expression + " 正则表达式:" + regExp + "不匹配 "
					+ (!message ? "" : message + " #校验失败");

			UIALogger.logWarning(errorMsg);
		}
	},

	/**
	 * 正则表达式验证 第一个参数 regexp 为自己设定的正则表达式，第二个参数 expression
	 * 为要验证的文本(比如控件的name或者value值) 判断要验证的文本是否符合给定的正则表达式。 message为要写入的日志信息
	 * 可以验证给定的字符是否能匹配到表达式
	 */
	assertRegExpMatch : function(regExp, expression, message) {

		if (expression.match(regExp) != null) {
			var passMsg = expression + " 满足表达式:" + regExp + "匹配 "
					+ (!message ? "" : message + "#校验成功");
			UIALogger.logMessage(passMsg);
		} else {
			var errorMsg = expression + " 不满足表达式" + regExp + "匹配 "
					+ (!message ? "" : message + " #验证失败");
			UIALogger.logWarning(errorMsg);
		}
	},

	/**
	 * 验证弹出的弹出框是否为期望的，通过popWindow的title进行判断
	 */
	assertPopWindowTitle : function(expectTitle, message) {

		delay(5);
		var actualTitle = popWindow.name();
		if (expectTitle == actualTitle) {
			var passMsg = "弹出窗口的实际title为 # " + actualTitle + " # 与期望的title # "
					+ expectTitle + " # 相符,"
					+ (!message ? "" : message + " #校验成功");
			UIALogger.logMessage(passMsg);
		} else {
			var msg = "弹出窗口的实际title为 # " + actualTitle + " # 与期望的title # "
					+ expectTitle + " # 不相符,"
					+ (!message ? "" : message + " #校验失败");
			UIALogger.logWarning(msg);
		}
	},
	
	/**
	 * 验证弹出的弹出框是否为期望的，通过popWindow内的文本进行判断，看是否包含期盼的文本值
	 */
	assertPopWindowContent : function(expectText, message) {

		popWinContent ="";
		delay(5);
		target.pushTimeout(0);
		__popWinContent(popWindow);
		target.popTimeout();
		if (popWinContent.search(expectText) != -1 ? true : false) {
			var passMsg = "弹出窗口的实际内容文本为 # " + popWinContent + " # 包含期望的文本 # "
					+ expectText + " # "
					+ (!message ? "" : message + " #校验成功");
			UIALogger.logMessage(passMsg);
		} else {
			var msg = "弹出窗口的实际内容文本为 # " + popWinContent + " # 不包含期望的文本 # "
					+ expectText + " # "
					+ (!message ? "" : message + " #校验失败");
			UIALogger.logWarning(msg);
		}
	},

	/**
	 * 验证Switch控件的当前开关状态。state =1 代表 on 状态，state =0 代表 off 状态 obj
	 * 为要验证的Switch控件，state 为要验证的状态
	 */
	assertSwitchState : function(obj, state) {
		var actualState = "error";
		try {
			var value = obj.value();
			value ? actualState = "on" : actualState = "off";
			if (value == state) {
				var msg = "Switch控件的实际状态为 # " + actualState + " # 与期望的状态  # "
						+ state + " # 相符 #校验成功";
				UIALogger.logMessage(msg);
			} else {
				var errorMsg = "Switch控件的实际状态为 # " + actualState
						+ " # 与期望的状态  # " + state + " # 不符 #校验失败";
				// throw (new Error("Fail", errorMsg));
				UIALogger.logWarning(errorMsg);
			}
		} catch (e) {
			throw e + " #Switch控件状态验证异常";
		}
	}
}

/**
 * 全局变量 代表弹窗的对话框
 */
popWindow = null;
popWinContent="";

/**
 * UIATarget 默认会捕获弹出的窗口，并点击相应的默认操作按钮，必须进行处理才能操作其它按钮，
 * 该事件返回true的时候，不触发默认的自动处理事件，在这里我们也加入了相应的日志，记录对话框的出现 注意事项：
 * 弹出对话框后，UIATarget的onAlert事件里面的操作和点击弹出对话框操作之后的步骤是并行的。
 * 但是假如遇到delay，则并行的同时等待并以onAlert事件里的为准，
 * 时间超出部分步骤继续等待，否则和onAlert中的delay一致。（只对弹出对话框后第一个delay起作用）。
 * onAlert事件完成之前，动作被阻塞，无法操作弹出窗口。完成onAlert事件后继续往下运行。
 */
UIATarget.onAlert = function onAlert(alert) {
	popWindow = alert;
	UIATarget.localTarget().delay(1);
	var msg = "There is a  popWindow  appeared and the title is "
			+ alert.name();
	UIALogger.logMessage(msg);

	return true;
}
function __popWinContent(element){
		
	popWinContent += element.name();
	var elements = element.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
		__popWinContent(elements[i]);
	}
}
// end
