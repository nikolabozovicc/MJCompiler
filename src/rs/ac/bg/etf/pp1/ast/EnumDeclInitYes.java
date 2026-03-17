// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclInitYes extends EnumDeclInit {

    private Integer enumInitValue;

    public EnumDeclInitYes (Integer enumInitValue) {
        this.enumInitValue=enumInitValue;
    }

    public Integer getEnumInitValue() {
        return enumInitValue;
    }

    public void setEnumInitValue(Integer enumInitValue) {
        this.enumInitValue=enumInitValue;
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
        buffer.append("EnumDeclInitYes(\n");

        buffer.append(" "+tab+enumInitValue);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclInitYes]");
        return buffer.toString();
    }
}
