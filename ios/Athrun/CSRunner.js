/**
 * 用例名称: C/S模式运行，启动脚本
 * 作   者: ziyu
 * 日   期: 2012-05-08
 * 备   注: Athrun C/S模式运行，启动脚本
 *
 */ 

#import "./LibJS/athrunImports.js"



__element = "UIAElementNil";
__elementArray =[];
__elementTree ="";
__index = 0;
target = UIATarget.localTarget();
app = target.frontMostApp();
win = app.mainWindow();
host = target.host();	

UIALogger.logStart("The case is running.");

try {
	
	var step = 1;
	var isEnd = false;
	sendToServer ="Get the next step.";
	while(!isEnd){
		
		var result = host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
		var stdout = result.stdout.split("##");
		var type = stdout[0];
		var script =stdout[1];
		
		UIALogger.logMessage("type :" + type);
		
		switch(type)
		{
			case "stringType":
			case "booleanType":
			case "voidType":
				sendToServer = eval(script);
				if(sendToServer ==null)
					sendToServer = "";
				break;
			case "JSONArray":
				var elementArray = new Array();
				var elements = eval(script);
				for(var i=0; i<elements.length;i++)
				{
					var element = {};
					element.type = __getClass(elements[i]);
					element.guid = script +"[" + i + "]";	
					element.label = elements[i].label();
					element.name = elements[i].name();
					element.value = elements[i].value();
					element.rect  = elements[i].rect();
					elementArray.push(element);
				}
				sendToServer = elementArray.toJSONString();
				break;
			case "JSONObject":
				var element = eval(script);
				var e = {};
				e.type = __getClass(element);
				e.guid = script;
				e.label = element.label();
				e.name = element.name();
				e.value = element.value();
				e.rect  = element.rect();
				sendToServer = e.toJSONString();
				break;
			default:
				sendToServer ="null";
				isEnd =true;
		}
        UIALogger.logMessage("sendToServer : " + sendToServer);
	}
	
	UIALogger.logPass("The case was passed.");

} catch (e) {
		
	sendToServer = "Error #" + e;
	
	//if has exception ,send the exception to server.
	host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
	//end Exit
	host.performTaskWithPathArgumentsTimeout("/Athrun/TcpSocket.sh",[sendToServer],60);
	UIALogger.logError(sendToServer);
	UIALogger.logFail("The case was failed.");	
}

function findElement(root, text, index, elementType){
	__element = "UIAElementNil";
	__index = 0;
	target.pushTimeout(0);
	__findElement(eval(root),root, text, index, elementType);
	target.popTimeout();
	
	return __element;
}

function __findElement(root, script, text, index, elementType){
		
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
	
		if(__assertContainText(elements[i],text)){
			
			var obj = {};
			obj.guid = script + ".elements()[" + i + "]";
			obj.type = __getClass(elements[i]);
			obj.name =  elements[i].name();
			obj.value = elements[i].value();
			obj.label = elements[i].label();
			obj.rect = elements[i].rect();
			
			if(elementType == "UIAElement"){	
				if(__index == index){
					__element = obj.toJSONString();
					break;
				}
				__index++;
			}else if( __getClass(elements[i])== elementType){
				if(__index == index){
					__element = obj.toJSONString();
					break;
				}
				__index++;
			}
		}
		
		__findElement(elements[i] , script + ".elements()[" + i + "]", text,index , elementType);
		
	}
}

function findElements(root, text, elementType){
	__elementArray =[];
	target.pushTimeout(0);
	__findElements(eval(root),root, text, elementType);
	target.popTimeout();
	
	return __elementArray.toJSONString();
}

function __findElements(root, script, text, elementType){
		
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
	
		if(__assertContainText(elements[i],text)){
			
			var obj = {};
			obj.guid = script + ".elements()[" + i + "]";
			obj.type = __getClass(elements[i]);
			obj.name =  elements[i].name();
			obj.value = elements[i].value();
			obj.label = elements[i].label();
			obj.rect = elements[i].rect();
			
			if(elementType == "UIAElement"){	
				__elementArray.push(obj);
				UIALogger.logMessage("UIAElement : " + obj.toJSONString());	
			}else if( __getClass(elements[i])== elementType){
				__elementArray.push(obj);
			}
		}
		__findElements(elements[i] , script + ".elements()[" + i + "]", text , elementType);
		
	}
}

function printElementTree(root){
	
	__elementTree = "";
	target.pushTimeout(0);
	__logElementTree(eval(root),"-", root);
	target.popTimeout();
	
	return __elementTree;
}

function __logElementTree(root,space,script){	
	
	var elements = root.elements();
	
	for(var i = 0 ; i<elements.length ;i++){
		
		var obj = {};
		obj.guid = script + ".elements()[" + i + "]";
		obj.type = __getClass(elements[i]);
		obj.name =  elements[i].name();
		obj.value = elements[i].value();
		obj.label = elements[i].label();
		obj.rect = elements[i].rect();
		__elementTree += "+" +space + obj.toJSONString() + "###";
		
		__logElementTree(elements[i] , space + "----", script + ".elements()[" + i + "]");
	}
}

function __getClass(object){
	
	return Object.prototype.toString.call(object).match(/^\[object\s(.*)\]$/)[1];	
}

function __assertContainText(obj, expectContain) {
	
	var name = obj.name();
	var labelText = obj.label();
	var value = obj.value();
	
	var isTrue1 = name == null ? false
				: (name.search(expectContain) != -1 ? true : false);
	var isTrue2 = labelText == null ? false : (labelText
				.search(expectContain) != -1 ? true : false);
	//var isTrue3 = value == null ? false : (value.search(expectContain) != -1 ? true : false);
	if (isTrue1 || isTrue2 ) {
		return true;
	} else {
		return false;
	}
}

function __assertEqualsText(obj,text) {
	
	var name = obj.name();
	var labelText = obj.label();
	var value = obj.value();
	var isTrue1 = name == text ? true : false;
	var isTrue2 = labelText == text ? true : false;
	var isTrue3 = value == text ? true : false;
	
	if (isTrue1 || isTrue2 || isTrue3) {
		return true;
	} else {
		return false;
	}
}