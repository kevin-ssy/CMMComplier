import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class Parse {
	public ArrayList<Token> tokens = new ArrayList<Token>();
	public static HashMap<String, Integer> declaredArray = new HashMap<String, Integer>();
	Splitter split;
	Token tok;
	String text; // token string. use
	int val; // token value
	Node root;
	int arrayBound = 0;
	JTree grammarTree;

	final int INTEGER = 257;
	final int DOUBLE = 258;
	final int IDENT = 260;
	final int CONSTH = 261;
	final int ARRAY = 262;

	public Parse(Splitter spl) {
		split = spl;
		program();
		// TreeWin win = new TreeWin();
		// win.validate();
	}

	// class TreeWin extends JFrame {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	//
	// TreeWin() {
	// String tokensToShow = new String();
	// setTitle("grammar tree");
	// setSize(600, 400);
	// setVisible(true);
	// setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	// Container con = getContentPane();
	// JScrollPane scrollpane = new JScrollPane(grammarTree);
	// con.add(scrollpane);
	// JFrame newjf = new JFrame("lexical");
	// newjf.setSize(300, 250);
	// newjf.setVisible(true);
	// newjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	// Container newcon = newjf.getContentPane();
	//
	// Iterator<Token> tokensIt = tokens.iterator();
	// while (tokensIt.hasNext()) {
	// Token nextToken = tokensIt.next();
	// String temp = "<" + nextToken.getString() + ","
	// + nextToken.getType() + ">" + "\n";
	// tokensToShow += temp;
	// }
	// JTextArea jta = new JTextArea();
	// jta.setText(tokensToShow);
	// newcon.add(jta);
	// JScrollPane newscrollpane = new JScrollPane(jta);
	// newcon.add(newscrollpane);
	// }
	// }

	/**
	 * to recognize the first token of the input and to build the root of the
	 * tree
	 */
	// TODO
	public void program() {
		// to build the root of the tree
		root = new Node(1, null, 1, "root");
		DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("root");

		// System.out.println("in program: " + text);
		lex(); // initial lex
		if (text.equals("int") || text.equals("char")) { // checks if listOfDec
			listOfDec(root, treeRoot);
		}
		// TODO
		listOfStat(root, treeRoot);
		grammarTree = new JTree(treeRoot);
	}

	private void listOfDec(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in listofdec: " + text);
		// to add the listOfDec node sub to the parent(root)
		Node n = addNode(p, 10, "listOfDec");
		DefaultMutableTreeNode treeListOfDec = new DefaultMutableTreeNode(
				"listOfDec");
		treep.add(treeListOfDec);
		declaration(n, treeListOfDec);
		// if the list is a declaration list then recurse this method
		if (text.equals("int") || text.equals("char")) { // checks if listOfDec
			listOfDec(n, treeListOfDec);
			// TODO perhaps here may be some problems to deal with
		}

	}

	private void declaration(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in dec: " + text);
		Node n = addNode(p, 20, "declaration");
		DefaultMutableTreeNode treeDeclaration = new DefaultMutableTreeNode(
				"declaration");
		treep.add(treeDeclaration);
		//
		type(n, treeDeclaration);
		// to get the variable list
		// TODO
		listOfVariables(n, treeDeclaration);

		if (text.equals(";")) {
			// System.out.println("finished declaration");
			lex();
		} else {
			// error();
		}

	}

	private void type(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in type: " + text);
		Node n = addNode(p, 30, "type");
		DefaultMutableTreeNode treeType = new DefaultMutableTreeNode("type");
		treep.add(treeType);
		// <type> --> "int" | "char"
		if (text.equals("int")) {
			// System.out.println("reached int keyword: " + text);
			addLeaf(n, treeType, "int");
			lex(); // end
		} else if (text.equals("char")) {
			// System.out.println("reached char keyword: " + text);
			addLeaf(n, treeType, "char");
			lex();
		} else {
			error();
		}

	}

	private void listOfVariables(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in listOfVariables : " + text);
		Node n = addNode(p, 40, "listOfVariables");
		DefaultMutableTreeNode treeListOfVariables = new DefaultMutableTreeNode(
				"listOfVariables");
		treep.add(treeListOfVariables);
		variable(n, treeListOfVariables);

		if (text.equals(",")) {
			lex();
			listOfVariables(n, treeListOfVariables);
		}

	}

	private void variable(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in var : " + text);
		Node n = addNode(p, 50, "variable");
		DefaultMutableTreeNode treeVariable = new DefaultMutableTreeNode(
				"variable");
		treep.add(treeVariable);
		if (val == (IDENT)) {
			// System.out.println("reached variable: " + text);

			addLeaf(n, treeVariable, text);
			lex(); // end..
		}
		// TODO
		if (val == (ARRAY)) {
			String arName = text.split("\\[")[0];
			int arIndex = Integer.valueOf(text.split("\\[")[1].substring(0, 1));
			if (!declaredArray.containsKey(arName)) {
				declaredArray.put(arName, arIndex);
				for (int arId = 0; arId < arrayBound; arId++) {
					String arFullName = arName + "[" + Integer.toString(arId)
							+ "]";
					val = IDENT;
					text = arFullName;
					declaredArray.put(arName, arrayBound);
					addLeaf(n, treeVariable, arFullName);
				}
			} else {
				if (arrayBound >= declaredArray.get(arName)) {
					error();
					System.err.println("ArrayOutOfIndexException");
				} else {
					addLeaf(n, treeVariable, text);
					lex();
				}

			}

			lex();
		}
	}

	// <listofstatements --> <statement> ";" <listofstatements>
	private void listOfStat(Node p, DefaultMutableTreeNode treep) {

		if (val == 500) {
			System.out.println("finshed listOfStat and all tokens");
		}

		else {
			// System.out.println("in listOfstat " + text);
			Node n = addNode(p, 60, "listOfStat");
			DefaultMutableTreeNode treeListOfStat = new DefaultMutableTreeNode(
					"listOfStat");
			treep.add(treeListOfStat);
			statement(n, treeListOfStat);

			if (text.equals(";")) {
				lex();
				listOfStat(n, treep);
			}
		}

	}

	private void statement(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in statement " + text);
		Node n = addNode(p, 70, "statement");
		DefaultMutableTreeNode treeStatement = new DefaultMutableTreeNode(
				"statement");
		treep.add(treeStatement);
		// <statement> --> <cin> | <cout> | <if> | <assign> | <while> | "{"
		// <listofstatements> "}"

		int curTok = val;
		switch (curTok) {
		case 0:
			cin(n, treeStatement);
			break;
		case 1:
			cout(n, treeStatement);
			break;
		case 2:
			ifStat(n, treeStatement);
			break;
		case 3:
			whileStat(n, treeStatement);
			break;
		default:
			if (text.equals("{")) {
				lex();
				listOfStat(n, treeStatement);

				if (text.equals("}")) {

					// System.out.println("finished statement");
					lex();
				} else {
					error();
				}
			} else {
				assignStat(n, treeStatement);
			}
		}
	}

	private void whileStat(Node p, DefaultMutableTreeNode treep) {
		// need else by everyone to call error()
		// System.out.println("whileStat");
		Node n = addNode(p, 80, "whileStat");
		DefaultMutableTreeNode treeWhileStat = new DefaultMutableTreeNode(
				"whileStat");
		treep.add(treeWhileStat);
		// less messy
		// if(ifLex("while")&&ifLex("(")){
		// exp(n);
		// if(ifLex(")")){
		// statement(n);
		// }
		// }

		if (text.equals("while")) {
			lex();

			// extra node for before expression (can also go directly to it)
			Node whileSpot = new Node(902, text, 902, "jumpToHereAfterWhile");
			n.addNode(whileSpot);

			if (text.equals("(")) {
				lex();
				exp(n, treeWhileStat);

				if (text.equals(")")) {
					lex();

					// addjumpfalse node
					Node jumpFalse = new Node(900, text, 900,
							"jumpAwayFromHereIfFalse");
					n.addNode(jumpFalse);

					statement(n, treeWhileStat);

					// add jump to exp node
					Node jump = new Node(905, text, 905, "jumpToExpression");
					n.addNode(jump);

				}
			}
		}

	}

	private void assignStat(Node p, DefaultMutableTreeNode treep) {
		// <assign> --> <> "=" <exp>
		// System.out.println("assignStat");
		Node n = addNode(p, 90, "assignStat");
		DefaultMutableTreeNode treeAssignStat = new DefaultMutableTreeNode(
				"assignStat");
		treep.add(treeAssignStat);
		listOfVariables(n, treeAssignStat);

		if (text.equals("=")) {
			DefaultMutableTreeNode treeEqualSign = new DefaultMutableTreeNode(
					"=");
			treeAssignStat.add(treeEqualSign);
			lex();
			exp(n, treeAssignStat);
		} else {
			error();
		}
	}

	private void ifStat(Node p, DefaultMutableTreeNode treep) {
		// <if> --> "if"(" <exp> ")" <statement> ["else" <statement>]'
		Node n = addNode(p, 100, "ifStat");
		DefaultMutableTreeNode treeIfStat = new DefaultMutableTreeNode("ifStat");
		treep.add(treeIfStat);
		// System.out.println("ifStat");

		// can use ifLex();
		// if(ifLex("if")&&ifLex("(")){
		// exp(n);
		// }
		// if(ifLex(")")){
		// statement(n);
		// }
		// if(ifLex("else")){
		// statement(n);
		// }

		if (text.equals("if")) {
			lex();
			DefaultMutableTreeNode treeIf = new DefaultMutableTreeNode("if");
			treeIfStat.add(treeIf);
			if (text.equals("(")) {
				addLeaf(n, treeIfStat, "(");
				lex();
				exp(n, treeIfStat);

				if (text.equals(")")) {
					addLeaf(n, treeIfStat, ")");
					// add special child for quads to get point **
					Node jumpFalse = new Node(900, text, 900,
							"jumpAwayFromHereIfFalse");

					n.addNode(jumpFalse);
					DefaultMutableTreeNode treeJumpFalse = new DefaultMutableTreeNode(
							"jumpFalse");
					treeIfStat.add(treeJumpFalse);

					lex();
					statement(n, treeIfStat);

					// System.out.println ("after stmt:"+text);

					// if there's a ";" will need another lex
					if (text.equals(";")) {
						addLeaf(n, treeIfStat, ")");
						lex();
					}

					if (text.equals("else")) {
						lex();

						// //System.out.println ("lex'd else"+text);

						Node leaf = new Node(val, text, 901, "else");
						n.addNode(leaf);
						DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode(
								"leaf");
						treeIfStat.add(treeLeaf);

						statement(n, treeIfStat);

						// add special child for quads to get point **
						// not needed?
						Node elseSpot = new Node(902, text, 902, "endElse");
						DefaultMutableTreeNode treeElseSpot = new DefaultMutableTreeNode(
								"elseSpot");
						treeIfStat.add(treeElseSpot);
						n.addNode(elseSpot);
					}
				}
			}
		}
	}

	private void cout(Node p, DefaultMutableTreeNode treep) {
		// <cout> --> "cout" "<<" <listofexpressions>
		Node n = addNode(p, 110, "write");
		DefaultMutableTreeNode treeCout = new DefaultMutableTreeNode("write");
		treep.add(treeCout);
		// if(ifLex("cout")&&ifLex("<<")){
		// listOfExpressions(n);
		// }
		if (text.equals("write")) {
			addLeaf(n, treeCout, text);
			lex();
			if (text.equals("<<")) {
				addLeaf(n, treeCout, text);
				lex(); // need here
				listOfExpressions(n, treeCout);
			}
		}
	}

	private void cin(Node p, DefaultMutableTreeNode treep) {
		// "cin" ">>" <listofvariables>
		Node n = addNode(p, 120, "read");
		DefaultMutableTreeNode treeCin = new DefaultMutableTreeNode("read");
		treep.add(treeCin);
		if (text.equals("read")) {
			addLeaf(n, treeCin, text);
			lex();
			if (text.equals(">>")) {
				addLeaf(n, treeCin, text);
				lex();
				listOfVariables(n, treeCin);
			}
		}
		// if(ifLex("cin")&&ifLex(">>")){
		// listOfVariables(n);
		// }
	}

	// <listofexpressions>--><exp> |<exp>,<listofexpressions>
	private void listOfExpressions(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 130, "listOfExpressions");
		DefaultMutableTreeNode treeListOfExpressions = new DefaultMutableTreeNode(
				"listOfExpressions");
		treep.add(treeListOfExpressions);
		exp(n, treeListOfExpressions);
		if (ifLex(",")) {
			addLeaf(n, treeListOfExpressions, ",");
			listOfExpressions(n, treep);
		}

	}

	private void exp(Node p, DefaultMutableTreeNode treep) {
		// <exp> --> <exp1> { "||" <exp1>}*
		// System.out.println("in exp");
		Node n = addNode(p, 140, "exp");
		DefaultMutableTreeNode treeExp = new DefaultMutableTreeNode("exp");
		treep.add(treeExp);
		exp1(n, treeExp);
		while (text.equals("||")) {
			addLeaf(n, treeExp, text);
			// directly add Node since not continuing with child
			Node leaf = new Node(val, text, 490, text); // leaf num
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp.add(treeLeaf);
			lex();
			exp1(n, treeExp);
		} // always ends with lex(); so next thing after it doesn't need it.
	}

	private void exp1(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 150, "exp1");
		DefaultMutableTreeNode treeExp1 = new DefaultMutableTreeNode("exp1");
		treep.add(treeExp1);

		exp2(n, treeExp1);

		while (text.equals("&&")) {
			Node leaf = new Node(val, text, 491, text);
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp1.add(treeLeaf);
			lex();
			exp2(n, treeExp1);
		}
	}

	// note** may need to mark these stuff instead of just lexing them?

	// <exp2> --> "!" <exp2> | <exp3> { "|" <exp3>}*
	private void exp2(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 160, "exp2");
		DefaultMutableTreeNode treeExp2 = new DefaultMutableTreeNode("exp2");
		treep.add(treeExp2);
		if (text.equals("!")) {
			Node leaf = new Node(val, text, 492, text);
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp2.add(treeLeaf);
			lex();
			exp2(n, treeExp2);

		} else {
			exp3(n, treeExp2);

			while (text.equals("|")) {
				addLeaf(n, treeExp2, text);
				Node leaf = new Node(val, text, 493, text);
				n.addNode(leaf);
				DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode(
						"leaf");
				treeExp2.add(treeLeaf);
				lex();
				exp3(n, treeExp2);

			}

		}
	}

	private void exp3(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 170, "exp3");
		DefaultMutableTreeNode treeExp3 = new DefaultMutableTreeNode("exp3");
		treep.add(treeExp3);
		exp4(n, treeExp3);
		while (text.equals("^")) {
			addLeaf(n, treeExp3, text);
			Node leaf = new Node(val, text, 494, text);
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp3.add(treeLeaf);
			lex();
			exp4(n, treeExp3);
		}

	}

	private void exp4(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 180, "exp4");
		DefaultMutableTreeNode treeExp4 = new DefaultMutableTreeNode("exp4");
		treep.add(treeExp4);
		exp5(n, treeExp4);
		while (text.equals("&")) {
			addLeaf(n, treeExp4, text);
			Node leaf = new Node(val, text, 495, text);
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp4.add(treeLeaf);
			lex();
			exp5(n, treeExp4);
		}

	}

	private void exp5(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 190, "exp5");
		DefaultMutableTreeNode treeExp5 = new DefaultMutableTreeNode("exp5");
		treep.add(treeExp5);
		if (text.equals("~")) {
			addLeaf(n, treeExp5, text);
			Node leaf = new Node(val, text, 496, text);
			n.addNode(leaf);
			DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode("leaf");
			treeExp5.add(treeLeaf);
			lex();
			exp5(n, treep);
		} else {
			exp6(n, treeExp5);
			while (text.equals("==") || text.equals("!=")) { // mark which*
				Node leaf = new Node(val, text, 497, text);
				n.addNode(leaf);
				DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode(
						"leaf");
				treeExp5.add(treeLeaf);
				lex();
				exp6(n, treeExp5);
			}

		}
	}

	// <exp6> --> <exp7> { <relop> <exp?>}*
	private void exp6(Node p, DefaultMutableTreeNode treep) {
		Node n = addNode(p, 200, "exp6");
		DefaultMutableTreeNode treeExp6 = new DefaultMutableTreeNode("exp6");
		treep.add(treeExp6);
		exp7(n, treeExp6);
		// run it and do nothing if it does nothing..
		DefaultMutableTreeNode treeRelop = new DefaultMutableTreeNode("relop");
		treeExp6.add(treeRelop);
		while (relop(n, treeRelop)) {
			lex();
			exp7(n, treeExp6); // this doesn't seem to get called since it
								// returns false
			// ?**
		}

	}

	// <relop>--> "<="|"<"|">"|">="
	// very risky way..
	private boolean relop(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("CHECKING RELOP");
		// end insert leaves
		// now adds specific for relop and gen for leaf*
		// System.out.println ("the relop: "+text + val);
		Node n;
		switch (val) {
		case 24:
			n = addNode(p, 410, "relop");
			addLeaf(n, treep, "<=");
			return true;
		case 19:
			n = addNode(p, 411, "relop");
			addLeaf(n, treep, "<");
			// //System.out.println("<");
			return true;
		case 26:
			n = addNode(p, 412, "relop");
			addLeaf(n, treep, ">");
			// System.out.println(">");
			return true;
		case 27:
			n = addNode(p, 413, "relop");
			addLeaf(n, treep, ">=");
			return true;
		default:
			// System.out.println ("no relop");
			return false;
			// continue?
		}
		// return true;
	}

	private void exp7(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("EXP7");
		Node n = addNode(p, 220, "exp7");
		DefaultMutableTreeNode treeExp7 = new DefaultMutableTreeNode("exp7");
		treep.add(treeExp7);
		exp8(n, treeExp7);
		DefaultMutableTreeNode treeAddop = new DefaultMutableTreeNode("addop");
		treeExp7.add(treeAddop);
		while (addop(n, treeAddop)) {
			lex();
			exp8(n, treeExp7);
		}

	}

	// mulop addaop relop, had tried to reduce code poorly
	// is there still a redundacy with addNode and addLeaf??***

	private boolean addop(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("CHECKING ADDOP");
		// end
		Node n;
		if (val == 29) {
			// +
			n = addNode(p, 430, "addop");
			addLeaf(n, treep, "+");
			return true;
		} else if (val == 30) {
			// -
			n = addNode(p, 431, "addop");
			addLeaf(n, treep, "-");
			return true;
		} else {
			return false;
		}
	}

	private void exp8(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("EXP8");
		Node n = addNode(p, 240, "exp8");
		DefaultMutableTreeNode treeExp8 = new DefaultMutableTreeNode("exp8");
		treep.add(treeExp8);
		exp9(n, treeExp8);
		DefaultMutableTreeNode treeMulop = new DefaultMutableTreeNode("mulop");
		treeExp8.add(treeMulop);
		while (mulop(n, treeMulop)) { // check to make sure its not calling each
										// time**
			lex(); // can move below
			exp9(n, treeExp8);
		}

	}

	private boolean mulop(Node p, DefaultMutableTreeNode treep) {
		// end**

		// put by each can't auto do*** check also relop etc.
		Node n;
		if (val == 31) {
			// *
			n = addNode(p, 450, "mulop");
			addLeaf(n, treep, "*");
			return true;
		} else if (val == 32) {
			// /
			n = addNode(p, 451, "mulop");
			addLeaf(n, treep, "/");
			return true;
		} else if (val == 33) {
			// %
			n = addNode(p, 452, "mulop");
			addLeaf(n, treep, "%");
			return true;
		} else {
			return false;
		}
	}

	// --> ("<exp>") |INTEGER|IDENT|CONSTCH | <uarop> <exp9>
	private void exp9(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in exp9 " + text + " "+ val);
		Node n = addNode(p, 260, "exp9");
		DefaultMutableTreeNode treeExp9 = new DefaultMutableTreeNode("exp9");
		treep.add(treeExp9);
		// or select case..

		// end
		if (text.equals("(")) {
			lex();
			exp(n, treeExp9);
			if (text.equals(")")) {
				lex();
			}
		} else if (val == IDENT) {
			// System.out.println("IDENT:" + text);
			addLeaf(n, treeExp9, text);
			lex();

		} else if (val == INTEGER) {
			// System.out.println("INTEGER " + text+"."+val);
			addLeaf(n, treeExp9, text);
			lex();
		} else if (val == CONSTH) {
			// System.out.println("CONSTH");
			addLeaf(n, treeExp9, text);
			lex();
		} else {

			uarop(n, treeExp9);
			exp9(n, treep);
		}

	}

	private void uarop(Node p, DefaultMutableTreeNode treep) {
		// System.out.println("in uarop");

		// end
		if (text.equals("-")) {
			Node n = addNode(p, 270, "uarop");
			addLeaf(n, treep, "-");
			lex();
		} else if (text.equals("+")) {
			Node n = addNode(p, 271, "uarop");
			addLeaf(n, treep, "+");
			lex();
		} else {
			error();
			lex(); // (or die)
		}

	}

	/* General methods */
	/**
	 * this method is to recognize the tokens from the input and them separate
	 * the name and the type of it.
	 */
	public void lex() {
		try {
			tok = split.getToken();
			text = tok.getString();
			val = tok.getType();
			if (val == ARRAY) {
				arrayBound = tok.getArrayBound();
			}
			if (tok.getType() != 500) {
				System.out.println("<" + tok.getString() + "," + tok.getType()
						+ ">");
				tokens.add(tok);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void error() {
		System.err.println("error: " + text + " " + val);
	}

	/**
	 * to add the child node to the parent node or to build a new node
	 * 
	 * @param parent
	 *            the parent node
	 * @param method
	 * @param strMethod
	 * @return
	 */
	private Node addNode(Node parent, int method, String strMethod) {
		Node newNode = new Node(val, text, method, strMethod);
		parent.addNode(newNode);
		return newNode;
	}

	private void addLeaf(Node parent, DefaultMutableTreeNode treep,
			String leafstr) {
		Node leaf = new Node(val, text, 0, text); // leaf num
		parent.addNode(leaf);
		DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode(leafstr);
		treep.add(treeLeaf);
	}

	// run boolean check while lexing..
	private boolean ifLex(String s) {
		if (text.equals("s")) {
			lex();
			return true;
		} else {
			return false;
		}
	}

	// after tree created, can print it.
	private void printSubTree(Node n) {
		// now post order
		if (n.children != null) {
			for (Node child : n.children) {
				int counter = 0;
				++counter;
				int i = 0;
				while (i < counter) {
					System.out.println("\t");
					i++;
				}
				printSubTree(child);
			}

		}
		// System.out.println ("");
		n.print();
		System.out.println("\n");

	}

	public void printTree() {
		printSubTree(root);
		System.out.println("\n");
	}

	public Node returnRoot() {
		return root;
	}

}
