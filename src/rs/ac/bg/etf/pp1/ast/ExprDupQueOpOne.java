// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class ExprDupQueOpOne extends ExprDupQueOp {

    private ExprSub ExprSub;

    public ExprDupQueOpOne (ExprSub ExprSub) {
        this.ExprSub=ExprSub;
        if(ExprSub!=null) ExprSub.setParent(this);
    }

    public ExprSub getExprSub() {
        return ExprSub;
    }

    public void setExprSub(ExprSub ExprSub) {
        this.ExprSub=ExprSub;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprSub!=null) ExprSub.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprSub!=null) ExprSub.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprSub!=null) ExprSub.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDupQueOpOne(\n");

        if(ExprSub!=null)
            buffer.append(ExprSub.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDupQueOpOne]");
        return buffer.toString();
    }
}
