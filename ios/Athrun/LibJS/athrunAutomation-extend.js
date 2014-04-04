/**
 * AutoMation Extends
 * 
 */

function delay(time) {
	UIATarget.localTarget().delay(time);
}

/**
 * print message
 * 
 */
function print(msg) {
	UIALogger.logMessage(msg);
}

// -----------------------------UIAElement Extends-----------------------------
extend(UIAElement.prototype, {
	/**
	 * 通过delay等待元素可见有效，通过参数的不同控制delay的时间 参数一:tryTime,delay的次数，
	 * 参数二:oneDelayTime,每一次delay的时间(seconds)
	 */
	waitForValid : function(tryTimes, oneDelayTime) {

		var tries = 0;
		while (!this.isVisible() && !this.isValid()) {

			delay(oneDelayTime);
			if (tries < tryTimes) {
				tries++;
			} else {
				var errorMsg = "The element type of " + typeof (this)
						+ " is invisible or invalid.";
				throw (new Error("Fail", errorMsg));
			}
		}
		// 每次默认延时一秒，方便看清楚脚本的运行过程
		delay(1);
	},
	/**
	 * 优化元素的tap动作，通过delay来提高操作的稳定性，避免由于元素不可见或不可用而造成的tap失败引起用例错误。 tap失败时写入具体原因
	 */
	doTap : function() {

		this.waitForValid(2, 3);
		this.tap();

		var message = "The element type of " + this.name()
				+ " doTap successed.";
		(new Error("Message", message)).log();
	},

	/**
	 * A shortcut for waiting an element to become visible and tap.
	 */
	vtap : function() {
		this.waitForValid(2, 3);
		this.tap();
	},
	/**
	 * 处理元素的doubletap
	 */
	doDoubleTap : function() {

		this.waitForValid(2, 3);
		this.doubleTap();

		var message = "The element type of " + this.name()
				+ " doDoubleTap successed.";
		(new Error("Message", message)).log();
		delay(1);
	},

	/**
	 * a print method
	 */
	print : function() {
		this.logElementTree();
	},
	/**
	 * 验证控件对象是否包含期盼的文本，主要通过元素的name和label进行匹配判断 expectContain为期盼包含的字符
	 * 未作异常处理，参数不满足预期类型系统抛出异常被appTest捕获。
	 * 注：这里可以扩大匹配范围，遍历包含的element，逐个进行校验返回结果。暂时不支持 return 0 not found return 1 if
	 * found
	 */
	containText : function(expectContain) {

		var name = this.name();
		var labelText = this.label();
		var value = this.value();
		var isTrue1 = name == null ? false
				: (name.search(expectContain) != -1 ? true : false);
		var isTrue2 = labelText == null ? false : (labelText
				.search(expectContain) != -1 ? true : false);
		var isTrue3 = value == null ? false
				: (value.search(expectContain) != -1 ? true : false);
		if (isTrue1 || isTrue2 || isTrue3) {
			return 1;
		} else {
			return 0;
		}
	},
	/**
	 * Move the width & height function out of rect()
	 */
	width : function() {
		return this.rect().size.width;
	},
	height : function() {
		return this.rect().size.height;
	}
})
// ---------------------------UIAElement extends end---------------------------

// ------------------------UIAApplication extends start------------------------
extend(UIAApplication.prototype, {
	/**
	 * A shortcut for getting the current view controller's title from the
	 * navigation bar. If there is no navigation bar, this method returns null
	 */
	/*
	 * navigationTitle: function() { navBar = this.mainWindow().navigationBar();
	 * if (navBar) { return navBar.name(); } return null; },
	 */
	navigationTitle : function(index) {
		if (index == null) {
			index = 0;
		}
		var title = this.mainWindow().navigationBar().staticTexts()[index]
				.value();
		if (title) {
			return title;
		}
		return null;
	},

	/**
	 * A shortcut for checking that the interface orientation in either portrait
	 * mode
	 */
	isPortraitOrientation : function() {
		var orientation = this.interfaceOrientation();
		return orientation == UIA_DEVICE_ORIENTATION_PORTRAIT
				|| orientation == UIA_DEVICE_ORIENTATION_PORTRAIT_UPSIDEDOWN;
	},

	/**
	 * A shortcut for checking that the interface orientation in one of the
	 * landscape orientations.
	 */
	isLandscapeOrientation : function() {
		var orientation = this.interfaceOrientation();
		return orientation == UIA_DEVICE_ORIENTATION_LANDSCAPELEFT
				|| orientation == UIA_DEVICE_ORIENTATION_LANDSCAPERIGHT;
	}
});
// -------------------------UIAApplication extends end--------------------------

// ------------------------UIANavigationBar extends start-----------------------
extend(UIANavigationBar.prototype, {
	/**
	 * Asserts that the left button's name matches the given +name+ argument
	 */
	assertLeftButtonNamed : function(name) {
		Athrun.assertEquals(name, this.leftButton().name());
	},

	/**
	 * Asserts that the right button's name matches the given +name+ argument
	 */
	assertRightButtonNamed : function(name) {
		Athrun.assertEquals(name, this.rightButton().name());
	}
});
// -----------------------UIANavigationBar extends end-----------------------

// ------------------------UIATableView extends start------------------------
extend(UIATableView.prototype, {
	/**
	 * A shortcut for: this.cells().firstWithName(name)
	 */
	cellNamed : function(name) {
		return this.cells().firstWithName(name);
	},
	/**
	 * return a cell which contains name given
	 */
	cellContainText : function(name) {
		for ( var int = 0; int < this.cells().length; int++) {
			this.cells()[int].print();
			if (this.cells()[int].containText(name)) {
				return this.cells()[int];
			}
		}
		return null;
	},

	/**
	 * Asserts that this table has a cell with the name (accessibility label)
	 * matching the given +name+ argument.
	 */
	assertCellNamed : function(name) {
		Athrun.assertNotNull(this.cellNamed(name),
				"No table cell found named '" + name + "'");
	}
});
// --------------------------UIATableView extends end--------------------------

