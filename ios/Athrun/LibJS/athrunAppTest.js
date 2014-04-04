/**
 *测试入口，每个测试脚本都作为参数在这个方法中运行。
 *第一个参数为该用例的名称，用例运行结束可以根据该名称在日志中找到该用例的运行日志。
 *第二个参数为一个方法，里面为该用例的具体执行步骤，传入该方法会在appTest中调用执行以便统一捕获异常、记录日志。
 *第三个参数为可选参数，该参数标志是否在用例出错是往日志打印出控件对象的树结构。
 *
 *使用示例：
 *appTest("Login", function(target,application){
 *	//case steps and validates
 *}, false);
 */

function appTest(caseName, f, islogTree) {
	
	try {
		target = UIATarget.localTarget();
		app = target.frontMostApp();
		win = app.mainWindow()
		UIALogger.logStart(caseName);
        UIALogger.logMessage("The case " + caseName + " is running.");
		
		//Invoke the function and run the case steps.
		f(target, win);

		UIALogger.logPass("The case " + caseName + " was passed.");
	} catch (e) {
		if(e instanceof Error) {
			e.log();
		} else {
			UIALogger.logError(e +" #other error!" );
		}
		if(islogTree)
			target.logElementTree();
		UIALogger.logFail("The case " + caseName + " was failed.");
	}
}