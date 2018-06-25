import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComboBox;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import windowless.KB;

import java.awt.Component;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.CompoundBorder;


public class Modify extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JRadioButton rdbtnNotAssigned;
	private JRadioButton rdbtnFulltimeAssigned;
	private JRadioButton rdbtnFirstHalffirst;
	private JRadioButton rdbtnSecondHalflast;
	private JRadioButton rdbtnTheory;
	private JRadioButton rdbtnSessional;
	private JPanel panel_3;
	private JLabel lblCreditHourCovered;
	private JLabel lblTheory;
	private JLabel lblSessional;
	private JLabel lblAssignedCourses;
	private JComboBox cboFaculty;
	private JList lstCourses;
	private JPanel panel;
	private boolean theorySelected;
	private JLabel lblInstructors;
	HomePage hp;
	Summary s;
	
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
	 * Create the dialog.
	 */

public void actionPerformed(ActionEvent e){    
if(rdbtnTheory.isSelected()){
	//theorySelected = true;
	//lstCourses.setSelectedIndex(1);
	updateCourseList(true);
    //System.out.println("1 selected");
	
}    
if(rdbtnSessional.isSelected()){
	//theorySelected = false;
	//lstCourses.setSelectedIndex(1);
	updateCourseList(false);
	//System.out.println("2 selected");
} 
if(rdbtnNotAssigned.isSelected())
{
	
}
if(rdbtnFulltimeAssigned.isSelected())
{
	
} 
if(rdbtnFirstHalffirst.isSelected())
{
	
} 
if(rdbtnSecondHalflast.isSelected())
{
	
}

} 

private void UpdatePage()
{
	int idx = lstCourses.getSelectedIndex();
	int f_idx = cboFaculty.getSelectedIndex();
	
	//String name = cboFaculty.getSelectedItem().toString();
	lblTheory.setText("Theory: " + KB.getContactHr(f_idx, true));
	lblSessional.setText("Sessional: " + KB.getContactHr(f_idx, false));
	
	lblAssignedCourses.setText("Theory: " + KB.getFacultiesCourses(f_idx, true) + ", Sessional: " + KB.getFacultiesCourses(f_idx, false));
	
	if (idx > -1)
	{
		if (theorySelected)
		{
			if (idx > -1)
			{
				int val = KB.theoryMatrix[f_idx][idx];
				//System.out.println("th -> " + val);
				if (val == 0)
					rdbtnNotAssigned.setSelected(true);
				else if (val == 1)
					rdbtnFulltimeAssigned.setSelected(true);
				else if (val == 2)
					rdbtnFirstHalffirst.setSelected(true);
				else if (val == 3)
					rdbtnSecondHalflast.setSelected(true);
				
			}
			
			ArrayList<String> temp = KB.getAssignedTeachers(idx, true);
			String s = new String("<html>"+ KB.theoryCodes.get(idx) + "<br>" + KB.theoryList.get(idx).title + "<br>Credit Hour: " + KB.theoryList.get(idx).CrHr + "<br><br> Assigned teachers: (" + temp.size() + ")");
			int p = 1;
			for (String t: temp)
			{
				s += "<br>" + p + ". " + t;
				p++;
			}
			lblInstructors.setText(s);
		}
		else
		{
			int val = KB.sessionalMatrix[f_idx][idx];
			
			if (val == 0)
				rdbtnNotAssigned.setSelected(true);
			else if (val == 1)
				rdbtnFulltimeAssigned.setSelected(true);
			else if (val == 2)
				rdbtnFirstHalffirst.setSelected(true);
			else if (val == 3)
				rdbtnSecondHalflast.setSelected(true);
			
			ArrayList<String> temp = KB.getAssignedTeachers(idx, false);
			String s = new String("<html>"+ KB.sessionalCodes.get(idx) + "<br>" + KB.sessionalList.get(idx).title + "<br>Credit Hour: " + KB.sessionalList.get(idx).CrHr + "<br>Capacity: " + KB.sessionalList.get(idx).capacity + " person(s)<br><br> Assigned teachers: (" + temp.size() + "/" + KB.sessionalList.get(idx).capacity + ")");
			int p = 1;
			for (String t: temp)
			{
				s += "<br>" + p + ". " + t;
				p++;
			}
			lblInstructors.setText(s);
		}
		
		
	}
	
	hp.UpdateTables();
	s.UpdateTable();
}

