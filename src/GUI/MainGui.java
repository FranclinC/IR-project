package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGui extends JFrame {

	private JPanel contentPane;
	private JTextField txtWelcomeToLucene;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui frame = new MainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public MainGui() {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtWelcomeToLucene = new JTextField();
		txtWelcomeToLucene.setForeground(Color.RED);
		txtWelcomeToLucene.setHorizontalAlignment(SwingConstants.CENTER);
		txtWelcomeToLucene.setText("IR System - Mineração web");
		txtWelcomeToLucene.setEditable(false);
		txtWelcomeToLucene.setBounds(6, 6, 288, 64);
		contentPane.add(txtWelcomeToLucene);
		txtWelcomeToLucene.setColumns(10);
		
		JButton btn_index = new JButton("Index files");
		btn_index.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callIndex();
			}
		});
		btn_index.setBounds(6, 93, 117, 29);
		contentPane.add(btn_index);
		
		JButton btn_search = new JButton("Search files");
		btn_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callSearch();
			}
		});
		btn_search.setBounds(177, 93, 117, 29);
		contentPane.add(btn_search);
		
		JLabel lblChooseOption = new JLabel("Pick an option:");
		lblChooseOption.setBounds(106, 75, 134, 16);
		contentPane.add(lblChooseOption);
		
	}
	
	private void callIndex() {
		new IndexGUI().setVisible(true);;
	}
	
	private void callSearch() {
		new SourceGUI().setVisible(true);
	}

}
