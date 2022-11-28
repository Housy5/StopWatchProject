import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class StopWatch implements Receiver {

    private  JButton stopBtn;
    private JButton startBtn;
    private JFrame frame;
    private JPanel content;
    private JLabel timeLbl;
    private Dimension size;

    private int hours, minutes, seconds, millis;
    private long startTime;
    private boolean started = false;
    private Timer timer;

    private EventListener listener = new EventListener();

    static {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public StopWatch() {
        int width = 250;
        size = new Dimension(width, width * (16 / 9));
        content = new JPanel();
        content.setMinimumSize(size);
        content.setMaximumSize(size);
        content.setPreferredSize(size);
        content.setLayout(new BorderLayout());

        frame = new JFrame("Stop Watch");
        frame.setContentPane(content);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout());
        startBtn = new JButton("START");
        startBtn.addMouseListener(listener);
        stopBtn = new JButton("STOP");
        stopBtn.addMouseListener(listener);
        btnPanel.add(startBtn); btnPanel.add(stopBtn);

        JPanel display = new JPanel();
        timeLbl = new JLabel("00:00:00:000");
        timeLbl.setFont(new Font("Segeo UI", Font.BOLD, 24));
        timeLbl.setVerticalAlignment(JLabel.CENTER);
        timeLbl.setHorizontalAlignment(JLabel.CENTER);
        display.setLayout(new CardLayout());
        display.add(timeLbl);

        content.add(display, BorderLayout.CENTER);
        content.add(btnPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        listener.sub(this);
    }

    private void tick() {
        millis += System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        if (millis >= 1_000) {
            millis -= 1_000;
            if (++seconds == 60) {
                seconds = 0;
                if (++minutes == 60) {
                    minutes = 0;
                    hours++;
                }
            }
        }
        var text = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, millis);
        timeLbl.setText(text);
    }

    private void reset() {
        millis = seconds = minutes = hours = 0;
        timeLbl.setText("00:00:00:000");
    }

    public void run() {
        while (started) {
            try {
                tick();
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void receive(Object obj) {
        if (obj instanceof String str) {
            if (str.equals("start") && !started) {
                started = true;
                startTime = startTime == 0 ? System.currentTimeMillis() : startTime;
                new Thread(() -> {
                    run();
                }).start();
                stopBtn.setText("STOP");
            } else if (str.equals("stop") && started) {
                started = false;
                startBtn.setText("RESUME");
                stopBtn.setText("RESET");
            } else if (str.equals("reset") && !started) {
                reset();
                stopBtn.setText("STOP");
                startBtn.setText("START");
            }
         }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        var stopWatch = new StopWatch();
        EventQueue.invokeLater(() -> stopWatch.show());
    }
}
