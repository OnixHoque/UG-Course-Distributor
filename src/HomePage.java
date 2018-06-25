import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import windowless.KB;

import java.awt.Component;

import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Rectangle;
import java.awt.Cursor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;


public class HomePage {

	private JFrame frmUgCourseDistributor;
	private JTable tblFaculty;
	private JTable tblTheory;
	private JTable tblSessional;
	private JLabel lblStatus;
	public JButton btnUpdate;
	Summary s;
	HomePage hp;
	Modify m;
	Export exp;
	private JToolBar toolBar_1;
	private JTabbedPane tabbedPane;
	private JScrollPane sp;
	private JScrollPane sp2;
	private JScrollPane sp3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage window = new HomePage();
					window.frmUgCourseDistributor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void UpdateTables()
	{
		String facultyColumn[] = {"Name", "Type", "Theory Assigned", "Theory CtHr", "Sessional Assigned", "Sessional CtHr", "Total CtHr"};
		String facultyTable[][] = new String [KB.facultyCount][7];
		for (int i = 0; i< KB.facultyCount; i++)
		{
			double t = KB.getContactHr(i, true);
			double s = KB.getContactHr(i, false);
			facultyTable[i][0] = String.valueOf(i+1) + ". " + KB.facultyNames.get(i);
			facultyTable[i][1] = KB.facultyList.get(i).category;
			facultyTable[i][2] = KB.getFacultiesCourses(i, true);
			facultyTable[i][3] = String.valueOf(t);
			facultyTable[i][4] = KB.getFacultiesCourses(i, false);
			facultyTable[i][5] = String.valueOf(s);
			facultyTable[i][6] = String.valueOf(t + s);
		}
		DefaultTableModel fTable = new DefaultTableModel(facultyTable, facultyColumn);
		tblFaculty.setModel(fTable);
		
		String theoryColumn[] = {"Code", "Title", "Assigned Teachers", "Contact Hour", "Credit Hour", "Non Departmental"};
		String theoryTable[][] = new String [KB.theoryCount][6];
		for (int i = 0; i< KB.theoryCount; i++)
		{
			theoryTable[i][0] = String.valueOf(i+1) + ". " + KB.theoryCodes.get(i);
			theoryTable[i][1] = KB.theoryList.get(i).title;
			theoryTable[i][2] = KB.getAssignedTeachers(i, true).toString();
			theoryTable[i][3] = String.valueOf(KB.theoryList.get(i).CtHr);
			theoryTable[i][4] = String.valueOf(KB.theoryList.get(i).CrHr);
			if (KB.theoryList.get(i).non_dept)
				theoryTable[i][5] = "Yes";
			else
				theoryTable[i][5] = "No";
		}
		DefaultTableModel tTable = new DefaultTableModel(theoryTable, theoryColumn);
		tblTheory.setModel(tTable);
		
		String sessionalColumn[] = {"Code", "Title", "Assigned Teachers", "Credit Hour", "Capacity", "Total Contact Hour", "Contact Hr Covered", "Non Departmental", "Remark"};
		String sessionalTable[][] = new String [KB.sessionalCount][9];
		for (int i = 0; i< KB.sessionalCount; i++)
		{
			double ct = KB.sessionalList.get(i).CtHr;
			int cap = KB.sessionalList.get(i).capacity;
			ArrayList<String> temp = KB.getAssignedTeachers(i, false);
			sessionalTable[i][0] = String.valueOf(i+1) + ". " + KB.sessionalCodes.get(i);
			sessionalTable[i][1] = KB.sessionalList.get(i).title;
			sessionalTable[i][2] = temp.toString();
			sessionalTable[i][3] = String.valueOf(KB.sessionalList.get(i).CrHr);
			sessionalTable[i][4] = String.valueOf(cap);
			sessionalTable[i][5] = String.valueOf(ct) + " x " + String.valueOf(cap) + " = " + String.valueOf(ct * cap * 1.0);
			//sessionalTable[i][6] = String.valueOf(ct * temp.size() * 1.0);
			sessionalTable[i][6] = String.valueOf(KB.getSessionalTotalContact(i) * 1.0);
			
			if (KB.sessionalList.get(i).non_dept)
				sessionalTable[i][7] = "Yes";
			else
				sessionalTable[i][7] = "No";
			
			//int diff = cap - temp.size();
			double diff = cap*ct - KB.getSessionalTotalContact(i);
			int person = 0;
			String extra = "";
			if (diff != 0)
			{
				if (diff % 3 == 0)
					person = (int) (diff / 3);
				else if ((diff>1.5) && (diff-1.5)%3 == 0) 
				{
					person = (int) ((diff -1.5) / 3);
					extra = "+";
				}
					
				else if (diff == 1.5)
					person = 1;
				else
					person = 0;
			}
			String p = "";
			if (person != 0)
				p = " (" + person + extra + " person)";
			
			if (diff == 0)
				sessionalTable[i][8] = "OK";
			else if (diff < 0)
				sessionalTable[i][8] = diff * (-1) + " CtHr extra" + p;
			else
				sessionalTable[i][8] = diff  + " CtHr remaining" + p;
		}
		DefaultTableModel sTable = new DefaultTableModel(sessionalTable, sessionalColumn);
		tblSessional.setModel(sTable);
		
	}
	private void setState()
	{
		int i = KB.loadState();
		if (i == -1)
		{
			lblStatus.setText("Status: Fresh configuration file loaded");
		}
		else if (i == 0)
		{
			lblStatus.setText("Status: Ready");
		}
		else
		{
			lblStatus.setText("Status: Configuration file mismatch");
			KB.backupState();
		}
	}
	/**
	 * Create the application.
	 */
	public HomePage() {
		hp = this;
		SetNativeLook();
		initialize();
		KB.readFromFile();
		setState();	
		UpdateTables();
		s = new Summary();
		s.setVisible(true);
		m = new Modify(frmUgCourseDistributor, "", Dialog.ModalityType.MODELESS, hp, s);
		frmUgCourseDistributor.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar_1, lblStatus, tabbedPane, sp, tblFaculty, sp2, tblTheory, sp3, tblSessional}));
		exp = new Export();
		//m.setVisible(false);
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
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmUgCourseDistributor = new JFrame();
		frmUgCourseDistributor.setIconImage(Toolkit.getDefaultToolkit().getImage(HomePage.class.getResource("/windowless/icon_faculty.png")));
		frmUgCourseDistributor.setTitle("UG Course Distributor");
		frmUgCourseDistributor.setBounds(100, 100, 1012, 627);
		frmUgCourseDistributor.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmUgCourseDistributor.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				//super.windowClosing(e);
				//System.out.println("Window Closing");
				KB.saveState();
				System.exit(0);
			}
			
		});
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frmUgCourseDistributor.setLocation(dim.width/2-frmUgCourseDistributor.getSize().width/2, dim.height/2-frmUgCourseDistributor.getSize().height/2);
		
		
		
		JToolBar toolBar = new JToolBar();
		toolBar.setName("Toolbar");
		toolBar.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		toolBar.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2), UIManager.getBorder("ToolBar.border")));
		frmUgCourseDistributor.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnUpdate = new JButton("Edit");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		btnUpdate.setIcon(new ImageIcon(HomePage.class.getResource("/windowless/edit2.png")));
		//hp = this;
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Modify m = new Modify(frmUgCourseDistributor, "", Dialog.ModalityType.MODELESS, hp, s);
				m.setVisible(true);
				//hp.btnUpdate.setEnabled(false);
				//m.show();
			}
		});
		toolBar.add(btnUpdate);
		
		JButton btnRefresh = new JButton("Refresh");
		//btnRefresh.setHorizontalTextPosition(JLabel.CENTER);
		//btnRefresh.setVerticalTextPosition(JLabel.BOTTOM);
		btnRefresh.setIcon(new ImageIcon(HomePage.class.getResource("/windowless/refresh.png")));
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//KB.readFromFile();
				UpdateTables();
			}
		});
		toolBar.add(btnRefresh);
		
		JButton btnSave = new JButton("Export");
		//btnSave.setHorizontalTextPosition(JLabel.CENTER);
		//btnSave.setVerticalTextPosition(JLabel.BOTTOM);
		btnSave.setIcon(new ImageIcon(HomePage.class.getResource("/windowless/_export.png")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Export export = new Export();
				exp.setVisible(true);
			}
		});
		toolBar.add(btnSave);
		
		toolBar_1 = new JToolBar();
		toolBar_1.setBorder(new CompoundBorder(UIManager.getBorder("ToolBar.border"), new EmptyBorder(5, 5, 5, 5)));
		toolBar_1.setFloatable(false);
		frmUgCourseDistributor.getContentPane().add(toolBar_1, BorderLayout.SOUTH);
		
		lblStatus = new JLabel("Status: Ready");
		toolBar_1.add(lblStatus);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(0, 5, 0, 5));
		frmUgCourseDistributor.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		tblFaculty = new JTable();
		sp=new JScrollPane(tblFaculty); 
		sp.setBorder(UIManager.getBorder("FormattedTextField.border"));
		tabbedPane.addTab("Faculty", null, sp, null);
		
		tblTheory = new JTable();
		sp2 =new JScrollPane(tblTheory);
		tabbedPane.addTab("Theory Course", null, sp2, null);
		
		tblSessional = new JTable();
		sp3=new JScrollPane(tblSessional);
		tabbedPane.addTab("Sessional", null, sp3, null);
		
		frmUgCourseDistributor.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar_1, tblFaculty, tblTheory, tblSessional}));
	}

}



