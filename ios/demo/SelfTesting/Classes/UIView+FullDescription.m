//
//  UIView+FullDescription.m
//  SelfTesting
//
//  Created by Matt Gallagher on 9/10/08.
//  Copyright 2008 Matt Gallagher. All rights reserved.
//
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "UIView+FullDescription.h"

@implementation UIView (FullDescription)

//
// fullDescription
//
// Builds a tree of data about all the views starting at this view
// and traversing all subviews. Data includes:
//	- className (name of the subclass of UIView)
//	- address (address in memory)
//	- tag
//	- text (if any)
//	- title (if any)
//	- subviews (recursive structures)
//
- (NSMutableDictionary *)fullDescription
{
	NSDictionary *frame =
		[NSDictionary dictionaryWithObjectsAndKeys:
			[NSNumber numberWithFloat:self.frame.origin.x], @"x",
			[NSNumber numberWithFloat:self.frame.origin.y], @"y",
			[NSNumber numberWithFloat:self.frame.size.width], @"width",
			[NSNumber numberWithFloat:self.frame.size.height], @"height",
		nil];
	NSMutableDictionary *description =
		[NSMutableDictionary dictionaryWithObjectsAndKeys:
			[NSNumber numberWithInteger:(NSInteger)self], @"address",
			NSStringFromClass([self class]), @"className",
			frame, @"frame",
			[NSNumber numberWithInteger:[self tag]], @"tag",
			[self valueForKeyPath:@"subviews.fullDescription"], @"subviews",
		nil];
	
	if ([self respondsToSelector:@selector(text)])
	{
		[description setValue:[self performSelector:@selector(text)] forKey:@"text"];
	}
	if ([self respondsToSelector:@selector(title)])
	{
		[description setValue:[self performSelector:@selector(title)] forKey:@"title"];
	}
	if ([self respondsToSelector:@selector(currentTitle)])
	{
		[description setValue:[self performSelector:@selector(currentTitle)] forKey:@"currentTitle"];
	}
	
	return description;
}

@end
