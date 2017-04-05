package examen;

import java.time.Duration;

interface IncomingCallFixtureBuilder {
	static int minCallTime = 500;
	static int maxCallTime = 1000;
	
	public class IncomingCallFixture {
		private int callNumber = 1;
		private long totalDuration = 0;

		public IncomingCall any() {
			long duration = (long) (minCallTime + (Math.random() * (maxCallTime - minCallTime)));
			return of(duration);
		}
		
		public IncomingCall of(long duration) {
			totalDuration += duration;
			return new IncomingCall(callNumber++, Duration.ofMillis(duration));
		}
		
		public long getTotalDuration() {
			return totalDuration;
		}
	}
	
	static IncomingCallFixture begin() {
		return new IncomingCallFixture();
	}
}
