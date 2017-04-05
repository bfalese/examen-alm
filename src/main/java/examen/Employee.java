package examen;

/**
 * Call answering employee.
 * 
 * @author bernardo
 */
public class Employee implements WithLogger, Comparable<Employee> {
	private int employeeNumber;
	private EmployeeType employeeType;
	private int answeredCalls;

	public Employee(int employeeNumber, EmployeeType employeeType) {
		this.employeeNumber = employeeNumber;
		this.employeeType = employeeType;
		this.answeredCalls = 0;
	}

	/**
	 * Takes an incoming call, adds one to his answered call count and marks this call was answered.
	 * @param call
	 * @throws InterruptedException
	 */
	public void answerCall(IncomingCall call) throws InterruptedException {
		logger().info(this.toString() + " answering " + call.toString());
		Thread.sleep(call.getDuration().toMillis());
		answeredCalls++;
		call.markAnswered(this.toString());
	}

	@Override
	public String toString() {
		return "Employee#" + employeeNumber + " (" + employeeType + ")";
	}

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public int getAnsweredCalls() {
		return answeredCalls;
	}

    @Override
    public int compareTo(Employee o) {
        return getEmployeeType().compareTo(o.getEmployeeType());
    }
}
