package org.athrun.android.framework.transform.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;

public class ViewAction extends BaseAction {
	protected static final String FIND_BY_ID = "findElementById";
	protected static final String FIND_ELEMENT_BY_TEXT = "findElementByText";
	protected static final String FIND_ELEMENT_BY_INDEX = "findElementByIndex";

	protected static final String VIEW_ELEMENT = "ViewElement";
	protected static final String TEXTVIEW_ELEMENT = "TextViewElement";
	protected static final String VIEWGROUP_ELEMENT = "ViewGroupElement";
	protected static final String ABSLISTVIEW_ELEMENT = "AbsListViewElement";

	protected static final String CLICK = "click";
	protected static final String LONG_CLICK = "longclick";
	protected static final String TYPE = "type";
	public static final String ITEM_CLICK = "itemclick";
	public static final String ITEM_LONG_CLICK = "itemlongclick";

	protected static final String CLICK_VALUE = "doClick";
	protected static final String LONG_CLICK_VALUE = "doLongClick";
	protected static final String TYPE_VALUE = "setText";
	protected static final String ITEM_CLICK_VALUE = "doClick";
	protected static final String ITEM_LONG_CLICK_VALUE = "doLongClick";

	protected static final String VIEW_ID = "viewid";
	protected static final String VIEW_TEXT = "viewtext";
	protected static final String VIEW_INDEX = "viewindex";

	private static final int RANDOM_LENGTH = 5;
	protected String viewName = null;

	private static final String WARNING = "warningInfoUsedForAthrun";

	protected static Map<String, String> viewOperation = new HashMap<String, String>();

	static {
		viewOperation.put(CLICK, CLICK_VALUE);
		viewOperation.put(LONG_CLICK, LONG_CLICK_VALUE);
		viewOperation.put(TYPE, TYPE_VALUE);
		viewOperation.put(ITEM_CLICK, ITEM_CLICK_VALUE);
		viewOperation.put(ITEM_LONG_CLICK, ITEM_LONG_CLICK_VALUE);
	}

	public ViewAction(Map<String, String> action, AST ast) {
		super(action, ast);
		this.viewName = getRandomName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void toJavaCode(Block methodBlock) {
		
		if (!this.action.containsKey("viewid")
				&& !this.action.containsKey("viewtext")) {
			methodBlock.statements().add(
					this.ast.newExpressionStatement(getStatementForSpecial(
							methodBlock, WARNING)));
		}
		createComment(methodBlock);
		createWaitIfNeeded(methodBlock);

		methodBlock.statements().add(getFindStatement());
		methodBlock.statements().add(getOperationStatement());

		createBlank(methodBlock);
	}

	protected VariableDeclarationStatement getFindStatement() {
		VariableDeclarationStatement findStatement = null;

		if (this.action.containsKey(VIEW_ID)) {
			findStatement = createFindStatement(
					getMethodInvocation(this.action.get(VIEW_ID), VIEW_ELEMENT),
					this.viewName, VIEW_ELEMENT);

		} else {
			findStatement = createFindStatement(
					getMethodInvocation("0", this.action.get("viewtype"),
							VIEW_ELEMENT), this.viewName, VIEW_ELEMENT);
		}

		return findStatement;
	}

	protected VariableDeclarationStatement createFindStatement(
			MethodInvocation findBy, String viewName, String returnType) {
		VariableDeclarationFragment fragment = getVariableDeclarationFragment(viewName);
		fragment.setInitializer(findBy);

		VariableDeclarationStatement variableStatement = ast
				.newVariableDeclarationStatement(fragment);
		variableStatement.setType(ast.newSimpleType(ast
				.newSimpleName(returnType)));

		return variableStatement;
	}

	protected VariableDeclarationFragment getVariableDeclarationFragment(
			String viewName) {
		VariableDeclarationFragment fragment = ast
				.newVariableDeclarationFragment();
		fragment.setName(ast.newSimpleName(viewName));
		return fragment;
	}

	protected MethodInvocation getMethodInvocation(String... args) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();

		if (1 == args.length) {
			createFindByTextInvocation(methodInvocation, args[0]);

		} else if (2 == args.length) {
			createFindByIdInvocation(methodInvocation, args[0], args[1]);

		} else if (3 == args.length) {
			createFindByIndexInvocation(methodInvocation, args[0], args[1],
					args[2]);
		}

		return methodInvocation;
	}

	@SuppressWarnings("unchecked")
	protected void createFindByTextInvocation(
			MethodInvocation methodInvocation, String viewText) {
		methodInvocation.setName(ast.newSimpleName(FIND_ELEMENT_BY_TEXT));
		StringLiteral textParam = ast.newStringLiteral();
		textParam.setLiteralValue(viewText);
		methodInvocation.arguments().add(textParam);
	}

	@SuppressWarnings("unchecked")
	protected void createFindByIdInvocation(MethodInvocation methodInvocation,
			String viewId, String returnType) {
		methodInvocation.setName(ast.newSimpleName(FIND_BY_ID));

		if (viewId.startsWith("@")) {
			StringLiteral idParam = ast.newStringLiteral();
			idParam.setLiteralValue(viewId.replace("@", ""));
			methodInvocation.arguments().add(idParam);

		} else {
			NumberLiteral idParam = ast.newNumberLiteral(viewId);
			methodInvocation.arguments().add(idParam);
		}

		TypeLiteral returnTypeParam = ast.newTypeLiteral();
		returnTypeParam
				.setType(ast.newSimpleType(ast.newSimpleName(returnType)));

		methodInvocation.arguments().add(returnTypeParam);
	}

	@SuppressWarnings("unchecked")
	protected void createFindByIndexInvocation(
			MethodInvocation methodInvocation, String index, String viewType,
			String returnType) {
		methodInvocation.setName(ast.newSimpleName(FIND_ELEMENT_BY_INDEX));
		NumberLiteral indexParam = ast.newNumberLiteral(index);

		TypeLiteral sdkTypeParam = ast.newTypeLiteral();
		sdkTypeParam.setType(ast.newSimpleType(ast.newSimpleName(viewType)));

		TypeLiteral returnTypeParam = ast.newTypeLiteral();
		returnTypeParam
				.setType(ast.newSimpleType(ast.newSimpleName(returnType)));

		methodInvocation.arguments().add(indexParam);
		methodInvocation.arguments().add(sdkTypeParam);
		methodInvocation.arguments().add(returnTypeParam);
	}

	private String getRandomName(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		sb.append("modify_");
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}

		return sb.append("_this").toString();
	}

	protected String getRandomName() {
		return getRandomName(RANDOM_LENGTH);
	}

	protected ExpressionStatement getOperationStatement() {
		MethodInvocation operation = getOperationInvocation();
		return ast.newExpressionStatement(operation);
	}

	protected MethodInvocation getOperationInvocation() {
		MethodInvocation operation = ast.newMethodInvocation();
		operation.setExpression(ast.newSimpleName(this.viewName));
		operation
				.setName(ast.newSimpleName(viewOperation.get(this.actiontype)));

		return operation;
	}

	// protected String getViewId() {
	// String id = this.action.get(VIEW_ID);
	// if (id.startsWith("@")) {
	// id = id.replace("@", "");
	// }
	//
	// return id;
	// }
}
