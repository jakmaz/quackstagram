import UI.MainFrame;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#1E1F22"));
        FlatMacLightLaf.setup();
        UIManager.put("Button.background", Color.decode("#e3e3e3"));
        UIManager.put("Component.focusWidth", 0);
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getInstance();
            frame.setVisible(true);
        });
    }
}
