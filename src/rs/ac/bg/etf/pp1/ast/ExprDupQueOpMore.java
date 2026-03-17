// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class ExprDupQueOpMore extends ExprDupQueOp {

    private ExprDupQueOp ExprDupQueOp;
    private DupQueOpMark DupQueOpMark;
    private ExprSub ExprSub;

    public ExprDupQueOpMore (ExprDupQueOp ExprDupQueOp, DupQueOpMark DupQueOpMark, ExprSub ExprSub) {
        this.ExprDupQueOp=ExprDupQueOp;
        if(ExprDupQueOp!=null) ExprDupQueOp.setParent(this);
        this.DupQueOpMark=DupQueOpMark;
        if(DupQueOpMark!=null) DupQueOpMark.setParent(this);
        this.ExprSub=ExprSub;
        if(ExprSub!=null) ExprSub.setParent(this);
    }

    public ExprDupQueOp getExprDupQueOp() {
        return ExprDupQueOp;
    }

    public void setExprDupQueOp(ExprDupQueOp ExprDupQueOp) {
        this.ExprDupQueOp=ExprDupQueOp;
    }

    public DupQueOpMark getDupQueOpMark() {
        return DupQueOpMark;
    }

    public void setDupQueOpMark(DupQueOpMark DupQueOpMark) {
        this.DupQueOpMark=DupQueOpMark;
    }

    public ExprSub getExprSub() {
        return ExprSub;
    }

    public void setExprSub(ExprSub ExprSub) {
        this.ExprSub=ExprSub;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprDupQueOp!=null) ExprDupQueOp.accept(visitor);
        if(DupQueOpMark!=null) DupQueOpMark.accept(visitor);
        if(ExprSub!=null) ExprSub.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprDupQueOp!=null) ExprDupQueOp.traverseTopDown(visitor);
        if(DupQueOpMark!=null) DupQueOpMark.traverseTopDown(visitor);
        if(ExprSub!=null) ExprSub.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprDupQueOp!=null) ExprDupQueOp.traverseBottomUp(visitor);
        if(DupQueOpMark!=null) DupQueOpMark.traverseBottomUp(visitor);
        if(ExprSub!=null) ExprSub.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDupQueOpMore(\n");

        if(ExprDupQueOp!=null)
            buffer.append(ExprDupQueOp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DupQueOpMark!=null)
            buffer.append(DupQueOpMark.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprSub!=null)
            buffer.append(ExprSub.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDupQueOpMore]");
        return buffer.toString();
    }
}
