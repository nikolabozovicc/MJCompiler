// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class TermOne extends Term {

    private CondOperator CondOperator;

    public TermOne (CondOperator CondOperator) {
        this.CondOperator=CondOperator;
        if(CondOperator!=null) CondOperator.setParent(this);
    }

    public CondOperator getCondOperator() {
        return CondOperator;
    }

    public void setCondOperator(CondOperator CondOperator) {
        this.CondOperator=CondOperator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondOperator!=null) CondOperator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondOperator!=null) CondOperator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondOperator!=null) CondOperator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermOne(\n");

        if(CondOperator!=null)
            buffer.append(CondOperator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermOne]");
        return buffer.toString();
    }
}
