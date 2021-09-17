package Shared;
public final class Trio<O1, O2, O3>{  
	private Dual<O1, O2> dual;
	private O3 value3;

	public Trio(O1 v1, O2 v2, O3 v3){
		dual = new Dual<>(v1, v2);
		value3 = v3;
	}

	public O1 getValue1() { return dual.getValue1(); }
	public O2 getValue2() { return dual.getValue2(); }
	public O3 getValue3() { return value3; }
	public void setValue1(O1 v) { dual.setValue1(v); }
	public void setValue2(O2 v) { dual.setValue2(v); }
	public void setValue3(O3 v) { value3 = v; }
}