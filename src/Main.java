import UI.MainFrame;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        UIManager.put("Component.focusWidth", 0);
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#808080"));
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getInstance();
            frame.setVisible(true);
        });
    }
}
