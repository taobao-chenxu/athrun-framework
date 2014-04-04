//
//  HelloTaoViewController.h
//  TaoTest
//
//  Created by ziyu on 12-1-10.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HelloTaoViewController : UIViewController<UITextFieldDelegate>
{
    NSString *userName;
}

@property (nonatomic,copy) NSString *userName;

@property (nonatomic, retain) IBOutlet UILabel *label;

@property (nonatomic, retain) IBOutlet UITextField *textField;

- (IBAction)changeGreeting:(id)sender;
@end
