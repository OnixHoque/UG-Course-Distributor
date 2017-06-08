import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import java.awt.BorderLayout;

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
			lblStatus.setText("Status: Configuration file loaded successfully");
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
		frmUgCourseDistributor.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnUpdate = new JButton("Edit");
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
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//KB.readFromFile();
				UpdateTables();
			}
		});
		toolBar.add(btnRefresh);
		
		JButton btnSave = new JButton("Export");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Export export = new Export();
				exp.setVisible(true);
			}
		});
		toolBar.add(btnSave);
		
		JToolBar toolBar_1 = new JToolBar();
		frmUgCourseDistributor.getContentPane().add(toolBar_1, BorderLayout.SOUTH);
		
		lblStatus = new JLabel("Status: Ready");
		toolBar_1.add(lblStatus);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frmUgCourseDistributor.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		tblFaculty = new JTable();
		JScrollPane sp=new JScrollPane(tblFaculty); 
		tabbedPane.addTab("Faculty", null, sp, null);
		
		tblTheory = new JTable();
		JScrollPane sp2 =new JScrollPane(tblTheory);
		tabbedPane.addTab("Theory Course", null, sp2, null);
		
		tblSessional = new JTable();
		JScrollPane sp3=new JScrollPane(tblSessional);
		tabbedPane.addTab("Sessional", null, sp3, null);
		
		frmUgCourseDistributor.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{toolBar_1, tblFaculty, tblTheory, tblSessional}));
	}

}
