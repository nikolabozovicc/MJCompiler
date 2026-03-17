// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class EnumDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String enumValueName;
    private EnumDeclInit EnumDeclInit;

    public EnumDecl (String enumValueName, EnumDeclInit EnumDeclInit) {
        this.enumValueName=enumValueName;
        this.EnumDeclInit=EnumDeclInit;
        if(EnumDeclInit!=null) EnumDeclInit.setParent(this);
    }

    public String getEnumValueName() {
        return enumValueName;
    }

    public void setEnumValueName(String enumValueName) {
        this.enumValueName=enumValueName;
    }

    public EnumDeclInit getEnumDeclInit() {
        return EnumDeclInit;
    }

    public void setEnumDeclInit(EnumDeclInit EnumDeclInit) {
        this.EnumDeclInit=EnumDeclInit;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDeclInit!=null) EnumDeclInit.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDeclInit!=null) EnumDeclInit.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDeclInit!=null) EnumDeclInit.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecl(\n");

        buffer.append(" "+tab+enumValueName);
        buffer.append("\n");

        if(EnumDeclInit!=null)
            buffer.append(EnumDeclInit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDecl]");
        return buffer.toString();
    }
}
