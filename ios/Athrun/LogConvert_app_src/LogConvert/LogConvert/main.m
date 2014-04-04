//
//  main.m
//  LogConvert
//
//  Created by  on 12-3-6.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import <stdio.h>
#import <Foundation/Foundation.h>
//#import <NSXMLDocument.h>

int main(int argc, char *argv[])
{
    
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	NSString *testcase[100][2];
	NSString *errorinfo[100];
    NSString *time[100];
	NSString *errorstr=NULL;
	NSString *tag = NULL;
    
    NSFileManager *manager = [NSFileManager defaultManager];
    NSString *logPath = @"/Athrun/Log/";
    
    NSString *home = [logPath stringByExpandingTildeInPath];
    
    NSLog(@"LogPath: %@",home);
    
    NSDirectoryEnumerator *direnum = [manager enumeratorAtPath:home];
    NSMutableArray *files = [NSMutableArray arrayWithCapacity:5];
    NSString *fileName;
    
    while (fileName=[direnum nextObject]) {
        
        if( [[fileName pathExtension] isEqualToString:@"plist"]){
            [files addObject:fileName];            
        }
    }
    
    int i = 0;
    NSEnumerator *fileenum;
    fileenum = [files objectEnumerator];
    while (fileName =[fileenum nextObject]) {
        
        fileName  = [NSString stringWithFormat: @"%@%@",logPath ,fileName];
        
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithContentsOfFile:fileName];
        
        NSArray *logSetp = [dict objectForKey:@"All Samples"];
        
        NSDictionary *caseInfor = [logSetp objectAtIndex:0];
        
        NSString *caseName = [caseInfor objectForKey:@"Message"];
        testcase[i][0] =caseName;  
        NSString *logType = [caseInfor objectForKey:@"LogType"];
        
        NSDate *beginTime = [caseInfor objectForKey:@"Timestamp"];     
        
        if([logType isEqualToString:@"Pass"]){
            testcase[i][1] =@"Pass";         
            errorinfo[i] = [[logSetp lastObject] objectForKey:@"Message"];
        }else{
            
            NSString *message =@"" ;
            int caseStepCount=0;
            NSEnumerator *itemEnum;
            itemEnum = [logSetp objectEnumerator];    
            NSDictionary *item ;
            while (item =[itemEnum nextObject]) {
                
                caseStepCount++;
                message = [NSString stringWithFormat: @"%@[caseStep %d :",message,caseStepCount];
                
                NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
                NSDate *date = [item objectForKey:@"Timestamp"];     
                NSString *dateStr = [dateFormatter stringFromDate:date];
                message = [NSString stringWithFormat: @"%@ %@ %@",message, @"DateTime" ,dateStr];              
                NSDate *LogType = [item objectForKey:@"LogType"];                
                message = [NSString stringWithFormat: @"%@ %@ %@",message, @"LogType" ,LogType];              
                NSDate *msg = [item objectForKey:@"Message"];               
                message = [NSString stringWithFormat: @"%@ %@ %@] ",message, @"Message" ,msg];    
            }
            testcase[i][1] =@"Fail";
            errorinfo[i] = message;
        }
        i++;
    }
    
    direnum= [manager enumeratorAtPath:home];
    while (fileName=[direnum nextObject]) {
        NSLog(@"fileName: %@",fileName);
        fileName  = [NSString stringWithFormat: @"%@%@",logPath ,fileName];
        
        if( ![fileName isEqualToString:@"log.xml"]){
            [manager removeFileAtPath:fileName handler:nil];
        }
    }
    
	//xml creat
	NSXMLElement *testsuits =[[NSXMLElement alloc] initWithName:@"testsuits"];
	
	NSXMLElement *testsuit = [[NSXMLElement alloc] initWithName:@"testsuite"];
	[testsuits addChild:testsuit];
	
	NSXMLElement *properties = [[NSXMLElement alloc] initWithName:@"properties"];
	[testsuit addChild:properties];
	[properties release];
	
	//add testcase node by testcase number
	int j = 0;
	int failNumber = 0;
	int testNumber = 0;
	//NSString *checkStr = testcase[j][0];
	while (j<i)
	{
		NSXMLElement *testcaseNode = [[NSXMLElement alloc] initWithName:@"testcase"];
		[testcaseNode addAttribute:[NSXMLNode attributeWithName:@"name" stringValue:testcase[j][0]]];
		
	    [testsuit addChild:testcaseNode];
		
		if ([testcase[j][1] isEqualToString:@"Fail"])
		{
			failNumber = failNumber+1;
			
			NSXMLElement *failNode = [[NSXMLElement alloc] initWithName:@"failure"];
			[failNode addAttribute:[NSXMLNode attributeWithName:@"type" stringValue:@"FAILURES!!!"]];
			[failNode addAttribute:[NSXMLNode attributeWithName:@"message" stringValue:@"info"]];
			[failNode setStringValue:errorinfo[j]];
			[testcaseNode addChild:failNode];
            [failNode release];
		}
        
		[testcaseNode release];			 
		j = j+1;        
		testNumber = j;
		//checkStr = testcase[j][0];
	}
	
	[testsuit addAttribute:[NSXMLNode attributeWithName:@"errors" stringValue:@"0"]];
	[testsuit addAttribute:[NSXMLNode attributeWithName:@"failures" stringValue:[NSString stringWithFormat:@"%d",failNumber]]];
	[testsuit addAttribute:[NSXMLNode attributeWithName:@"name" stringValue:@"iphone auto test"]];
	[testsuit addAttribute:[NSXMLNode attributeWithName:@"tests" stringValue:[NSString stringWithFormat:@"%d",testNumber]]];
	NSXMLElement *systemO = [[NSXMLElement alloc] initWithName:@"system-out"];
	[testsuit addChild:systemO];
	[systemO release];
	
	NSXMLElement *systemE = [[NSXMLElement alloc] initWithName:@"system-err"];
	[testsuit addChild:systemE];
	[systemE release];
	
	[testsuit release];
	
	NSXMLDocument *xmlre = [NSXMLDocument documentWithRootElement:testsuits];
	[testsuits release];
	
	//NSLog(@"xml Document is \n%@",xmlre);
	
	NSData *xmlData = [xmlre XMLData];
    
    NSString *xmlFile = [NSString stringWithFormat:@"%@%@",logPath,@"log.xml"];
	[xmlData writeToFile:xmlFile atomically:FALSE];
	[pool drain];
	return 0;
}

