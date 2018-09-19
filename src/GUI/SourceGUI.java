package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import lucene.SearchFiles;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

public class SourceGUI extends JFrame {

	private JPanel contentPane;
	private JList source_list;
	
	private SearchGUI searchGUI2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SourceGUI frame = new SourceGUI();
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
	public SourceGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 183, 163);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String[] values = new String[] {"Original", "Stemming", "Stopword", "Stemming + stopword"};
		source_list = new JList(values);
		source_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		source_list.setBounds(26, 32, 132, 68);
		contentPane.add(source_list);
		
		JButton btn_select = new JButton("Select");
		btn_select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSource(source_list.getSelectedIndex());
			}
		});
		btn_select.setBounds(50, 106, 83, 29);
		contentPane.add(btn_select);
		
		JLabel lblNewLabel = new JLabel("Pick a source:");
		lblNewLabel.setBounds(26, 6, 132, 16);
		contentPane.add(lblNewLabel);
		
		searchGUI2 = new SearchGUI();
	}
	
	private void selectedSource(int code) {
		searchGUI2.sourceCode = code;
		searchGUI2.setVisible(true);
		dispose();
	}
	
}
