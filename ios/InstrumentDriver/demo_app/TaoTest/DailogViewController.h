//
//  AlertViewController.h
//  TaoTest
//
//  Created by ziyu on 12-1-11.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DailogViewController : UIViewController<UIAlertViewDelegate>

@property (nonatomic ,retain) IBOutlet UILabel *label;
@property (nonatomic,retain) IBOutlet UISegmentedControl *mysegmented;


-(IBAction)segmentedValueChange:(id)sender;

@end
