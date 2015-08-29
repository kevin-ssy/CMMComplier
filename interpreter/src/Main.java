
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {
	static String filename;
	static Splitter spl;
	static Parse par;
	static File f;
	static class TreeWin extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		TreeWin() {
			String tokensToShow = new String();
			setTitle("grammar tree");
			setSize(600, 400);
			setVisible(true);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Container con = getContentPane();
			JScrollPane scrollpane = new JScrollPane(par.grammarTree);
			con.add(scrollpane);
			JFrame newjf = new JFrame("lexical");
			newjf.setSize(300, 250);
			newjf.setVisible(true);
			newjf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Container newcon = newjf.getContentPane();

			Iterator<Token> tokensIt = par.tokens.iterator();
			while (tokensIt.hasNext()) {
				Token nextToken = tokensIt.next();
				String temp = "<" + nextToken.getString() + ","
						+ nextToken.getType() + ">" + "\n";
				tokensToShow += temp;
			}
			JTextArea jta = new JTextArea();
			jta.setText(tokensToShow);
			newcon.add(jta);
			JScrollPane newscrollpane = new JScrollPane(jta);
			newcon.add(newscrollpane);
			newjf.validate();
		}
	}

	static class MainWindow extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Container con = getContentPane();
		JButton fileButton = new JButton("select file");
		JButton parseButton = new JButton("lexical & syntaxical analysis");
		JButton semanticButton = new JButton("semantical analysis");
		JFileChooser jfc = new JFileChooser();// 文件选择器

		public MainWindow() {
			setTitle("cmm interpreter");
			setSize(200, 100);
			setVisible(true);
			fileButton.setVisible(true);
			fileButton.setSize(80, 40);
			con.setLayout(new GridLayout(1,1));
			con.add(fileButton);
			
			fileButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设定只能选择到文件夹
					int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
					if (state == 1) {
						return;// 撤销则返回
					} else {
						f = jfc.getSelectedFile();// f为选择到的目录
						filename = f.getAbsolutePath();
						JOptionPane.showMessageDialog(null, "file selected successfully!","notice",JOptionPane.PLAIN_MESSAGE);
						Splitter newspl = null;
						try {
							newspl = new Splitter(f);
							spl = new Splitter(f);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Parser newpar = new Parser(newspl);
						Quadrupler quad = new Quadrupler(newpar.returnRoot());
						@SuppressWarnings("unused")
						Interpret pc = new Interpret(quad.getQuadArray());
						par = new Parse(spl);
						@SuppressWarnings("unused")
						TreeWin win = new TreeWin();
					}
				}
			});
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		File file = new File("sample.txt");
//		Splitter newspl = new Splitter(file);
//		Parser newpar = new Parser(newspl);
//		Quadrupler quad = new Quadrupler(newpar.returnRoot());
//		Interpret pc = new Interpret(quad.getQuadArray());
		new MainWindow().validate();
		
//		System.out
//				.println("***************************\n"
//						+ "Parsing is complete. Program will now go through the actual tree of Nodes and print out their NodeCodes. \n"
//						+ "Each Node has a node code that corresponds to a a specific variable. For example Declaration's code is 20 ");
//
//		System.out.println("******* the tree ******");
////		par.printTree();
//
//		System.out.println("quads*******");
		
//		
//
//		System.out
//				.println("Hooray. Code has been compiled and interpreted! :)");

	}

}
