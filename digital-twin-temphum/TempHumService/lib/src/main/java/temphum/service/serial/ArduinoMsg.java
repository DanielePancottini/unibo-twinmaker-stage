package temphum.service.serial;

public class ArduinoMsg {

	private int b;
	private int a;
	
	public ArduinoMsg(final int b, final int a) {
		super();
		this.b = b;
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(final int b) {
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(final int a) {
		this.a = a;
	}

	@Override
	public String toString() {
		return "ArduinoMsg [b=" + b + ", a=" + a + "]";
	}
	
	
}
