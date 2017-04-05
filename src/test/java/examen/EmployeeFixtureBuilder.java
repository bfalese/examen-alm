package examen;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeFixtureBuilder {

	public class EmployeeFixture {
		private int empNumber = 1;
		private List<Employee> emps = new ArrayList<>();
		
		public EmployeeFixture add(EmployeeType type) {
			emps.add(new Employee(empNumber++, type));
			return this;
		}
		
		public List<Employee> build() {
			return emps;
		}
	}
	
	static EmployeeFixture begin() {
		return new EmployeeFixture();
	}
}