//
////Old Code---
//
//import java.awt.Dialog;
//import java.awt.Dimension;
//import java.awt.EventQueue;
//import java.awt.Toolkit;
//
//import javax.swing.JFrame;
//import javax.swing.JToolBar;
//
//import java.awt.BorderLayout;
//
//import javax.swing.JLabel;
//import javax.swing.JButton;
//import javax.swing.JTabbedPane;
//import javax.swing.JTable;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//
//import org.eclipse.wb.swing.FocusTraversalOnArray;
//
//import windowless.KB;
//
//import java.awt.Component;
//
//
//public class HomePage {
//
//	private JFrame frmUgCourseDistributor;
//	private JTable tblFaculty;
//	private JTable tblTheory;
//	private JTable tblSessional;
//
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					HomePage window = new HomePage();
//					window.frmUgCourseDistributor.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public HomePage() {
//		SetNativeLook();
//		initialize();
//		KB.readFromFile();
//	}
//	
//	private void SetNativeLook()
//	{
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		
//		frmUgCourseDistributor = new JFrame();
//		frmUgCourseDistributor.setTitle("UG Course Distributor");
//		frmUgCourseDistributor.setBounds(100, 100, 791, 537);
//		frmUgCourseDistributor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//		frmUgCourseDistributor.setLocation(dim.width/2-frmUgCourseDistributor.getSize().width/2, dim.height/2-frmUgCourseDistributor.getSize().height/2);
//		
//		
//		JToolBar toolBar = new JToolBar();
//		frmUgCourseDistributor.getContentPane().add(toolBar, BorderLayout.NORTH);
//		
//		JButton btnUpdate = new JButton("Update");
//		btnUpdate.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				Modify m = new Modify(frmUgCourseDistributor, "", Dialog.ModalityType.DOCUMENT_MODAL);
//				//m.show();
//			}
//		});
//		toolBar.add(btnUpdate);
//		
//		JButton btnRefresh = new JButton("Refresh");
//		btnRefresh.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				KB.readFromFile();
//			}
//		});
//		toolBar.add(btnRefresh);
//		
//		JButton btnSave = new JButton("Save");
//		toolBar.add(btnSave);
//		
//		JToolBar toolBar_1 = new JToolBar();
//		frmUgCourseDistributor.getContentPane().add(toolBar_1, BorderLayout.SOUTH);
//		
//		JLabel lblStatus = new JLabel("Status: Ready");
//		toolBar_1.add(lblStatus);
//		
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		frmUgCourseDistributor.getContentPane().add(tabbedPane, BorderLayout.CENTER);
//		
//		tblFaculty = new JTable();
//		tabbedPane.addTab("Faculty", null, tblFaculty, null);
//		
//		tblTheory = new JTable();
//		tabbedPane.addTab("Theory Course", null, tblTheory, null);
//		
//		tblSessional = new JTable();
//		tabbedPane.addTab("Sessional", null, tblSessional, null);
//		frmUgCourseDistributor.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar_1, tblFaculty, tblTheory, tblSessional}));
//	}
//
//}
