package org.athrun.android.framework.transform.action;

import org.eclipse.jdt.core.dom.Block;

public interface IAction {
	/**
	 * Create java code.
	 * @param methodBlock
	 */
	void toJavaCode(Block methodBlock);
}
