// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementCaret extends DesignatorStatement {

    private Designator Designator;
    private Integer mulFactor;

    public DesignatorStatementCaret (Designator Designator, Integer mulFactor) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.mulFactor=mulFactor;
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public Integer getMulFactor() {
        return mulFactor;
    }

    public void setMulFactor(Integer mulFactor) {
        this.mulFactor=mulFactor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementCaret(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+mulFactor);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementCaret]");
        return buffer.toString();
    }
}
