package examen;

import java.time.Duration;

/**
 * A call to be answered.
 * 
 * @author bernardo
 */
public class IncomingCall implements WithLogger {
	private int id;
	private Duration duration;
	private boolean answered;
	private String answeredBy;

	public IncomingCall(int id, Duration duration) {
		this.id = id;
		this.duration = duration;
		this.answered = false;
	}

	public Duration getDuration() {
		return duration;
	}

	public boolean isAnswered() {
		return answered;
	}

	/**
	 * Set this call as answered and stores the answering employee's id.
	 * @param employee Answering employee's id.
	 */
	public void markAnswered(String employee) {
		this.answered = true;
		this.answeredBy = employee;
		logger().info("IncomingCall#{} answered {} by {}", id, answered, employee);
	}

	public String getAnsweredBy() {
		return answeredBy;
	}

	@Override
	public String toString() {
		return "IncomingCall#" + id + " (" + duration.toMillis() + " ms)";
	}
}
