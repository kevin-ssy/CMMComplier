import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class Splitter {

	public ArrayList<String> declaredArray = new ArrayList<String>();
	final int INTEGER = 257;
	final int DOUBLE = 258;
	final int IDENT = 260;
	final int CONSTH = 261;
	final int ARRAY = 262;

	private Character c = ' ';
	File f;
	BufferedReader reader;
	String[] ar;
	ArrayList<String> al;
	Token t;

	final char END = (char) -1; // used for finding end of input

	public Splitter(File file) throws Exception {

		f = file;
		reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(f), Charset.forName("UTF-8")));

		// can also use hashmap. 0-7,8-19,21-30-34
		// <cin> | <cout> | <if> | <assign> | <while>

		ar = new String[] { "read", "write", "if", "while", "assign-holder",
				"int", "char", "else", ";", ",", "{", "}", "||", "&&", "!",
				"|", "^", "&", "~", "<", "==", "!=", "(", ")", "<=", "<", ">",
				">=", "=", "+", "-", "*", "/", "%", "<<", ">>" };

		al = new ArrayList<String>(Arrays.asList(ar));
	}

	public Token getToken() throws Exception {

		String str = "";
		@SuppressWarnings("unused")
		int n;

		// only if ' '
		while (Character.isWhitespace(c)) {
			int re = reader.read();
			c = (char) re;
		}

		String ch = Character.toString(c); // (can use same string as str)

		if (Character.isDigit(c)) {
			do {
				str = str + c;
				c = (char) reader.read();
			} while (Character.isDigit(c)); // always holds next char*
			n = Integer.parseInt(str);

			t = new Token(str, INTEGER);
		} else if (ch.equals("/")) {
			c = (char) reader.read();
			if (Character.toString(c).equals("*")) {
				while (true) {
					c = (char) reader.read();
					if (Character.toString(c).equals("*")) {
						c = (char) reader.read();
						if (Character.toString(c).equals("/")) {
							c = (char) reader.read();
							while (Character.isWhitespace(c)) {
								int re = reader.read();
								c = (char) re;
							}
							break;
						}
					}
				}
				getToken();
			} else if (Character.toString(c).equals("/")) {
				while (true) {
					c = (char) reader.read();
					if (Character.toString(c).equals("\n")) {
						c = (char) reader.read();
						break;
					}
				}
				getToken();
			}
		}

		// check for decimal
		// if (i<len-1 && s.substring(i, i+1).equals(".")){
		// //repeat
		// i++;
		// while(i<len && Character.isDigit(s.charAt(i))){
		// i++;
		// }
		// t = new Token(s.substring(h, i), DOUBLE);
		// }

		else if (Character.isLetter(c)) {
			int arrayBound = 0;
			do {
				str = str + c;
				c = (char) reader.read();
				if(String.valueOf(c).equals("[")){
					str = str + c;
					c = (char) reader.read();
					if(Character.isDigit(c)){
						arrayBound = Integer.valueOf(Character.toString(c));
					}
				}
			} while (Character.isLetterOrDigit(c)
					|| String.valueOf(c).equals("[")
					|| String.valueOf(c).equals("]"));

			if (al.contains(str))
				t = new Token(str, al.indexOf(str));
			else if(str.contains("[")){
				String arName = str.split("\\[")[0];
				if(declaredArray.contains(arName)){
					t = new Token(str, IDENT);
				}else{
					declaredArray.add(arName);
					t = new Token(str, ARRAY,arrayBound);
				}
			}else{
				t = new Token(str, IDENT);
			}
//			else{
//				if (str.contains("[")) {
//					t = new Token(str, ARRAY,arrayBound);
//			}
//				t = new Token(str, IDENT);
//			}
			
		}

		// ELSE ITS NOT ABC OR INT SO IT'S A Random CHAR. CHECK WHICH ONE..

		else if (al.contains(ch)) {
			// check for different 2 char possibilities..
			c = (char) reader.read();
			str = str + ch + c;

			if (al.contains(str)) {
				t = new Token(str, al.indexOf(str));
				c = (char) reader.read(); // so as to always keep next char*
			} else {
				t = new Token(ch, al.indexOf(ch));
			}
		}

		// also check for "" around strings

		// check if end of input
		else if (c.equals(END)) {
			// //System.out.println("end of input");
			t = new Token("null", 500); // or null
		}

		else {
			// invalid character
			// System.out.println("token debug: " + ch);
			t = new Token("error", 501); // 501 error code
		}

		return t;

		// continue with c next time...

	}

	public void printTokens() throws Exception {

		Token tok = getToken();
		while (tok.getType() != 500) {
			// System.out.println(t.getString() + " t:" + t.getType() + "|");
			tok = getToken();
		}
	}

}
