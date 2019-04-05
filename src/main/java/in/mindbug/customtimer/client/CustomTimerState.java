package in.mindbug.customtimer.client;

public class CustomTimerState extends com.vaadin.shared.AbstractComponentState {

	private static final long serialVersionUID = 1L;
	// State can have both public variable and bean properties

	// time in seconds
	public int startsAt = 0;
	public int warnsAt = 0;
	public int stopsAt = 0;
	public int alertInterval = 0;
	public boolean countdown;
	
	// style
	public String backgroundColor = "white";
	public String fontColor = "blue";
	public String warningColor = "red";
	public String fontSize = "18pt";
}