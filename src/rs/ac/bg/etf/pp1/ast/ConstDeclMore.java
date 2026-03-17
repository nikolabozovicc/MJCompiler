// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclMore extends ConstDeclInner {

    private ConstDeclInner ConstDeclInner;
    private ConstDecl ConstDecl;

    public ConstDeclMore (ConstDeclInner ConstDeclInner, ConstDecl ConstDecl) {
        this.ConstDeclInner=ConstDeclInner;
        if(ConstDeclInner!=null) ConstDeclInner.setParent(this);
        this.ConstDecl=ConstDecl;
        if(ConstDecl!=null) ConstDecl.setParent(this);
    }

    public ConstDeclInner getConstDeclInner() {
        return ConstDeclInner;
    }

    public void setConstDeclInner(ConstDeclInner ConstDeclInner) {
        this.ConstDeclInner=ConstDeclInner;
    }

    public ConstDecl getConstDecl() {
        return ConstDecl;
    }

    public void setConstDecl(ConstDecl ConstDecl) {
        this.ConstDecl=ConstDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclInner!=null) ConstDeclInner.accept(visitor);
        if(ConstDecl!=null) ConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclInner!=null) ConstDeclInner.traverseTopDown(visitor);
        if(ConstDecl!=null) ConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclInner!=null) ConstDeclInner.traverseBottomUp(visitor);
        if(ConstDecl!=null) ConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclMore(\n");

        if(ConstDeclInner!=null)
            buffer.append(ConstDeclInner.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDecl!=null)
            buffer.append(ConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclMore]");
        return buffer.toString();
    }
}
