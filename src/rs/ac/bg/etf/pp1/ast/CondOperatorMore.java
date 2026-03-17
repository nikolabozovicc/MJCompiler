// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class CondOperatorMore extends CondOperator {

    private CondOperator CondOperator;
    private Factor Factor;

    public CondOperatorMore (CondOperator CondOperator, Factor Factor) {
        this.CondOperator=CondOperator;
        if(CondOperator!=null) CondOperator.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public CondOperator getCondOperator() {
        return CondOperator;
    }

    public void setCondOperator(CondOperator CondOperator) {
        this.CondOperator=CondOperator;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondOperator!=null) CondOperator.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondOperator!=null) CondOperator.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondOperator!=null) CondOperator.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondOperatorMore(\n");

        if(CondOperator!=null)
            buffer.append(CondOperator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondOperatorMore]");
        return buffer.toString();
    }
}
