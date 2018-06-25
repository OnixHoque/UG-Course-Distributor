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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import java.awt.Color;
import java.awt.Desktop;

import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;

public class Export extends JDialog {

	private JPanel contentPane;
	public JCheckBox chckbxCreditSummary;
	public JCheckBox chckbxFaculty;
	public JCheckBox chckbxCourseAssignment;
	public JCheckBox chckbxSessionalCourseAssignment;
	private JLabel lblStatus;
	
	private static String SUMMARY_FILE_NAME = "output/Summary.docx";
	private static String FACULTY_FILE_NAME = "output/Faculty.docx";
	private static String COURSE_FILE_NAME = "output/Courses.docx";
	
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
	
	public static void makeTable(XWPFDocument document, String [][] array, Integer []widths)
	{
		int row = array.length;
		int col = array[0].length;
		
		XWPFTable table = document.createTable(row, col);
		//create the header
		
		 //values are in unit twentieths of a point (1/1440 of an inch)
		  table.setWidth(5*1440); //should be 5 inches width, not sure how this is useful
	
		  //create CTTblGrid for this table with widths of the 2 columns. 
		  //necessary for Libreoffice/Openoffice to accept the column widths.
		  //first column = 2 inches width
		  table.getCTTbl().addNewTblGrid().addNewGridCol().setW(BigInteger.valueOf(2*1440));
		  //other columns (only one in this case) = 3 inches width
		  for (int c = 1 ; c < col; c++) {
			   table.getCTTbl().getTblGrid().addNewGridCol().setW(BigInteger.valueOf(3*1440));
			  }
		  
		  CTTblWidth tblWidth = table.getRow(0).getCell(0).getCTTc().addNewTcPr().addNewTcW();
		  tblWidth.setW(BigInteger.valueOf(widths[0]*1440));
		  //STTblWidth.DXA is used to specify width in twentieths of a point.
		  tblWidth.setType(STTblWidth.DXA);
		  
		  for (int i = 1; i<col; i++)
		  {
			  tblWidth = table.getRow(0).getCell(i).getCTTc().addNewTcPr().addNewTcW();
			  tblWidth.setW(BigInteger.valueOf(widths[i]*1440));
			  tblWidth.setType(STTblWidth.DXA);
		  }
		 
		
		XWPFTableRow tableRowOne = table.getRow(0);
		tableRowOne.getCell(0).setText(array[0][0]);	//(0, 0)
		for (int i = 1; i<col; i++)
		{
			String s = array[0][i];
	     	//tableRowOne.addNewTableCell().setText(s);	//(0, ... )
			tableRowOne.getCell(i).setText(s);	//(0, ... )
		}
		
		//First row is added, now add the rest of the rows
		for (int i = 1;i<row; i++)
		{
			String [] cur_row = array[i];
			//XWPFTableRow new_row = table.createRow();
			XWPFTableRow new_row = table.getRow(i);
			int c = 0;
			for (String item : cur_row)
			{
				if (item.contains("\n"))
				{
					String lines[] = item.split("\n");
					XWPFTableCell cl = new_row.getCell(c++);
					
					int ser = 1;
					for (String it : lines)
					{
						it = it.trim();
						//System.out.println(it);
						if (it.equals(""))
							continue;
						if (ser == 1)
						{
							cl.setText(ser + ". " + it);
						}
						else
						{
							cl.addParagraph().createRun().setText(ser + ". " + it);
						}
						
						ser++;
					}
					
				}
				else
					new_row.getCell(c++).setText(item);
			}
		}
		
	}
	
