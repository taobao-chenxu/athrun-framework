//
//  AlertViewController.m
//  TaoTest
//
//  Created by ziyu on 12-1-11.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "DailogViewController.h"


@implementation DailogViewController

@synthesize label;
@synthesize mysegmented;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
   
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [self.label release];
    [self.mysegmented release];
    self.label = nil;  
    self.mysegmented = nil;
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)showAlertDailog
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"AlertDailog" message:@"This is a Alert dailog" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
    [alert show];	 
   // [alert release];
}

- (void)showConfirmDailog
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"ConfirmDailog" message:@"This is a ConfirmDailog" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
    [alert show]; 
    //[alert release];
}

- (void)showOtherDailog
{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"CustomDailog" message:@"This is a Custom Dailog" delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"IOS", @"Android", nil];
    [alert show];
    //[alert release];
}    

-(void)segmentedValueChange:(id)sender
{       
    switch (self.mysegmented.selectedSegmentIndex)
    {
        case 0:
        {
            [self showAlertDailog];
            break;
        }
        case 1:
        {
            [self showConfirmDailog];
            break;
        }
        default:
        {
            [self showOtherDailog];
            break;
        }
    }
}

//Occur when click the dailog's someone button.
-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{    
    self.label.text = alertView.title;
}

@end