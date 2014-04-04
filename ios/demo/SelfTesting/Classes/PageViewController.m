//
//  PageViewController.m
//  SelfTesting
//
//  Created by Matt Gallagher on 22/11/08.
//  Copyright 2008 Matt Gallagher. All rights reserved.
//
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "PageViewController.h"


@implementation PageViewController

//
// controllerClassForPageName:inStructure:
//
// Convenience method to return the proper class for allocating a view
// controller for a given page.
//
+ (Class)controllerClassForPageName:(NSString *)pageName inStructure:(NSDictionary *)navigationStructure
{
	NSDictionary *pageStructure = [navigationStructure objectForKey:pageName];
	Class controllerClass =
		NSClassFromString([pageStructure objectForKey:@"controllerClassName"]);
	return controllerClass;	
}

//
// initWithNavigationStructure:pageName:
//
// Shows a view for the page specified by the key "newPageName"
// in the dictionary "newNavigationStructure":
//
//	- newNavigationStructure (the dictionary containing the full set of page
//		structure information dictionaries)
//	- newPageName (the key for the page in newMenuStructure)
//
- (id)initWithNavigationStructure:(NSDictionary *)newNavigationStructure pageName:(NSString *)newPageName
{
	NSDictionary *pageStructure = [newNavigationStructure objectForKey:newPageName];
	
	self =
		[super
			initWithNibName:[pageStructure objectForKey:@"nibName"]
			bundle:nil];
	if (self != nil)
	{
		navigationStructure = [navigationStructure retain];
		pageName = [newPageName retain];
		self.title = [pageStructure objectForKey:@"pageTitle"];
	}
	return self;
}

@end
