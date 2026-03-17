// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String varName;
    private VarDeclBrackets VarDeclBrackets;

    public VarDecl (String varName, VarDeclBrackets VarDeclBrackets) {
        this.varName=varName;
        this.VarDeclBrackets=VarDeclBrackets;
        if(VarDeclBrackets!=null) VarDeclBrackets.setParent(this);
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public VarDeclBrackets getVarDeclBrackets() {
        return VarDeclBrackets;
    }

    public void setVarDeclBrackets(VarDeclBrackets VarDeclBrackets) {
        this.VarDeclBrackets=VarDeclBrackets;
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
        if(VarDeclBrackets!=null) VarDeclBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclBrackets!=null) VarDeclBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclBrackets!=null) VarDeclBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(VarDeclBrackets!=null)
            buffer.append(VarDeclBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
