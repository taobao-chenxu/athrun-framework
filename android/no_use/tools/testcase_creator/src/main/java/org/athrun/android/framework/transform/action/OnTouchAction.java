package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class OnTouchAction extends DeviceAction {
	private static final String TAP = "tap";
	
	protected String x;
	protected String y;

	public OnTouchAction(Map<String, String> action, AST ast) {
		super(action, ast);
		String[] xy = this.action.get("start").split(":");
		this.x = xy[0];
		this.y = xy[1];
	}

	@Override
	public void toJavaCode(Block methodBlock) {
		createComment(methodBlock);
		createWaitIfNeeded(methodBlock);
		createTap(methodBlock);
		createBlank(methodBlock);
	}
	
	@SuppressWarnings("unchecked")
	private void createTap(Block methodBlock) {
		MethodInvocation tap = ast.newMethodInvocation();

		tap.setExpression(createGetDevice());
		tap.setName(ast.newSimpleName(TAP));
		
		NumberLiteral xLiteral = ast.newNumberLiteral(this.x);
		NumberLiteral yLiteral = ast.newNumberLiteral(this.y);

		tap.arguments().add(xLiteral);
		tap.arguments().add(yLiteral);

		methodBlock.statements().add(ast.newExpressionStatement(tap));
	}
}
