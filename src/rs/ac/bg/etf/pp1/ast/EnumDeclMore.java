// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclMore extends EnumDeclInner {

    private EnumDeclInner EnumDeclInner;
    private EnumDecl EnumDecl;

    public EnumDeclMore (EnumDeclInner EnumDeclInner, EnumDecl EnumDecl) {
        this.EnumDeclInner=EnumDeclInner;
        if(EnumDeclInner!=null) EnumDeclInner.setParent(this);
        this.EnumDecl=EnumDecl;
        if(EnumDecl!=null) EnumDecl.setParent(this);
    }

    public EnumDeclInner getEnumDeclInner() {
        return EnumDeclInner;
    }

    public void setEnumDeclInner(EnumDeclInner EnumDeclInner) {
        this.EnumDeclInner=EnumDeclInner;
    }

    public EnumDecl getEnumDecl() {
        return EnumDecl;
    }

    public void setEnumDecl(EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDeclInner!=null) EnumDeclInner.accept(visitor);
        if(EnumDecl!=null) EnumDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDeclInner!=null) EnumDeclInner.traverseTopDown(visitor);
        if(EnumDecl!=null) EnumDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDeclInner!=null) EnumDeclInner.traverseBottomUp(visitor);
        if(EnumDecl!=null) EnumDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclMore(\n");

        if(EnumDeclInner!=null)
            buffer.append(EnumDeclInner.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDecl!=null)
            buffer.append(EnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclMore]");
        return buffer.toString();
    }
}
