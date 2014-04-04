package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class ViewGroupAction extends ViewAction {
	protected static final String PARENT_ID = "parentid";
	protected static final String PARENT_TYPE = "parenttype";
	protected static final String PARENT_INDEX = "parentindex";
	protected static final String INDEX = "viewpos";

	protected static final String VIEW_TYPE = "viewtype";

	protected static final String GET_CHILD_BY_INDEX = "getChildByIndex";

	private static final String SDK_TYPE = "android.widget.";

	protected String parentName;
	protected String parentType;
	protected String viewType;

	private static final String DEFAULT_INDEX = "0";

	public ViewGroupAction(Map<String, String> action, AST ast) {
		super(action, ast);
		this.parentName = getRandomName();
		this.parentType = action.get(PARENT_TYPE);
		this.viewType = action.get("viewtype");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void toJavaCode(Block methodBlock) {
		createComment(methodBlock);
		createWaitIfNeeded(methodBlock);

		methodBlock.statements().add(getFindParentStatement());
		methodBlock.statements().add(this.getFindStatement());
		methodBlock.statements().add(getOperationStatement());

		createBlank(methodBlock);
	}

	protected VariableDeclarationStatement getFindParentStatement() {
		return getFindParentStatement(VIEWGROUP_ELEMENT);
	}

	protected VariableDeclarationStatement getFindParentStatement(
			String parentReturnType) {
		VariableDeclarationStatement findParent = null;
		if (this.action.containsKey(PARENT_ID)) {
			findParent = createFindStatement(super.getMethodInvocation(
					this.action.get(PARENT_ID), parentReturnType),
					this.parentName, parentReturnType);

		} else {
			findParent = createFindStatement(
					super.getMethodInvocation(getParentIndex(),
							getParentType(), parentReturnType),
					this.parentName, parentReturnType);
		}

		return findParent;
	}

	protected String getParentIndex() {
		return this.action.containsKey(PARENT_INDEX) ? this.action
				.get(PARENT_INDEX) : DEFAULT_INDEX;
	}

	protected String getParentType() {
		if (parentType.startsWith(SDK_TYPE)) {
			return parentType.replace(SDK_TYPE, "");
		}
		return null;
	}

	protected void createFindChildByIdInvocation(
			MethodInvocation methodInvocation, String viewId, String returnType) {
		methodInvocation.setExpression(ast.newName(this.parentName));
		super.createFindByIdInvocation(methodInvocation, viewId, returnType);
	}

	@SuppressWarnings("unchecked")
	protected void createGetByIndexInvocation(MethodInvocation methodInvocation) {
		methodInvocation.setExpression(ast.newName(this.parentName));
		methodInvocation.setName(ast.newSimpleName(GET_CHILD_BY_INDEX));
		NumberLiteral indexParam = ast.newNumberLiteral(this.action.get(INDEX));

		TypeLiteral returnTypeParam = ast.newTypeLiteral();
		returnTypeParam.setType(ast.newSimpleType(ast
				.newSimpleName(getChildReturnType())));

		methodInvocation.arguments().add(indexParam);
		methodInvocation.arguments().add(returnTypeParam);
	}

	protected MethodInvocation getMethodInvocation() {
		MethodInvocation methodInvocation = ast.newMethodInvocation();

		if (this.action.containsKey(VIEW_ID)) {
			createFindChildByIdInvocation(methodInvocation,
					this.action.get(VIEW_ID), this.viewType);

		} else if (this.action.containsKey(INDEX)) {
			createGetByIndexInvocation(methodInvocation);
		}

		return methodInvocation;
	}

	@Override
	protected VariableDeclarationStatement getFindStatement() {
		return createFindStatement(this.getMethodInvocation(), this.viewName,
				getChildReturnType());
	}

	protected String getChildReturnType() {
		String returnType = null;

		if (this.viewType.contains("Layout")) {
			returnType = VIEWGROUP_ELEMENT;

		} else if (this.viewType.contains("Text")) {
			returnType = TEXTVIEW_ELEMENT;

		} else {
			returnType = VIEW_ELEMENT;
		}

		return returnType;
	}
}
