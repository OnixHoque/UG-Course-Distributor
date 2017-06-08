package windowless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;


//The knowledgeBase
public class KB {
	public static ArrayList<Faculty> facultyList;
	public static ArrayList<Theory> theoryList;
	public static ArrayList<Sessional> sessionalList;
	
	//redundant
	public static ArrayList<String> facultyNames;
	public static ArrayList<String> theoryCodes;
	public static ArrayList<String> sessionalCodes;
	
	public static DefaultListModel lm_theoryCodes, lm_sessionalCodes, assigned_teachers;
	
	
	public static int facultyCount, theoryCount, sessionalCount;
	
	//Most important 2 2d arrays
	public static int [][] theoryMatrix;
	public static int [][] sessionalMatrix;
	
	public static double getSessionalTotalContact(int sessionalId)
	{
		double ret = 0.0;
		double ctHr = sessionalList.get(sessionalId).CtHr;
		
		for (int i = 0; i<facultyCount; i++)
		{
			int val = sessionalMatrix[i][sessionalId];
			if (val != 0)
			{	
				double temp = ctHr;
				if (val != 1) temp = temp / 2.0;
				ret += temp;
			}
		}
		return ret;
	}
	/**
	 * get total contact hour of a faculty
	 * @param facultyId
	 * @param th
	 * @return
	 */
	public static double getContactHr(int facultyId, boolean th)
	{
		double ret = 0.0;
		if (th)
		{
			for (int i = 0; i<theoryCount; i++)
			{
				int val = theoryMatrix[facultyId][i];
				if (val != 0)
				{
					//String code = theoryCodes.get(i);
					double ctHr = theoryList.get(i).CtHr;
					if (val != 1) ctHr = ctHr / 2.0;
					ret += ctHr;
				}
			}
		}
		else
		{
			for (int i = 0; i<sessionalCount; i++)
			{
				int val = sessionalMatrix[facultyId][i];
				if (val != 0)
				{
					//String code = sessionalCodes.get(i);
					double ctHr = sessionalList.get(i).CtHr;
					if (val != 1) ctHr = ctHr / 2.0;
					ret += ctHr;
				}
			}
		}
		return ret;
	}
	/**
	 * get total contact of either theory/sessional
	 * @param facultyId
	 * @param th
	 * @return
	 */
	public static double getTotalContactHr(boolean th)
	{
		double ret = 0.0;
		if (th)
		{
			for (int i = 0; i<theoryCount; i++)
			{
				ret += theoryList.get(i).CtHr;
				//System.out.println(ret);
			}
		}
		else
		{
			for (int i = 0; i<sessionalCount; i++)
			{
				ret += sessionalList.get(i).CtHr * sessionalList.get(i).capacity;
			}
		}
		
		return ret;
	}
	
	/**
	 * get total contact hour of a faculty type (perm/att/...)
	 * @param facultyId
	 * @param th
	 * @return
	 */
	public static double getContactHrCovered(String type, boolean th)
	{
		double ret = 0.0;
		for (int i = 0; i< facultyCount; i++)
		{
			if (facultyList.get(i).category.equals(type))
				ret += getContactHr(i, th);
		}
		return ret;
	}
	
	public static ArrayList<String> getAssignedTeachers(int courseId, boolean th)
	{
		ArrayList<String> ret = new ArrayList<>();
		if (th)
		{
			for (int i = 0; i<facultyCount; i++)
			{
				int val = theoryMatrix[i][courseId];
				if (val != 0)
				{
					String name = facultyNames.get(i);
					if (val == 2)
						name += " (1st half)";
					if (val == 3)
						name += " (2nd half)";
					ret.add(name);
				}
			}
		}
		else
		{
			for (int i = 0; i<facultyCount; i++)
			{
				int val = sessionalMatrix[i][courseId];
				if (val != 0)
				{
					String name = facultyNames.get(i);
					if (val == 2)
						name += " (1st half)";
					if (val == 3)
						name += " (2nd half)";
					ret.add(name);
				}
			}
		}
			return ret;
		
	}
	
