//
//  SelfTestingAppDelegate.m
//  SelfTesting
//
//  Created by Matt Gallagher on 9/10/08.
//  Copyright Matt Gallagher 2008. All rights reserved.
//
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "SelfTestingAppDelegate.h"
#import "PageViewController.h"

#ifdef SCRIPT_DRIVEN_TEST_MODE_ENABLED
#import "ScriptRunner.h"
#endif

@implementation SelfTestingAppDelegate

//
// applicationDidFinishLaunching:
//
// Finish loading (since the iPhone NIB file doesn't it it for us) and
// optionally start the testing script.
//
- (void)applicationDidFinishLaunching:(UIApplication *)application
{
	//
	// Create the menu data description dictionary
	//
	NSDictionary *navigationStructure =
		[NSDictionary dictionaryWithContentsOfFile:
			[[NSBundle mainBundle] pathForResource:@"MenuStructure" ofType:@"plist"]];
	
	//
	// Create the menu view controller from the description
	//
	NSString *initialPageName = @"topLevelPage";
	Class controllerClass =
		[PageViewController
			controllerClassForPageName:initialPageName
			inStructure:navigationStructure];
	PageViewController *pageViewController =
		[[[controllerClass alloc]
			initWithNavigationStructure:navigationStructure
			pageName:initialPageName]
		autorelease];
	[navigationController pushViewController:pageViewController animated:NO];
	
	//
	// Configure and show the window
	//
	[window addSubview:[navigationController view]];
	[window makeKeyAndVisible];

#ifdef SCRIPT_DRIVEN_TEST_MODE_ENABLED
	[[[ScriptRunner alloc] init] autorelease];
#endif
}

@end
