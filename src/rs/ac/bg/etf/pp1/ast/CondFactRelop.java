// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class CondFactRelop extends CondFact {

    private ExprNonTern ExprNonTern;
    private Relop Relop;
    private ExprNonTern ExprNonTern1;

    public CondFactRelop (ExprNonTern ExprNonTern, Relop Relop, ExprNonTern ExprNonTern1) {
        this.ExprNonTern=ExprNonTern;
        if(ExprNonTern!=null) ExprNonTern.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.ExprNonTern1=ExprNonTern1;
        if(ExprNonTern1!=null) ExprNonTern1.setParent(this);
    }

    public ExprNonTern getExprNonTern() {
        return ExprNonTern;
    }

    public void setExprNonTern(ExprNonTern ExprNonTern) {
        this.ExprNonTern=ExprNonTern;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public ExprNonTern getExprNonTern1() {
        return ExprNonTern1;
    }

    public void setExprNonTern1(ExprNonTern ExprNonTern1) {
        this.ExprNonTern1=ExprNonTern1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprNonTern!=null) ExprNonTern.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(ExprNonTern1!=null) ExprNonTern1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprNonTern!=null) ExprNonTern.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(ExprNonTern1!=null) ExprNonTern1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprNonTern!=null) ExprNonTern.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(ExprNonTern1!=null) ExprNonTern1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactRelop(\n");

        if(ExprNonTern!=null)
            buffer.append(ExprNonTern.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprNonTern1!=null)
            buffer.append(ExprNonTern1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactRelop]");
        return buffer.toString();
    }
}
