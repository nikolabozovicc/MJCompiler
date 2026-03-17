// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementFunc extends DesignatorStatement {

    private Designator Designator;
    private DesignStmtFuncPars DesignStmtFuncPars;

    public DesignatorStatementFunc (Designator Designator, DesignStmtFuncPars DesignStmtFuncPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignStmtFuncPars=DesignStmtFuncPars;
        if(DesignStmtFuncPars!=null) DesignStmtFuncPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignStmtFuncPars getDesignStmtFuncPars() {
        return DesignStmtFuncPars;
    }

    public void setDesignStmtFuncPars(DesignStmtFuncPars DesignStmtFuncPars) {
        this.DesignStmtFuncPars=DesignStmtFuncPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignStmtFuncPars!=null) DesignStmtFuncPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignStmtFuncPars!=null) DesignStmtFuncPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignStmtFuncPars!=null) DesignStmtFuncPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementFunc(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignStmtFuncPars!=null)
            buffer.append(DesignStmtFuncPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementFunc]");
        return buffer.toString();
    }
}
