package examen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import examen.EmployeeFixtureBuilder.EmployeeFixture;
import examen.IncomingCallFixtureBuilder.IncomingCallFixture;

/**
 * {@link Dispatcher}
 * 
 * @author bernardo
 */
public class DispatcherTest {

    @Test
    public void oneOperatorShouldAnswerOneCall() throws InterruptedException {
        List<Employee> employees = EmployeeFixtureBuilder.begin().add(EmployeeType.Operator).build();

        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.init();

        IncomingCallFixture callFixture = IncomingCallFixtureBuilder.begin();
        IncomingCall call = callFixture.any();

        dispatcher.dispatchCall(call);

        Thread.sleep(callFixture.getTotalDuration() + IncomingCallFixtureBuilder.maxCallTime);
        Assert.assertTrue(call.isAnswered());
        Assert.assertEquals(1, employees.get(0).getAnsweredCalls());
    }

    @Test
    public void oneOpeartorShouldAnswerTwoCalls() throws InterruptedException {
        List<Employee> employees = EmployeeFixtureBuilder.begin().add(EmployeeType.Operator).build();

        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.init();

        IncomingCallFixture callFixture = IncomingCallFixtureBuilder.begin();
        IncomingCall call = callFixture.any();
        IncomingCall call2 = callFixture.any();

        dispatcher.dispatchCall(call);
        dispatcher.dispatchCall(call2);

        Thread.sleep(callFixture.getTotalDuration() + IncomingCallFixtureBuilder.maxCallTime);
        Assert.assertTrue(call.isAnswered());
        Assert.assertTrue(call2.isAnswered());
        Assert.assertEquals(2, employees.get(0).getAnsweredCalls());
    }

    @Test
    public void tenOperatorsShouldAnswerTenConcurrentCalls() throws InterruptedException {
        long duration = 100;
        EmployeeFixture employeeFixture = EmployeeFixtureBuilder.begin();
        IntStream.rangeClosed(1, 10).forEach(i -> employeeFixture.add(EmployeeType.Operator));
        List<Employee> employees = employeeFixture.build();

        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.init();

        IncomingCallFixture callFixture = IncomingCallFixtureBuilder.begin();
        List<IncomingCall> calls = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> calls.add(callFixture.of(duration)));

        calls.parallelStream().forEach(c -> dispatcher.dispatchCall(c));

        Thread.sleep(duration + IncomingCallFixtureBuilder.maxCallTime);

        calls.forEach(call -> Assert.assertTrue(call.isAnswered()));
        employees.forEach(employee -> Assert.assertEquals(1, employee.getAnsweredCalls()));
    }

    @Test
    public void sevenEmployeesShouldAnswerSixConcurrentCalls() throws InterruptedException {
        long duration = 100;

        EmployeeFixture employeeFixture = EmployeeFixtureBuilder.begin();
        employeeFixture.add(EmployeeType.Director);
        IntStream.rangeClosed(1, 5).forEach(i -> employeeFixture.add(EmployeeType.Operator));
        employeeFixture.add(EmployeeType.Supervisor);
        List<Employee> employees = employeeFixture.build();

        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.init();

        IncomingCallFixture callFixture = IncomingCallFixtureBuilder.begin();
        List<IncomingCall> calls = new ArrayList<>();
        IntStream.rangeClosed(1, 6).forEach(i -> calls.add(callFixture.of(duration)));

        calls.parallelStream().forEach(c -> dispatcher.dispatchCall(c));

        Thread.sleep(duration + IncomingCallFixtureBuilder.maxCallTime);

        calls.forEach(call -> Assert.assertTrue(call.isAnswered()));
        employees.stream().filter(e -> e.getEmployeeType() == EmployeeType.Director).forEach(e -> Assert.assertEquals(0, e.getAnsweredCalls()));
        employees.stream().filter(e -> e.getEmployeeType() != EmployeeType.Director).forEach(e -> Assert.assertEquals(1, e.getAnsweredCalls()));
    }

    @Test
    public void callShouldRemainInQueueIfNoEmployeeIsAvailable() throws InterruptedException {
        List<Employee> employees = Collections.emptyList();

        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.init();

        IncomingCallFixture callFixture = IncomingCallFixtureBuilder.begin();
        IncomingCall call = callFixture.any();

        dispatcher.dispatchCall(call);

        Thread.sleep(callFixture.getTotalDuration() + IncomingCallFixtureBuilder.maxCallTime);
        Assert.assertFalse(call.isAnswered());
        Assert.assertEquals(1, dispatcher.getCallQueue().size());
    }
}
