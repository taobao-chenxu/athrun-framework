//
//  TBObject.h
//  AppFramework
//
//  Created by nan fei on 6/19/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  complete search element
//
//  add by nan fei
//

#import <Foundation/Foundation.h>


@interface TBElement : NSObject {
	
	NSDictionary *pages;    //控件树
	NSInteger boolNumber;   //计数同属性控件的序号
	NSInteger headNumber;   //计数控件层级

}

-(UIView *)find_element:(NSString *)pageName elementName:(NSString *)element;

-(UIView *)find:(UIView *)vs propertie:(NSDictionary *)properties;

-(UIView *)location:(UIView *)vs;

-(UIView *)find_table:(UIView *)vs;

-(UIView *)find_alert:(UIView *)vs propertie:(NSDictionary *)properties;

-(UIView *)find_path:(UIWindow *)vs propertie:(NSDictionary *)properties;

-(UIView *)find_v:(UIView *)vs;

@end
