package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class AbsListViewAction extends ViewGroupAction {

	public AbsListViewAction(Map<String, String> action, AST ast) {
		super(action, ast);
	}
	
	@Override
	protected VariableDeclarationStatement getFindParentStatement() {
		return super.getFindParentStatement(ABSLISTVIEW_ELEMENT);
	}

	@Override
	protected MethodInvocation getMethodInvocation() {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		createGetByIndexInvocation(methodInvocation);
		return methodInvocation;
	}
}
