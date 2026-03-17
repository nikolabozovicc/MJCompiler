// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class VarDeclMore extends VarDeclInner {

    private VarDeclInner VarDeclInner;
    private VarDecl VarDecl;

    public VarDeclMore (VarDeclInner VarDeclInner, VarDecl VarDecl) {
        this.VarDeclInner=VarDeclInner;
        if(VarDeclInner!=null) VarDeclInner.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public VarDeclInner getVarDeclInner() {
        return VarDeclInner;
    }

    public void setVarDeclInner(VarDeclInner VarDeclInner) {
        this.VarDeclInner=VarDeclInner;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclInner!=null) VarDeclInner.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclInner!=null) VarDeclInner.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclInner!=null) VarDeclInner.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclMore(\n");

        if(VarDeclInner!=null)
            buffer.append(VarDeclInner.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclMore]");
        return buffer.toString();
    }
}
