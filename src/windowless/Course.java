package windowless;

public class Course {
	public String code;
	public String title;
	public Double CrHr;
	public Double CtHr;
	public Boolean non_dept;
	public int level;
	
	public Course(String code, String title, Double crHr, Double ctHr,
			Boolean non_dept, int level) {
		super();
		this.code = code;
		this.title = title;
		CrHr = crHr;
		CtHr = ctHr;
		this.non_dept = non_dept;
		this.level = level;
	}
	
}
