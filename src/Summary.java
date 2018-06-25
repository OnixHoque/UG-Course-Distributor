import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;

import windowless.FacultyType;
import windowless.KB;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.JSeparator;

public class Summary extends JFrame {

	private JPanel contentPane;
	private JLabel lblTotalTheory;
	JLabel lblTotalSessional;
	JLabel lblPermTheory;
	JLabel lblPermSessional;
	JLabel lblAttTheory;
	JLabel lblAttSessional;
	JLabel lblGuestTheory;
	JLabel lblGuestSessional;
	JLabel lblAdjTheory;
	JLabel lblAdjSessional;
	JLabel lblRemainingTheory;
	JLabel lblRemainingSessional;
	

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					summary frame = new summary();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/

	/**
	 * Create the frame.
	 */
	public void UpdateTable()
	{
		lblTotalTheory.setText(String.valueOf(KB.getTotalContactHr(true)));
		lblTotalSessional.setText(String.valueOf(KB.getTotalContactHr(false)));
		
		lblPermTheory.setText(String.valueOf(KB.getContactHrCovered(FacultyType.PERM, true)));
		lblPermSessional.setText(String.valueOf(KB.getContactHrCovered(FacultyType.PERM, false)));
		
		lblAttTheory.setText(String.valueOf(KB.getContactHrCovered(FacultyType.ATT, true)));
		lblAttSessional.setText(String.valueOf(KB.getContactHrCovered(FacultyType.ATT, false)));
		
		lblGuestTheory.setText(String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, true)));
		lblGuestSessional.setText(String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, false)));
		
		lblAdjTheory.setText(String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, true)));
		lblAdjSessional.setText(String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, false)));
		
		double remainingT = Double.valueOf(lblTotalTheory.getText()) - (Double.valueOf(lblPermTheory.getText()) + 
				Double.valueOf(lblAttTheory.getText()) +
				Double.valueOf(lblGuestTheory.getText()) +
				Double.valueOf(lblAdjTheory.getText()));
		double remainingS = Double.valueOf(lblTotalSessional.getText()) - (Double.valueOf(lblPermSessional.getText()) + 
				Double.valueOf(lblAttSessional.getText()) +
				Double.valueOf(lblGuestSessional.getText()) +
				Double.valueOf(lblAdjSessional.getText()));
		
		
		lblRemainingTheory.setText(String.valueOf(remainingT));
		lblRemainingSessional.setText(String.valueOf(remainingS));
		
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
	public Summary() {
		SetNativeLook();
		setType(Type.UTILITY);
		setTitle("Contact Hour Distribution");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 278, 205);
		contentPane = new JPanel();
		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 3, 15, 0));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width-this.getSize().width, dim.height/2-this.getSize().height/2);
		
		JLabel lblTitle = new JLabel("Covered by");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblTitle);
		
		JLabel lblTheory = new JLabel("Theory");
		lblTheory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblTheory);
		
		JLabel lblSessional = new JLabel("Sessional");
		lblSessional.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(lblSessional);
		
		JLabel lblTotal = new JLabel("(Total)");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblTotal);
		
		lblTotalTheory = new JLabel("200");
		contentPane.add(lblTotalTheory);
		
		lblTotalSessional = new JLabel("New label");
		contentPane.add(lblTotalSessional);
		
		JLabel lblPerm = new JLabel("Perm");
		lblPerm.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblPerm);
		
		lblPermTheory = new JLabel("100.5");
		contentPane.add(lblPermTheory);
		
		lblPermSessional = new JLabel("78");
		contentPane.add(lblPermSessional);
		
		JLabel lblAtt = new JLabel("Att");
		lblAtt.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblAtt);
		
		lblAttTheory = new JLabel("New label");
		contentPane.add(lblAttTheory);
		
		lblAttSessional = new JLabel("New label");
		contentPane.add(lblAttSessional);
		
		JLabel lblGuest = new JLabel("Guest");
		lblGuest.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblGuest);
		
		lblGuestTheory = new JLabel("lbl");
		contentPane.add(lblGuestTheory);
		
		lblGuestSessional = new JLabel("New label");
		contentPane.add(lblGuestSessional);
		
		JLabel lblAdjunct = new JLabel("Adjunct");
		lblAdjunct.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblAdjunct);
		
		lblAdjTheory = new JLabel("New label");
		contentPane.add(lblAdjTheory);
		
		lblAdjSessional = new JLabel("New label");
		contentPane.add(lblAdjSessional);
		
		JLabel lblRemaining = new JLabel("(Remaining)");
		lblRemaining.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRemaining.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblRemaining);
		
		lblRemainingTheory = new JLabel("New label");
		lblRemainingTheory.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblRemainingTheory);
		
		lblRemainingSessional = new JLabel("New label");
		lblRemainingSessional.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPane.add(lblRemainingSessional);
		
		UpdateTable();
	}

}
