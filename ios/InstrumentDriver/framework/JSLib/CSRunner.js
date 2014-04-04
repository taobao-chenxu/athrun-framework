#import "./common.js"

UIALogger.logStart("The case is running.");

var step = 1;
var isEnd = false;
var sendToServer ="Get the next step.";
try {
	while(!isEnd){
		var result = host.performTaskWithPathArgumentsTimeout("%InstrumentRoot%/TcpSocket.sh",[sendToServer, step],60);
		step++;
		UIALogger.logMessage("stdout : " + result.stdout);
		
		var stdout = result.stdout.split("##");
		var type = stdout[0];
		var script =stdout[1];
		__initial();
		switch(type)
		{
			case "stringType":
			case "booleanType":
			case "voidType":
			case "numberType":
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
					element.val = elements[i].value();
					element.rect  = elements[i].rect();
					elementArray.push(element);
				}
				sendToServer = JSON.stringify(elementArray);
				break;
			case "JSONObject":
				var element = eval(script);
				var e = {};
				e.type = __getClass(element);
				e.guid = script;
				e.label = element.label();
				e.name = element.name();
				e.val = element.value();
				e.rect  = element.rect();
				sendToServer = JSON.stringify(e);
				break;
			default:
				isEnd = true;
		}
		if(sendToServer!=null){
			sendToServer = sendToServer.toString().replace(/\\r\\n/ig,"");
			sendToServer = sendToServer.toString().replace(/\\r/ig,"");
			sendToServer = sendToServer.toString().replace(/\\n/ig,"");
		}
        UIALogger.logMessage("sendToServer : " + sendToServer);
	}
	
	UIALogger.logPass("The case was passed.");

} catch (e) {
		
	sendToServer = "Exception # " + e + ". The script is : " + script;
	
	// if has exception ,send the exception to server.
	host.performTaskWithPathArgumentsTimeout("%InstrumentRoot%/TcpSocket.sh",[sendToServer, step],60);
	// end Exit
	host.performTaskWithPathArgumentsTimeout("%InstrumentRoot%/TcpSocket.sh",[sendToServer, step],60);
	UIALogger.logError(sendToServer);
	UIALogger.logFail("The case was failed.");	
}