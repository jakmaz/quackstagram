package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingPanelUI extends BaseUI {

    public LoadingPanelUI() {
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        initializeUI();
    }

    public void initializeUI() {
        JPanel headerPanel = createHeaderPanel("Loading...");
        JPanel loadingPanel = createLoadingPanel();
        JPanel navigationPanel = createNavigationPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(loadingPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createLoadingPanel() {
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadingPanel.setBackground(Color.WHITE);

        // Create an icon from an animated GIF
        ImageIcon loadingIcon = new ImageIcon("img/storage/loading/loading.gif");
        JLabel loadingLabel = new JLabel(loadingIcon);

        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        return loadingPanel;
    }
}
