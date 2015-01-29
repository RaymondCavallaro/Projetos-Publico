package socket;

public class RunOver extends LoggedObjectBase implements Runnable {

	protected Thread thread;
	protected Runnable runnable;

	public RunOver() {
	}

	public RunOver(Runnable run) {
		this();
		runnable = run;
		start();
	}

	public boolean getRunOverCondition() {
		return true;
	}

	public void startAndWait() {
		try {
			Thread loop = start();
			loop.join();
		} catch (InterruptedException e) {
			logger.log(level, "InterruptedException on RunOver.startAndWait", e);
		}
	}

	public Thread start() {
		Thread loop = new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					try {
						loop();
					} catch (InterruptedException e) {
						break;
					}
				} while (getRunOverCondition());
			}
		});
		loop.start();
		return loop;
	}

	protected void loop() throws InterruptedException {
		Thread t = new Thread(this);
		thread = t;
		t.start();
		t.join();
	}

	@Override
	public void run() {
		runnable.run();
	}

}