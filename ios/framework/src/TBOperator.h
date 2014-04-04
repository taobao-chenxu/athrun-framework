//
//  TBOperator.h
//  AppFramework
//
//  Created by nan fei on 5/25/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  define operator funcation
//
//  add by nan fei
//

#import <Foundation/Foundation.h>


@interface TBOperator : NSObject {

}

/*
 operator: touch
 describe: simulate a touch event on the object
*/
+(bool)simluteTouch:(UIView *)object;

/*
 operator: set
 describe: 
 */
+(bool)Set:(UIView *)object string:(NSString *)string;

/*
 operator: scrollTo
 describe: 
 */
+(bool)scrollTo:(NSArray *)object indexpath:(NSIndexPath *)indexpath;

/*
 operator: getProperties
 describe: 
 */
+(NSString *)getProperties:(UIView *)object propert:(NSString *)properties;

/*
 operator: checkProperties
 describe: 
 */
+(bool)checkProperties:(UIView *)object expect:(NSString *)expectString;

/*
 operator:Switch
 describe:simulate a touch event on the UISwitch object
*/
+(bool)Switch:(UIView *)object state:(NSString *)stateString;


@end
