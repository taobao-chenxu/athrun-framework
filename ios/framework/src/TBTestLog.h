//
//  TBTestLog.h
//  AppFramework
//
//  Created by nan fei on 11-7-7.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  do log info by this class
//  add by nan fei
//

#import <Foundation/Foundation.h>

@interface TBTestLog : NSObject {

}

+(void)debugLog:(NSString *)str;

+(void)imageLog:(UIView *)v elementName:(NSString *)elementName;

+(void)TBLog:(NSString *)str;

+(void)TBDebug:(NSString *)str;

+(void)TBWarn:(NSString *)str;

+(void)TBError:(NSString *)str;

+(void)TBRun:(NSString *)str;

+(void)elementInfo:(UIView *)v pathNumber:(NSInteger )pathNumber;

@end
