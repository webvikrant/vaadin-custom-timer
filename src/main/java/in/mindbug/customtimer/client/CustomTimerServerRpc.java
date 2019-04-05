package in.mindbug.customtimer.client;

import com.vaadin.shared.communication.ServerRpc;

// ServerRpc is used to pass events from client to server
public interface CustomTimerServerRpc extends ServerRpc {

    // Example API: Widget click is clicked
    public void timeout();
    public void alert(long secondsElapsed);
}
