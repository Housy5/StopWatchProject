import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class EventListener implements MouseListener, Transmitter {

    private Set<Receiver> receivers;

    public EventListener() {
        receivers = new HashSet<>();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            var comp = e.getComponent();
            if (comp instanceof JButton btn) {
                if (btn.getText().equals("START") || btn.getText().equals("RESUME")) {
                    transmit("start");
                } else if (btn.getText().equals("STOP")) {
                    transmit("stop");
                } else if (btn.getText().equals("RESET")) {
                    transmit("reset");
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void transmit(Object obj) {
        receivers.forEach(x -> x.receive(obj));
    }

    @Override
    public void sub(Receiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void unsub(Receiver receiver) {
        receivers.remove(receiver);
    }
}
