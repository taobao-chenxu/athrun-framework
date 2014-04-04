//
//  TBRunner.m
//  AppFramework
//
//  Created by nan fei on 6/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  add by nan fei and su ying
//

#ifdef SCRIPT_DRIVEN_TEST_MODE_ENABLED

#import "TBRunner.h"
#import "TBOperator.h"
#import "TBElement.h"
#import "XPathQuery.h"
#import "TBTestLog.h"
#import "TBAssert.h"


const float SCRIPT_RUNNER_INTER_COMMAND_DELAY = 3.0;
const float MAX_WAIT_ATTEMPTS = 60;
const float WAIT_ATTEMPT_DELAY = 0.25;
const float BACKBUTTON_WAIT_DELAY = 0.75;

@implementation TBTestRunner

-(id)init
{
	self = [super init];
	if (self != nil)
	{
		
		NSData *scriptData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"ScriptIndex" ofType:@"plist"]];
		arrayScript = [[NSPropertyListSerialization propertyListFromData:scriptData 
														mutabilityOption:NSPropertyListMutableContainers 
																  format:nil 
														errorDescription:nil] retain];
		testcaseIndex = [arrayScript count];
		testcaseIndex = testcaseIndex-1;
	    scriptName = [arrayScript objectAtIndex:testcaseIndex];
							  
		//测试对象初始时，读取测试用例脚本
		NSData *fileData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:scriptName ofType:@"plist"]];
		arrayCommands = [[NSPropertyListSerialization propertyListFromData:fileData 
														  mutabilityOption:NSPropertyListMutableContainers 
																	format:nil 
														  errorDescription:nil] retain];
		
		//调用自身的测试指令执行函数，在被测应用加载完成后2秒
		[self retain];
		[self performSelector:@selector(runCommand) withObject:nil afterDelay:2.0];
		
	}
		
	[TBTestLog TBLog:@"\n\n"];
	NSString *temp = [[NSString alloc] initWithFormat:@"Test Case %@ start run!\n",scriptName];
	[TBTestLog TBRun:temp];
	
	return self;
}


-(void)dealloc
{
	[arrayCommands release];
	[super dealloc];
}

