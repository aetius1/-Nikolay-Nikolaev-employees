package nextProject;

/**
 * @author NN
 * 
 * Utility Bean to keep the file lines as single Objects for better processing
 *
 */
public class Item {
	
	
	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public int getEmployeId() {
		return employeId;
	}



	public void setEmployeId(int employeId) {
		this.employeId = employeId;
	}



	public int getProjectId() {
		return projectId;
	}



	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}



	String startDate;
	String endDate;
	int employeId;
	int projectId;
	
	
	
Item (String start,String end,int emplId,int pid) {
	
	
	this.startDate=start;
	this.endDate=end;
    this.employeId=emplId;
    this.projectId=pid;
	
}
	
	
	
	
	
	
	
}