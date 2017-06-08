package windowless;

public class Sessional extends Course {
	public int capacity;  
	public Sessional(String code, String title, Double crHr, Double ctHr, Boolean non_dept, int level, int _capacity) 
	{
		super(code, title, crHr, ctHr, non_dept, level);
		capacity = _capacity;
	}

}
