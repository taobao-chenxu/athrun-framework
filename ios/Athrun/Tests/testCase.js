#import "../LibJS/athrunImports.js"

/**
 *测试Picker的相关扩展
 */
 appTest("PickerTest",function(target,application){
		 
		 target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Demo 2, test Pickers"].tap();
		 var yearWheel = target.frontMostApp().mainWindow().pickers()[0].wheels()[2];
		 yearWheel.selectValue(2016);
		 yearWheel.doSelectValue(2010);
		
		 var cityWheel = target.frontMostApp().mainWindow().pickers()[1].wheels()[1];
		 cityWheel.doSelectValue("深圳");
		 
		 var time = yearWheel.parent().getValue();
		 var city = cityWheel.parent().getValue();
		 
		 UIALogger.logMessage(time +"@@" + city);
		 
		 target.frontMostApp().navigationBar().leftButton().tap();
		 
		 },false)

/**
 * Switch控件状态的测试
 */
appTest("SwitchTest", function(target,application){
		//case steps and validates
		
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Demo 3, test imageView"].doTap();

		var _switch = target.frontMostApp().mainWindow().switches()[0];
		
		_switch.assertState(1);
		target.frontMostApp().mainWindow().switches()[0].setValue(0);
		
		_switch.assertState(0);
		_switch.assertState(1);
		target.frontMostApp().mainWindow().switches()[0].setValue(1);		
		assertSwitchState(_switch,1);
		assertSwitchState(_switch,0);
		
		target.frontMostApp().navigationBar().leftButton().doTap();
		
		},false);

/**
 *测试弹出窗口的捕获
 */
 appTest("AlertTest",function(target,application){
		
		target.frontMostApp().mainWindow().tableViews()["Empty list"].cells()["Demo 4, test dailogView"].doTap();
		target.frontMostApp().mainWindow().segmentedControls()[0].buttons()["Confirm"].doTap();
		// Alert detected. Expressions for handling alerts should be moved into the UIATarget.onAlert function definition.
		
		assertPopWindowTitle("ConfirmDailog","The popWindow's title test");
		assertPopWindowTitle("ConfirmDailog@@","The popWindow's title test"); 
		 
		target.frontMostApp().alert().cancelButton().doTap();
		target.frontMostApp().mainWindow().segmentedControls()[0].buttons()["Other"].doTap();
		// Alert detected. Expressions for handling alerts should be moved into the UIATarget.onAlert function definition.
		target.frontMostApp().alert().buttons()["IOS"].doTap();
		target.frontMostApp().mainWindow().segmentedControls()[0].buttons()["Alert"].doTap();
		// Alert detected. Expressions for handling alerts should be moved into the UIATarget.onAlert function definition.
		target.frontMostApp().alert().defaultButton().doTap();
		target.frontMostApp().navigationBar().leftButton().doTap();
		
		},false)
