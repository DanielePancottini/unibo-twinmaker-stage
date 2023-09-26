package temphum.service.serial;

/**
 *
 */
public class SimpleSerialMonitor  {

	public static void main(String[] args) throws Exception {
		final String portName = "COM3";
		System.out.println("Start monitoring serial port "+portName+" at 9600 boud rate");
		try {
			CommChannel monitor = new SerialCommChannel(portName, 9600);
			while(true) {
				if(monitor.isMsgAvailable()) {
					String msg = monitor.receiveMsg();
					System.out.print(msg);
				} else {
					System.out.print("No msg available");
				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}