//
//  TBAssert.m
//  AppFramework
//
//  Created by Suying on 11-10-18.
//  Copyright 2011年 perun. All rights reserved.
//

#import "TBAssert.h"
#import "TBTestLog.h"
#import "TBElement.h"

@implementation TBAssert
+(BOOL) assertEquals:(NSString *)expected receivedStr: (NSString *)received messageStr: (NSString *)message {
    
    
    if(![received isEqualToString: expected]){
        
    
      NSString* err=@"";
                err =[err stringByAppendingString:@"assert equal failed,expected:"];
                err=[err stringByAppendingString:expected];
                err=[err stringByAppendingString:@";but received:"];
                err=[err stringByAppendingString:received];
    
        if(message==NULL){
            message=@"Expected assert Equal";
        }
        
        NSException* myException=[NSException exceptionWithName:@"assertEqualException" reason: message userInfo:nil];
        
		
        
      @throw myException; 
	  return NO;
            
            
        
    }else{
		return YES;
           
    }
    
    }
    
    
+(BOOL)assertTrue:(BOOL )expression messageStr: (NSString *)message {
    
    NSString* err=@"";
    
    err =[err stringByAppendingString:@"expression failed:"];
    err=[err stringByAppendingString:expression];
    if(message==NULL){
        message=@"assert failed";
    }
    
    NSException* myException=[NSException exceptionWithName:@"assertTrueException" reason: message userInfo:nil];
    
     
    if(!expression){
       [TBTestLog TBRun:@"assert failed"];
       @throw myException;
		return NO;
		
		
    }else{
	    return YES;	
	}
    
    
}

+(BOOL)assertFalse:(BOOL )expression messageStr: (NSString *)message {
    [TBAssert assertTrue: (!expression) messageStr: message];
}

+(BOOL)assertNotNull:(TBElement *)object messageStr: (NSString *)message {
    NSString* err=@"";
    err =[err stringByAppendingString:@"element don't present on page:"];
    if(message==NULL){
        message=@"Expected not nil object";
    }
    
    NSException* myException=[NSException exceptionWithName:@"assertNotNULLException" reason: message userInfo:nil];    
    if(object==NULL){
        
        @throw myException;
		return NO;
        
    }else{
		return YES;
	}
}

+(BOOL)assertContainText:(NSString*) valuePage valueVerify: (NSString *) valueVerify messageStr: (NSString *)message {
    
    NSString* err=@"";
    err =[err stringByAppendingString:@"the element doesn't contain the text:"];
    err=[err stringByAppendingString:valueVerify];
    if(message==NULL){
        message=@"Expected text not present";
    }
    
    NSException* myException=[NSException exceptionWithName:@"assertContainTextException" reason: message userInfo:nil];      
    NSRange range=[valuePage rangeOfString: valueVerify];
    if(range.length==0){
        
        @throw myException;
		return NO;
        
    }else {
	    return YES;
	}

    
}



@end
