//
//  TBRunner.h
//  AppFramework
//
//  Created by nan fei on 6/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  control the test script run
//
//  add by nan fei
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface TBTestRunner : NSObject {
	
	//story test script command
	NSMutableArray *arrayScript;   //保存测试脚本序列文件
	NSMutableArray *arrayCommands; //保存脚本中操作步骤
	NSInteger testcaseIndex;       //保存执行的测试脚本序列号
	NSString *scriptName;           //保存当前执行脚本的名字
	

}

@end
