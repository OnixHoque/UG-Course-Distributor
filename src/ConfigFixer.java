import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;


public class ConfigFixer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtLineNumber;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfigFixer dialog = new ConfigFixer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigFixer() {
		SetNativeLook();
		initializeElements();
	}
	
	private void initializeElements()
	{
		setTitle("Configuration File Fixer");
		setBounds(100, 100, 469, 392);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
		{
			JPanel panel = new JPanel();
			panel.setBorder(null);
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(5, 5));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JLabel lblChnageInFacultycourse = new JLabel("Change in Faculty/Course List detected!");
					panel_1.add(lblChnageInFacultycourse);
					lblChnageInFacultycourse.setFont(new Font("Tahoma", Font.BOLD, 13));
				}
				{
					JLabel lblPleaseMakeAppropriate = new JLabel("Please make appropriate change(s) accordingly to fix the state.config file");
					panel_1.add(lblPleaseMakeAppropriate);
				}
			}
			{
				JLabel lblWarning = new JLabel("");
				panel.add(lblWarning, BorderLayout.WEST);
				lblWarning.setIcon(new ImageIcon(getClass().getClassLoader().getResource("warning.png")));
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(0, 3, 5, 0));
			{
				JTextArea txtCourse = new JTextArea();
				txtCourse.setEditable(false);
				panel.add(txtCourse);
			}
			{
				JTextArea txtTheory = new JTextArea();
				txtTheory.setEditable(false);
				txtTheory.setText("");
				panel.add(txtTheory);
			}
			{
				JTextArea txtSessional = new JTextArea();
				txtSessional.setEditable(false);
				panel.add(txtSessional);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPanel.add(panel, BorderLayout.SOUTH);
			panel.setLayout(new BorderLayout(5, 0));
			{
				JButton btnFix = new JButton("Go");
				panel.add(btnFix, BorderLayout.EAST);
			}
			{
				JComboBox comboBox = new JComboBox();
				comboBox.setModel(new DefaultComboBoxModel(new String[] {"Insert empty", "Delete"}));
				panel.add(comboBox, BorderLayout.WEST);
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new GridLayout(1, 0, 5, 0));
				{
					JComboBox comboBox = new JComboBox();
					comboBox.setModel(new DefaultComboBoxModel(new String[] {"Faculty", "Theory Course", "Sessional Course"}));
					panel_1.add(comboBox);
				}
				{
					JLabel lblAtLineNumber = new JLabel("after line number");
					panel_1.add(lblAtLineNumber);
				}
				{
					txtLineNumber = new JTextField();
					txtLineNumber.setText("1");
					panel_1.add(txtLineNumber);
					txtLineNumber.setColumns(10);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Start Program");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void SetNativeLook()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
