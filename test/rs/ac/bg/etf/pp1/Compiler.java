package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/program.mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			// AST forming
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        
			// AST print
			log.info(prog.toString(""));
			log.info("===================================");
			
			// Symbol table initialization
			Tab.init();
			Struct boolType = new Struct(Struct.Bool);
			Obj boolObj = Tab.insert(Obj.Type, "bool", boolType);
			boolObj.setAdr(-1);
			boolObj.setLevel(-1);
			
			Obj rotateObj = null;
			Tab.currentScope().addToLocals(rotateObj = new Obj(Obj.Meth, "rotateRight", Tab.noType, 0, 2));
			Tab.openScope();
			Tab.currentScope().addToLocals(new Obj(Obj.Var, "arr", new Struct(Struct.Array, Tab.noType), 0, 1));
			Tab.currentScope().addToLocals(new Obj(Obj.Var, "reps", Tab.intType, 1, 1));
			Tab.chainLocalSymbols(rotateObj);
			Tab.closeScope();
			
			// Change chr, ord, len parameter fpPos field to 1
			for(Obj obj: Tab.currentScope().values()) {
				if (obj.getKind() == Obj.Meth) {
					obj.getLocalSymbols().forEach(paramObj -> paramObj.setFpPos(1));
				}
			}
			
			// Semantic analyze
			SemanticAnalyzer sa = new SemanticAnalyzer();
			
			prog.traverseBottomUp(sa);
			
			// Symbol table print
			log.info("===================================");
			Tab.dump();
			
			if (!p.errorDetected && sa.passed()) {
				log.info("Compilation completed successfully");
				
				// Code Generation
				File objFile = new File("test/program.obj");
				if (objFile.exists()) objFile.delete();
				
				CodeGenerator cg = new CodeGenerator();
				prog.traverseBottomUp(cg);
				Code.dataSize = sa.getnVars();
				Code.mainPc = cg.getmainPc();
				Code.write(new FileOutputStream(objFile));
			}
			else {
				log.error("Compilation FAILED");
			}
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}