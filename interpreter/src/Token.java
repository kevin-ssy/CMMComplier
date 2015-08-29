
public class Token {
	
	String str;
	int type;
	int arrayBound = 0;
	
	public Token(String s, int t){
		str = s;
		type = t;
	}
	public Token(String s, int t,int ab){
		str = s;
		type = t;
		arrayBound = ab;
	}
	
	public String getString(){
		return str;
	}
	
	public int getType(){
		return type;
	}
	public int getArrayBound() {
		return arrayBound;
	}
	public void setArrayBound(int arrayBound) {
		this.arrayBound = arrayBound;
	}

}
