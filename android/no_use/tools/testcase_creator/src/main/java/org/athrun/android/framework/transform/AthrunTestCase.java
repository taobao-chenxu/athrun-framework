package org.athrun.android.framework.transform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.athrun.android.framework.transform.action.ActionType;
import org.athrun.android.framework.transform.action.BaseAction;
import org.athrun.android.framework.transform.action.DeviceAction;
import org.athrun.android.framework.transform.action.IAction;
import org.athrun.android.framework.transform.action.OptionItemAction;
import org.athrun.android.framework.transform.action.ViewAction;
import org.athrun.android.framework.transform.taobaoview.TaobaoSkuViewAction;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public final class AthrunTestCase {
	private final Logger logger = Logger.getLogger(getClass());

	private static final String NEWLINE = "blankUsedForAthrun();";
	private static final String COMMENT = "commentUsedForAthrun";
	private static final String UNKOWN_ACTION = "unkownActionUsedForAthrun";
	private static final String WARNING = "warningInfoUsedForAthrun();";
	
	private static final String WIDGET_IMPORTS = "org.athrun.androidwidgets";
	private static final String VIEW_IMPORTS = "org.athrun.androidview";
	private static final String FRAMEWORK_IMPORTS = "org.athrun.frameworks";
	private static final String VIEWELEMENT_IMPORTS = "org.athrun.viewelements";
	private static final String WEBVIEW_IMPORTS = "org.athrun.webviews";
	private static final String TAOBAOVIEW_IMPORTS = "org.athrun.taobaoviews";

	private File xmlFile;

	private List<IAction> actions = new ArrayList<IAction>();

	private AST ast;
	private CompilationUnit compilationUnit;
	private TypeDeclaration athrunTestCaseClass;

	private ActionFactory actionFactory;

	private String testCaseName;
	private String testMethodName;
	private String mainActivityName;

	private static ArrayList<String> imports = new ArrayList<String>();

	static {
		imports.add(WIDGET_IMPORTS);
		imports.add(VIEW_IMPORTS);
		imports.add(FRAMEWORK_IMPORTS);
		imports.add(VIEWELEMENT_IMPORTS);
		imports.add(WEBVIEW_IMPORTS);
		imports.add(TAOBAOVIEW_IMPORTS);
	}

	private AthrunTestCase(File xmlFile) {
		this.ast = AST.newAST(AST.JLS3);
		this.compilationUnit = ast.newCompilationUnit();
		this.xmlFile = xmlFile;
		this.actionFactory = ActionFactory.getInstance(ast);
	}

	/**
	 * 
	 * @param testCaseName
	 * @param testPackageName
	 * @param mainActivity
	 * @param testMethodName
	 */
	private void init(String testCaseName, String testPackageName, String mainActivity,
			String testMethodName) {
		this.testCaseName = testCaseName;
		this.testMethodName = testMethodName;
		this.mainActivityName = mainActivity;

		initActions();

		createTestCaseClass(this.testCaseName);
		setPackage(testPackageName);
		setImports(imports);
		setConstructor(getAppPackageName(), this.mainActivityName);
		logger.info("init() finished.");
	}

	private String getAppPackageName() {
		return XmlParser.getRootElement(this.xmlFile).attributeValue(
				"packagename");
	}

	@SuppressWarnings("unchecked")
	private void createTestCaseClass(String testCaseName) {
		athrunTestCaseClass = ast.newTypeDeclaration();
		athrunTestCaseClass.setSuperclassType(ast.newSimpleType(ast
				.newSimpleName("AthrunTestCase")));
		athrunTestCaseClass.setName(ast.newSimpleName(testCaseName));
		athrunTestCaseClass.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		compilationUnit.types().add(athrunTestCaseClass);
		logger.info("createTestCaseClass(" + testCaseName + ") finished.");
	}

	private void setPackage(String testPackageName) {
		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		packageDeclaration.setName(ast.newName(testPackageName));
		this.compilationUnit.setPackage(packageDeclaration);
		logger.info("setPackage(" + testPackageName + ") finished.");
	}

	@SuppressWarnings("unchecked")
	private void setImports(List<String> packages) {
		for (String imp : packages) {
			ImportDeclaration importDeclaration = ast.newImportDeclaration();
			importDeclaration.setName(ast.newName(imp));
			compilationUnit.imports().add(importDeclaration);
		}
		
		logger.info("setImports() finished.");
	}

	@SuppressWarnings("unchecked")
	private void setConstructor(String appPkgName, String mainActivity) {
		this.mainActivityName = mainActivity;
		MethodDeclaration constructor = ast.newMethodDeclaration();
		constructor.setConstructor(true);
		constructor.thrownExceptions().add(ast.newSimpleName("Exception"));
		constructor.setName(ast.newSimpleName(this.testCaseName));
		constructor.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		Block constructBlock = ast.newBlock();
		constructor.setBody(constructBlock);
		SuperConstructorInvocation superConstructorInvocation = ast
				.newSuperConstructorInvocation();
		constructBlock.statements().add(superConstructorInvocation);

		StringLiteral appPackage = ast.newStringLiteral();
		StringLiteral appMainActivity = ast.newStringLiteral();
		appPackage.setLiteralValue(appPkgName);
		appMainActivity.setLiteralValue(this.mainActivityName);

		superConstructorInvocation.arguments().add(appPackage);
		superConstructorInvocation.arguments().add(appMainActivity);
		this.athrunTestCaseClass.bodyDeclarations().add(constructor);
		logger.info("setConstructor() finished.");
	}

	@SuppressWarnings("unchecked")
	private Block initTestMethod() {

		MethodDeclaration testMethodDeclaration = ast.newMethodDeclaration();
		testMethodDeclaration.setName(ast.newSimpleName(this.testMethodName));
		testMethodDeclaration.thrownExceptions().add(
				ast.newSimpleName("Exception"));
		testMethodDeclaration.modifiers().add(
				ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		testMethodDeclaration.setReturnType2(ast
				.newPrimitiveType(PrimitiveType.VOID));

		Block testMethodBlock = ast.newBlock();
		testMethodDeclaration.setBody(testMethodBlock);

		athrunTestCaseClass.bodyDeclarations().add(testMethodDeclaration);
		logger.info("initTestMethod() finished.");

		return testMethodBlock;
	}

	private void addTestStatements(Block testMethodBlock) {
		int i = 1;
		for (IAction action : actions) {
			logger.debug("Transform the " + i + " action.");
			action.toJavaCode(testMethodBlock);
			i++;
		}
		
		logger.info("addTestStatements() finished.");
	}

	private void initActions() {
		List<Map<String, String>> xmlActions = XmlParser
				.getAllActions(this.xmlFile);
		
		logger.info("There are " + xmlActions.size() + " actions recorded in xml file.");
		
		int i = 1;
		for (Map<String, String> action : xmlActions) {
			IAction iAction = null;
			
			logger.debug("This is the " + i + " action.");
			logger.debug("The original content is: " + action.toString());

			if (isDevice(action)) {
				logger.debug("It is a DeviceAction.");
				iAction = getDeviceAction(action);

			} else if (isView(action)) {
				iAction = getViewAction(action);

			} else {
				// actions that does not support now
				logger.warn("This is an Unknow action.");
				iAction = getUnkownAction(action);
			}
			
			i++;

			this.actions.add(iAction);
		}
		
		logger.info("initActions() finished, add " + this.actions.size() + " instance of iAction.");
	}

	private IAction getDeviceAction(Map<String, String> action) {
		IAction iAction = null;
		if (isOnTouch(action)) {
			iAction = actionFactory.getAction(action, ActionType.OnTouchAction);

		} else {
			iAction = actionFactory.getAction(action, ActionType.DeviceAction);
		}

		return iAction;
	}

	private IAction getViewAction(Map<String, String> action) {
		IAction iAction = null;

		if (isInAbsListView(action)) {
			logger.debug("This an AbsListViewAction.");
			iAction = actionFactory.getAction(action,
					ActionType.AbsListViewAction);

		} else if (isInViewGroup(action)) {
			logger.debug("This a ViewGroupAction.");
			iAction = actionFactory.getAction(action,
					ActionType.ViewGroupAction);

		} else if (isTaobaoSkuViewAction(action)) {
			logger.debug("This a TaobaoSkuViewAction.");
			iAction = actionFactory.getAction(action,
					ActionType.TaobaoSkuViewAction);

		} else if (isTextView(action)) {
			logger.debug("This a TextViewAction.");
			iAction = actionFactory
					.getAction(action, ActionType.TextViewAction);

		} else if (isOptionItemAction(action)) {
			logger.debug("This an OptionItemAction.");
			iAction = actionFactory.getAction(action,
					ActionType.OptionItemAction);

		} else {
			logger.debug("This a default ViewAction.");
			iAction = actionFactory.getAction(action, ActionType.ViewAction);
		}

		return iAction;
	}

	private IAction getUnkownAction(Map<String, String> action) {
		IAction iAction = actionFactory.getAction(action,
				ActionType.UnkownAction);
		return iAction;
	}

	private boolean isOnTouch(Map<String, String> action) {
		return action.get("actiontype").equalsIgnoreCase(DeviceAction.ON_TOUCH);
	}

	private boolean isView(Map<String, String> action) {
		return action.containsKey("viewtype") || action.containsKey("itemtype");
	}

	private boolean isTextView(Map<String, String> action) {
		return action.containsKey("viewtext")
				|| action.containsKey("inputtext");
	}

	private boolean isInAbsListView(Map<String, String> action) {
		return (isInViewGroup(action) && action.containsKey("parenttype") && (action
				.get("parenttype").contains("ListView") || action.get(
				"parenttype").contains("GridView")));
	}

	private boolean isInViewGroup(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				ViewAction.ITEM_CLICK)
				|| action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
						ViewAction.ITEM_LONG_CLICK);
	}

	private boolean isDevice(Map<String, String> action) {
		String actionType = action.get(BaseAction.ACTION_TYPE);

		return actionType.equalsIgnoreCase(DeviceAction.PRESS_KEY)
				|| actionType.equalsIgnoreCase(DeviceAction.ON_TOUCH);
	}

	private boolean isOptionItemAction(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				OptionItemAction.OPTION_ITEM);
	}

	private boolean isTaobaoSkuViewAction(Map<String, String> action) {
		return action.get(BaseAction.ACTION_TYPE).equalsIgnoreCase(
				TaobaoSkuViewAction.SKU_CLICK);
	}

	private String toJavaCode() {
		return this.compilationUnit.toString();
	}

	/**
	 * 
	 * @param path
	 *            The folder path that stores the java files.
	 */
	private void toJavaFile(String path) {
		try {
			FileUtils.writeStringToFile(new File(path + "/" + this.testCaseName
					+ ".java"), format(this.toJavaCode()));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String format(String javaCode) {
		String codeWithComment = formatComment(javaCode);
		String codeWithUnkown = formateUnkown(codeWithComment);
		String codeWithBlank = formatBlank(codeWithUnkown);
		String codeWithWarning = formateWarning(codeWithBlank);
		return formateImports(codeWithWarning);
	}

	private String formatComment(String javaCode) {
		return javaCode.replace(COMMENT, "//");
	}

	private String formatBlank(String javaCode) {
		return javaCode.replace(NEWLINE, "\r");
	}

	private String formateUnkown(String javaCode) {
		return javaCode
				.replace(UNKOWN_ACTION,
						"//ERROR: Unkown action! You should write the code by yourself.");
	}

	private String formateWarning(String javaCode) {
		return javaCode.replace(WARNING,
				"//WARNING: You may have to modify the parameters.");
	}

	private String formateImports(String javaCode) {
		return javaCode.replace(WIDGET_IMPORTS, "android.widget.*")
				       .replace(VIEW_IMPORTS, "android.view.*")
				       .replace(FRAMEWORK_IMPORTS, "org.athrun.android.framework.*")
				       .replace(VIEWELEMENT_IMPORTS, "org.athrun.android.framework.viewelement.*")
				       .replace(WEBVIEW_IMPORTS, "org.athrun.android.framework.webview.*")
				       .replace(TAOBAOVIEW_IMPORTS, "org.athrun.android.framework.special.taobaoview.*");
	}

	private static Map<String, String> initArgs(String args[]) {
		Map<String, String> argMap = new HashMap<String, String>();
		for (int i = 0; i < args.length; i = i + 2) {
			argMap.put(args[i].replace("-", ""), args[i + 1]);
		}

		return argMap;
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out
				.println("-xmlFilePath [xmlFilePath] -testCaseName [testCaseName] -testPackageName [testPackageName] -mainActivityName [mainActivityName] -testMethodName [testMethodName] -javaFilePath [javaFilePath]");
	}

	public static void main(String args[]) {
		
		if (0 == args.length) {
			printUsage();

		} else {
			Map<String, String> argsMap = initArgs(args);

			AthrunTestCase testCase = new AthrunTestCase(new File(
					argsMap.get("xmlFilePath")));
			testCase.init(argsMap.get("testCaseName"),
					argsMap.get("testPackageName"),
					argsMap.get("mainActivityName"),
					argsMap.get("testMethodName"));
			testCase.addTestStatements(testCase.initTestMethod());
			System.out.println(testCase.format(testCase.toJavaCode()));
			testCase.toJavaFile(argsMap.get("javaFilePath"));
		}
	}
}