	public static String getFacultiesCourses(int facultyId, boolean th)
	{
		String ret = new String("");
		if (th)
		{
			for (int i = 0; i<theoryCount; i++)
			{
				int val = theoryMatrix[facultyId][i];
				if (val != 0)
				{
					String code = theoryCodes.get(i);
					double ctHr = theoryList.get(i).CtHr;
					if (val != 1) ctHr = ctHr / 2.0;
					ret += code + " (" + String.valueOf(ctHr) + ") ";
				}
			}
		}
		else
		{
			for (int i = 0; i<sessionalCount; i++)
			{
				int val = sessionalMatrix[facultyId][i];
				if (val != 0)
				{
					String code = sessionalCodes.get(i);
					double ctHr = sessionalList.get(i).CtHr;
					if (val != 1) ctHr = ctHr / 2.0;
					ret += code + " (" + String.valueOf(ctHr) + ") ";
				}
			}
		}
			return ret;
	}
	public static void saveState()
	{
		try{
		    PrintWriter writer = new PrintWriter("db/state.config", "UTF-8");	//, "UTF-8"
		    
		    //write the theory assignment
		    for (int i = 0; i<facultyCount; i++)
		    	for (int j = 0; j<theoryCount; j++)
		    	{
		    		writer.println(theoryMatrix[i][j]);
		    	}
		    
		    //write the sessional assignment
		    for (int i = 0; i<facultyCount; i++)
		    	for (int j = 0; j<sessionalCount; j++)
		    	{
		    		writer.println(sessionalMatrix[i][j]);
		    	}
		    
		    
		    writer.close();
		    //System.out.println("config saved");
		} catch (IOException e) {
		   // do something
		}
	}
	
	public static void backupState()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date date = new Date();
		String fname = dateFormat.format(date) + "state.config.bak"; //2016/11/16 12:08:43
		File in = new File("db/state.config");
		File out = new File("db/" + fname);
		try {
			Files.copy(in.toPath(), out.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Warning, could not write backup" + e.getMessage());
		}
		
	}
	
	public static int loadState()
	{
		File f = new File("db/state.config");
		if (f.exists() && !f.isDirectory())
		{
			ArrayList<String> lines = getLinesFromFile("db/state.config");
			//System.out.println(lines.size());
			int k = 0;
			while (k<lines.size())
			{
				try
				{
					//read theory
					for (int i = 0; i<facultyCount; i++)
				    	for (int j = 0; j<theoryCount; j++)
				    	{
				    		String line = lines.get(k);
				    		theoryMatrix[i][j] = Integer.valueOf(line);
				    		k++;
				    	}
					//read sessional
					for (int i = 0; i<facultyCount; i++)
				    	for (int j = 0; j<sessionalCount; j++)
				    	{
				    		String line = lines.get(k);
				    		sessionalMatrix[i][j] = Integer.valueOf(line);
				    		k++;
				    	}
					break;
				}
				catch (IndexOutOfBoundsException e)
				{
					
					System.out.println("mismatch in config (Error code -2: Out of bound) " + e.toString());
					return -2;
				}
			}
			if (k != lines.size())
			{
				System.out.println("mismatch in config (Error code -3: Run out of variable)");
				return -3;
			}
			else
				return 0;
		}
		else
			return -1;
		
	}
	
	public static void readFromFile()
	{
		readFaculty();
		readTheory();
		readSessional();
		
		facultyCount = facultyList.size();
		theoryCount = theoryList.size();
		sessionalCount = sessionalList.size();
		
		theoryMatrix = new int[facultyCount][theoryCount];
		sessionalMatrix = new int[facultyCount][sessionalCount];
		//System.out.println("value " + theoryMatrix[2][2] + " " + sessionalMatrix[2][2]);
		
		//for (int i = 0; i< facultyCount; i++)
		//	theoryMatrix[i] = new int[theoryCount];
	}
	
