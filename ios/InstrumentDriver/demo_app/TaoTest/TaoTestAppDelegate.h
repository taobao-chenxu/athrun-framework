//
//  TaoTestAppDelegate.h
//  TaoTest
//
//  Created by ziyu on 11-12-30.
//  Copyright (c) 2011年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TaoTestAppDelegate : UIResponder <UIApplicationDelegate>{
    UIWindow *window;
	UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end
