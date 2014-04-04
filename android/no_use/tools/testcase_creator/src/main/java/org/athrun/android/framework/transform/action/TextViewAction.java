package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class TextViewAction extends ViewAction {

	public TextViewAction(Map<String, String> action, AST ast) {
		super(action, ast);
	}

	@Override
	protected VariableDeclarationStatement getFindStatement() {
		if (action.containsKey(VIEW_ID) && action.get(VIEW_ID).startsWith("@")) {
			return createFindStatement(
					getMethodInvocation(this.action.get(VIEW_ID), TEXTVIEW_ELEMENT), this.viewName,
					TEXTVIEW_ELEMENT);
			
		} else {
			return createFindStatement(
					getMethodInvocation(this.action.get(VIEW_TEXT)), this.viewName,
					TEXTVIEW_ELEMENT);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ExpressionStatement getOperationStatement() {
		MethodInvocation operation = getOperationInvocation();

		if (this.actiontype.equalsIgnoreCase(TYPE)) {
			StringLiteral textParam = ast.newStringLiteral();
			textParam.setLiteralValue(this.action.get("inputtext"));
			operation.arguments().add(textParam);
		}

		return ast.newExpressionStatement(operation);
	}

}
