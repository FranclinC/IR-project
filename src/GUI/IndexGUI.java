package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import lucene.IndexFiles;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class IndexGUI extends JFrame {

	private JPanel contentPane;
	IndexFiles indexFiles;
	private static JTextArea ta_content;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexGUI frame = new IndexGUI();
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
	public IndexGUI() {
		setTitle("Index files");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btn_index = new JButton("Index");
		btn_index.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexFiles();
			}
		});
		btn_index.setBounds(242, 244, 117, 29);
		contentPane.add(btn_index);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 588, 226);
		contentPane.add(scrollPane);

		ta_content = new JTextArea();
		ta_content.setEditable(false);
		scrollPane.setViewportView(ta_content);
		
		indexFiles = new IndexFiles();
	}
	
	private void indexFiles() {
		indexFiles.indexFiles();
	}
	
	public static void setToContent(String line) {
		ta_content.append(line+'\n');
		ta_content.update(ta_content.getGraphics());
	}
}

