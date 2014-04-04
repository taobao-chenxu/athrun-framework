//
//  TBTestLog.m
//  AppFramework
//
//  Created by jerryding on 11-7-7.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "TBTestLog.h"
#import "TBRunner.h"
#import <UIKit/UIKit.h>


@implementation TBTestLog

//输出page的view层结构
+(void)debugLog:(NSString *)str
{
	NSString *path = @"/AppFrameworkLog/AppFrameworkTree.log";
	NSFileHandle *file;
	file = [NSFileHandle fileHandleForWritingAtPath:path];
	if (file == nil)
    {
		NSData *startStr = [@"taobao iphone test is run!\n \n" dataUsingEncoding:NSUTF8StringEncoding];
		[startStr writeToFile:path atomically:YES];
		file = [NSFileHandle fileHandleForWritingAtPath:path];
	}
	[file seekToEndOfFile];
	NSData *dataToWrite = [str dataUsingEncoding:NSUTF8StringEncoding];
	[file writeData:dataToWrite];
}

//输出运行时抓取的图片
+(void)imageLog:(UIView *)v elementName:(NSString *)elementName
{
	if(v != NULL)
	{
	if (UIGraphicsBeginImageContextWithOptions != NULL)
	{
		UIGraphicsBeginImageContextWithOptions(v.frame.size, NO, 0.0);
	}
	else 
	{		
		UIGraphicsBeginImageContext(v.frame.size);
	}
	
	[v.layer renderInContext:UIGraphicsGetCurrentContext()];
	UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	NSString *path = [@"/AppFrameworkLog/image" stringByAppendingFormat:@"/%s.png",[elementName UTF8String]];
	if ([UIImagePNGRepresentation(image) writeToFile:path atomically:YES])
	{
		NSLog(@"Succeeded!");
	}
	else 
	{
		NSLog(@"Failed!");
	}
	}
	else 
	{
	UIWindow *w = [[UIApplication sharedApplication] windows];
	UIWindow *subW;
	NSString *imageHead = [NSString stringWithFormat:@"%@",[NSDate date]];
	NSInteger index = 1;
	
	for (subW in w) 
	{
		if (UIGraphicsBeginImageContextWithOptions != NULL)
	    {
			UIGraphicsBeginImageContextWithOptions(subW.frame.size, NO, 0.0);
	    }
	    else 
		{		
			UIGraphicsBeginImageContext(subW.frame.size);
	    }
	
	    [subW.layer renderInContext:UIGraphicsGetCurrentContext()];
	    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
	    UIGraphicsEndImageContext();
	
	    NSString *path = [@"/AppFrameworkLog/image" stringByAppendingFormat:@"/%s-%d.png",[imageHead UTF8String],index];
	    if ([UIImagePNGRepresentation(image) writeToFile:path atomically:YES])
	    {
			index += 1;
			NSLog(@"Succeeded!");
	    }
	    else 
	    {
			NSLog(@"Failed!");
	    }
	}
	}
}

//输出运行日志
+(void)TBLog:(NSString *)str 
{
	NSString *path = @"/AppFrameworkLog/AppFrameworkRun.log";
	NSFileHandle *file;
	file = [NSFileHandle fileHandleForWritingAtPath:path];
	if (file == nil)
    {
		NSData *startStr = [@"taobao iphone test is run!\n \n" dataUsingEncoding:NSUTF8StringEncoding];
		[startStr writeToFile:path atomically:YES];
		file = [NSFileHandle fileHandleForWritingAtPath:path];
	}
	[file seekToEndOfFile];
	NSData *dataToWrite = [str dataUsingEncoding:NSUTF8StringEncoding];
	[file writeData:dataToWrite];
}

+(void)TBDebug:(NSString *)str
{
	NSString *debugStr = [NSString stringWithFormat: @"%@---[Debug]:",[NSDate date]];
	debugStr = [debugStr stringByAppendingString:str];
	[self TBLog:debugStr];
}

+(void)TBWarn:(NSString *)str
{
	NSString *warnStr = [NSString stringWithFormat: @"%@---[Warn]:",[NSDate date]];
	warnStr = [warnStr stringByAppendingString:str];
	[self TBLog:warnStr];
}

+(void)TBError:(NSString *)str
{
	NSString *errorStr = [NSString stringWithFormat: @"%@---[Error]:",[NSDate date]];
	errorStr = [errorStr stringByAppendingString:str];
	[self TBLog:errorStr];
}

+(void)TBRun:(NSString *)str
{
	NSString *runStr = [NSString stringWithFormat: @"%@---[Run]:",[NSDate date]];
	runStr = [runStr stringByAppendingString:str];
	[self TBLog:runStr];
}

+(void)elementInfo:(UIView *)v pathNumber:(NSInteger )pathNumber
{
	NSString *headS = [[NSString alloc] initWithFormat:@"--"];
	NSInteger i;
	for (i=0; i<pathNumber; i++) 
	{
		[TBTestLog debugLog:headS];
	}
	
	NSString *stringInfo= [[v description] cStringUsingEncoding:NSUTF8StringEncoding];
	NSString *temp = [[NSString alloc] initWithFormat:@"subviews (%d):%s\n",pathNumber,stringInfo];
	[TBTestLog debugLog:temp];
	
	
	if ([v respondsToSelector:@selector(text)])
	{
		NSString *l = [v text];
		[TBTestLog debugLog:@"text:"];
		[TBTestLog debugLog:l];	
		[TBTestLog debugLog:@"\n"];
	}
}

@end
