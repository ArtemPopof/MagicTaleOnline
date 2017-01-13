package common.launcher;

import javax.swing.*;
import java.awt.*;

public class ClientPanel extends JPanel {
    private final JLabel clientNicknameLabel;
    private final JTextField clientNicknameField;
    private final JLabel ipLabel;
    private final JTextField ipField;

    ClientPanel() {
        clientNicknameLabel = new JLabel("Nickname");
        clientNicknameField = new JTextField(15);

        ipLabel = new JLabel("IP");
        ipField = new JTextField(15);

        makeGUI();
    }

    private void makeGUI() {
        GridLayout gridLayout = new GridLayout(2, 2);
        setLayout(gridLayout);

        clientNicknameField.setText("user");
        ipField.setText("127.0.0.1");

        add(clientNicknameLabel);
        add(clientNicknameField);
        add(ipLabel);
        add(ipField);
    }

    public String getNickname() {
        return clientNicknameField.getText();
    }

    public String getIP() {
        return ipField.getText();
    }
}
