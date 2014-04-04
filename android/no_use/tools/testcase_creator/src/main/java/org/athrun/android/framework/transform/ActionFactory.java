package org.athrun.android.framework.transform;

import java.util.Map;

import org.athrun.android.framework.transform.action.AbsListViewAction;
import org.athrun.android.framework.transform.action.ActionType;
import org.athrun.android.framework.transform.action.DeviceAction;
import org.athrun.android.framework.transform.action.IAction;
import org.athrun.android.framework.transform.action.OnTouchAction;
import org.athrun.android.framework.transform.action.OptionItemAction;
import org.athrun.android.framework.transform.action.TextViewAction;
import org.athrun.android.framework.transform.action.UnknowAction;
import org.athrun.android.framework.transform.action.ViewAction;
import org.athrun.android.framework.transform.action.ViewGroupAction;
import org.athrun.android.framework.transform.taobaoview.TaobaoSkuViewAction;
import org.eclipse.jdt.core.dom.AST;

public class ActionFactory {
	private AST ast;
	private static ActionFactory instance;
	
	private ActionFactory(AST ast) {
		this.ast = ast;
	}
	
	public static ActionFactory getInstance(AST ast) {
		if (null == instance) {
			instance = new ActionFactory(ast);
		}
		
		return instance;
	}
	
	public IAction getAction(Map<String, String> action, ActionType type) {
		switch (type) {
		
		case DeviceAction:
            return new DeviceAction(action, this.ast);
            
		case ViewAction:
			return new ViewAction(action, this.ast);
			
		case TextViewAction:
			return new TextViewAction(action, this.ast);
			
		case ViewGroupAction:
			return new ViewGroupAction(action, this.ast);
			
		case AbsListViewAction:
			return new AbsListViewAction(action, this.ast);
			
		case OptionItemAction:
			return new OptionItemAction(action, this.ast);
			
		case TaobaoSkuViewAction:
			return new TaobaoSkuViewAction(action, this.ast);
			
		case UnkownAction:
			return new UnknowAction(action, this.ast);
			
		case OnTouchAction:
			return new OnTouchAction(action, this.ast);

		default:
			return null;
		}

	}
}
