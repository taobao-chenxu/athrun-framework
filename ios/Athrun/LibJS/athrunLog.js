/**
 *统一处理错误日志
 *
 */

function Error(errorType, message) {

	this.type = errorType;
	this.message = message;
}

Error.prototype.log = function() {

	//这里只处理错误的日志，出错时根据错误类型和想要写入的日志信息实例化一个Error对象然后抛出
	if(this.type == "Warning") {

		UIALogger.logWarning(this.message);

	} else if(this.type == "Error") {

		UIALogger.logError(this.message);

	} else if(this.type == "Message") {

		UIALogger.logMessage(this.message)

	} else if(this.type == "Issue") {

		UIALogger.logIssue(this.message);

	} else {
		UIALogger.logFail(this.message);
	}
}
/**
 * 错误截图，传入截图的图片名称和可选的message写入日志
 */
Error.captureScreen = function(name, message) {

	var picName = getDateStr(new Date()) + "_" + name;
	var msg = "Capture Screen and the picture name is " + name + " " + (!message ? "" : message);

	UIALogger.logMessage(msg);
	UIATarget.localTarget().captureScreenWithName(picName);
}
/**
 * 时间格式函数，返回结果形如： 2012-02-22 21:45:27
 */
function getDateStr(date) {
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	if(month < 10) {
		month = "0" + month;
	}
	var day = date.getDate();
	if(day < 10) {
		day = "0" + day;
	}
	var h = date.getHours();
	if(h < 10) {
		h = "0" + h;
	}
	var m = date.getMinutes();
	if(m < 10) {
		m = "0" + m;
	}
	var s = date.getSeconds();
	if(s < 10) {
		s = "0" + s;
	}
	return year + "-" + month + "-" + day + " " + h + ":" + m + ":" + s;
}