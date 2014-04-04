//
//  PickersViewController.h
//  TaoTest
//
//  Created by ziyu on 12-1-10.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PickersViewController : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource>
{
    NSDate *date;
}

@property (nonatomic,copy) NSDate *date;
@property (nonatomic,retain) NSArray *pickerViewArray;

@property (nonatomic,retain) IBOutlet UIDatePicker *datepicker;
@property (nonatomic,retain) IBOutlet UILabel *label;
 
@property (nonatomic, retain) IBOutlet UIPickerView *picker;

-(IBAction)theDate:(id)sender;

@end
