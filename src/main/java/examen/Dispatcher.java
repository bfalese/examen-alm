package examen;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Incoming call dispatcher. Assigns calls to idle employees. All calls are
 * placed in a waiting queue before any employee can answer it. Employees take
 * call when they become idle prioritizing with {@link Employee#compareTo(Employee)}
 * 
 * @author bernardo
 */
public class Dispatcher implements WithLogger {
	private BlockingQueue<IncomingCall> callQueue;
	private PriorityBlockingQueue<Employee> availableEmployees;
	private ExecutorService executor;
	private boolean enabled;

	public Dispatcher(List<Employee> employees) {
	    this(new LinkedBlockingQueue<>(), new PriorityBlockingQueue<>(employees));
    }
	
	/**
	 * Create a dispatcher with the selected callQueue and availableEmployees implementation.
	 * @param callQueue Call waiting queue.
	 * @param availableEmployees Prioritized employee queue.
	 */
	public Dispatcher(BlockingQueue<IncomingCall> callQueue, PriorityBlockingQueue<Employee> availableEmployees) {
		this.callQueue = callQueue;
		this.availableEmployees = availableEmployees;
		this.enabled = true;
	}

	/**
	 * Puts all employees in place to start taking calls.
	 */
	public void init() {
		executor = Executors.newScheduledThreadPool(availableEmployees.size());
		availableEmployees.forEach(emp -> executor.submit(() -> readyEmployee()));
	}

	/**
	 * Tries to take an incoming call from queue, when it obtains one call tries to find an idle employee to asign as callTaker.
	 * After answering this employee returns to the queue and waits for another incoming call.
	 */
	private void readyEmployee() {
		while (enabled) {
			IncomingCall callToTake = null;
			Employee callTaker = null;
			try {
				callToTake = callQueue.take();
				callTaker = availableEmployees.take();
				callTaker.answerCall(callToTake);
				availableEmployees.add(callTaker);
			} catch (InterruptedException e) {
				logger().error("Error while taking calls for call " + Optional.ofNullable(callToTake) + " by "
						+ Optional.ofNullable(callTaker), e);
			}
		}
	}

	/**
	 * Add one call to the waiting queue to be taken by the first idle employee.
	 * 
	 * @param call
	 *            Incoming call to be answered.
	 */
	public void dispatchCall(IncomingCall call) {
		callQueue.add(call);
	}
	
	public PriorityBlockingQueue<Employee> getAvailableEmployees() {
        return availableEmployees;
    }
	
	public BlockingQueue<IncomingCall> getCallQueue() {
        return callQueue;
    }
	
	public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
