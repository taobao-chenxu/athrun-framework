/*
 *taobao4iPhone 自动化测试的公共方法
 */

/*
 *用户登录，参数：用户名，密码 
 *点击我的淘宝，输入用户名，密码，登录后回到主页
 */
function Login(name,password) {
	app.tabBar().buttons()["我的淘宝"].doTap();
	
	var text = app.navigationBar().rightButton().name();
	
	if(app.navigationBar().rightButton().name() == "登录") {
			
		var userNameField = win.tableViews()[0].cells()["帐  号:"].textFields()[0];
		userNameField.setValue(""); //清空登录时，帐号	的输入框
		userNameField.doTap();
		
		userNameField.setValue(name);
		
		var passWordField =win.tableViews()[0].cells()["密  码:"].secureTextFields()[0];
		
		passWordField.tap();
		passWordField.setValue(password);
		
		app.navigationBar().buttons()["登录"].doTap();
		
	} else {
		app.navigationBar().buttons()["退出登录"].doTap();
		app.actionSheet().buttons()["确定"].tap();
		
		var userNameField = win.tableViews()[0].cells()["帐  号:"].textFields()[0];
		userNameField.tap();
		userNameField.setValue(name);
		
		var passWordField =win.tableViews()[0].cells()["密  码:"].secureTextFields()[0];
		
		passWordField.tap();
		passWordField.setValue(password);
		
		app.navigationBar().buttons()["登录"].doTap();
	}
	app.tabBar().buttons()["首页"].doTap();
}




/*
 *获取宝贝的价格
 */
function getTheItemPrice(cellText){
	
	var price = cellText.split(", ￥")[1].split(',')[0]
	//var length = priceStr.length;
	
	//var price = priceStr.substring(2,length);
	
	return 	parseFloat(price);
}


/*
 *检查是否为价格从低到高排序
 */
function checkPriceLowToHight(itemTable)
{
	checkItemPriceSort(itemTable,true);
}

/*
 *检查是否为价格从高到低排序
 */
function checkPriceHightToLow(itemTable)
{
	checkItemPriceSort(itemTable,false);
	
}

function checkItemPriceSort(itemTable,isLowToHight)
{
	var tableCells =itemTable.cells().length;

	var firstCell = itemTable.cells()[0].name();
	
	//UIALogger.logMessage("firstCell: " + firstCell);
		
	var price =getTheItemPrice(firstCell);		
	var sortIsTrue = true;
	
	for(i= 1;i<5;i++)
	{	
		var celltext2 = itemTable.cells()[i].name();		
		var price2 =getTheItemPrice(celltext2);
		var price2 = parseFloat(price2);
		
		if(isLowToHight)
		{	
			if(price > price2)	
			{	
				sortIsTrue = false;
				break;
			}else{
				price = price2;
			}
		}else
		{
			if(price < price2)	
			{
				UIALogger.logMessage("---price: " + price + " price2 : " + price2);
				sortIsTrue = false;
				break;
			}else{
				price = price2;
			}
		}
	}
	Athrun.assertTrue(sortIsTrue,"校验宝贝价格排序");
	
}
/*
 *获取宝贝的销售量
 */
function getTheItemSalesVolume(cellText){
	
	var arr =cellText.split(',');
	var arrLength = arr.length;
	var salesVolume = arr[arrLength - 1].split(':')[1];
	var length = salesVolume.length;
	var count = salesVolume.substring(0,length-1);
	return count;
}

/*
 *检查是否为销售量从高到低排序
 */
function checkSalesVolumeHightToLow(itemTable)
{
	checkItemSalesVolumeSort(itemTable,false);
	
}

function checkItemSalesVolumeSort(itemTable,isLowToHight)
{
	var tableCells =itemTable.cells().length;

	var firstCell = itemTable.cells()[0].name();
		
	var count = getTheItemSalesVolume(firstCell);		
	var sortIsTrue = true;
	
	for(i= 1;i<5;i++)
	{	
		var celltext2 = itemTable.cells()[i].name();		
		var count2 = getTheItemSalesVolume(celltext2);
		
		if(isLowToHight)
		{
			if(count > count2)	
			{
				sortIsTrue = false;
				break;
			}else{
				count = count2;
			}
		}else
		{
			if(count < count2)	
			{
				sortIsTrue = false;
				break;
			}else{
				count = count2;
			}
		}
	}
	Athrun.assertTrue(sortIsTrue,"检查宝贝销量排序");
	//UIALogger.logMessage("HightSalesVolume: " + count + " tableCells : " + tableCells);
}

/*
 *获取宝贝的邮费
 */
function getTheItemPostage(cellText){
	
	var arr =cellText.split(',');
	var arrLength = arr.length;
	var postage = arr[arrLength - 2].split(':')[1];
	return parseFloat(postage);
}


/*
 *检查列表的宝贝是否免运费
 */
function checkItemPostageisFree(itemTable)
{
	var tableCells =itemTable.cells().length;	
	
	var isFree = true;
	
	for(i= 0;i<5;i++)
	{	
		var cellText = itemTable.cells()[i].name();		
		var postage = getTheItemPostage(cellText);	

		if(postage !=0)
		{
			isFree = false;
			break;			
		}
	}
	Athrun.assertTrue(isFree,"检查宝贝免运费");
	//UIALogger.logMessage("Postage is: " + postage + " tableCells : " + tableCells);
}


/*
 *检查列表的宝贝价格在一个区间
 */
function checkItemPriceRange(itemTable,low,height)
{
	var tableCells =itemTable.cells().length;	
	
	var isTrue = true;
	
	for(i= 0;i<5;i++)
	{	
		var cellText = itemTable.cells()[i].name();		
		var price = getTheItemPrice(cellText);	

		if( price < low || price > height)
		{
			isTrue = false;
			break;			
		}
	}
	Athrun.assertTrue(isTrue,"检查宝贝价格区间");
	//UIALogger.logMessage("price is: " + price + " tableCells : " + tableCells);
}
