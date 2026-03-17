// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class ExprTern implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private CondFact CondFact;
    private ExprSub ExprSub;
    private ColonMark ColonMark;
    private ExprSub ExprSub1;

    public ExprTern (CondFact CondFact, ExprSub ExprSub, ColonMark ColonMark, ExprSub ExprSub1) {
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
        this.ExprSub=ExprSub;
        if(ExprSub!=null) ExprSub.setParent(this);
        this.ColonMark=ColonMark;
        if(ColonMark!=null) ColonMark.setParent(this);
        this.ExprSub1=ExprSub1;
        if(ExprSub1!=null) ExprSub1.setParent(this);
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public ExprSub getExprSub() {
        return ExprSub;
    }

    public void setExprSub(ExprSub ExprSub) {
        this.ExprSub=ExprSub;
    }

    public ColonMark getColonMark() {
        return ColonMark;
    }

    public void setColonMark(ColonMark ColonMark) {
        this.ColonMark=ColonMark;
    }

    public ExprSub getExprSub1() {
        return ExprSub1;
    }

    public void setExprSub1(ExprSub ExprSub1) {
        this.ExprSub1=ExprSub1;
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
        if(CondFact!=null) CondFact.accept(visitor);
        if(ExprSub!=null) ExprSub.accept(visitor);
        if(ColonMark!=null) ColonMark.accept(visitor);
        if(ExprSub1!=null) ExprSub1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
        if(ExprSub!=null) ExprSub.traverseTopDown(visitor);
        if(ColonMark!=null) ColonMark.traverseTopDown(visitor);
        if(ExprSub1!=null) ExprSub1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        if(ExprSub!=null) ExprSub.traverseBottomUp(visitor);
        if(ColonMark!=null) ColonMark.traverseBottomUp(visitor);
        if(ExprSub1!=null) ExprSub1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprTern(\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprSub!=null)
            buffer.append(ExprSub.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ColonMark!=null)
            buffer.append(ColonMark.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprSub1!=null)
            buffer.append(ExprSub1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprTern]");
        return buffer.toString();
    }
}
