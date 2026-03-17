package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc = 0;
	private Obj currMethod = null;
	
	private Struct boolType = Tab.find("bool").getType();
	
	private boolean isArrayFinal = false;
	
	public int getmainPc() {
		return mainPc;
	}
	
	// --------- Method ---------------------
	
	@Override
	public void visit(MethodName methodName) {
		mainPc = Code.pc;
		methodName.obj.setAdr(Code.pc);
		currMethod = methodName.obj;
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel());
		Code.put(methodName.obj.getLocalSymbols().size() + 2); // +2 for modifications locals
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		currMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	// --------- CONSTANTS -------------------
	
	@Override
	public void visit(ConstantNumber constantNumber) {
		if (constantNumber.getParent() instanceof FactorOptionsConst) {
			Code.loadConst(constantNumber.getNumConst());
		}
	}
	
	@Override
	public void visit(ConstantChar constantChar) {
		if (constantChar.getParent() instanceof FactorOptionsConst) {
			Code.loadConst(constantChar.getCharConst());
		}
	}
	
	@Override
	public void visit(ConstantBool constantBool) {
		if (constantBool.getParent() instanceof FactorOptionsConst) {
			Code.loadConst(constantBool.getBoolConst());
		}
	}
	
	// ---------- FACTOR ------------------------------------
	
	@Override
	public void visit(FactorMinus factorMinus) {
		Code.put(Code.neg);
	}
	
	@Override
	public void visit(FactorOptionsDesign factorOptionsDesign) {
		if (!(factorOptionsDesign.getDesignator() instanceof DesignatorLen 
				|| factorOptionsDesign.getDesignator() instanceof DesignatorHashtag)) {
			Code.load(factorOptionsDesign.getDesignator().obj);
		}
		Obj arrObj = factorOptionsDesign.getDesignator().obj;
		Obj resObj = new Obj(Obj.Var, "result", Tab.intType, currMethod.getLocalSymbols().size(), 1);
		if (arrObj.getType().getKind() == Struct.Array && arrObj.getType().getElemType().equals(boolType)) {
			// array of type bool is interpreted as int value. Elements of bool array can be 0 or 1 (false or true)
			// and those values represent binary representation of some number that will be formed here
			// ex. true false false true = 1 0 0 1 = 9
			int start, skipThen;
			Code.loadConst(0);
			Code.put(Code.dup);
			Code.store(resObj);
			start = Code.pc;
			Code.put(Code.dup);
			Code.load(arrObj);
			Code.put(Code.arraylength);
			Code.putFalseJump(Code.ne, 0);
			skipThen = Code.pc - 2;
			Code.put(Code.dup2);
			Code.put(Code.aload);
			Code.load(resObj);
			Code.loadConst(2);
			Code.put(Code.mul);
			Code.put(Code.add);
			Code.store(resObj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.putJump(start);
			Code.fixup(skipThen);
			Code.put(Code.pop);
			Code.put(Code.pop);
			Code.load(resObj);
		}
	}
	
	@Override
	public void visit(FactorOptionsNew factorOptionsNew) {
		// double the size if array is final, so elements from len to len-1 will be flags for elements
		// from 0 to len-1 if they are set or not
		if (isArrayFinal) {
			Code.loadConst(2);
			Code.put(Code.mul);
			isArrayFinal = false;
		}
		Code.put(Code.newarray);
		if (factorOptionsNew.getType().struct.equals(Tab.charType)) {
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(FactorOptionsDesignFunc factorOptionsDesignFunc) {
		if (factorOptionsDesignFunc.getDesignator().obj.getName().equals("len")) {
			Code.put(Code.arraylength);
		}
	}
	
	@Override
	public void visit(FactorOptionsAT factorOptionsAT) {
		// arr1 @ arr1 - returns new array that has the length of bigger of those two
		// newArray[i] = arr1[i] + arr2[i]
		// if arr1 and arr2 have different lengths, fill gap with zeroes
		// loop while there are both arr1[i] and arr2[i], when that loop finishes, loop through leftovers
		// in bigger array and just put its remaining elems (sum with zero)
		Obj arr1 = factorOptionsAT.getDesignator().obj;
		Obj arr2 = factorOptionsAT.getDesignator1().obj;
		Obj big = new Obj(Obj.Var, "bigger", new Struct(Struct.Array), currMethod.getLocalSymbols().size(), 1);
		Obj small = new Obj(Obj.Var, "smaller", new Struct(Struct.Array), currMethod.getLocalSymbols().size() + 1, 1);
		
		int skipSecond, skipFirst, start1, start2, skipThen1, skipThen2;
		Code.load(arr1);
		Code.put(Code.arraylength);
		Code.load(arr2);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.gt, 0);
		skipSecond = Code.pc - 2;
		Code.load(arr2);
		Code.store(small);
		Code.load(arr1);
		Code.store(big);
		Code.putJump(0);
		skipFirst = Code.pc - 2;
		Code.fixup(skipSecond);
		Code.load(arr1);
		Code.store(small);
		Code.load(arr2);
		Code.store(big);
		Code.fixup(skipFirst);
		
		Code.load(big);
		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(1);
		Code.loadConst(0);
		
		start1 = Code.pc;
		Code.put(Code.dup);
		Code.load(small);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne, 0);
		skipThen1 = Code.pc - 2;
		Code.put(Code.dup2);
		Code.put(Code.dup);
		Code.load(small);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);		
		Code.put(Code.dup);	
		Code.load(big);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);	
		Code.put(Code.aload);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.add);
		Code.put(Code.astore);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(start1);
		Code.fixup(skipThen1);
		
		start2 = Code.pc;
		Code.put(Code.dup);
		Code.load(big);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne, 0);
		skipThen2 = Code.pc - 2;
		Code.put(Code.dup2);
		Code.put(Code.dup);
		Code.load(big);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.astore);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(start2);
		Code.fixup(skipThen2);
		Code.put(Code.pop);
	}
	
	// ---------- DESIGNATOR STATEMENT ------------------------------------
	
	@Override
	public void visit(DesignatorStatementAssign designatorStatementAssign) {
		Obj varObj = designatorStatementAssign.getDesignator().obj;
		if (varObj.getFpPos() != 5 || varObj.getKind() == Obj.Var) {
			Code.store(varObj);			
		}
		else {
			// here is for final arrays elements assign - arr[2] = val; arr - final array
			// throw trap if arr[i] has already been set. If it wasn't, set arr[i+len] to 1
			// and arr[i] to val
			Obj valObj = new Obj(Obj.Var, "valObj", varObj.getType(), currMethod.getLocalSymbols().size(), 1);
			int skipTrap;
			Code.store(valObj);
			Code.put(Code.dup2);
			Code.put(Code.dup2);
			Code.put(Code.pop);
			Code.put(Code.arraylength);
			Code.loadConst(2);
			Code.put(Code.div);
			Code.put(Code.add);
			Code.put(Code.dup2);
			Code.load(varObj);
			Code.loadConst(0);
			Code.putFalseJump(Code.ne, 0);
			skipTrap = Code.pc - 2;
			Code.put(Code.trap);
			Code.put(5);
			Code.fixup(skipTrap);
			Code.loadConst(1);
			Code.store(varObj);
			Code.load(valObj);
			Code.store(varObj);
		}
	}
	
	@Override
	public void visit(DesignatorStatementInc designatorStatementInc) {
		Designator designator = designatorStatementInc.getDesignator();
		// arr[i]++;
		if (designator instanceof DesignatorArrayIndex) {
			Code.put(Code.dup2);
			Code.load(designator.obj);
			Code.loadConst(1);
			Code.put(Code.add);
			Code.store(designator.obj);
		}
		// a++;
		if (designator instanceof DesignatorVar) {
			if (designator.obj.getLevel() == 0) {
				Code.load(designator.obj);
				Code.loadConst(1);
				Code.put(Code.add);
				Code.store(designator.obj);
			}
			else {
				Code.put(Code.inc);
				Code.put(designator.obj.getAdr());
				Code.put(1);
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatementDec designatorStatementDec) {
		Designator designator = designatorStatementDec.getDesignator();
		// arr[i]--;
		if (designator instanceof DesignatorArrayIndex) {
			Code.put(Code.dup2);
			Code.load(designator.obj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(designator.obj);
		}
		// a--;
		if (designator instanceof DesignatorVar) {
			if (designator.obj.getLevel() == 0) {
				Code.load(designator.obj);
				Code.loadConst(1);
				Code.put(Code.sub);
				Code.store(designator.obj);
			}
			else {
				Code.put(Code.inc);
				Code.put(designator.obj.getAdr());
				Code.put(-1);
			}
		}
	}
	
	@Override
	public void visit(DesignatorStatementFunc designatorStatementFunc) {
		Obj methObj = designatorStatementFunc.getDesignator().obj;
		if (methObj.getName().equals("rotateRight")) {
			// rotateRight(arr, n) - rotates array to right for n positions. One right rotation is moving last element
			// to first position by swapping it place by place to the leftside. It needs to be done n times, so use
			// two loops, inner for one right rotation, outer for n times. 
			Struct type = null;
			for(Obj arrObj: methObj.getLocalSymbols()) {
				type = arrObj.getType();
				break;
			}
			Obj tempObj = new Obj(Obj.Var, "tempObj", type, currMethod.getLocalSymbols().size(), 1);
			Obj repsObj = new Obj(Obj.Var, "repsObj", Tab.intType, currMethod.getLocalSymbols().size() + 1, 1);
			boolean isChar = type.equals(Tab.charType) ? true : false;
			int start, skipThen, startReps, endReps;
			Code.put(Code.dup2);
			Code.put(Code.pop);
			Code.put(Code.arraylength);
			Code.put(Code.rem);
			Code.store(repsObj);
			startReps = Code.pc;
			Code.load(repsObj);
			Code.loadConst(0);
			Code.putFalseJump(Code.ne, 0);
			endReps = Code.pc - 2;
			Code.put(Code.dup);
			Code.put(Code.arraylength);
			Code.loadConst(1);
			Code.put(Code.sub);
			start = Code.pc;
			Code.put(Code.dup);
			Code.loadConst(0);
			Code.putFalseJump(Code.ne, 0);
			skipThen = Code.pc - 2;
			Code.put(Code.dup2);
			Code.put(Code.dup2);
			if (isChar) {
				Code.put(Code.baload);
			}
			else {
				Code.put(Code.aload);
			}
			Code.store(tempObj);
			Code.put(Code.dup2);
			Code.loadConst(1);
			Code.put(Code.sub);
			if (isChar) {
				Code.put(Code.baload);
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.aload);
				Code.put(Code.astore);
			}
			Code.put(Code.dup2);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.load(tempObj);
			if (isChar) {
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.astore);
			}
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.putJump(start);
			Code.fixup(skipThen);
			Code.put(Code.pop);
			Code.load(repsObj);
			Code.loadConst(1);
			Code.put(Code.sub);
			Code.store(repsObj);
			Code.putJump(startReps);
			Code.fixup(endReps);
			Code.put(Code.pop);
		}
		else {
			Code.put(Code.pop);
		}
	}
	
	@Override
	public void visit(DesignatorStatementCaret designatorStatementCaret) {
		// arr ^ NUMCONST - multiplies all array elems with NUMCONST
		Obj arrObj = designatorStatementCaret.getDesignator().obj;
		int mulFactor = designatorStatementCaret.getMulFactor();
		int start, skipThen;
		
		Code.load(arrObj);
		Code.loadConst(0);
		start = Code.pc;
		Code.put(Code.dup);
		Code.load(arrObj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne, 0);
		skipThen = Code.pc - 2;
		Code.put(Code.dup2);
		Code.put(Code.dup2);
		Code.put(Code.aload);
		Code.loadConst(mulFactor);
		Code.put(Code.mul);
		Code.put(Code.astore);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(start);
		Code.fixup(skipThen);
		Code.put(Code.pop);
		Code.put(Code.pop);
	}
	
	// ---------- DESIGNATOR ----------------------------------------------
	
	@Override
	public void visit(DesignatorLen designatorLen) {
		Code.put(Code.arraylength);
	}
	
	@Override
	public void visit(DesignatorHashtag designatorHashtag) {
		// arr# - finds maximum of array elems
		Obj arrObj = designatorHashtag.getDesignatorName().obj;
		Obj maxObj;
		boolean isIntType = arrObj.getType().getElemType().equals(Tab.intType);
		if (isIntType) {
			maxObj = new Obj(Obj.Var, "maxElem", Tab.intType, currMethod.getLocalSymbols().size(), 1);
		}
		else {
			maxObj = new Obj(Obj.Var, "maxElem", Tab.charType, currMethod.getLocalSymbols().size(), 1);
		}
		
		int start, skipThen, skipMaxChange, skipElse, skipTrap;

		Code.loadConst(0);
		Code.load(arrObj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.eq, 0);
		skipTrap = Code.pc - 2;
		Code.put(Code.trap);
		Code.put(0);
		Code.fixup(skipTrap);
		Code.loadConst(0);
		Code.put(Code.dup2);
		if (isIntType) {
			Code.put(Code.aload);
		}
		else {
			Code.put(Code.baload);
		}
		Code.store(maxObj);
		start = Code.pc;
		Code.put(Code.dup);
		Code.load(arrObj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.ne, 0);
		skipThen = Code.pc - 2;
		Code.put(Code.dup2);
		if (isIntType) {
			Code.put(Code.aload);
		}
		else {
			Code.put(Code.baload);
		}
		Code.put(Code.dup);
		Code.load(maxObj);
		Code.putFalseJump(Code.gt, 0);
		skipMaxChange = Code.pc - 2;
		Code.store(maxObj);
		Code.putJump(0);
		skipElse = Code.pc - 2;
		Code.fixup(skipMaxChange);
		Code.put(Code.pop);
		Code.fixup(skipElse);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.putJump(start);
		Code.fixup(skipThen);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.load(maxObj);
	}
	
	@Override
	public void visit(DesignatorName designatorName) {
		SyntaxNode parent = designatorName.getParent();
		if (parent instanceof DesignatorArrayIndex || parent instanceof DesignatorLen || parent instanceof DesignatorHashtag) {
			Code.load(designatorName.obj);
		}
	}
	
	@Override
	public void visit(DesignatorVar designatorVar) {
		// we set fpPos field of array to value of 5 in semantic analyse as an indicator that array is final
		if (designatorVar.obj.getFpPos() == 5) {
			isArrayFinal = true;
		}
		else {
			isArrayFinal = false;
		}
	}
	
	// ---------- TERM ---------------------------------
	
	@Override
	public void visit(TermMore termMore) {
		if (termMore.getMulop() instanceof MulopMul) {
	        Code.put(Code.mul);
	    } 
	    else if (termMore.getMulop() instanceof MulopDiv) {
	        Code.put(Code.div);
	    } 
	    else if (termMore.getMulop() instanceof MulopMod) {
	        Code.put(Code.rem);
	    }
	}
	
	// ---------- CONDOP MODIF ---------------------------
	
	int skipToOne, skipToMinus, skipToEnd[];
	
	@Override
	public void visit(CondOperatorMore condOperatorMore) {
		// factor1 <=> factor2 - result is 1 if factor1 > factor2, 0 if equal, -1 if factor1 < factor2
		// this operator has biggest priority, after unary minus
		skipToEnd = new int[2];
		Code.put(Code.dup2);
		Code.putFalseJump(Code.le, 0);
		skipToOne = Code.pc - 2;
		Code.putFalseJump(Code.ge, 0);
		skipToMinus = Code.pc - 2;
		Code.loadConst(0);
		Code.putJump(0);
		skipToEnd[0] = Code.pc - 2;
		Code.fixup(skipToMinus);
		Code.loadConst(-1);
		Code.putJump(0);
		skipToEnd[1] = Code.pc - 2;
		Code.fixup(skipToOne);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.loadConst(1);
		for (int pc: skipToEnd) {
			Code.fixup(pc);
		}
	}
	
	// --------- CONDFACT ------------------------

	Deque<Integer> skipThenStack = new ArrayDeque<>();
	Deque<Integer> skipElseStack = new ArrayDeque<>();
	
	@Override
	public void visit(CondFactExpr condFactExpr) {
		Code.loadConst(0);
		// if condition is false, skip THEN code, jump to ELSE code
		Code.putFalseJump(Code.ne, 0);
		skipThenStack.push(Code.pc - 2);
	}
	
	@Override
	public void visit(CondFactRelop condFactRelop) {
		int op = 0;

	    if (condFactRelop.getRelop() instanceof RelopEq) op = Code.eq;
	    else if (condFactRelop.getRelop() instanceof RelopNe) op = Code.ne;
	    else if (condFactRelop.getRelop() instanceof RelopGt) op = Code.gt;
	    else if (condFactRelop.getRelop() instanceof RelopGe) op = Code.ge;
	    else if (condFactRelop.getRelop() instanceof RelopLt) op = Code.lt;
	    else if (condFactRelop.getRelop() instanceof RelopLe) op = Code.le;
	    
	    Code.putFalseJump(op, 0);
	    skipThenStack.push(Code.pc - 2);
	}
	
	// ----------- EXPR ---------------------------------------
		
		@Override
		public void visit(ColonMark colonMark) {
			// You done then code, so now skip ELSE, jump to the end 
			Code.putJump(0);
			skipElseStack.push(Code.pc - 2);
			// This should be hop destination for jumps that wanted to skip THEN code, so fix them
			// This is start of ELSE code
			Code.fixup(skipThenStack.pop());
		}
		
		@Override
		public void visit(ExprTern exprTern) {
			// This is hop destination for jumps that wanted to skip ELSE code, so fix them
			// This is finish of ELSE code, end
			Code.fixup(skipElseStack.pop());
		}
		
		@Override
		public void visit(ExprNonTernMore exprNonTernMore) {
			if (exprNonTernMore.getAddop() instanceof AddopPlus) {
		        Code.put(Code.add);
		    } 
		    else if (exprNonTernMore.getAddop() instanceof AddopMinus) {
		        Code.put(Code.sub);
		    }
		}
		
		// ---------- MODIF ?? ---------------
		
		Deque<Integer> skipSecodOperator = new ArrayDeque<>();
		
		@Override
		public void visit(ExprDupQueOpMore exprDupQueOpMore) {
			Code.fixup(skipSecodOperator.pop());
		}
		
		@Override
		public void visit(DupQueOpMark DupQueOpMark) {
			// expr -> expr1 ?? expr2 - result is expr1 if expr1 != 0 and expr2 if expr1 == 0
			// this operator has smallest priority
			Code.put(Code.dup);
			Code.loadConst(0);
			Code.putFalseJump(Code.eq, 0);
			skipSecodOperator.push(Code.pc - 2);
			Code.put(Code.pop);
		}
		
	// ----------- STATEMENTS -----------------------------------
		
		@Override
		public void visit(ElseYes elseYes) {
			Code.fixup(skipElseStack.pop());
		}
		
		@Override
		public void visit(ElseEps elseEps) {
			Code.fixup(skipThenStack.pop());
		}
		
		@Override
		public void visit(ElseMark elseMark) {
			Code.putJump(0);
			skipElseStack.push(Code.pc - 2);
			Code.fixup(skipThenStack.pop());			
		}
		
		@Override
		public void visit(StatementPrint statementPrint) {
			if (!(statementPrint.getExpr().struct.getKind() == Struct.Array)) {
				if (statementPrint.getPrintWidth() instanceof PrintWidthEps) {
					Code.loadConst(5);
				}
				if (statementPrint.getExpr().struct == Tab.charType) {
					Code.put(Code.bprint);
				}
				else {
					Code.put(Code.print);
				}
			}
			else {
				// print(arr) - prints array elems; print(arr, i) - prints i-th array elem
				boolean isArrayChar = statementPrint.getExpr().struct.getElemType().equals(Tab.charType) ? true : false;
				if (statementPrint.getPrintWidth() instanceof PrintWidthYes) {
					Code.put(Code.dup2);
					Code.put(Code.dup_x1);
					Code.put(Code.pop);
					Code.put(Code.arraylength);
					Code.putFalseJump(Code.ge, Code.pc + 5);
					Code.put(Code.trap);
					Code.put(1);
					
					
					if (isArrayChar) {
						Code.put(Code.baload);
						Code.loadConst(5);
						Code.put(Code.bprint);
					}
					else {
						Code.put(Code.aload);
						Code.loadConst(5);
						Code.put(Code.print);
					}
				}
				else {
					int start, skipThen;
					Code.loadConst(0);
					start = Code.pc;
					Code.put(Code.dup2);	
					Code.put(Code.dup_x1);	
					Code.put(Code.pop);	
					Code.put(Code.arraylength);	
					Code.putFalseJump(Code.ne, 0);
					skipThen = Code.pc - 2;
					Code.put(Code.dup2);
					if (isArrayChar) {
						Code.put(Code.baload);
					}
					else {
						Code.put(Code.aload);
					}
					Code.put(Code.const_5);
					if (isArrayChar) {
						Code.put(Code.bprint);
					}
					else {
						Code.put(Code.print);
					}
					Code.loadConst(1);
					Code.put(Code.add);
					Code.putJump(start);
					Code.fixup(skipThen);
					Code.put(Code.pop);
					Code.put(Code.pop);
				}
			}
		}
		
		@Override
		public void visit(PrintWidthYes printWidthYes) {
			Code.loadConst(printWidthYes.getPrintWidthValue());
		}
		
		@Override
		public void visit(StatementRead statementRead) {
			Obj arrObj = statementRead.getDesignator().obj;
			if (arrObj.getType().getKind() == Struct.Array) {
				// read(arr) - reads elements from input and puts them in array
				int start, skipThen;
				boolean isChar = arrObj.getType().getElemType().equals(Tab.charType) ? true : false;
				Code.load(arrObj);
				Code.loadConst(0);
				start = Code.pc;
				Code.put(Code.dup);
				Code.load(arrObj);
				Code.put(Code.arraylength);
				Code.putFalseJump(Code.ne, 0);
				skipThen = Code.pc - 2;
				Code.put(Code.dup2);
				if (isChar) {
					Code.put(Code.bread);
					Code.put(Code.bastore);
				}
				else {
					Code.put(Code.read);
					Code.put(Code.astore);
				}
				Code.loadConst(1);
				Code.put(Code.add);
				Code.putJump(start);
				Code.fixup(skipThen);
				Code.put(Code.pop);
				Code.put(Code.pop);
			}
			else {
				if (arrObj.getType() == Tab.charType) {
					Code.put(Code.bread);
				}
				else {
					Code.put(Code.read);
				}
				Code.store(arrObj);
			}
		}
		
}
