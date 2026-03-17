// generated with ast extension for cup
// version 0.8
// 17/2/2026 20:52:30


package rs.ac.bg.etf.pp1.ast;

public class TermMore extends Term {

    private Term Term;
    private Mulop Mulop;
    private CondOperator CondOperator;

    public TermMore (Term Term, Mulop Mulop, CondOperator CondOperator) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.CondOperator=CondOperator;
        if(CondOperator!=null) CondOperator.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public CondOperator getCondOperator() {
        return CondOperator;
    }

    public void setCondOperator(CondOperator CondOperator) {
        this.CondOperator=CondOperator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Term!=null) Term.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(CondOperator!=null) CondOperator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(CondOperator!=null) CondOperator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(CondOperator!=null) CondOperator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermMore(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondOperator!=null)
            buffer.append(CondOperator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermMore]");
        return buffer.toString();
    }
}