	private static ArrayList<String> getLinesFromFile(String filename)
	{
		ArrayList<String> ret = new ArrayList<>();
		
		try {

            //File f = new File("db/Theory.csv");
			File f = new File(filename);
            FileInputStream fis = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            if (filename.equals("db/Sessional.csv") || filename.equals("db/Theory.csv") || filename.equals("db/Faculty.csv"))
            	line = br.readLine();	//remove the first line (Header)
        	while ((line = br.readLine()) != null) {
        		
        		//System.out.println(line);
        		ret.add(line);
        		
        	}
         
        	br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return ret;
	}
	
	private static void readSessional()
	{
		ArrayList<String> lines = getLinesFromFile("db/Sessional.csv");
		sessionalList = new ArrayList<>();
		sessionalCodes = new ArrayList<>();
		lm_sessionalCodes = new DefaultListModel<>();
		for (String line : lines)
		{
			String [] info = line.split(",");
    		Sessional ss;
    		//System.out.println(info[2]);
    		if (info[4].equals("1"))
    		{
    			ss = new Sessional(info[0], info[1], Double.valueOf(info[2]), Double.valueOf(info[3]), true, Integer.valueOf(info[5]),Integer.valueOf(info[6]));
    			//lm_sessionalCodes.addElement(info[0] + " - " + info[2] + " CtHr (Non-dept)");
    			if (ss.CrHr != 1.5)
    				lm_sessionalCodes.addElement(info[0] + " (" + info[2] + " CreditHr, Non-dept)");
    			else
    				lm_sessionalCodes.addElement(info[0] + " (Non-dept)");
    			//System.out.println("Mil");
    		}
    		else
    		{
    			ss = new Sessional(info[0], info[1], Double.valueOf(info[2]), Double.valueOf(info[3]), false, Integer.valueOf(info[5]),Integer.valueOf(info[6]));
    			//lm_sessionalCodes.addElement(info[0] + " - " + info[2] + " CtHr");
    			if (ss.CrHr != 1.5)
    				lm_sessionalCodes.addElement(info[0] + " (" + info[2] + " CreditHr)");
    			else
    				lm_sessionalCodes.addElement(info[0]); 
    			//System.out.println("Civ");
    		}
    		sessionalList.add(ss);
    		sessionalCodes.add(info[0]);
    		
		}
		
	}
	
	private static void readTheory()
	{
		ArrayList<String> lines = getLinesFromFile("db/Theory.csv");
		theoryList = new ArrayList<>();
    	theoryCodes = new ArrayList<>();
    	lm_theoryCodes = new DefaultListModel<>();
		for (String line : lines)
		{
			String [] info = line.split(",");
    		Theory th;
    		//System.out.println(info[2]);
    		if (info[4].equals("1"))
    		{
    			th = new Theory(info[0], info[1], Double.valueOf(info[2]), Double.valueOf(info[3]), true, Integer.valueOf(info[5]));
    			if (th.CrHr != 3.0)
    				lm_theoryCodes.addElement(info[0] + " (" + info[2] + " CreditHr, Non-dept)");
    			else
    				lm_theoryCodes.addElement(info[0] + " (Non-dept)");
    			//System.out.println("Mil");
    		}
    		else
    		{
    			th = new Theory(info[0], info[1], Double.valueOf(info[2]), Double.valueOf(info[3]), false, Integer.valueOf(info[5]));
    			if (th.CrHr != 3.0)
    				lm_theoryCodes.addElement(info[0] + " (" + info[2] + " CreditHr)");
    			else
    				lm_theoryCodes.addElement(info[0]); 
    			//System.out.println("Civ");
    		}
    		theoryList.add(th);
    		theoryCodes.add(info[0]);
    		//lm_theoryCodes.addElement(info[0]);
    		//System.out.println(info[0]);
		}
		
	}
	private static void readFaculty()
	{
		ArrayList<String> lines = getLinesFromFile("db/Faculty.csv");
		facultyList = new ArrayList<>();
		facultyNames = new ArrayList<>();
		
		
		for (String line : lines)
		{
			String [] info = line.split(",");
			Faculty fc;
    		
			if (info[2].equals("1"))
    		{
    			fc = new Faculty(info[0], info[1], true);
    			//System.out.println("Mil");
    		}
    		else
    		{
    			fc = new Faculty(info[0], info[1], false);
    			//System.out.println("Civ");
    		}
    		facultyList.add(fc);
    		facultyNames.add(info[0]);
    		
		}
	}
}
