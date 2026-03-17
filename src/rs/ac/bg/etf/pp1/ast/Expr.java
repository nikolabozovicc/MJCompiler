// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class Expr implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private ExprDupQueOp ExprDupQueOp;

    public Expr (ExprDupQueOp ExprDupQueOp) {
        this.ExprDupQueOp=ExprDupQueOp;
        if(ExprDupQueOp!=null) ExprDupQueOp.setParent(this);
    }

    public ExprDupQueOp getExprDupQueOp() {
        return ExprDupQueOp;
    }

    public void setExprDupQueOp(ExprDupQueOp ExprDupQueOp) {
        this.ExprDupQueOp=ExprDupQueOp;
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
        if(ExprDupQueOp!=null) ExprDupQueOp.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprDupQueOp!=null) ExprDupQueOp.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprDupQueOp!=null) ExprDupQueOp.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr(\n");

        if(ExprDupQueOp!=null)
            buffer.append(ExprDupQueOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr]");
        return buffer.toString();
    }
}
