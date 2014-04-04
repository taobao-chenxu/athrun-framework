//
//  PickersViewController.m
//  TaoTest
//
//  Created by ziyu on 12-1-10.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "PickersViewController.h"

@implementation PickersViewController

@synthesize date;
@synthesize pickerViewArray;
@synthesize label;
@synthesize datepicker;
@synthesize picker;

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
    self.title =@"Pickers Test";    
    
	self.pickerViewArray = [NSArray arrayWithObjects:@"北京", @"上海", @"杭州",@"深圳",nil];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    /*[self.date release];
    self.date=nil;
    [self.pickerViewArray release];
    self.pickerViewArray=nil;
    [self.label release];
    self.label=nil;
    [self.datepicker release];
    self.datepicker=nil;
    [self.picker release];
    self.picker=nil;*/
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

//get the pickerdate's value.
-(void)theDate:(id)sender
{
    NSDateFormatter * formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-mm-dd"];
    self.label.text = [formatter stringFromDate:self.datepicker.date];
    //[formatter release];
}

//fill the data
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
	return [pickerViewArray count];
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 2;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
	NSString *returnStr = @"";
	
	// note: custom picker doesn't care about titles, it uses custom views
	if (pickerView == self.picker)
	{
		if (component == 0)
		{
            returnStr = [[NSNumber numberWithInt:row] stringValue];
		}
		else
		{
            returnStr = [pickerViewArray objectAtIndex:row];
		}
	}	
    //[returnStr autorelease];
	return returnStr;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
	if (pickerView == self.picker)	
	{
		// get the selected value and display in label.
		self.label.text = [NSString stringWithFormat:@"%d - %@",
                           [pickerView selectedRowInComponent:0],                           
                           [pickerViewArray objectAtIndex:[pickerView selectedRowInComponent:1]]];
	}
}

@end
