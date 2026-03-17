// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class VarDeclList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private FinalKeyword FinalKeyword;
    private Type Type;
    private VarDeclInner VarDeclInner;

    public VarDeclList (FinalKeyword FinalKeyword, Type Type, VarDeclInner VarDeclInner) {
        this.FinalKeyword=FinalKeyword;
        if(FinalKeyword!=null) FinalKeyword.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarDeclInner=VarDeclInner;
        if(VarDeclInner!=null) VarDeclInner.setParent(this);
    }

    public FinalKeyword getFinalKeyword() {
        return FinalKeyword;
    }

    public void setFinalKeyword(FinalKeyword FinalKeyword) {
        this.FinalKeyword=FinalKeyword;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public VarDeclInner getVarDeclInner() {
        return VarDeclInner;
    }

    public void setVarDeclInner(VarDeclInner VarDeclInner) {
        this.VarDeclInner=VarDeclInner;
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
        if(FinalKeyword!=null) FinalKeyword.accept(visitor);
        if(Type!=null) Type.accept(visitor);
        if(VarDeclInner!=null) VarDeclInner.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FinalKeyword!=null) FinalKeyword.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarDeclInner!=null) VarDeclInner.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FinalKeyword!=null) FinalKeyword.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarDeclInner!=null) VarDeclInner.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclList(\n");

        if(FinalKeyword!=null)
            buffer.append(FinalKeyword.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclInner!=null)
            buffer.append(VarDeclInner.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclList]");
        return buffer.toString();
    }
}
