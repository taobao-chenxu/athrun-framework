package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class OptionItemAction extends TextViewAction {
	public static final String OPTION_ITEM = "optionitem";
	protected String optionText;
	private static final String OPTION_TEXT = "text";

	public OptionItemAction(Map<String, String> action, AST ast) {
		super(action, ast);
		this.optionText = this.action.get(OPTION_TEXT);
	}

	@Override
	protected VariableDeclarationStatement getFindStatement() {
		return createFindStatement(
				getMethodInvocation(this.optionText), this.viewName,
				TEXTVIEW_ELEMENT);
	}

	@Override
	protected ExpressionStatement getOperationStatement() {
		MethodInvocation operation = ast.newMethodInvocation();
		operation.setExpression(ast.newSimpleName(this.viewName));
		operation
				.setName(ast.newSimpleName(viewOperation.get(CLICK)));

		return ast.newExpressionStatement(operation);
	}
}
