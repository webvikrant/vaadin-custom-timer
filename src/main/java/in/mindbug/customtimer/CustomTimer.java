package in.mindbug.customtimer;

import com.vaadin.ui.Button;

import in.mindbug.customtimer.client.CustomTimerClientRpc;
import in.mindbug.customtimer.client.CustomTimerServerRpc;
import in.mindbug.customtimer.client.CustomTimerState;

// This is the server-side UI component that provides public API 
// for CustomTimer
public class CustomTimer extends com.vaadin.ui.AbstractComponent implements CustomTimerServerRpc {

	private static final long serialVersionUID = 1L;

	private final Button timeoutButton;
	private final Button alertButton;

	public CustomTimer(boolean countdown, int startsAt, int warnsAt, int stopsAt, int alertInterval,
			String backGroundColor, String fontColor, String warningColor, String fontSize, Button timeoutButtonutton,
			Button alertButton) {

		this.timeoutButton = timeoutButtonutton;
		this.alertButton = alertButton;

		// To receive events from the client, we register ServerRpc
		CustomTimerServerRpc rpc = this;
		registerRpc(rpc);

		getState().countdown = countdown;
		getState().startsAt = startsAt;
		getState().warnsAt = warnsAt;
		getState().stopsAt = stopsAt;
		getState().alertInterval = alertInterval;

		// set state
		if (backGroundColor != null && !backGroundColor.isEmpty()) {
			getState().backgroundColor = backGroundColor;
		}

		if (fontColor != null && !fontColor.isEmpty()) {
			getState().fontColor = fontColor;
		}

		if (fontSize != null && !fontSize.isEmpty()) {
			getState().fontSize = fontSize;
		}
	}

	// We must override getState() to cast the state to CustomTimerState
	@Override
	protected CustomTimerState getState() {
		return (CustomTimerState) super.getState();
	}

	public void start() {
		getRpcProxy(CustomTimerClientRpc.class).start();
	}

	public void stop() {
		getRpcProxy(CustomTimerClientRpc.class).stop();
	}

	@Override
	public void timeout() {
//		Notification.show("Timeout");
		if (timeoutButton != null) {
			timeoutButton.click();
		}
	}

	@Override
	public void alert(long secondsElapsed) {
		if (alertButton != null) {
			alertButton.setData(secondsElapsed);
			alertButton.click();
		}
	}
}
