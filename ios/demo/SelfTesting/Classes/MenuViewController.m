//
//  MenuViewController.m
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

#import "MenuViewController.h"
#import "SelfTestingAppDelegate.h"


@implementation MenuViewController

@synthesize tableView;

//
// initWithMenuStructure:pageName:
//
// Shows a basic text menu for the page specified by the key "newPageName"
// in the dictionary "newMenuStructure":
//
//	- newMenuStructure (the dictionary containing page structure information)
//	- newPageName (the key for the page in newMenuStructure)
//
- (id)initWithNavigationStructure:(NSDictionary *)newNavigationStructure pageName:(NSString *)newPageName
{
	self = [super init];
	if (self != nil)
	{
		NSDictionary *pageStructure = [newNavigationStructure objectForKey:newPageName];

		navigationStructure = [newNavigationStructure retain];
		pageName = [newPageName retain];
		self.title = [pageStructure objectForKey:@"pageTitle"];
	}
	return self;
}


//
// numberOfSectionsInTableView
//
// Single section tables only.
//
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


//
// tableView:numberOfRowsInSection:
//
// Number of rows comes from the menuItems count.
//
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	NSDictionary *pageStructure = [navigationStructure objectForKey:pageName];

    return [[pageStructure objectForKey:@"menuItems"] count];
}


//
// tableView:cellForRowAtIndexPath:
//
// Cell is configured with the corresponding childMenuItem's pageName. The
// accessory is only shown if the child is a menu itself (and not a dead-end
// item).
//
- (UITableViewCell *)tableView:(UITableView *)tableViewIn cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell =
		[tableViewIn
			dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil)
	{
        cell =
			[[[UITableViewCell alloc]
				initWithFrame:CGRectZero
				reuseIdentifier:CellIdentifier]
			autorelease];
    }
    
	NSDictionary *pageStructure = [navigationStructure objectForKey:pageName];

	cell.text =
		[[[pageStructure objectForKey:@"menuItems"]
			objectAtIndex:indexPath.row]
				objectForKey:@"menuLabel"];
	
	if ([[[pageStructure objectForKey:@"menuItems"]
			objectAtIndex:indexPath.row]
				objectForKey:@"linkedPageName"])
	{
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
	else
	{
		cell.accessoryType = UITableViewCellAccessoryNone;
	}
	
    return cell;
}


//
// tableView:didSelectRowAtIndexPath:
//
// Spawns a hierarchic descendant using the childMenuItem as the new
// description.
//
- (void)tableView:(UITableView *)tableViewIn didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSDictionary *pageStructure = [navigationStructure objectForKey:pageName];

	if ([[pageStructure objectForKey:@"menuItems"]
			objectAtIndex:indexPath.row] != nil &&
		[[[pageStructure objectForKey:@"menuItems"]
			objectAtIndex:indexPath.row]
				objectForKey:@"linkedPageName"])
	{
		NSString *linkedPageName =
			[[[pageStructure objectForKey:@"menuItems"]
				objectAtIndex:indexPath.row]
					objectForKey:@"linkedPageName"];
			
		Class controllerClass =
			[PageViewController
				controllerClassForPageName:linkedPageName
				inStructure:navigationStructure];
		
		PageViewController *pageViewController =
			[[[controllerClass alloc]
				initWithNavigationStructure:navigationStructure
				pageName:linkedPageName]
			autorelease];
		[self.navigationController pushViewController:pageViewController animated:YES];
		[tableViewIn deselectRowAtIndexPath:indexPath animated:YES];
	}
}

//
// loadView
//
// If no nib name is specified and we don't otherwise have a table view
// configure, create one.
//
- (void)loadView
{
	if (self.nibName)
	{
		[super loadView];
		return;
	}
	
	if (!self.tableView)
	{
		UITableView *newTableView =
			[[[UITableView alloc]
				initWithFrame:CGRectZero
				style:[self tableViewStyle]]
			autorelease];
		self.view = newTableView;
		self.tableView = newTableView;
		newTableView.delegate = self;
		newTableView.dataSource = self;
	}
	else
	{
		self.view = self.tableView;
	}
}

//
// tableViewStyle
//
// An overrideable method to set the UITableViewStyle for any table created
// in the -loadView method.
//
- (UITableViewStyle)tableViewStyle
{
	return UITableViewStyleGrouped;
}

//
// dealloc
//
// Releases instance memory.
//
- (void)dealloc
{
	[tableView release];
	[super dealloc];
}


@end

