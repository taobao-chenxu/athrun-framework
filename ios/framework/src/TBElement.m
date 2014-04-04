//
//  TBElement.m
//  AppFramework
//
//  Created by nan fei on 6/19/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  add by nan fei
//

#import "TBElement.h"
#import "TBTestLog.h"


@implementation TBElement

-(id)init
{
	pages =[NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Objects" ofType:@"plist"]];	
}

//从对象树中拿到控件属性，搜索控件
-(UIView *)find_element:(NSString *)pageName elementName:(NSString *)element
{
	//加载整个对象树
	pages =[NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Objects" ofType:@"plist"]];
	//初始搜索控件序号
	boolNumber = 0;
	headNumber = 0;
	
	//获取主窗口
	UIWindow *window = [[UIApplication sharedApplication].delegate window];
	UIView *v,*fv;
	fv = NULL;
	
	//读取控件的搜索属性
	NSDictionary *page = [pages objectForKey:pageName];
	NSDictionary *object = [page objectForKey:element];
	
	NSString *type = [object objectForKey:@"type"];
	NSDictionary *selector = [object objectForKey:@"selector"];
	
	if ([selector objectForKey:@"path"]) 
	{				
		fv = [self find_path:window propertie:selector];
			
		//如果控件未找到，给出提示日志
	    if (fv == NULL) 
	    {
		    NSString *nilElement = [[NSString alloc] initWithFormat:@"this element not find:%@\n",element];
		    [TBTestLog TBError:nilElement];
	    }

		return fv;
	}
	
	//搜索table控件
	if ([type isEqualToString:@"Table"])
	{
		
	    //遍历主窗口下的view，搜索控件
	    for (v in [window subviews])
			
	    {
		    fv = [self find_table:v];
				
		    //返回非空，搜索到控件，终止循环
		    if (fv != NULL)
		    {
			    break;
		    }
	    }
	
	    //如果控件未找到，给出提示日志
	    if (fv == NULL) 
	    {
		    NSString *nilElement = [[NSString alloc] initWithFormat:@"this element not find:%@\n",element];
		    [TBTestLog TBError:nilElement];
	    }
		else if([selector objectForKey:@"index"])
		{
			NSInteger rowIndex = [[selector objectForKey:@"index"] intValue];
			
			NSInteger checkNumber = 0 ;
			UIView *loopV= NULL;
			for (loopV in [fv subviews])
			{
				if (checkNumber == rowIndex)
				{
					fv = loopV;
					break;
				}
				
				checkNumber = checkNumber + 1;
			}
			
			checkNumber = 0;
		}
	
	    return fv;
	}

				
	//搜索的是主窗口，直接返回
	if ([type isEqualToString:@"System"])
	{
		fv = window;
	    return fv;
	}
	
	//搜索弹出系统视图
	if ([type isEqualToString:@"Alert"])
	{
		UIWindow *w1 = [[UIApplication sharedApplication] windows];

		UIWindow *w = NULL;
		UIView *r = NULL;
		UIView *fr = NULL;
		
		for (w in w1)
		{
			
		    for (r in [w subviews])
			{
				fr = [self find_alert:r propertie:selector];
				
				if (fr != NULL)
				{					
					break;
				}
			}
			
		}
		
		//如果控件未找到，给出提示日志
		if (fr == NULL) 
		{
			NSString *nilElement = [[NSString alloc] initWithFormat:@"this element not find:%s\n",element];
			[TBTestLog TBError:nilElement];
		}
		
		return fr;
	}
	
	//遍历主窗口下的view，搜索控件
	for (v in [window subviews])
	{
		fv = [self find:v propertie:selector];
		
		//返回非空，搜索到控件，终止循环
		if (fv != NULL)
		{
			if ([type isEqualToString:@"TableCell"])
			{
				fv = [self location:fv];
			}
			break;
		}
	}
	
	//如果控件未找到，给出提示日志
	if (fv == NULL) 
	{
		NSString *nilElement = [[NSString alloc] initWithFormat:@"this element not find:%s\n",element];
		[TBTestLog TBError:nilElement];
	}
	
	return fv;
}

//对告警框的搜索
-(UIView *)find_alert:(UIView *)vs propertie:(NSDictionary *)properties
{
    //控件信息输出
	headNumber = headNumber + 1;
	[TBTestLog elementInfo:vs pathNumber:headNumber];
	
	
	if ([vs isKindOfClass:[UIAlertView class]] || [vs isKindOfClass:[UIActionSheet class]])
	{
		UIView *bv = NULL;
		NSInteger tagNumber = [[properties objectForKey:@"tag"] intValue];
		for (bv in [vs subviews]) 
		{
			if ([bv tag] == tagNumber)
			{
				vs = bv;
			}
			
		}
		
		headNumber = headNumber - 1;
		return vs;
	}
	
	
	UIView *rv = NULL;
	
	if ([vs.subviews count] != 0)
	{
		
		UIView *vv;
		for (vv in [vs subviews])
		{
			rv = [self find_alert:vv propertie:properties];
			if (rv != NULL)
			{
				headNumber = headNumber-1;
				return rv;
			}
		}		
	}
	
	headNumber = headNumber-1;
	return rv; 
}

//操作tablecell里控件时，定位tablecel坐标
-(UIView *)location:(UIView *)vs
{
	UIView *lv = [vs superview];
	
	while (!([lv isKindOfClass:[UITableViewCell class]] || [lv isKindOfClass:[UIWindow class]]))
	{

	    lv = [lv superview];
	}
	
	if ([lv isKindOfClass:[UIWindow class]])
	{
	    return NULL;
	}
	else
	{
	    return lv;
	}

}

//遍历当前视图层，搜索控件
-(UIView *)find:(UIView *)vs propertie:(NSDictionary *)properties
{
	//调式时，输出整个视图结构中每个视图信息	
	headNumber = headNumber + 1;
	[TBTestLog elementInfo:vs pathNumber:headNumber];	
	
	//初始返回视图信息
	UIView *rv=NULL;
	NSString *str=NULL;
	NSString *temp;
	
	//读取搜索selector的属性设置
    NSString *proText = [properties objectForKey:@"text"];
	NSString *proTag = [properties objectForKey:@"tag"];
	NSInteger proNumber = [[properties objectForKey:@"index"] intValue];
	
	//无index属性，默认设置为1
	if (proNumber == NULL)
	{
		proNumber = 1;
	}
	
	NSInteger boolTag = 0;
	NSInteger boolText = 0;
	if (proTag != NULL)
	{
		if ([proTag intValue] == [vs tag])
		{
			//找到控件，控件序号＋1，返回
		    boolNumber = boolNumber+1;
			if (boolNumber == proNumber)
			{
				rv = vs;
				headNumber = headNumber - 1;
			    return rv;
			}
		}
		
	}
	
	if (proText != NULL)
	{
		//非label视图，无text属性，不需要进行比对
	    if ([vs respondsToSelector:@selector(text)])
	    {
			str = [vs text];
		}
		
		//如果为空，赋予空字符串，避免匹配比对时异常
		if (str == NULL)
		{
			str = @"";
		}
		
		//对目标字符串匹配
		temp = [[NSString alloc] initWithFormat:str];
	    NSRange match = [temp rangeOfString:proText];
	    if (match.location != NSNotFound)
	    {
			//找到控件，控件序号＋1，返回
			boolNumber = boolNumber+1;
			if (boolNumber == proNumber)
			{
				rv = vs;
				headNumber = headNumber - 1;
		        return rv;
			}
	    }
	}
		
	//未匹配上，且包含子视图，继续遍历下层视图
	if ([vs.subviews count] != 0)
	{

		UIView *vv;
		for (vv in [vs subviews])
		{
            rv = [self find:vv propertie:properties];
			if (rv != NULL)
			{
				headNumber = headNumber - 1;
				return rv;
			}
		}		
	}
	
	headNumber = headNumber - 1;
	return rv;

}

//搜索table控件
-(UIView *)find_table:(UIView *)vs
{
	//调式时，输出整个视图结构中每个视图信息
	headNumber = headNumber + 1;	
	[TBTestLog elementInfo:vs pathNumber:headNumber];
	
	//初始返回视图信息
	UIView *rv=NULL;
	NSString *str=NULL;
	NSString *temp;	
	
	if ([vs isKindOfClass:[UITableView class]])
	{
		rv = vs;
		headNumber = headNumber - 1;
		return rv;
	}
	
	//未匹配上，且包含子视图，继续遍历下层视图
	if ([vs.subviews count] != 0)
	{
		
		UIView *vv;
		for (vv in [vs subviews])
		{
			rv = [self find_table:vv];
			if (rv != NULL)
			{
				headNumber = headNumber - 1;
				return rv;
			}
		}		
	}
	
	headNumber = headNumber-1;
	return rv;
	
}

//根据path路径查找控件
-(UIView *)find_path:(UIWindow *)ws propertie:(NSDictionary *)properties
{
	//计算树结构信息
	headNumber = headNumber + 1;
	
	//开始搜索控件
	NSString *path = [properties objectForKey:@"path"];
	NSArray *viewArray = [path componentsSeparatedByString:@">"];
	NSInteger loop;
	UIView *v,*rv;
	
	//获取窗口下的根视图
	NSString *rootPath = [viewArray objectAtIndex:0];
	NSArray *rootArray = [rootPath componentsSeparatedByString:@":"];
	NSInteger rootNumber;
	if ([rootArray count] == 2) 
	{
		NSString *rootNumberStr = [rootArray objectAtIndex:1];
		rootNumber = [rootNumberStr intValue];
	}
	else 
	{
		rootNumber = 1;
	}
	NSInteger rootloop = 1;
	for (v in [ws subviews])
	{
		[TBTestLog elementInfo:v pathNumber:headNumber];
		
		if (rootloop == rootNumber) 
		{
			rv = v;
			break;
		}
		
		rootloop = rootloop+1;
	}
	
	//获取path描述的控件
	for (loop=1; loop<[viewArray count]; loop++) 
	{
		//处理树结构输出信息
		headNumber = headNumber+1;
		
		NSString *subv = [viewArray objectAtIndex:loop]; 
		NSArray *subviewArray = [subv componentsSeparatedByString:@":"];
		NSInteger viewNumber;
		
		if ([subviewArray count] == 2)
		{
			NSString *viewNumberStr = [subviewArray objectAtIndex:1];
			viewNumber = [viewNumberStr intValue];
		}
		else
		{
			viewNumber = 1;
		}
		
		NSInteger viewloop = 1;
		for (v in [rv subviews])
		{
			//输出树结构信息
			[TBTestLog elementInfo:v pathNumber:headNumber];
			
			if (viewloop == viewNumber)
			{
				rv = v;
				break;
			}
			viewloop = viewloop+1;
		}
		
	}
	
	NSString *str1= [[rv description] cStringUsingEncoding:NSUTF8StringEncoding];
	NSString *temp1 = [[NSString alloc] initWithFormat:@"subviews :%s\n",str1];
	[TBTestLog debugLog:temp1];	
	return rv;
	
}

//备用遍历应用结构
-(UIView *)find_v:(UIView *)vs
{
	//调式时，输出整个视图结构中每个视图信息	
	headNumber = headNumber + 1;
	[TBTestLog elementInfo:vs pathNumber:headNumber];
	
	UIView *rv=NULL;
	if ([vs.subviews count] != 0)
	{
		
		UIView *vv;
		for (vv in [vs subviews])
		{
			rv = [self find_table:vv];
			if (rv != NULL)
			{
				headNumber = headNumber - 1;
				return rv;
			}
		}		
	}
	
	headNumber = headNumber-1;
	return rv;	
}

@end
