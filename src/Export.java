import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import windowless.FacultyType;
import windowless.KB;

import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.CardLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;

public class Export extends JDialog {

	private JPanel contentPane;
	public JCheckBox chckbxCreditSummary;
	public JCheckBox chckbxFaculty;
	public JCheckBox chckbxCourseAssignment;
	public JCheckBox chckbxSessionalCourseAssignment;
	private JLabel lblStatus;
	
	
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

	void exportSummary()
	{
		try{
		    PrintWriter writer = new PrintWriter("output/Summary.csv", "UTF-8");	//, "UTF-8"
		    
		    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
		    
			writer.println("Generated On: " + date_time);
			writer.println("");
			
			writer.println(",UG Course Distribution Summary,");
		    writer.println("");
		    writer.println("Taken By,Theory,Sessional");
		    writer.println("Total," + String.valueOf(KB.getTotalContactHr(true)) + "," + String.valueOf(KB.getTotalContactHr(false)));
		    writer.println("Perm," + String.valueOf(KB.getContactHrCovered(FacultyType.PERM, true)) + "," + String.valueOf(KB.getContactHrCovered(FacultyType.PERM, false)));
		    writer.println("Att," + String.valueOf(KB.getContactHrCovered(FacultyType.ATT, true)) + "," + String.valueOf(KB.getContactHrCovered(FacultyType.ATT, false)));
		    writer.println("Guest," + String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, true)) + "," + String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, false)));
		    writer.println("Adjunct," + String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, true)) + "," + String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, false)));
		    
		    double diff_theory = KB.getTotalContactHr(true) - 
		    		(KB.getContactHrCovered(FacultyType.PERM, true)
		    				+ KB.getContactHrCovered(FacultyType.ATT, true)
		    				+ KB.getContactHrCovered(FacultyType.GUEST, true)
		    				+ KB.getContactHrCovered(FacultyType.ADJUNCT, true));
		    
		    double diff_sessional =KB.getTotalContactHr(false) - 
		    		(KB.getContactHrCovered(FacultyType.PERM, false)
		    				+ KB.getContactHrCovered(FacultyType.ATT, false)
		    				+ KB.getContactHrCovered(FacultyType.GUEST, false)
		    				+ KB.getContactHrCovered(FacultyType.ADJUNCT, false));
		    
		    writer.println("Unassigned," + diff_theory + "," + diff_sessional);
		    writer.close();
		    
		} catch (IOException e) {
		   // do something
		}
	}
	
	void exportCourses()
	{
		try{
		    PrintWriter writer = new PrintWriter("output/Courses.csv", "UTF-8");	//, "UTF-8"
		    
		    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
		    
			writer.println("Generated On: " + date_time);
			writer.println("");
		    
		    writer.println(",UG Course Distribution Summary,");
		    writer.println("");
		    for (int level = 1; level<=4; level++)
			{
		    	
		    	writer.println("Level-" + level);
		    	
		    	//Departmental
		    	writer.println("Theory");
		    	writer.println("Ser,Course Code,Course Title,CtHr,Teacher,Remark");
		    	int serial = 1;
		    	double Cttotal = 0.0;
		    	for (int i = 0; i<KB.theoryCount; i++)
		    	{
		    		if ((KB.theoryList.get(i).level == level) && !(KB.theoryList.get(i).non_dept))
		    		{
		    			Cttotal += KB.theoryList.get(i).CtHr; 
		    			writer.println(serial + "," + KB.theoryCodes.get(i) + "," + KB.theoryList.get(i).title + "," + KB.theoryList.get(i).CtHr + ","+ KB.getAssignedTeachers(i, true).toString().replace(",", "; ").replace("[", "").replace("]", "") + ",");
		    			serial++;
		    		}
		    	}
		    	writer.println(",,Total,"+ Cttotal + ",");
		    	writer.println("");
		    	
		    	writer.println("Sessional");
		    	writer.println("Ser,Course Code,Course Title,CrHr,Teacher,Total CtHr,Remark");
		    	serial = 1;
		    	Cttotal = 0.0;
		    	for (int i = 0; i<KB.sessionalCount; i++)
		    	{
		    		if ((KB.sessionalList.get(i).level == level) && !(KB.sessionalList.get(i).non_dept))
		    		{
		    			//double subtotal_cr =  KB.sessionalList.get(i).capacity * KB.sessionalList.get(i).CtHr;
		    			double subtotal_cr = KB.getSessionalTotalContact(i);
		    			Cttotal += subtotal_cr; 
		    			//writer.println(serial + "," + KB.sessionalCodes.get(i) + "," + KB.sessionalList.get(i).title + "," + KB.sessionalList.get(i).CrHr + ","+ KB.getAssignedTeachers(i, false).size()  + "x Teachers," + subtotal_cr + "," + KB.getAssignedTeachers(i, false).toString().replace(",", "; ").replace("[", "").replace("]", "") );
		    			writer.println(serial + "," + KB.sessionalCodes.get(i) + "," + KB.sessionalList.get(i).title + "," + KB.sessionalList.get(i).CrHr + ","+ KB.sessionalList.get(i).capacity + "x Teachers," + subtotal_cr + "," + KB.getAssignedTeachers(i, false).toString().replace(",", "; ").replace("[", "").replace("]", "") );
		    			serial++;
		    		}
		    	}
		    	writer.println(",,,,Total,"+ Cttotal + ",");
		    	writer.println("");
		    	
		    	
		    	//Non-Departmental
		    	writer.println("");
		    	writer.println("Non-Departmental");
		    	writer.println("Theory");
		    	writer.println("Ser,Course Code,Course Title,CtHr,Teacher,Remark");
		    	serial = 1;
		    	Cttotal = 0.0;
		    	for (int i = 0; i<KB.theoryCount; i++)
		    	{
		    		if ((KB.theoryList.get(i).level == level) && (KB.theoryList.get(i).non_dept))
		    		{
		    			Cttotal += KB.theoryList.get(i).CtHr; 
		    			writer.println(serial + "," + KB.theoryCodes.get(i) + "," + KB.theoryList.get(i).title + "," + KB.theoryList.get(i).CtHr + ","+ KB.getAssignedTeachers(i, true).toString().replace(",", "; ").replace("[", "").replace("]", "") + ",");
		    			serial++;
		    		}
		    	}
		    	writer.println(",,Total,"+ Cttotal + ",");
		    	writer.println("");
		    	
		    	writer.println("Sessional");
		    	writer.println("Ser,Course Code,Course Title,CrHr,Teacher,Total CtHr,Remark");
		    	serial = 1;
		    	Cttotal = 0.0;
		    	for (int i = 0; i<KB.sessionalCount; i++)
		    	{
		    		if ((KB.sessionalList.get(i).level == level) && (KB.sessionalList.get(i).non_dept))
		    		{
		    			//double subtotal_cr =  KB.sessionalList.get(i).capacity * KB.sessionalList.get(i).CtHr;
		    			double subtotal_cr = KB.getSessionalTotalContact(i);
		    			Cttotal += subtotal_cr; 
		    			//writer.println(serial + "," + KB.sessionalCodes.get(i) + "," + KB.sessionalList.get(i).title + "," + KB.sessionalList.get(i).CrHr + ","+ KB.getAssignedTeachers(i, false).size() + "x Teachers," + subtotal_cr + "," + KB.getAssignedTeachers(i, false).toString().replace(",", "; ").replace("[", "").replace("]", "") );
		    			writer.println(serial + "," + KB.sessionalCodes.get(i) + "," + KB.sessionalList.get(i).title + "," + KB.sessionalList.get(i).CrHr + ","+ KB.sessionalList.get(i).capacity + "x Teachers," + subtotal_cr + "," + KB.getAssignedTeachers(i, false).toString().replace(",", "; ").replace("[", "").replace("]", "") );
		    			serial++;
		    		}
		    	}
		    	writer.println(",,,,Total,"+ Cttotal + ",");
		    	writer.println("");
		    	
			}
		    writer.close();
		    
		} catch (IOException e) {
		   // do something
		}
		
	}
	
	void exportFaculty()
	{
		try{
		    PrintWriter writer = new PrintWriter("output/Faculty.csv", "UTF-8");	//, "UTF-8"
		    
		    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
		    
			writer.println("Generated On: " + date_time);
			writer.println("");
		    
		    writer.println(",Faculty Course Distribution Summary,");
		    int serial = 1;
		    double totalCtT, totalCtS;
		    writer.println(",,,,");
		    writer.println(",,PERM FACULTY,,");
		    writer.println("Ser,Name,Theory CrHr,Sessional CrHt,Remark");
		    totalCtT = totalCtS = 0.0;
		    for (int i = 0; i<KB.facultyCount; i++)
		    	if (KB.facultyList.get(i).category.equals(FacultyType.PERM))
		    	{
		    		double t = KB.getContactHr(i, true);
		    		double s = KB.getContactHr(i, false);
		    		totalCtT += t;
		    		totalCtS += s;
		    		writer.println(serial + "," + KB.facultyNames.get(i) + "," + t + "," + s + ",");
		    		serial++;
		    	}
		    writer.println(",Total Covered," + totalCtT + "," + totalCtS + ",");
		    
		    writer.println(",,,,");
		    writer.println(",,ATT FACULTY,,");
		    writer.println("Ser,Name,Theory CrHr,Sessional CrHt,Remark");
		    totalCtT = totalCtS = 0.0;
		    serial = 1;
		    for (int i = 0; i<KB.facultyCount; i++)
		    	if (KB.facultyList.get(i).category.equals(FacultyType.ATT))
		    	{
		    		double t = KB.getContactHr(i, true);
		    		double s = KB.getContactHr(i, false);
		    		totalCtT += t;
		    		totalCtS += s;
		    		
		    		writer.println(serial + "," + KB.facultyNames.get(i) + "," + t + "," + s + ",");
		    		serial++;
		    	}
		    writer.println(",Total Covered," + totalCtT + "," + totalCtS + ",");
		    
		    writer.println(",,,,");
		    writer.println(",,GUEST FACULTY,,");
		    writer.println("Ser,Name,Theory CrHr,Sessional CrHt,Remark");
		    totalCtT = totalCtS = 0.0;
		    serial = 1;
		    for (int i = 0; i<KB.facultyCount; i++)
		    	if (KB.facultyList.get(i).category.equals(FacultyType.GUEST))
		    	{
		    		double t = KB.getContactHr(i, true);
		    		double s = KB.getContactHr(i, false);
		    		totalCtT += t;
		    		totalCtS += s;
		    		writer.println(serial + "," + KB.facultyNames.get(i) + "," + t + "," + s + ",");
		    		serial++;
		    	}
		    writer.println(",Total Covered," + totalCtT + "," + totalCtS + ",");
		    
		    writer.println(",,,,");
		    writer.println(",,ADJUNCT FACULTY,,");
		    writer.println("Ser,Name,Theory CrHr,Sessional CrHt,Remark");
		    totalCtT = totalCtS = 0.0;
		    serial = 1;
		    for (int i = 0; i<KB.facultyCount; i++)
		    	if (KB.facultyList.get(i).category.equals(FacultyType.ADJUNCT))
		    	{
		    		double t = KB.getContactHr(i, true);
		    		double s = KB.getContactHr(i, false);
		    		totalCtT += t;
		    		totalCtS += s;
		    		writer.println(serial + "," + KB.facultyNames.get(i) + "," + t + "," + s + ",");
		    		serial++;
		    	}
		    writer.println(",Total Covered," + totalCtT + "," + totalCtS + ",");
		    
		    writer.close();
		    
		} catch (IOException e) {
		   // do something
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Export() {
		setType(Type.UTILITY);
		setAlwaysOnTop(true);
		
		SetNativeLook();
		setTitle("Export to output folder");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 270, 217);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 1, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(5, 5, 5, 5)));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		chckbxCreditSummary = new JCheckBox("Credit Summary");
		chckbxCreditSummary.setSelected(true);
		panel.add(chckbxCreditSummary);
		
		chckbxFaculty = new JCheckBox("Faculty Assignment");
		chckbxFaculty.setSelected(true);
		panel.add(chckbxFaculty);
		
		chckbxCourseAssignment = new JCheckBox("Course Assignment");
		chckbxCourseAssignment.setSelected(true);
		panel.add(chckbxCourseAssignment);
		
		chckbxSessionalCourseAssignment = new JCheckBox("Budget Summary");
		chckbxSessionalCourseAssignment.setEnabled(false);
		panel.add(chckbxSessionalCourseAssignment);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(1, 1, 1, 1));
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblStatus.setText("Please wait, saving...");
				
				if (chckbxCreditSummary.isSelected())
				{
					lblStatus.setText("Saving Summary...");
					exportSummary();
				}
				
				if (chckbxFaculty.isSelected())
				{
					lblStatus.setText("Saving Faculty Distribution...");
					exportFaculty();
				}
				
				if (chckbxCourseAssignment.isSelected())
				{
					lblStatus.setText("Saving Course Assignment Outline...");
					exportCourses();
				}
				
				lblStatus.setText("");
				
				dispose();
			}
		});
		
		lblStatus = new JLabel("");
		panel_1.add(lblStatus);
		panel_1.add(btnExport);
		this.setVisible(false);
	}

}
