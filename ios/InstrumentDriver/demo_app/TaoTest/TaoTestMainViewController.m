//
//  TaoTestMasterViewController.m
//  TaoTest
//
//  Created by ziyu on 11-12-30.
//  Copyright (c) 2011年 __MyCompanyName__. All rights reserved.
//

#import "TaoTestMainViewController.h"
#import "HelloTaoViewController.h"
#import "PickersViewController.h"
#import "ImageviewController.h"
#import "DailogViewController.h"

static NSString *kCellIdentifier = @"MyIdentifier";
static NSString *kTitleKey = @"title";
static NSString *kExplainKey = @"explanation";
static NSString *kViewControllerKey = @"viewController";

@implementation TaoTestMainViewController

@synthesize menuList;


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    // construct the array of page descriptions we will use (each description is a dictionary)
	//
	self.menuList = [NSMutableArray array];
        
	HelloTaoViewController *helloTaoViewController = [[HelloTaoViewController alloc]
                                                    initWithNibName:@"HelloTaoViewController" bundle:nil];

	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 1", kTitleKey,
                              @"test textFeid and button", kExplainKey,
                              helloTaoViewController, kViewControllerKey,
							  nil]];
    [helloTaoViewController release];    
     
        
	PickersViewController  *pickersViewController = [[PickersViewController alloc]
                                                      initWithNibName:@"PickersViewController" bundle:nil];
	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 2", kTitleKey,
                              @"test Pickers", kExplainKey,
                              pickersViewController, kViewControllerKey,
							  nil]];
    [pickersViewController release];
    
    ImageviewController  *imageViewController = [[ImageviewController alloc]
                                                     initWithNibName:@"imageViewController" bundle:nil];
	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 3", kTitleKey,
                              @"test imageView", kExplainKey,
                              imageViewController, kViewControllerKey,
							  nil]];
    [imageViewController release];
    
    DailogViewController  *dailogViewController = [[DailogViewController alloc]
                                                   initWithNibName:@"DailogViewController" bundle:nil];
	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 4", kTitleKey,
                              @"test dailogView", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]]; 
    
	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 5", kTitleKey,
                              @"test table row 5", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]]; 
    
	[self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 6", kTitleKey,
                              @"test table row 6", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 7", kTitleKey,
                              @"test table row 7", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];

    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 8", kTitleKey,
                              @"test table row 8", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 9", kTitleKey,
                              @"test table row 9", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 10", kTitleKey,
                              @"test table row 10", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 11", kTitleKey,
                              @"test table row 11", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 12", kTitleKey,
                              @"test table row 12", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 13", kTitleKey,
                              @"test table row 13", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [self.menuList addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                              @"Demo 14", kTitleKey,
                              @"test table row 14", kExplainKey,
                              dailogViewController, kViewControllerKey,
							  nil]];
    [dailogViewController release];
    
    // create a custom navigation bar button and set it to always say "Back"
	UIBarButtonItem *temporaryBarButtonItem = [[UIBarButtonItem alloc] init];    
	temporaryBarButtonItem.title = @"Back";
	self.navigationItem.backBarButtonItem = temporaryBarButtonItem;	     
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [self.menuList release];
    self.menuList = nil;
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}


-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return  [self.menuList count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellIdentifier];
	if (cell == nil)
	{
		cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:kCellIdentifier] ;
		cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	}
	
	cell.textLabel.text = [[self.menuList objectAtIndex:indexPath.row] objectForKey:kTitleKey];
    cell.detailTextLabel.text = [[self.menuList objectAtIndex:indexPath.row] objectForKey:kExplainKey];
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.row<4)
    {
        UIViewController *targetViewController = [[self.menuList objectAtIndex: indexPath.row] objectForKey:kViewControllerKey];
        [[self navigationController] pushViewController:targetViewController animated:YES];
    }
}

@end