private void initialize() {
	
	setBounds(100, 100, 683, 606);
	
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	contentPanel.setLayout(new BorderLayout(0, 0));
	{
		panel = new JPanel();
		panel.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(5, 5, 5, 5)));
		contentPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		{
			cboFaculty = new JComboBox();
			cboFaculty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					
					UpdatePage();
				}
			});
			panel.add(cboFaculty);
		}
		{
			panel_3 = new JPanel();
			panel.add(panel_3);
			panel_3.setLayout(new GridLayout(1, 3, 0, 0));
			{
				lblCreditHourCovered = new JLabel("Contact Hour Covered");
				lblCreditHourCovered.setHorizontalAlignment(SwingConstants.CENTER);
				lblCreditHourCovered.setFont(new Font("Tahoma", Font.BOLD, 11));
				panel_3.add(lblCreditHourCovered);
			}
			{
				lblTheory = new JLabel("Theory: 3.0");
				panel_3.add(lblTheory);
			}
			{
				lblSessional = new JLabel("Sessional: 1.5");
				panel_3.add(lblSessional);
			}
		}
		{
			lblAssignedCourses = new JLabel("HUM271 A, HUM272 B, CSE106");
			panel.add(lblAssignedCourses);
		}
		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panel_3, lblCreditHourCovered, lblTheory, lblSessional, lblAssignedCourses}));
	}
	{
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);
		{
			JPanel TheoryPanel = new JPanel();
			tabbedPane.addTab("Course Assignment", null, TheoryPanel, null);
			TheoryPanel.setLayout(new GridLayout(1, 2, 0, 0));
			{
				JPanel panel = new JPanel();
				panel.setBorder(new EmptyBorder(5, 5, 5, 5));
				TheoryPanel.add(panel);
				panel.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_1 = new JPanel();
					panel_1.setBorder(new EmptyBorder(3, 3, 3, 3));
					panel.add(panel_1, BorderLayout.NORTH);
									
					panel_1.setLayout(new GridLayout(1, 2, 0, 0));
					{
						rdbtnTheory = new JRadioButton("Theory Course");
						rdbtnTheory.addActionListener(this); 
						rdbtnTheory.setSelected(true);
						panel_1.add(rdbtnTheory);
					}
					{
						rdbtnSessional = new JRadioButton("Sessional Course");
						rdbtnSessional.addActionListener(this);
						panel_1.add(rdbtnSessional);
					}
					ButtonGroup course_group = new ButtonGroup();
					course_group.add(rdbtnTheory);
					course_group.add(rdbtnSessional);
					
				}
				
				{
					lstCourses = new JList(KB.lm_sessionalCodes);
					lstCourses.setBorder(new EmptyBorder(5, 5, 5, 5));
					lstCourses.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent arg0) {
							int idx = lstCourses.getSelectedIndex();
							int f_idx = cboFaculty.getSelectedIndex();
							
							if (theorySelected == true)
							{
								if (idx > -1)
								{
									/*
									int val = KB.theoryMatrix[f_idx][idx];
									//System.out.println("th -> " + val);
									if (val == 0)
										rdbtnNotAssigned.setSelected(true);
									else if (val == 1)
										rdbtnFulltimeAssigned.setSelected(true);
									else if (val == 2)
										rdbtnFirstHalffirst.setSelected(true);
									else if (val == 3)
										rdbtnSecondHalflast.setSelected(true);
									*/
									
								}
									//lblAssignedCourses.setText(" " + idx + " " + KB.theoryCodes.get(idx));
								//System.out.println("th");
								
								/*ArrayList<String> temp = KB.getAssignedTeachers(idx, true);
								String s = new String("<html>"+ KB.theoryCodes.get(idx) + "<br>Credit Hour: " + KB.theoryList.get(idx).CrHr + "<br><br> Assigned teachers: (" + temp.size() + ")");
								int p = 1;
								for (String t: temp)
								{
									s += "<br>" + p + ". " + t;
								}
								lblInstructors.setText(s);*/
							}
							else
							{
								if (idx > -1)
								{
									/*
									int val = KB.sessionalMatrix[f_idx][idx];
									
									if (val == 0)
										rdbtnNotAssigned.setSelected(true);
									else if (val == 1)
										rdbtnFulltimeAssigned.setSelected(true);
									else if (val == 2)
										rdbtnFirstHalffirst.setSelected(true);
									else if (val == 3)
										rdbtnSecondHalflast.setSelected(true);
									*/
									
									/*ArrayList<String> temp = KB.getAssignedTeachers(idx, false);
									String s = new String("<html>"+ KB.sessionalCodes.get(idx) + "<br>Credit Hour: " + KB.sessionalList.get(idx).CrHr + "<br>Capacity: " + KB.sessionalList.get(idx).capacity + "<br><br> Assigned teachers: (" + temp.size() + ")");
									int p = 1;
									for (String t: temp)
									{
										s += "<br>" + p + ". " + t;
									}
									lblInstructors.setText(s);*/
								}
									//lblAssignedCourses.setText(" " + idx + " " + KB.sessionalCodes.get(idx));
								//System.out.println("ss");
							}
							
							UpdatePage();
							
							
								
						}
					});
					lstCourses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
					//lstCourses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
					lstCourses.setVisibleRowCount(-1);
					JScrollPane listScroller = new JScrollPane(lstCourses);
					listScroller.setPreferredSize(new Dimension(250, 80));
					panel.add(listScroller, BorderLayout.CENTER);
				}
			}
			
			{
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(5, 5, 5, 5)));
				TheoryPanel.add(panel_1);
				panel_1.setLayout(new BorderLayout(0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new EmptyBorder(10, 0, 10, 0));
					panel_1.add(panel_2, BorderLayout.NORTH);
					panel_2.setLayout(new GridLayout(0, 1, 0, 10));
					{
						rdbtnNotAssigned = new JRadioButton("Not assigned");
						rdbtnNotAssigned.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								//System.out.println("NA");
								//System.out.println("Not assigned");
								int f, c;
								f = cboFaculty.getSelectedIndex();
								c = lstCourses.getSelectedIndex();
								if (theorySelected == true)
									KB.theoryMatrix[f][c] = 0;
								else
									KB.sessionalMatrix[f][c] = 0;
								UpdatePage();
							}
						});
						
						//rdbtnNotAssigned.addActionListener(this);
						rdbtnNotAssigned.setSelected(true);
						panel_2.add(rdbtnNotAssigned);
					}
					{
						rdbtnFulltimeAssigned = new JRadioButton("Full-time assigned (14 wk)");
						rdbtnFulltimeAssigned.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								//System.out.println("FT");
								
								//System.out.println("Fulltime");
								int f, c;
								f = cboFaculty.getSelectedIndex();
								c = lstCourses.getSelectedIndex();
								if (theorySelected == true)
									KB.theoryMatrix[f][c] = 1;
								else
									KB.sessionalMatrix[f][c] = 1;
								UpdatePage();
							}
						});
						//rdbtnFulltimeAssigned.addActionListener(this);
						panel_2.add(rdbtnFulltimeAssigned);
					}
					{
						rdbtnFirstHalffirst = new JRadioButton("First half (First 7 wk)");
						rdbtnFirstHalffirst.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								//System.out.println("FT");
								
								//System.out.println("First Half");
								int f, c;
								f = cboFaculty.getSelectedIndex();
								c = lstCourses.getSelectedIndex();
								if (theorySelected == true)
									KB.theoryMatrix[f][c] = 2;
								else
									KB.sessionalMatrix[f][c] = 2;
								UpdatePage();
							}
						});
						//rdbtnFirstHalffirst.addActionListener(this);
						panel_2.add(rdbtnFirstHalffirst);
					}
					{
						rdbtnSecondHalflast = new JRadioButton("Second half (Last 7 wk)");
						//rdbtnSecondHalflast.addActionListener(this);
						rdbtnSecondHalflast.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								//System.out.println("FT");
								//System.out.println("Second half");
								int f, c;
								f = cboFaculty.getSelectedIndex();
								c = lstCourses.getSelectedIndex();
								if (theorySelected == true)
									KB.theoryMatrix[f][c] = 3;
								else
									KB.sessionalMatrix[f][c] = 3;
								UpdatePage();
							}
						});
						panel_2.add(rdbtnSecondHalflast);
					}
				}
				
				ButtonGroup assignment_group = new ButtonGroup();
				assignment_group.add(rdbtnNotAssigned);
				assignment_group.add(rdbtnFulltimeAssigned);
				assignment_group.add(rdbtnFirstHalffirst);
				assignment_group.add(rdbtnSecondHalflast);
				{
					JPanel panel_2 = new JPanel();
					panel_2.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Course Details", TitledBorder.LEADING, TitledBorder.TOP, null, null)));
					panel_1.add(panel_2);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						lblInstructors = new JLabel("CSE304A instructors:\r");
						panel_2.add(lblInstructors, BorderLayout.NORTH);
					}
				}
			}
		}
	}
	{
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton cancelButton = new JButton("Close");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					//panel.dispatchEvent(new WindowEvent(, WindowEvent.WINDOW_CLOSING));
					dispose();
					//hp.btnUpdate.setEnabled(true);
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}
}


	private void eventHandler()
	{
		
	}
	
	void updateCourseList(boolean th)
	{
		if (th)
		{
			lstCourses.setModel(KB.lm_theoryCodes);
			theorySelected = true;
		}
		else
		{
			lstCourses.setModel(KB.lm_sessionalCodes);
			theorySelected = false;
		}
		lstCourses.setSelectedIndex(0);
		//theorySelected = th;
		//System.out.println(theorySelected + "*");
	}
	void updateFacultyCombo()
	{
		cboFaculty.removeAllItems();
		for (String s : KB.facultyNames)
		{
			cboFaculty.addItem(s);
		}
	}
	public Modify(Window arg0, String arg1, ModalityType arg2, HomePage _hp, Summary _s) {
		super(arg0, arg1, arg2);
		hp = _hp;
		s = _s;
		
		SetNativeLook();
		initialize();
		eventHandler();
		
		updateFacultyCombo();
		updateCourseList(true);
		try {
			//Modify dialog = new Modify();
			
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	}
	

}


