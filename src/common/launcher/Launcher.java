package common.launcher;

import com.p3k.magictale.game.AbstractGame;

import javax.swing.*;
import java.awt.*;

/**
 * Класс, предназначенный для запуска игры (выбор клиент/сервер,
 * установки некоторых глобальных параметров)
 */
public class Launcher extends JFrame {
    private final JTabbedPane tabbedPane;
    private final JPanel serverPanel;
    private final ClientPanel clientPanel;
    private final JButton launchButton;
    private AbstractGame abstractGame;

    private Launcher() {
        super("MagicTaleOnline launcher");

        tabbedPane = new JTabbedPane();
        serverPanel = new JPanel();
        clientPanel = new ClientPanel();
        launchButton = new JButton("Start");

        makeGUI();

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Launcher();
    }

    private void makeGUI() {
        Container container = getContentPane();
        BorderLayout borderLayout = new BorderLayout();
        container.setLayout(borderLayout);

        tabbedPane.addTab("Client", clientPanel);
        tabbedPane.addTab("Server", serverPanel);

        container.add(tabbedPane, BorderLayout.CENTER);
        container.add(launchButton, BorderLayout.SOUTH);

        JFrame thisFrame = this;
        launchButton.addActionListener(e -> {
            switch (tabbedPane.getSelectedIndex()) {
                case 0:
                    abstractGame = AbstractGame.getClientInstance();
                    break;
                case 1:
                    abstractGame = AbstractGame.getServerInstance();
                    break;
                default:
                    System.err.println("Something went wrong with choosed tab");
                    break;
            }
            thisFrame.setVisible(false);
            abstractGame.mainLoop();
        });

        setMinimumSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
    }
}
