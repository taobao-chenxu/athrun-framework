package org.athrun.android.framework.transform.taobaoview;

import java.util.Map;

import org.athrun.android.framework.transform.action.ViewAction;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class TaobaoSkuViewAction extends ViewAction {
	private static final String FIND_SKU_BY_TEXT = "findSkuOptionByText";
	protected String skuText;
	protected static final String SKU_TEXT = "viewtext";
	
	private static final String SKU_OPTION_ELEMENT = "SkuOptionElement";
	
	public static final String SKU_CLICK = "skuclick";
	protected static final String SKU_CLICK_VALUE = "doClick";
	
	static {
		viewOperation.put(SKU_CLICK, SKU_CLICK_VALUE);
	}

	public TaobaoSkuViewAction(Map<String, String> action, AST ast) {
		super(action, ast);
		this.skuText = this.action.get(SKU_TEXT);
	}

	@Override
	protected VariableDeclarationStatement getFindStatement() {
		return createFindStatement(getMethodInvocation(), this.viewName, SKU_OPTION_ELEMENT);
	}

	@SuppressWarnings("unchecked")
	protected MethodInvocation getMethodInvocation() {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setName(ast.newSimpleName(FIND_SKU_BY_TEXT));
		StringLiteral textParam = ast.newStringLiteral();
		textParam.setLiteralValue(this.skuText);
		methodInvocation.arguments().add(textParam);
		return methodInvocation;
	}
}