//import java.awt.BorderLayout;
//import java.awt.FlowLayout;
//
//import javax.swing.JButton;
//import javax.swing.JDialog;
//import javax.swing.JPanel;
//import javax.swing.border.EmptyBorder;
//
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//
//import javax.swing.JComboBox;
//
//import java.awt.Dialog;
//import java.awt.Dimension;
//import java.awt.GridLayout;
//import java.awt.CardLayout;
//import java.awt.GridBagLayout;
//import java.awt.Toolkit;
//import java.awt.Window;
//
//import javax.swing.JLabel;
//
//import java.awt.Font;
//
//import javax.swing.ButtonGroup;
//import javax.swing.JScrollPane;
//import javax.swing.ListSelectionModel;
//import javax.swing.SwingConstants;
//import javax.swing.JTabbedPane;
//import javax.swing.JList;
//import javax.swing.JRadioButton;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//import javax.swing.border.BevelBorder;
//import javax.swing.border.EtchedBorder;
//
//import org.eclipse.wb.swing.FocusTraversalOnArray;
//
//import windowless.KB;
//
//import java.awt.Component;
//
//import javax.swing.border.TitledBorder;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ChangeListener;
//import javax.swing.event.ChangeEvent;
//import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
//
//
//public class Modify extends JDialog implements ActionListener {
//
//	private final JPanel contentPanel = new JPanel();
//	private JRadioButton rdbtnNotAssigned;
//	private JRadioButton rdbtnFulltimeAssigned;
//	private JRadioButton rdbtnFirstHalffirst;
//	private JRadioButton rdbtnSecondHalflast;
//	private JRadioButton rdbtnTheory;
//	private JRadioButton rdbtnSessional;
//	private JPanel panel_3;
//	private JLabel lblCreditHourCovered;
//	private JLabel lblTheory;
//	private JLabel lblSessional;
//	private JLabel lblAssignedCourses;
//	private JComboBox cboFaculty;
//	private JList lstCourses;
//	private JList lstFaculty;
//	private JPanel panel;
//	private boolean theorySelected;
//	
//private void SetNativeLook()
//{
//	try {
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//	} catch (ClassNotFoundException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (InstantiationException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (IllegalAccessException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (UnsupportedLookAndFeelException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//}
//	/**
//	 * Create the dialog.
//	 */
//
//public void actionPerformed(ActionEvent e){    
//if(rdbtnTheory.isSelected()){
//	//theorySelected = true;
//	//lstCourses.setSelectedIndex(1);
//	updateCourseList(true);
//    //System.out.println("1 selected");
//	
//}    
//if(rdbtnSessional.isSelected()){
//	//theorySelected = false;
//	//lstCourses.setSelectedIndex(1);
//	updateCourseList(false);
//	//System.out.println("2 selected");
//} 
//if(rdbtnNotAssigned.isSelected())
//{
//	//System.out.println("Not assigned");
//	int f, c;
//	f = cboFaculty.getSelectedIndex();
//	c = lstCourses.getSelectedIndex();
//	if (theorySelected == true)
//		KB.theoryMatrix[f][c] = 0;
//	else
//		KB.sessionalMatrix[f][c] = 0;
//}
//if(rdbtnFulltimeAssigned.isSelected())
//{
//	//System.out.println("Fulltime");
//	int f, c;
//	f = cboFaculty.getSelectedIndex();
//	c = lstCourses.getSelectedIndex();
//	if (theorySelected == true)
//		KB.theoryMatrix[f][c] = 1;
//	else
//		KB.sessionalMatrix[f][c] = 1;
//} 
//if(rdbtnFirstHalffirst.isSelected())
//{
//	//System.out.println("First Half");
//	int f, c;
//	f = cboFaculty.getSelectedIndex();
//	c = lstCourses.getSelectedIndex();
//	if (theorySelected == true)
//		KB.theoryMatrix[f][c] = 2;
//	else
//		KB.sessionalMatrix[f][c] = 2;
//} 
//if(rdbtnSecondHalflast.isSelected())
//{
//	//System.out.println("Second half");
//	int f, c;
//	f = cboFaculty.getSelectedIndex();
//	c = lstCourses.getSelectedIndex();
//	if (theorySelected == true)
//		KB.theoryMatrix[f][c] = 3;
//	else
//		KB.sessionalMatrix[f][c] = 3;
//}
//
//} 
//
//private void initialize() {
//	
//	setBounds(100, 100, 683, 510);
//	
//	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
//	
//	getContentPane().setLayout(new BorderLayout());
//	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
//	getContentPane().add(contentPanel, BorderLayout.CENTER);
//	contentPanel.setLayout(new BorderLayout(0, 0));
//	{
//		panel = new JPanel();
//		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
//		contentPanel.add(panel, BorderLayout.NORTH);
//		panel.setLayout(new GridLayout(3, 1, 0, 0));
//		{
//			cboFaculty = new JComboBox();
//			cboFaculty.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					int index = cboFaculty.getSelectedIndex();
//					String name = cboFaculty.getSelectedItem().toString();
//					lblAssignedCourses.setText(index + " " + name);
//				}
//			});
//			panel.add(cboFaculty);
//		}
//		{
//			panel_3 = new JPanel();
//			panel.add(panel_3);
//			panel_3.setLayout(new GridLayout(1, 3, 0, 0));
//			{
//				lblCreditHourCovered = new JLabel("Credit Hour Covered");
//				lblCreditHourCovered.setHorizontalAlignment(SwingConstants.CENTER);
//				lblCreditHourCovered.setFont(new Font("Tahoma", Font.BOLD, 11));
//				panel_3.add(lblCreditHourCovered);
//			}
//			{
//				lblTheory = new JLabel("Theory: 3.0");
//				panel_3.add(lblTheory);
//			}
//			{
//				lblSessional = new JLabel("Sessional: 1.5");
//				panel_3.add(lblSessional);
//			}
//		}
//		{
//			lblAssignedCourses = new JLabel("HUM271 A, HUM272 B, CSE106");
//			panel.add(lblAssignedCourses);
//		}
//		panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{panel_3, lblCreditHourCovered, lblTheory, lblSessional, lblAssignedCourses}));
//	}
//	{
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		contentPanel.add(tabbedPane, BorderLayout.CENTER);
//		{
//			JPanel TheoryPanel = new JPanel();
//			tabbedPane.addTab("Course Assignment", null, TheoryPanel, null);
//			TheoryPanel.setLayout(new GridLayout(1, 2, 0, 0));
//			{
//				JPanel panel = new JPanel();
//				TheoryPanel.add(panel);
//				panel.setLayout(new BorderLayout(0, 0));
//				{
//					JPanel panel_1 = new JPanel();
//					panel.add(panel_1, BorderLayout.NORTH);
//									
//					panel_1.setLayout(new GridLayout(1, 2, 0, 0));
//					{
//						rdbtnTheory = new JRadioButton("Theory Course");
//						rdbtnTheory.addActionListener(this); 
//						rdbtnTheory.setSelected(true);
//						panel_1.add(rdbtnTheory);
//					}
//					{
//						rdbtnSessional = new JRadioButton("Sessional Course");
//						rdbtnSessional.addActionListener(this);
//						panel_1.add(rdbtnSessional);
//					}
//					ButtonGroup course_group = new ButtonGroup();
//					course_group.add(rdbtnTheory);
//					course_group.add(rdbtnSessional);
//					
//				}
//				
//				{
//					lstCourses = new JList(KB.lm_sessionalCodes);
//					lstCourses.addListSelectionListener(new ListSelectionListener() {
//						public void valueChanged(ListSelectionEvent arg0) {
//							int idx = lstCourses.getSelectedIndex();
//							if (theorySelected == true)
//							{
//								if (idx > -1)
//									lblAssignedCourses.setText(" " + idx + " " + KB.theoryCodes.get(idx));
//								//System.out.println("th");
//							}
//							else
//							{
//								if (idx > -1)
//									lblAssignedCourses.setText(" " + idx + " " + KB.sessionalCodes.get(idx));
//								//System.out.println("ss");
//							}
//								
//						}
//					});
//					lstCourses.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//					//lstCourses.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//					lstCourses.setVisibleRowCount(-1);
//					JScrollPane listScroller = new JScrollPane(lstCourses);
//					listScroller.setPreferredSize(new Dimension(250, 80));
//					panel.add(listScroller, BorderLayout.CENTER);
//				}
//			}
//			
//			{
//				JPanel panel_1 = new JPanel();
//				panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//				TheoryPanel.add(panel_1);
//				panel_1.setLayout(new GridLayout(0, 1, 10, 10));
//				{
//					JPanel panel_2 = new JPanel();
//					panel_1.add(panel_2);
//					panel_2.setLayout(new GridLayout(0, 1, 0, 0));
//					{
//						rdbtnNotAssigned = new JRadioButton("Not assigned");
//						rdbtnNotAssigned.addActionListener(this);
//						rdbtnNotAssigned.setSelected(true);
//						panel_2.add(rdbtnNotAssigned);
//					}
//					{
//						rdbtnFulltimeAssigned = new JRadioButton("Full-time assigned (14 wk)");
//						rdbtnFulltimeAssigned.addActionListener(this);
//						panel_2.add(rdbtnFulltimeAssigned);
//					}
//					{
//						rdbtnFirstHalffirst = new JRadioButton("First half (First 7 wk)");
//						rdbtnFirstHalffirst.addActionListener(this);
//						panel_2.add(rdbtnFirstHalffirst);
//					}
//					{
//						rdbtnSecondHalflast = new JRadioButton("Second half (Last 7 wk)");
//						rdbtnSecondHalflast.addActionListener(this);
//						panel_2.add(rdbtnSecondHalflast);
//					}
//				}
//				
//				ButtonGroup assignment_group = new ButtonGroup();
//				assignment_group.add(rdbtnNotAssigned);
//				assignment_group.add(rdbtnFulltimeAssigned);
//				assignment_group.add(rdbtnFirstHalffirst);
//				assignment_group.add(rdbtnSecondHalflast);
//				{
//					JPanel panel_2 = new JPanel();
//					panel_1.add(panel_2);
//					panel_2.setLayout(new BorderLayout(0, 0));
//					{
//						JLabel lblInstructors = new JLabel("CSE304A instructors:\r");
//						panel_2.add(lblInstructors, BorderLayout.NORTH);
//					}
//					{
//						lstFaculty = new JList();
//						panel_2.add(lstFaculty, BorderLayout.CENTER);
//					}
//				}
//			}
//		}
//	}
//	{
//		JPanel buttonPane = new JPanel();
//		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
//		getContentPane().add(buttonPane, BorderLayout.SOUTH);
//		{
//			JButton cancelButton = new JButton("Close");
//			cancelButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//				}
//			});
//			cancelButton.setActionCommand("Cancel");
//			buttonPane.add(cancelButton);
//		}
//	}
//}
//
//
//	private void eventHandler()
//	{
//		
//	}
//	
//	void updateCourseList(boolean th)
//	{
//		if (th)
//		{
//			lstCourses.setModel(KB.lm_theoryCodes);
//			theorySelected = true;
//		}
//		else
//		{
//			lstCourses.setModel(KB.lm_sessionalCodes);
//			theorySelected = false;
//		}
//		lstCourses.setSelectedIndex(0);
//		//theorySelected = th;
//		//System.out.println(theorySelected + "*");
//	}
//	void updateFacultyCombo()
//	{
//		cboFaculty.removeAllItems();
//		for (String s : KB.facultyNames)
//		{
//			cboFaculty.addItem(s);
//		}
//	}
//	public Modify(Window arg0, String arg1, ModalityType arg2) {
//		super(arg0, arg1, arg2);
//		
//		SetNativeLook();
//		initialize();
//		eventHandler();
//		
//		updateFacultyCombo();
//		updateCourseList(true);
//		try {
//			//Modify dialog = new Modify();
//			
//			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			this.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		// TODO Auto-generated constructor stub
//	}
//	
//
//}
