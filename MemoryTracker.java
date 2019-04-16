package meraj;

/**
 * This thread class tracks the status of Free memory available.
 * 
 * @author mrasool
 *
 */
public class MemoryTracker extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Free memory left: " + Runtime.getRuntime().freeMemory() + " bytes.");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				System.out.println("Exiting..");
				break;
			}
		}
	}
}
