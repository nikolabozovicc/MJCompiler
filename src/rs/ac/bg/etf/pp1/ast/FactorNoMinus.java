// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class FactorNoMinus extends Factor {

    private FactorOptions FactorOptions;

    public FactorNoMinus (FactorOptions FactorOptions) {
        this.FactorOptions=FactorOptions;
        if(FactorOptions!=null) FactorOptions.setParent(this);
    }

    public FactorOptions getFactorOptions() {
        return FactorOptions;
    }

    public void setFactorOptions(FactorOptions FactorOptions) {
        this.FactorOptions=FactorOptions;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FactorOptions!=null) FactorOptions.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FactorOptions!=null) FactorOptions.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FactorOptions!=null) FactorOptions.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNoMinus(\n");

        if(FactorOptions!=null)
            buffer.append(FactorOptions.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNoMinus]");
        return buffer.toString();
    }
}