-(void)runCommand
{
	
	@try {
	
	
	NSString *run = @"";
	NSString *debug = @"";
	NSString *warn = @"";
	NSString *err = @"";
	
	//读取一条测试指令
	NSDictionary *command = [arrayCommands objectAtIndex:0];
	NSString *commandName = [command objectForKey:@"command"];
	
	//操作对象不能为空
	NSString *viewXpath;
	if ([command objectForKey:@"element"])
	{
		viewXpath = [command objectForKey:@"element"];
	}
	else 
	{
		viewXpath = [command objectForKey:@"viewXPath"];
	}

	/*
	if (viewXpath == nil)
	{
		err = [[NSString alloc] initWithFormat:@"command require viewXPath was null!"];
		[TBTestLog TBError:err];
		exit(1);
	}
	*/
	
	//获取操作对象
	UIView *viewTarget;
	NSString *pageName = nil;
	NSString *elementName = nil;

    NSArray *tree = [viewXpath componentsSeparatedByString:@":"];
	pageName = [tree objectAtIndex:0];
	elementName = [tree objectAtIndex:1];
	viewTarget = [[TBElement alloc] find_element:pageName elementName:elementName];

	//截屏
	//UIView *imageView = [[[[UIApplication sharedApplication].delegate window] subviews] lastObject];
	[TBTestLog imageLog:NULL elementName:elementName];

	
	//分流操作指令			
	if ([commandName isEqualToString:@"simluteTouch"])
	{
		if ([TBOperator simluteTouch:viewTarget])   //点击操作
		{
			run = @"";
			run = [run stringByAppendingString:pageName];
			run = [run stringByAppendingString:@"-"];
			run = [run stringByAppendingString:elementName];
			run = [run stringByAppendingString:@" touch is ok!\n"];
			[TBTestLog TBRun:run];
		}
		else 
		{
			err = @"";
			err = [err stringByAppendingString:pageName];
			err = [err stringByAppendingString:@"-"];
			err = [err stringByAppendingString:elementName];
			err = [err stringByAppendingString:@" touch failed!\n"];
			[TBTestLog TBError:err];
		}

	}
	else if ([commandName isEqualToString:@"Set"])
		{
			NSString *valueString = [command objectForKey:@"valueString"];  //输入操作
		//支持将输入值变量化 add by suying++++++++++++++++++++++++++++++++++++++++++++++++++++	
			NSRange cRange = [valueString rangeOfString:@"Consts"];
			if(cRange.length!=0)
			{
				NSData *constsData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Consts" ofType:@"plist"]];
			    NSArray *arrConsts = [[NSPropertyListSerialization propertyListFromData:constsData 
			                                                           mutabilityOption:NSPropertyListMutableContainers 
																				 format:nil 
			                                                           errorDescription:nil] retain];
				
			    NSArray *valueArray = [valueString componentsSeparatedByString:@":"];		
			    NSString *varName = [valueArray objectAtIndex:1];
		        if ([arrConsts objectForKey:varName])
				{
					valueString = [arrConsts objectForKey:varName];
				}
				else 
				{
					valueString = @"";
					err = @"";
					err = [err stringByAppendingString:varName];
					err = [err stringByAppendingString:@" variable not found! value was set NULL!\n"];
					[TBTestLog TBError:err];
				}

			 
			 }
			 
		//支持将输入值变量化 add by suying++++++++++++++++++++++++++++++++++++++++++++++++++++	
			
			if ([TBOperator Set:viewTarget string:valueString])
			{
				run = @"";
				run = [run stringByAppendingString:pageName];
				run = [run stringByAppendingString:@"-"];
				run = [run stringByAppendingString:elementName];
				run = [run stringByAppendingString:@" set is ok!\n"];
				[TBTestLog TBRun:run];
			}
			else 
			{
				err = @"";
				err = [err stringByAppendingString:pageName];
				err = [err stringByAppendingString:@"-"];
				err = [err stringByAppendingString:elementName];
				err = [err stringByAppendingString:@" set failed!\n"];
				[TBTestLog TBError:err];
			}

		}
	else if ([commandName isEqualToString:@"scrollToRow"])
		{
			//转换控件类为TableView
			UITableView *view = viewTarget;
			NSNumber *rowIndex = [command objectForKey:@"rowIndex"];
			NSNumber *sectionIndex = [command objectForKey:@"sectionIndex"];
			
			CGRect sectionRect = [view rectForSection:[rowIndex integerValue]];
			//sectionRect.size.height = view.frame.size.height;
			sectionRect.size.width = view.frame.size.width;
			[view scrollRectToVisible:sectionRect animated:YES];
			
			run = @"";
			run = [run stringByAppendingString:pageName];
			run = [run stringByAppendingString:@"-"];
			run = [run stringByAppendingString:elementName];
			run = [run stringByAppendingString:@" scroll is ok!\n"];
			[TBTestLog TBRun:run];
			
		}
	else if ([commandName isEqualToString:@"getProperties"])
		{
			NSString *Gpropert = [command objectForKey:@"properties"];   //获取控件属性
			if ([TBOperator getProperties:viewTarget propert:Gpropert])
			{
				run = @"";
				run = [run stringByAppendingString:pageName];
				run = [run stringByAppendingString:@"-"];
				run = [run stringByAppendingString:elementName];
				run = [run stringByAppendingString:@" get properties is ok!\n"];
				[TBTestLog TBRun:run];
			}
			else 
			{
				err = @"";
				err = [err stringByAppendingString:pageName];
				err = [err stringByAppendingString:@"-"];
				err = [err stringByAppendingString:elementName];
				err = [err stringByAppendingString:@" get properties failed!\n"];
				[TBTestLog TBError:err];
			}
		}
    else if ([commandName isEqualToString:@"checkProperties"])
		{
			NSString *Cpropert = [command objectForKey:@"properties"];   //进行属性对比
			NSString *CexpectString = [command objectForKey:@"expectString"];
			if ([TBOperator checkProperties:viewTarget propert:Cpropert expect:CexpectString])
			{
				run = @"";
				run = [run stringByAppendingString:pageName];
				run = [run stringByAppendingString:@"-"];
				run = [run stringByAppendingString:elementName];
				run = [run stringByAppendingString:@" check properties is ok!"];
				[TBTestLog TBRun:run];
			}
			else 
			{
				err = @"";
				err = [err stringByAppendingString:pageName];
				err = [err stringByAppendingString:@"-"];
				err = [err stringByAppendingString:elementName];
				err = [err stringByAppendingString:@" check properties failed!"];
				[TBTestLog TBError:err];
			}
		}
	else if([commandName isEqualToString:@"sleep"]){
		NSString *numberStr=[command objectForKey:@"number"];
		int number=[numberStr intValue];
		sleep(number);
		
		
	}
	
	else if([commandName isEqualToString:@"Switch"])
	{
		NSString *stateString = [command objectForKey:@"state"];
		if ([TBOperator Switch:viewTarget state:stateString])
		{
			run = [run stringByAppendingString:pageName];
			run = [run stringByAppendingString:@"-"];
			run = [run stringByAppendingString:elementName];
			run = [run stringByAppendingString:@" Switch is ok!\n"];
			[TBTestLog TBRun:run];
		}
		else 
		{
			err = [err stringByAppendingString:pageName];
			err = [err stringByAppendingString:@"-"];
			err = [err stringByAppendingString:elementName];
			err = [err stringByAppendingString:@" Switch failed!\n"];
			[TBTestLog TBError:err];
		}
	}
	
	/*suying add-------
	 
	 else if([commandName isEqualToString:@"assertEquals"]){
	 
	 
	 NSString *expected = [command objectForKey:@"expectedString"];
	 NSString *received = [command objectForKey:@"receivedString"];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertEquals:expected receivedStr: received messageStr: message];    
	 
	 } 
	 else if([commandName isEqualToString:@"assertTrue"]){
	 
	 //   +(void)assertTrue:(BOOL)expression messageStr: (NSString *)message;
	 //         +(void)assertFalse:(BOOL)expression messageStr: (NSString *)message;
	 //         +(void)assertNotNull:(UIView *)object messageStr: (NSString *)message 
	 //         +(void)assertContainText:(NSString *) valuePage  valueVerify: (NSString *) messageStr: (NSString *)message;         
	 //         
	 
	 NSString *expressionStr = [command objectForKey:@"expression"];
	 BOOL expression=[expressionStr boolValue];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertTrue:expression messageStr:message]; 
	 
	 } 
	 else if([commandName isEqualToString:@"assertFalse"]){
	 
	 
	 NSString *expressionStr = [command objectForKey:@"expression"];
	 BOOL expression=[expressionStr boolValue];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertFalse:expression messageStr:message]; 
	 }  
	 else if([commandName isEqualToString:@"assertNotNull"]){
	 
	 
	 //此操作必须和viewXPath指令一起使用
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertNotNull:viewTarget messageStr:message]; 
	 }*/
	 else if([commandName isEqualToString:@"assertContainText"]){
	 
	 
	 //此操作必须和viewXPath指令一起使用
	 NSString *elementText=[TBOperator getProperties:viewTarget propert:@"text"];	 	 		 
	 NSString *verifyText=[command objectForKey:@"valueVerify"];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 if ([TBAssert assertContainText:elementText valueVerify:verifyText messageStr:message]) {
	    
		 
		 
		 run = @"";
		 run = [run stringByAppendingString:@"TestCase:"];
		 run = [run stringByAppendingString:scriptName];
		 run = [run stringByAppendingString:@"==="];
		 
		 run = [run stringByAppendingString:message];
		 [TBTestLog TBRun:run];
	 }
	 else 
	 {
		 err = @"";
		 err = [run stringByAppendingString:@"TestCase:"];
		 err = [run stringByAppendingString:scriptName];
		 err = [run stringByAppendingString:@"==="];
		 err = [err stringByAppendingString:@" assertContainText failed!"];
		 [TBTestLog TBError:err];
	 }
		 

	 }  
	 
	
	
	
	
	
	
	else
		{
			//操作指令未匹配到，系统暂时不支持
			//printf("### the command:%s does't support\n",[commandName UTF8String]);
			err = [[NSString alloc] initWithFormat: @"the command:%s does't support\n",[commandName UTF8String]];
			[TBTestLog TBError:err];
			exit(1);
		}	
	
	
	}
	@catch (NSException * e) 
	{
		NSString *eString = [NSString stringWithFormat:@"exception %@:%@.\n",[e name],[e reason]];
		[TBTestLog TBRun:eString];
	}
	
	@finally 
	{
	 [TBTestLog debugLog:@"\n\n this page is over! \n\n"];
	 
	 //这条指令执行完成，从指令集中去除
	 [arrayCommands removeObjectAtIndex:0];
	 
	 if ([arrayCommands count] == 0)
	 {
	 if (testcaseIndex == 0)
	 {
	 //指令集中指令执行完后，释放测试对象
	 [self performSelector:@selector(release) withObject:nil afterDelay:5.0];
	 //[self release];
	 //exit(0);
	 }
	 else 
	 {
	 NSString *tempEnd = [[NSString alloc] initWithFormat:@"Test Case %@ run end!\n",scriptName];
	 [TBTestLog TBRun:tempEnd];
	 
	 testcaseIndex = testcaseIndex-1;
	 scriptName = [arrayScript objectAtIndex:testcaseIndex];
	 
	 //测试对象初始时，读取测试用例脚本
	 NSData *fileData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:scriptName ofType:@"plist"]];
	 arrayCommands = [[NSPropertyListSerialization propertyListFromData:fileData 
	 mutabilityOption:NSPropertyListMutableContainers 
	 format:nil 
	 errorDescription:nil] retain];
	 
	 [self performSelector:@selector(runCommand) withObject:nil afterDelay:SCRIPT_RUNNER_INTER_COMMAND_DELAY];
	 
	 [TBTestLog TBLog:@"\n\n"];
	 NSString *temp = [[NSString alloc] initWithFormat:@"Test Case %@ start run!\n",scriptName];
	 [TBTestLog TBRun:temp];
	 }
	 }
	 else
	 {
	 //该指令执行完后，延时一段时间执行下一条指令
	 [self performSelector:@selector(runCommand) withObject:nil afterDelay:SCRIPT_RUNNER_INTER_COMMAND_DELAY];
	 }
	}	
}


@end

#endif
