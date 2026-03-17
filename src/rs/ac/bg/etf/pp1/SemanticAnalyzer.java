package rs.ac.bg.etf.pp1;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" on line ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" on line ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	// -------------- SEMANTIC ANALYZE --------------------------------------
	
	private Obj currentProgram = null;
	private Struct currentType = Tab.noType;
	private int constValue;
	private Struct boolType = Tab.find("bool").getType();
	private Obj currentMethod = null;
	private int currentEnumVal = 0;
	private Set<Integer> enumVals = new HashSet<>();
	private ArrayList<Struct> actualParametersTypes = new ArrayList<>();
	private int nVars = 0;
	
	private boolean isFinalDecl = false;
	
	public int getnVars() {
		return nVars;
	}
	
	// ----------- PROGRAM ---------------------------
	
	@Override
	public void visit(ProgramName programName) {
		programName.obj = currentProgram = Tab.insert(Obj.Prog, programName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	@Override
	public void visit(Program program) {
		Tab.chainLocalSymbols(program.getProgramName().obj);
		program.obj = program.getProgramName().obj;
		nVars = Tab.currentScope().getnVars(); 
		if (nVars > 65536) {
			report_error("There is more than 65536 global variables ", program);
		}
		Tab.closeScope();
	}
	
	// ------------- METHOD DECLARATION --------------------------------------------
	
	@Override
	public void visit(MethodName methodName) {
		methodName.obj = currentMethod = Tab.insert(Obj.Meth, "main", Tab.noType);
		Tab.openScope();
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		Tab.chainLocalSymbols(methodDecl.getMethodName().obj);
		methodDecl.obj = methodDecl.getMethodName().obj;
		if (methodDecl.obj.getLocalSymbols().size() > 256) {
			report_error("Method has more than 256 local variables ", methodDecl);
		}
		currentMethod = null;
		Tab.closeScope();
	}
	
	// ------------ CONST GLOBAL DEFINITIONS -----------------------------------------
	
	@Override
	public void visit(ConstDecl constDecl) {
		Obj constObj = Tab.find(constDecl.getConstName());
		if (constObj != Tab.noObj) {
			report_error("Multiple definitions of a constant: " + constDecl.getConstName(), constDecl);
		}
		else {
			if ((constDecl.getConstant().struct).assignableTo(currentType)) {
				constObj = Tab.insert(Obj.Con, constDecl.getConstName(), currentType);
				constObj.setAdr(constValue);
			}
			else {
				report_error("Incompatible constant type: " + constDecl.getConstName(), constDecl);
			}
		}
	}
	
	@Override
	public void visit(ConstantNumber constNum) {
		constValue = constNum.getNumConst();
		constNum.struct = Tab.intType;
	}
	
	@Override
	public void visit(ConstantChar constChar) {
		constValue = constChar.getCharConst();
		constChar.struct = Tab.charType;
	}
	
	@Override
	public void visit(ConstantBool constBool) {
		constValue = constBool.getBoolConst();
		constBool.struct = boolType;
	}
	
	// ------------ TYPE ----------------------------------
	
	@Override
	public void visit(Type type) {
		Obj typeObj = Tab.find(type.getTypeName());
		if (typeObj.equals(Tab.noObj) || typeObj.getKind() != Obj.Type) {
			report_error("No type named " + type.getTypeName(), type);
			type.struct = currentType = Tab.noType;
		}
		else {
			type.struct = currentType = typeObj.getType();
		}
	}
	
	// -------- VAR DECLARATIONS - GLOBAL AND LOCAL -----------
	
	@Override
	public void visit(VarDecl varDecl) {
		Obj varObj = Tab.find(varDecl.getVarName());
		if (varObj != Tab.noObj && varObj.getKind() == Obj.Type) {
			report_error("Illegal variable name: " + varDecl.getVarName(), varDecl);
		}
		else {
			if (Tab.currentScope().findSymbol(varDecl.getVarName()) != null) {
				report_error("Multiple declarations of a variable: " + varDecl.getVarName(), varDecl);
			}
			else {
				if (isFinalDecl && varObj.getLevel() > 0) {
					report_error("Local variable can't be final: " + varDecl.getVarName(), varDecl);
				}
				else {
					if (varDecl.getVarDeclBrackets() instanceof VarDeclBracketsYes) {
						varObj = Tab.insert(Obj.Var, varDecl.getVarName(), new Struct(Struct.Array, currentType));
						if (isFinalDecl) {
							varObj.setFpPos(5); // indicator that static array is final
						}
					}
					else {
						Tab.insert(Obj.Var, varDecl.getVarName(), currentType);
					}
				}
			}
		}
	}	
	
	@Override
	public void visit(FinalKeywordYes finalKeywordYes) {
		isFinalDecl = true;
	}	
	
	@Override
	public void visit(FinalKeywordEps finalKeywordEps) {
		isFinalDecl = false;
	}	
	
	// --------- ENUM DEFINITIONS ----------------------------
	
	@Override
	public void visit(EnumName enumName) {
		enumName.obj = new Obj(Obj.Type, enumName.getEnumName(), Tab.intType);
		Tab.openScope();
	}	
	
	@Override
	public void visit(EnumDecl enumDecl) {
		if (Tab.currentScope().findSymbol(enumDecl.getEnumValueName()) != null || enumVals.contains(currentEnumVal)) {
			report_error("Illegal Enum field declaration: " + enumDecl.getEnumValueName(), enumDecl);
		}
		else {
			Obj enumObj = Tab.insert(Obj.Con, enumDecl.getEnumValueName(), Tab.intType);
			enumObj.setAdr(currentEnumVal);
			enumVals.add(currentEnumVal);
			currentEnumVal++;
		}
	}	
	
	@Override
	public void visit(EnumDeclInitYes enumDeclInitYes) {
		currentEnumVal = enumDeclInitYes.getEnumInitValue();
	}	
	
	@Override
	public void visit(EnumDeclList enumDeclList) {
		currentEnumVal = 0;
		enumVals = new HashSet<>();
		Tab.chainLocalSymbols(enumDeclList.getEnumName().obj);
		Tab.closeScope();
		String enumName = enumDeclList.getEnumName().getEnumName();
		if (Tab.find(enumName) != Tab.noObj) {
			report_error("Illegal enum type name: " + enumName, enumDeclList);
		}
		else {
			Tab.currentScope().addToLocals(enumDeclList.getEnumName().obj);
		}
	}	
	
	// ---------- CONTEXT CONDITIONS ---------------------
	
	// -------- FACTOR ------------------------------
	
	@Override
	public void visit(FactorMinus factorMinus) {
		if (!factorMinus.getFactorOptions().struct.equals(Tab.intType)) {
			report_error("Negation of non int value ", factorMinus);
			factorMinus.struct = Tab.noType;
		}
		else {
			factorMinus.struct = Tab.intType;
		}
	}
	
	@Override
	public void visit(FactorNoMinus factorNoMinus) {
		factorNoMinus.struct = factorNoMinus.getFactorOptions().struct;
	}
	
	@Override
	public void visit(FactorOptionsConst factorOptionsConst) {
		factorOptionsConst.struct = factorOptionsConst.getConstant().struct;
	}
	
	@Override
	public void visit(FactorOptionsNew factorOptionsNew) {
		if (!factorOptionsNew.getExpr().struct.equals(Tab.intType)) {
			report_error("Size of array must be integer ", factorOptionsNew);
			factorOptionsNew.struct = Tab.noType;
		}
		else {
			factorOptionsNew.struct = new Struct(Struct.Array, currentType);
		}
	}
	
	@Override
	public void visit(FactorOptionsParen factorOptionsParen) {		
		factorOptionsParen.struct = factorOptionsParen.getExpr().struct;
	}
	
	@Override
	public void visit(FactorOptionsDesign factorOptionsDesign) {
		if (factorOptionsDesign.getDesignator().obj.getType().getKind() == Struct.Array 
				&& factorOptionsDesign.getDesignator().obj.getType().getElemType().equals(boolType)) {
			factorOptionsDesign.struct = Tab.intType; 
		}
		else {
			factorOptionsDesign.struct = factorOptionsDesign.getDesignator().obj.getType();
		}
	}
	
	@Override
	public void visit(FactorOptionsDesignFunc factorOptionsDesignFunc) {
		Obj methObj = factorOptionsDesignFunc.getDesignator().obj;
		if (methObj == Tab.noObj) {
			factorOptionsDesignFunc.struct = Tab.noType;
		}
		else {
			if (methObj.getKind() != Obj.Meth) {
				report_error("Trying to call non method " + methObj.getName(), factorOptionsDesignFunc);
				factorOptionsDesignFunc.struct = Tab.noType;
			}
			else {
				if (methObj.getLevel() != actualParametersTypes.size()) {
					report_error("Bad number of arguments in function " + methObj.getName(), factorOptionsDesignFunc);
					factorOptionsDesignFunc.struct = Tab.noType;
				}
				else {
					int i = 0;
					boolean goodParams = true;
					for(Obj paramObj: methObj.getLocalSymbols()) {
						if (paramObj.getFpPos() == 1) {
							if (!actualParametersTypes.get(i++).assignableTo(paramObj.getType()) ) {
								goodParams = false;
								break;
							}
						}
					}
					if (!goodParams) {
						report_error("Bad types of arguments in function " + methObj.getName(), factorOptionsDesignFunc);
						factorOptionsDesignFunc.struct = Tab.noType;
					}
					else {
						factorOptionsDesignFunc.struct = methObj.getType();
					}
				}
			}
		}		
		actualParametersTypes = new ArrayList<>();		
	}
	
	@Override
	public void visit(FactorOptionsAT factorOptionsAT) {
		Obj arrObj1 = factorOptionsAT.getDesignator().obj;
		Obj arrObj2 = factorOptionsAT.getDesignator1().obj;
		if (arrObj1.getType().getKind() != Struct.Array || arrObj2.getType().getKind() != Struct.Array) {
			report_error("Operands of @ operator must be arrays " + arrObj1.getName(), factorOptionsAT);
			factorOptionsAT.struct = Tab.noType;
		}
		else {
			factorOptionsAT.struct = arrObj1.getType();
		}
	}
	
	// -------- ACTUAL PARAMS -----------------------
	
	@Override
	public void visit(ActParsOne actParsOne) {
		actualParametersTypes.add(actParsOne.getExpr().struct);
	}
	
	@Override
	public void visit(ActParsMore actParsMore) {
		actualParametersTypes.add(actParsMore.getExpr().struct);
	}
	
	// --------- DESIGNATOR --------------------------
		
	@Override
	public void visit(DesignatorVar designatorVar) {
		Obj varObj = designatorVar.getDesignatorName().obj;
		if (varObj == Tab.noObj) {
			designatorVar.obj = Tab.noObj;
		}
		else {
			if (varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Con && varObj.getKind() != Obj.Meth) {
				report_error("Bad variable use: " + designatorVar.getDesignatorName().getVarName(), designatorVar);
				designatorVar.obj = Tab.noObj;
			}
			else {
				designatorVar.obj = varObj;				
			}
		}
	}
	
	@Override
	public void visit(DesignatorName designatorName) {
		Obj varObj = Tab.find(designatorName.getVarName());
		if (varObj == Tab.noObj) {
			report_error("Variable not defined: " + designatorName.getVarName(), designatorName);
			designatorName.obj = Tab.noObj;
		}
		else {
			designatorName.obj = varObj;
			SymbolTableVisitor stv = new DumpSymbolTableVisitor();
			varObj.accept(stv);
			log.info("Symbol found on line " + designatorName.getLine() + ": " + stv.getOutput());
		}
	}
	
	@Override
	public void visit(DesignatorArrayIndex designatorArrayIndex) {
		Obj arrObj = designatorArrayIndex.getDesignatorName().obj;
		if (arrObj == Tab.noObj) {
			designatorArrayIndex.obj = Tab.noObj;
		}
		else {
			if (!designatorArrayIndex.getExpr().struct.equals(Tab.intType)) {
				report_error("Index must be integer value ", designatorArrayIndex);
				designatorArrayIndex.obj = Tab.noObj;
			}
			else {
				if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
					report_error("Bad array variable use: " + arrObj.getName(), designatorArrayIndex);
					designatorArrayIndex.obj = Tab.noObj;
				}
				else {
					designatorArrayIndex.obj = new Obj(Obj.Elem, arrObj.getName() + "[index]", arrObj.getType().getElemType());
					designatorArrayIndex.obj.setFpPos(arrObj.getFpPos());
					SymbolTableVisitor stv = new DumpSymbolTableVisitor();
					designatorArrayIndex.obj.accept(stv);
					log.info("Array elem found on line " + designatorArrayIndex.getLine() + ": " + stv.getOutput());
				}
			}
		}
	}
	
	@Override
	public void visit(DesignatorLen designatorLen) {
		Obj arrObj = designatorLen.getDesignatorName().obj;
		if (arrObj == Tab.noObj) {
			designatorLen.obj = Tab.noObj;
		}
		else {
			if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
				report_error("Bad array variable use: " + arrObj.getName(), designatorLen);
				designatorLen.obj = Tab.noObj;
			}
			else {
				designatorLen.obj = new Obj(Obj.Type, arrObj.getName() + ".length", Tab.intType);
			}
		}
	}
	
	@Override
	public void visit(DesignatorHashtag designatorHashtag) {
		Obj arrObj = designatorHashtag.getDesignatorName().obj;
		if (arrObj == Tab.noObj) {
			designatorHashtag.obj = Tab.noObj;
		}
		else {
			if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array || 
					!(arrObj.getType().getElemType() != Tab.intType || arrObj.getType().getElemType() != Tab.charType)) {
				report_error("Bad array variable use: [Hash] " + arrObj.getName(), designatorHashtag);
				designatorHashtag.obj = Tab.noObj;
			}
			else {
				designatorHashtag.obj = new Obj(Obj.Type, "#" + arrObj.getName(), Tab.intType);
			}
		}
	}
	
	@Override
	public void visit(DesignatorEnum designatorEnum) {
		Obj enumObj = designatorEnum.getDesignatorName().obj;
		if (enumObj == Tab.noObj) {
			designatorEnum.obj = Tab.noObj;
		}
		else {
			if (enumObj.getKind() != Obj.Type) {
				report_error("Bad enum type use: " + enumObj.getName(), designatorEnum);
				designatorEnum.obj = Tab.noObj;
			}
			else {
				Obj enumConstObj = null;
				for(Obj obj: enumObj.getLocalSymbols()) {
					if (obj.getName().equals(designatorEnum.getFieldName())) {
						enumConstObj = obj;
					}
				}
				if (enumConstObj == null) {
					report_error("Non existing enum const: " + designatorEnum.getFieldName(), designatorEnum);
					designatorEnum.obj = Tab.noObj;
				}
				else {
					designatorEnum.obj = enumConstObj;
					SymbolTableVisitor stv = new DumpSymbolTableVisitor();
					enumConstObj.accept(stv);
					log.info("Enum const found on line " + designatorEnum.getLine() + ": " + stv.getOutput());
				}
			}
		}
	}
	
	// -------- TERM ------------------------
	
	@Override
	public void visit(TermOne termOne) {
		termOne.struct = termOne.getCondOperator().struct;
	}
	
	@Override
	public void visit(TermMore termMore) {
		if (termMore.getTerm().struct.equals(Tab.intType) && termMore.getCondOperator().struct.equals(Tab.intType)) {
			termMore.struct = Tab.intType;
		}
		else {
			report_error("Operand is not integer value ", termMore);
			termMore.struct = Tab.noType;
		}
	}
	
	// ---------- CONDOP MODIF ---------------------------
	
	@Override
	public void visit(CondOperatorOne condOperatorOne) {
		condOperatorOne.struct = condOperatorOne.getFactor().struct;
	}
	
	@Override
	public void visit(CondOperatorMore condOperatorMore) {
		Struct struct1 = condOperatorMore.getFactor().struct;
		Struct struct2 = condOperatorMore.getCondOperator().struct;
		if (struct1.equals(struct2) && (struct1.equals(Tab.intType) || struct1.equals(Tab.charType) || struct1.equals(boolType))) {
			condOperatorMore.struct = Tab.intType;
		}
		else {
			report_error("Operand is not integer, char or bool value or operands are not the same value ", condOperatorMore);
			condOperatorMore.struct = Tab.noType;
		}
	}
	
	// --------- EXPR ----------------------------
	
	@Override
	public void visit(Expr Expr) {
		Expr.struct = Expr.getExprDupQueOp().struct;
	}
	
	@Override
	public void visit(ExprDupQueOpOne exprDupQueOpOne) {
		exprDupQueOpOne.struct = exprDupQueOpOne.getExprSub().struct;
	}
	
	@Override
	public void visit(ExprDupQueOpMore exprDupQueOpMore) {
		if (exprDupQueOpMore.getExprDupQueOp().struct.equals(Tab.intType) && exprDupQueOpMore.getExprSub().struct.equals(Tab.intType)) {
			exprDupQueOpMore.struct = Tab.intType;
		}
		else {
			report_error("Operand is not integer value ", exprDupQueOpMore);
			exprDupQueOpMore.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(ExprNonTernary exprNonTernary) {
		exprNonTernary.struct = exprNonTernary.getExprNonTern().struct;
	}
	
	@Override
	public void visit(ExprTernary exprTernary) {
		exprTernary.struct = exprTernary.getExprTern().struct;
	}
	
	@Override
	public void visit(ExprNonTernOne exprNonTernOne) {
		exprNonTernOne.struct = exprNonTernOne.getTerm().struct;
	}
	
	@Override
	public void visit(ExprNonTernMore exprNonTernMore) {
		if (exprNonTernMore.getExprNonTern().struct.equals(Tab.intType) && exprNonTernMore.getTerm().struct.equals(Tab.intType)) {
			exprNonTernMore.struct = Tab.intType;
		}
		else {
			report_error("Operand is not integer value ", exprNonTernMore);
			exprNonTernMore.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(ExprTern exprTern) {
		if (exprTern.getCondFact().struct.equals(Tab.noType)) {
			exprTern.struct = Tab.noType;
		}
		else {
			if (!exprTern.getExprSub().struct.equals(exprTern.getExprSub1().struct)) {
				report_error("Expresions are not the same type ", exprTern);
				exprTern.struct = Tab.noType;
			}
			else {
				exprTern.struct = exprTern.getExprSub().struct;
			}
		}
	}
	
	// --------- COND FACTS -----------------------------------
	
	@Override
	public void visit(CondFactExpr condFactExpr) {
		if (!condFactExpr.getExprNonTern().struct.equals(boolType)) {
			report_error("Condition must be boolean value ", condFactExpr);
			condFactExpr.struct = Tab.noType;
		}
		else {
			condFactExpr.struct = boolType;
		}
	}
	
	@Override
	public void visit(CondFactRelop condFactRelop) {
		Struct expr1 = condFactRelop.getExprNonTern().struct;
		Struct expr2 = condFactRelop.getExprNonTern1().struct;
		if (!expr1.compatibleWith(expr2)) {
			report_error("Operands are not compatible ", condFactRelop);
			condFactRelop.struct = Tab.noType;
		}
		else {
			if ((expr1.isRefType() || expr2.isRefType()) && 
					!((condFactRelop.getRelop() instanceof RelopEq) || (condFactRelop.getRelop() instanceof RelopNe))) {
				report_error("Arrays can be compared just with == or != ", condFactRelop);
				condFactRelop.struct = Tab.noType;
			}
			else {
				condFactRelop.struct = boolType;
			}
		}
	}
	
	
	// --------------- STATEMENTS ---------------------------
	
	@Override
	public void visit(StatementRead statementRead) {
		Obj varObj = statementRead.getDesignator().obj;
		if(varObj != Tab.noObj) {
			if (varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Elem) {
				report_error("Bad parameter kind for read function: " + varObj.getName(), statementRead);
			}
			else {
				if (!(varObj.getType().equals(Tab.intType) || varObj.getType().equals(Tab.charType) 
						|| varObj.getType().equals(boolType) || varObj.getType().getKind() == Struct.Array)) {
					report_error("Read parameter must be type of int, char or bool, or array: " + varObj.getName(), statementRead);
				}
			}
		}
	}
	
	@Override
	public void visit(StatementPrint statementPrint) {
		Struct type = statementPrint.getExpr().struct;
		if (!(type.getKind() == Struct.Array ||type.equals(Tab.intType) || type.equals(Tab.charType) || type.equals(boolType))) {
			report_error("First print parameter must be type of int, char or bool, or array: ", statementPrint);
		}
	}
	
	// ----------- DESIGNATOR STATEMENTS -------------------------------------------
	
	@Override
	public void visit(DesignatorStatementInc designatorStatementInc) {
		Obj varObj = designatorStatementInc.getDesignator().obj;
		if(varObj != Tab.noObj) {
			if (varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Elem) {
				report_error("Bad kind of operand: " + varObj.getName(), designatorStatementInc);
			}
			else {
				if (!varObj.getType().equals(Tab.intType)) {
					report_error("Operand must be integer type: " + varObj.getName(), designatorStatementInc);
				}
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatementDec designatorStatementDec) {
		Obj varObj = designatorStatementDec.getDesignator().obj;
		if(varObj != Tab.noObj) {
			if (varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Elem) {
				report_error("Bad kind of operand: " + varObj.getName(), designatorStatementDec);
			}
			else {
				if (!varObj.getType().equals(Tab.intType)) {
					report_error("Operand must be integer type: " + varObj.getName(), designatorStatementDec);
				}
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatementFunc designatorStatementFunc) {
		Obj methObj = designatorStatementFunc.getDesignator().obj;
		if (methObj != Tab.noObj) {
			if (methObj.getKind() != Obj.Meth) {
				report_error("Trying to call non method " + methObj.getName(), designatorStatementFunc);
			}
			else {
				if (methObj.getLevel() != actualParametersTypes.size()) {
					report_error("Bad number of arguments in function " + methObj.getName(), designatorStatementFunc);
				}
				else {
					int i = 0;
					boolean goodParams = true;
					for(Obj paramObj: methObj.getLocalSymbols()) {
						if (paramObj.getFpPos() == 1) {
							if (!actualParametersTypes.get(i++).assignableTo(paramObj.getType()) ) {
								goodParams = false;
								break;
							}
						}
					}
					if (!goodParams) {
						report_error("Bad types of arguments in function " + methObj.getName(), designatorStatementFunc);
					}
				}
			}
		}	
		actualParametersTypes = new ArrayList<>();	
	}
	
	@Override
	public void visit(DesignatorStatementAssign designatorStatementAssign) {
		Obj dstObj = designatorStatementAssign.getDesignator().obj;
		Struct dstType = dstObj.getType();
		Struct srcType = designatorStatementAssign.getExpr().struct;
		
		if (dstObj != Tab.noObj && srcType != Tab.noType) {
			if (dstObj.getKind() != Obj.Var && dstObj.getKind() != Obj.Elem) {
				report_error("Destination must be variable or array element: " + dstObj.getName(), designatorStatementAssign);
			}
			else {
				if (!srcType.assignableTo(dstType)) {
					report_error("Non compatible assignment ", designatorStatementAssign);
				}
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatementCaret designatorStatementCaret) {
		Obj arrObj = designatorStatementCaret.getDesignator().obj;
		
		if (arrObj != Tab.noObj) {
			if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
				report_error("First operand must be array: " + arrObj.getName(), designatorStatementCaret);
			}
		}
	}
		
}