/*
int main(int argc, char *argv[])
{
    //return NSApplicationMain(argc, (const char **)argv);

    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc ] init ];
    
    NSString *logPath =@"/Users/jerryding/Desktop/AppTest";
    NSFileManager *manager = [NSFileManager defaultManager];
    NSString *home = [logPath stringByExpandingTildeInPath];
    
    NSLog(@"home: %@", home);
    
    NSDirectoryEnumerator *direnum = [manager enumeratorAtPath:home];
    
    NSMutableArray *files = [NSMutableArray arrayWithCapacity:10];
    NSString *filename ;
    while (filename = [direnum nextObject]) {
        if ([[filename pathExtension] isEqualTo:@"jpg"]) {
            [files addObject: filename];
        }
    }
    
    NSEnumerator *fileenum;
    fileenum = [files objectEnumerator];
    
    while (filename = [fileenum nextObject]) {
        NSLog(@"%@", filename);
    }
    

    NSString *path = @"/Users/jerryding/Desktop/AppTest/AutomationResults.plist";
    
    NSMutableDictionary *dict = [[NSMutableDictionary alloc]  initWithContentsOfFile:path ];
    
    NSArray *logStep = [dict valueForKey:@"All Samples"];
    
    NSDictionary *item1 = [logStep objectAtIndex:0];
    
    NSString *type = [item1 objectForKey:@"Type"];

    if([type isEqualToString:@"4"]){
        NSString *caseName = [item1 objectForKey:@"Message"];

        NSUInteger itemMessage = [logStep count] -1;
        NSDictionary *item2 = [logStep objectAtIndex:itemMessage];
    }
    [pool drain];
    
}

*/