	void exportSummary()
	{
		try{
			XWPFDocument document = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File(SUMMARY_FILE_NAME));
			
			//add the heading
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
			
			document.createParagraph().createRun().setText("Generated On: " + date_time);
			document.createParagraph().createRun().setText("UG Course Distribution Summary");
			
			String [][] summaryTable = new String[7][3];
			summaryTable[0][0] = "Taken By";		summaryTable[0][1] = "Theory";															summaryTable[0][2]="Sessional";
			summaryTable[1][0] = "Total";			summaryTable[1][1] = String.valueOf(KB.getTotalContactHr(true));						summaryTable[1][2]=String.valueOf(KB.getTotalContactHr(false));
			summaryTable[2][0] = "Perm";			summaryTable[2][1] = String.valueOf(KB.getContactHrCovered(FacultyType.PERM, true));	summaryTable[2][2]=String.valueOf(KB.getContactHrCovered(FacultyType.PERM, false));
			summaryTable[3][0] = "Att";				summaryTable[3][1] = String.valueOf(KB.getContactHrCovered(FacultyType.ATT, true));		summaryTable[3][2]=String.valueOf(KB.getContactHrCovered(FacultyType.ATT, false));
			summaryTable[4][0] = "Guest";			summaryTable[4][1] = String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, true));	summaryTable[4][2]=String.valueOf(KB.getContactHrCovered(FacultyType.GUEST, false));
			summaryTable[5][0] = "Adjunct";			summaryTable[5][1] = String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, true));	summaryTable[5][2]=String.valueOf(KB.getContactHrCovered(FacultyType.ADJUNCT, false));
			
			
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
			
			summaryTable[6][0] = "Unassigned";		summaryTable[6][1] = diff_theory + "";													summaryTable[6][2]=diff_sessional + "";
			
			Integer[] w = {1, 3, 3};
			makeTable(document, summaryTable, w);
			
			document.write(out);
			out.close();
			document.close();
		    
		} catch (IOException e) {
		   // do something
		}
	}
	
	@Deprecated
	void _exportSummary()
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
	
	void addSessionalTable(int level, boolean nondept, XWPFDocument document)
	{
	    ArrayList<Integer> selected_sessional_list = new ArrayList<>();
	    for (int i = 0; i<KB.sessionalCount; i++)
	    	if ((KB.sessionalList.get(i).level == level) && KB.sessionalList.get(i).non_dept == nondept)
	    	{
	    		selected_sessional_list.add(i);
	    	}
	    
	    String[][] sessionalTable = new String[selected_sessional_list.size()+2][];
	    sessionalTable[0] = new String[]{"Ser", "Course Code", "Course Title", "CrHr", "Teacher", "Total CtHr", "Remark"};
	    
	    double Cttotal = 0.0;
	    
	    for (int ser = 1; ser<=selected_sessional_list.size(); ser++)
	    	{
	    		int i = selected_sessional_list.get(ser-1);
	    		double subtotal_cr = KB.getSessionalTotalContact(i);
    			Cttotal += subtotal_cr;
	    		sessionalTable[ser] = new String[] {ser + "", KB.sessionalCodes.get(i) , KB.sessionalList.get(i).title, KB.sessionalList.get(i).CrHr + "", KB.sessionalList.get(i).capacity +"x Teachers", subtotal_cr+"", KB.getAssignedTeachers(i, false).toString().replace(",", "\n ").replace("[", "").replace("]", "")};
	    	}
	    sessionalTable[selected_sessional_list.size()+1] = new String[] {"","","","", "Total",  Cttotal + "" , ""};
	    
	    makeTable(document, sessionalTable, new Integer[] {1, 2, 2, 1, 2, 1, 3});
	}
	
	void addTheoryTable(int level, boolean nondept, XWPFDocument document)
	{
	    ArrayList<Integer> selected_theory_list = new ArrayList<>();
	    for (int i = 0; i<KB.theoryCount; i++)
	    	if ((KB.theoryList.get(i).level == level) && (KB.theoryList.get(i).non_dept == nondept))
	    	{
	    		selected_theory_list.add(i);
	    	}
	    
	    String[][] theoryTable = new String[selected_theory_list.size()+2][];
	    theoryTable[0] = new String[]{"Ser", "Course Code","Course Title","CtHr","Teacher" , "Remark"};
	    
	    double Cttotal = 0.0;
	    
	    for (int ser = 1; ser<=selected_theory_list.size(); ser++)
	    	{
	    		int i = selected_theory_list.get(ser-1);
	    		Cttotal += KB.theoryList.get(i).CtHr; 
	    		theoryTable[ser] = new String[] {ser + "", KB.theoryCodes.get(i) , KB.theoryList.get(i).title, KB.theoryList.get(i).CtHr + "",KB.getAssignedTeachers(i, true).toString().replace(",", "\n ").replace("[", "").replace("]", ""),""};
	    	}
	    theoryTable[selected_theory_list.size()+1] = new String[] {"","", "Total",  Cttotal + "" , "" , ""};
	    
	    makeTable(document, theoryTable, new Integer[] {1, 2, 2, 1, 2, 3, 0});
	}
	
	void exportCourses()
	{
		try{
			XWPFDocument document = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File(COURSE_FILE_NAME));
			
			//add the heading
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
			
			document.createParagraph().createRun().setText("Generated On: " + date_time);
			document.createParagraph().createRun().setText("UG Course Distribution Summary");
			
			for (int i = 1; i<=4; i++)
			{
				document.createParagraph().createRun().setText("");
				document.createParagraph().createRun().setText("Level-" + i);
				document.createParagraph().createRun().setText("Theory");
				addTheoryTable(i, false, document);
				
				document.createParagraph().createRun().setText("Sessional");
				addSessionalTable(i, false, document);
				
				int n_theory = KB.getNonDeptCount(i, false);
				int n_sessional= KB.getNonDeptCount(i, true);
				if (n_theory == 0 && n_sessional == 0)
					continue;
				document.createParagraph().createRun().setText("Non-Departmental");
				if (n_theory != 0)
				{
					document.createParagraph().createRun().setText("Theory");
					addTheoryTable(i, true, document);
				}

				if (n_sessional != 0)
				{
					document.createParagraph().createRun().setText("Sessional");
					addSessionalTable(i, true, document);
				}
			}
			
			document.write(out);
			out.close();
			document.close();
		    
		} catch (IOException e) {
		   // do something
		}
	}
	
	@Deprecated
	void _exportCourses()
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
	void addFacultyTable(String facultyType, XWPFDocument document)
	{
	    double totalCtT, totalCtS;
	    ArrayList<Integer> selected_faculty_list = new ArrayList<>();
	    for (int i = 0; i<KB.facultyCount; i++)
	    	if (KB.facultyList.get(i).category.equals(facultyType))
	    	{
	    		selected_faculty_list.add(i);
	    	}
	    
	    String[][] facultyTable = new String[selected_faculty_list.size()+2][];
	    facultyTable[0] = new String[]{"Ser", "Name","Theory CrHr","Sessional CrHt","Remark"};
	    
	    totalCtT = totalCtS = 0.0;
	    
	    for (int ser = 1; ser<=selected_faculty_list.size(); ser++)
	    	{
	    		int i = selected_faculty_list.get(ser-1);
	    		double t = KB.getContactHr(i, true);
	    		double s = KB.getContactHr(i, false);
	    		totalCtT += t;
	    		totalCtS += s;
	    		facultyTable[ser] = new String[] {ser + "", KB.facultyNames.get(i), t +"" , s + "", "" };
	    	}
	    facultyTable[selected_faculty_list.size()+1] = new String[] {"","Total Covered",  totalCtT +"" , totalCtS + "" , ""};
	    
	    makeTable(document, facultyTable, new Integer[] {1, 3, 2, 2, 2});
	}
	
	void exportFaculty()
	{
		try{
			XWPFDocument document = new XWPFDocument();
			FileOutputStream out = new FileOutputStream(new File(FACULTY_FILE_NAME));
			
			//add the heading
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
			Date date = new Date();
			String date_time = dateFormat.format(date); //2016/11/16 12:08:43
			
			document.createParagraph().createRun().setText("Generated On: " + date_time);
			document.createParagraph().createRun().setText("Faculty Course Distribution Summary");
			
			document.createParagraph().createRun().setText("");
			document.createParagraph().createRun().setText("PERM FACULTY");
			addFacultyTable(FacultyType.PERM, document);
			
			document.createParagraph().createRun().setText("");
			document.createParagraph().createRun().setText("ATT FACULTY");
			addFacultyTable(FacultyType.ATT, document);
			
			document.createParagraph().createRun().setText("");
			document.createParagraph().createRun().setText("GUEST FACULTY");
			addFacultyTable(FacultyType.GUEST, document);
			
			document.createParagraph().createRun().setText("");
			document.createParagraph().createRun().setText("ADJUNCT FACULTY");
			addFacultyTable(FacultyType.ADJUNCT, document);
			
			document.write(out);
			out.close();
			document.close();
		    
		} catch (IOException e) {
		   // do something
		}
	}
	@Deprecated
	void _exportFaculty()
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
	
	
	public Export() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Export.class.getResource("/windowless/_export.png")));
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
		
		final JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setMessage("Please wait, saving...");
				lblStatus.setText("Please wait, saving...");
				btnExport.setEnabled(false);
				new Timer(10, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//System.out.println("Executing");
						
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
						
						try {
							Desktop.getDesktop().open(new File("output"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						lblStatus.setText("");
						btnExport.setEnabled(true);
						dispose();
						((Timer)e.getSource()).stop();
						
					}
				}).start();
			}
		});
		
		lblStatus = new JLabel("");
		panel_1.add(lblStatus);
		panel_1.add(btnExport);
		this.setVisible(false);
	}

}
