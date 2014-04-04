//
//  TBAssert.h
//  AppFramework
//
//  Created by su ying on 11-10-8.
//  Copyright 2011年 perun. All rights reserved.
//  
//  add assert funcation by su ying
//

#import <Foundation/Foundation.h>
#import "TBElement.h"

@interface TBAssert : NSObject{
    
}
+(BOOL)assertEquals:(NSString *)expected receivedStr: (NSString *)received messageStr: (NSString *)message; 

+(BOOL)assertTrue:(BOOL)expression messageStr: (NSString *)message;
+(BOOL)assertFalse:(BOOL)expression messageStr: (NSString *)message;
+(BOOL)assertNotNull:(TBElement *)object messageStr: (NSString *)message; 
+(BOOL)assertContainText:(NSString *) valuePage  valueVerify: (NSString *)valueverify messageStr: (NSString *)message;
 

@end
