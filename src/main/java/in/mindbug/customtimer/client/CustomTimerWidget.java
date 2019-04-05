package in.mindbug.customtimer.client;

import com.google.gwt.user.client.ui.HTML;

// Extend any GWT Widget
public class CustomTimerWidget extends HTML {

	public CustomTimerWidget() {

		// CSS class-name should not be v- prefixed
		setStyleName("customtimer");

		// State is set to widget in CustomTimerConnector
	}

	public void setHtml(String html) {
		setHTML(html);
	}
}