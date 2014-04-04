//
//  main.m
//  TaoTest
//
//  Created by ziyu on 11-12-30.
//  Copyright (c) 2011年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "TaoTestAppDelegate.h"

int main(int argc, char *argv[])
{   
    /*
    @autoreleasepool {
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([TaoTestAppDelegate class]));
    } */   
    
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    int retVal = UIApplicationMain(argc, argv, nil, NSStringFromClass([TaoTestAppDelegate class]));
    UIApplicationMain(argc, argv, nil, nil);
    [pool release];
    return retVal;    
}
