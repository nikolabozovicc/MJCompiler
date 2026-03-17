// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class PrintWidthYes extends PrintWidth {

    private Integer printWidthValue;

    public PrintWidthYes (Integer printWidthValue) {
        this.printWidthValue=printWidthValue;
    }

    public Integer getPrintWidthValue() {
        return printWidthValue;
    }

    public void setPrintWidthValue(Integer printWidthValue) {
        this.printWidthValue=printWidthValue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintWidthYes(\n");

        buffer.append(" "+tab+printWidthValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintWidthYes]");
        return buffer.toString();
    }
}