// ---------------------------UIASlider extends start--------------------------
extend(UIASlider.prototype, {
	isMoving : function(timedelay) {
		var value1 = this.value();
		if (timedelay == null) {
			timedelay = 1;
		}
		delay(timedelay);

		var value2 = this.value();
		if (value1 == value2) {

			return 0;
		}

		return 1;
	}
});

// ---------------------------UIASlider extends end---------------------------

UIATarget.prototype.doTap = function(xy) {

	delay(2);
	this.tap(xy);

	var message = "The element type of " + this.name() + " doTap successed.";
	(new Error("Message", message)).log();
	// this.touchAndHold();
	delay(1);
}

/**
 * UIAKeyboard.prototype，扩展软键盘的字符输入方法，typeString相应的值然后写入日志
 */
UIAKeyboard.prototype.typeText = function(typeString) {

	try {
		var message = "键盘输入文本 # " + typeString + " #.";

		UIATarget.localTarget().frontMostApp().keyboard()
				.typeString(typeString);
		delay(1);
		(new Error("Message", message)).log();
	} catch (e) {
		var message = "当前应用为非输入状态！(键盘尚未调出为可用）" + e;
		throw (new Error("Fail", message))
	}
}
/**
 * 简便的键盘输入操作
 */
Athrun.typeString = UIAKeyboard.prototype.typeText;

/**
 * 设置wheel 的值，假如为datepicker 这里的value类型为数字，否则为类型为字符串
 */
UIAPickerWheel.prototype.doSelectValue = function(value) {

	var message = "设置pickerWheel 的值为: # " + value + " #";

	this.selectValue(value);
	delay(1);
	(new Error("Message", message)).log();
}

/**
 * 获取Picker 显示的value值 遍历Picker 的 wheels ，分别取出其value，拼接后返回
 */
UIAPicker.prototype.getValue = function() {

	var wheelsArray = this.wheels();
	var value = "";
	for ( var i = 0; i < wheelsArray.length; i++) {
		value += wheelsArray[i].value().split('.')[0];
	}
	return value;
}
/**
 * 验证Switch控件的当前开关状态。state =1 代表 on 状态，state =0 代表 off状态 state 为要验证的状态
 */
UIASwitch.prototype.assertState = function(state) {

	var actualState = "error";
	try {
		var value = this.value();
		value ? actualState = "on" : actualState = "off";
		if (value == state) {
			var msg = "Switch控件的实际状态为 # " + actualState + " # 与期望的状态  # "
					+ state + " # 相符";
			UIALogger.logMessage(msg);
		} else {
			var errorMsg = "Switch控件的实际状态为 # " + actualState + " # 与期望的状态  # "
					+ state + " # 不符";
			UIALogger.logWarning(errorMsg);
		}
	} catch (e) {
		throw e + " #Switch控件状态验证异常";
	}
}

/**
 * -----------------------------scroll extends-----------------------------
 * 上下左右滑动，每次调用滑动半个屏幕高度，左右滑动则有可能是滑到下一个页面 在需要滑动的位置调用即可，使用方式如下：Athrun.scrollUp();
 */

extend(Athrun, {
	/**
	 * 向上滑动,滑动一个范围，200单位 即大概半个屏幕的高度
	 */
	scrollUp : function() {
		try {
			UIATarget.localTarget().dragFromToForDuration({
				x : 150,
				y : 300
			}, {
				x : 150,
				y : 100
			}, 1);
			var msg = "向上滑动窗口半屏高度";
			UIALogger.logMessage(msg);
		} catch (e) {
			throw e + " #scrollUp 滑动窗口异常 ";
		}
		delay(1);

	},

	/**
	 * 向下滑动,滑动一个范围，200单位 即大概半个屏幕的高度
	 */
	scrollDown : function() {
		try {
			UIATarget.localTarget().dragFromToForDuration({
				x : 150,
				y : 100
			}, {
				x : 150,
				y : 300
			}, 1);
			var msg = "向下滑动窗口半屏高度";
			UIALogger.logMessage(msg);
		} catch (e) {
			throw e + " #scrollDown 滑动窗口异常 ";
		}
		delay(1);
	},
	/**
	 * 360*480 向左边滑动,滑动一个范围，200
	 */
	scrollLeft : function() {
		try {
			UIATarget.localTarget().dragFromToForDuration({
				x : 260,
				y : 200
			}, {
				x : 60,
				y : 200
			}, 1);
			var msg = "向左边滑动窗口200单位.";
			UIALogger.logMessage(msg);
		} catch (e) {
			throw e + " #scrollLeft 滑动窗口异常 ";
		}
		delay(1);
	},

	/**
	 * 向右边滑动,滑动一个范围，200
	 */
	scrollRight : function() {
		try {
			UIATarget.localTarget().dragFromToForDuration({
				x : 60,
				y : 200
			}, {
				x : 260,
				y : 200
			}, 1);
			var msg = "向右滑动窗口200单位.";
			UIALogger.logMessage(msg);
		} catch (e) {
			throw e + " #scrollRight 滑动窗口异常 ";
		}
		delay(1);
	}
})
// ---------------------------scroll extends end---------------------------

/**
 * Extend +destination+ with +source+
 */
function extend(destination, source) {
	for ( var property in source) {
		destination[property] = source[property];
	}
	return destination;
}