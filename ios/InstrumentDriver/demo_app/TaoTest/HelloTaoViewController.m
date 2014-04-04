//
//  HelloTaoViewController.m
//  TaoTest
//
//  Created by ziyu on 12-1-10.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "HelloTaoViewController.h"

@implementation HelloTaoViewController

@synthesize userName;
@synthesize label;
@synthesize textField;

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
    self.title =@"TextFeild button交互";
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
   
    [self.userName release];
    self.userName   = nil;
    [self.label release];
    self.label      = nil;
    [self.textField release];
    self.textField  = nil;
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (IBAction)changeGreeting:(id)sender {
    //这里实现按钮点击的处理
    userName =self.textField.text;
    NSString *nameString = self.userName;
    if([nameString length]==0){
        nameString =@"World";
    }
    NSString *greeting =[[NSString alloc] initWithFormat:@"Hello,%@!" , nameString];
    self.label.text =greeting;   
    //[nameString release];
}


- (BOOL) textFieldShouldReturn:(UITextField *)theTextField{
    
    //if(theTextField == self.textField){
    [theTextField resignFirstResponder];
    //}   
    return YES;
}
@end
