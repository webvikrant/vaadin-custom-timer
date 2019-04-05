package in.mindbug.customtimer.client;

import in.mindbug.customtimer.CustomTimer;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(CustomTimer.class)
public class CustomTimerConnector extends AbstractComponentConnector implements CustomTimerClientRpc {

	private static final long serialVersionUID = 1L;

	// ServerRpc is used to send events to server. Communication implementation
	// is automatically created here
	CustomTimerServerRpc rpc = RpcProxy.create(CustomTimerServerRpc.class, this);

	private final DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm:ss");
	private TimeZone timeZone = TimeZone.createTimeZone(0);

	private Date date;
	private long time = 0;
	private long timeLeft = 0;
	private com.google.gwt.user.client.Timer timer = null;

	private int startsAt = 0;
	private int warnsAt = 0;
	private int stopsAt = 0;
	private int alertInterval = 0;
	private boolean countdown = true;

	private String backgroundColor = "white";
	private String fontColor = "blue";
	private String warningColor = "red";
	private String fontWeight = "bold";
	private String fontSize = "20pt";
	private String html = null;
	private boolean visible = true;

	public CustomTimerConnector() {

		// To receive RPC events from server, we register ClientRpc implementation
		registerRpc(CustomTimerClientRpc.class, this);

		date = new Date();
		date.setTime(0);

		timer = new Timer() {
			@Override
			public void run() {
				time = time + 1000;
				timeLeft = timeLeft - 1000;
				showTime();
				// should the timer be cancelled now
				if (stopsAt > 0 && time >= stopsAt * 1000) {
					timer.cancel();
					rpc.timeout();
				}

				// alert
				if (alertInterval > 0) {
					if ((time / 1000) % alertInterval == 0) {
						rpc.alert(time / 1000);
					}
				}
			}
		};
	}

	private void showTime() {
		// TODO Auto-generated method stub
		if (countdown) {
			date.setTime(timeLeft);
		} else {
			date.setTime(time);
		}

		if ((warnsAt > 0 && time >= warnsAt * 1000)) {
			visible = !visible;// the blinking effect
			if (visible) {
				if (countdown) {
					date.setTime(timeLeft);
				} else {
					date.setTime(time);
				}
				html = generateHtml(warningColor);
				getWidget().setHtml(html);// warn color
			} else {
				html = generateHtml(fontColor);
				getWidget().setHtml(html);// normal color
			}
		} else {
			html = generateHtml(fontColor);
			getWidget().setHtml(html);
		}
	}

	private String generateHtml(String color) {
		String str = "<div align=\"center\" style=\"background-color:" + backgroundColor + ";\">"
				+ "<span style=\"color:" + color + "; font-weight:" + fontWeight + "; font-size:" + fontSize
				+ ";\">&nbsp;" + timeFormat.format(date, timeZone) + "&nbsp;</span></div>";
		return str;
	}

	// We must implement getWidget() to cast to correct type
	// (this will automatically create the correct widget type)
	@Override
	public CustomTimerWidget getWidget() {
		return (CustomTimerWidget) super.getWidget();
	}

	// We must implement getState() to cast to correct type
	@Override
	public CustomTimerState getState() {
		return (CustomTimerState) super.getState();
	}

	// Whenever the state changes in the server-side, this method is called
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		// State is directly readable in the client after it is set in server
		startsAt = getState().startsAt;
		warnsAt = getState().warnsAt;
		stopsAt = getState().stopsAt;
		alertInterval = getState().alertInterval;
		countdown = getState().countdown;

		backgroundColor = getState().backgroundColor;
		fontColor = getState().fontColor;
		warningColor = getState().warningColor;
		fontSize = getState().fontSize;

		time = startsAt * 1000;
		timeLeft = (stopsAt - startsAt) * 1000;

		showTime();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (timer != null && !timer.isRunning()) {
			timer.scheduleRepeating(1000);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		if (timer != null && timer.isRunning()) {
			timer.cancel();
		}
	}
}
