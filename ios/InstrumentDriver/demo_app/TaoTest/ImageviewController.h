//
//  ImageviewController.h
//  TaoTest
//
//  Created by ziyu on 12-1-11.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ImageviewController : UIViewController

@property (nonatomic,retain) IBOutlet UIImageView *imageView;
@property (nonatomic ,retain) IBOutlet UISlider *slider;
@property (nonatomic,retain) IBOutlet UISwitch *myswitch;

- (IBAction)sliderAction:(id)sender;
-(IBAction)switchChange:(id)sender;

@end
