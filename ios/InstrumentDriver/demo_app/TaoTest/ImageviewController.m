//
//  ImageviewController.m
//  TaoTest
//
//  Created by ziyu on 12-1-11.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "ImageviewController.h"

@implementation ImageviewController

@synthesize imageView;
@synthesize slider;
@synthesize myswitch;


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
    self.title =@"ImageViewTest";
    [super viewDidLoad];
    
    // set the imageView's data.
	self.imageView.animationImages = [NSArray arrayWithObjects:
                                      [UIImage imageNamed:@"scene1.jpg"],
                                      [UIImage imageNamed:@"scene2.jpg"],
                                      [UIImage imageNamed:@"scene3.jpg"],
                                      [UIImage imageNamed:@"scene4.jpg"],
                                      [UIImage imageNamed:@"scene5.jpg"],
									  nil];
	imageView.animationDuration = 8.0;
    [imageView startAnimating];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    /*[self.imageView release];
    self.imageView =nil; 
    [self.slider release];
    self.slider =nil;
    [self.myswitch release];
    self.myswitch = nil;   */
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (void)sliderAction:(id)sender
{    
	UISlider* durationSlider = sender;
	self.imageView.animationDuration = [durationSlider value];
	if (!self.imageView.isAnimating)
	   [self.imageView startAnimating];
    //[durationSlider release];
     
}

-(void)switchChange:(id)sender
{
    if([self.myswitch isOn])
    {
        [self.imageView startAnimating];
    }else
    {
        [self.imageView stopAnimating];
    }        
}

@end
