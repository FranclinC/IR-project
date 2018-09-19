package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import lucene.SearchFiles;

import javax.swing.ListSelectionModel;

public class SearchGUI extends JFrame {

	private static JPanel contentPane;
	private JTextField tf_query;
	private static JList list;
	private static JTextPane tp_matches;
	
	private SearchFiles sf;
	private ShowFileGUI showfile;
	private JButton btnPrevious;
	private JButton btnNext;
//	private JButton btnOpen;
	
	protected int sourceCode;
	private JButton btn_source;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchGUI frame = new SearchGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SearchGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tf_query = new JTextField();
		tf_query.setBounds(6, 6, 482, 28);
		contentPane.add(tf_query);
		tf_query.setColumns(10);
		
		JButton btn_search = new JButton("Search");
		btn_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFiles(tf_query.getText());
			}
		});
		btn_search.setBounds(494, 7, 100, 29);
		contentPane.add(btn_search);
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(6, 99, 588, 325);
		contentPane.add(list);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previousPage();
			}
		});
		btnPrevious.setBounds(6, 435, 91, 29);
		contentPane.add(btnPrevious);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextPage();
			}
		});
		btnNext.setBounds(106, 435, 91, 29);
		contentPane.add(btnNext);
		
		tp_matches = new JTextPane();
		tp_matches.setEditable(false);
		tp_matches.setBounds(6, 41, 588, 46);
		contentPane.add(tp_matches);
		
//		btnOpen = new JButton("Open");
//		btnOpen.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				openFile();
//			}
//		});
//		btnOpen.setBounds(206, 435, 91, 29);
//		contentPane.add(btnOpen);
		
		btn_source = new JButton("Source");
		btn_source.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SourceGUI().setVisible(true);
			}
		});
		btn_source.setBounds(503, 436, 91, 29);
		contentPane.add(btn_source);
		
		sf = new SearchFiles();
		showfile = new ShowFileGUI();
	}
	
	private void searchFiles(String input) {
		sf.search(input, sourceCode);
	}
	
	public static void setToContent(String[] line) {
		DefaultListModel listModel = new DefaultListModel();
		for (int i = 0; i < line.length; i++) {
		  listModel.addElement(line[i]);
		}
		list.setModel(listModel);
	}
	
	private void nextPage() {
		sf.nextPage();
	}
	
	private void previousPage() {
		sf.previousPage();
	}
	
	public static void setMatches(String str) {
		tp_matches.setText(str);
	}
	
	private void openFile() {
		String[] path = new String[2];
		String filePath = list.getSelectedValue().toString();
		path = filePath.split("\\s");
		
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			showfile.textArea.read(br, null);
			br.close();
			showfile.textArea.requestFocus();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		showfile.setVisible(true);
	}
}