//
//  TouchSynthesis.h
//  SelfTesting
//
//  Created by Matt Gallagher on 23/11/08.
//  Copyright 2008 Matt Gallagher. All rights reserved.
//
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import <UIKit/UIKit.h>

//
// UITouch (Synthesize)
//
// Category to allow creation and modification of UITouch objects.
//
@interface UITouch (Synthesize)

- (id)initInView:(UIView *)view;
- (void)setPhase:(UITouchPhase)phase;
- (void)setLocationInWindow:(CGPoint)location;

@end

//
// UIEvent (Synthesize)
//
// A category to allow creation of a touch event.
//
@interface UIEvent (Synthesize)

- (id)initWithTouch:(UITouch *)touch;

@end
