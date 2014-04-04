package org.athrun.android.framework.transform.action;

import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;

public class UnknowAction extends BaseAction {
	private static final String UNKOWN_ACTION = "unkownActionUsedForAthrun";

	public UnknowAction(Map<String, String> action, AST ast) {
		super(action, ast);
	}

	@Override
	public void toJavaCode(Block methodBlock) {
		createSpecial(methodBlock, UNKOWN_ACTION);
		createBlank(methodBlock);
	}
}
