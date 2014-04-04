//
//  MenuViewController.h
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

#import "PageViewController.h"

@interface MenuViewController : PageViewController <UITableViewDataSource, UITableViewDelegate>
{
	UITableView *tableView;
}

@property (nonatomic, retain) IBOutlet UITableView *tableView;

- (UITableViewStyle)tableViewStyle;

@end
