/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.betterui;

/**
 *
 * @author gmlol
 */
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GameVaultUI extends JFrame {

    private VaultManager manager;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtTitle, txtGenre, txtExtra, txtSearch;

    private JComboBox<String> comboTracker;
    private JComboBox<String> comboChapter;
    private JComboBox<String> comboLevel;

    private JCheckBox chkStory;

    private JLabel lblImageName;

    private String currentImagePath = "none";

    // MODERN COLORS
    private final Color BACKGROUND = new Color(24, 26, 32);
    private final Color PANEL = new Color(36, 38, 46);
    private final Color ACCENT = new Color(88, 101, 242);
    private final Color TEXT = new Color(230, 230, 230);
    private final Color TABLE_BG = new Color(30, 32, 40);

    private final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public GameVaultUI() {

        manager = new VaultManager();

        setupUI();
        setupLayout();
    }

    private void setupUI() {

        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("Button.arc", 20);

        setTitle("GameVault");
        setSize(1280, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);
    }

    private void setupLayout() {

        setLayout(new BorderLayout());

        // =========================
        // SEARCH BAR
        // =========================

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND);
        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 42));
        txtSearch.setFont(UI_FONT);

        txtSearch.putClientProperty(
                "JTextField.placeholderText",
                "Search games..."
        );

        topPanel.add(txtSearch, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        // =========================
        // TABLE
        // =========================

        String[] columns = {
                "Cover",
                "Title",
                "Genre",
                "Detail",
                "Status",
                "Story Progress"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Icon.class : String.class;
            }
        };

        table = new JTable(tableModel);

        table.setRowHeight(90);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 10));

        table.setBackground(TABLE_BG);
        table.setForeground(TEXT);

        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(Color.WHITE);

        table.setFont(UI_FONT);

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setBackground(PANEL);
        table.getTableHeader().setForeground(TEXT);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPane.getViewport().setBackground(TABLE_BG);

        refreshTable(manager.getGames().values());

        // =========================
        // SIDEBAR FORM
        // =========================

        JPanel sidebar = new JPanel(new GridBagLayout());

        sidebar.setPreferredSize(new Dimension(360, 0));

        sidebar.setBackground(PANEL);

        sidebar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(60, 60, 60)),
                        new EmptyBorder(20, 20, 20, 20)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TITLE

        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitle = createLabel("Title");
        sidebar.add(lblTitle, gbc);

        gbc.gridy++;

        txtTitle = createTextField();
        sidebar.add(txtTitle, gbc);

        // GENRE

        gbc.gridy++;

        sidebar.add(createLabel("Genre"), gbc);

        gbc.gridy++;

        txtGenre = createTextField();
        sidebar.add(txtGenre, gbc);

        // EXTRA

        gbc.gridy++;

        sidebar.add(createLabel("Extra Detail"), gbc);

        gbc.gridy++;

        txtExtra = createTextField();
        sidebar.add(txtExtra, gbc);

        // STATUS

        gbc.gridy++;

        sidebar.add(createLabel("Status"), gbc);

        gbc.gridy++;

        comboTracker = new JComboBox<>(
                new String[]{
                        "Still Playing",
                        "Finished",
                        "Dropped"
                }
        );

        comboTracker.setFont(UI_FONT);

        sidebar.add(comboTracker, gbc);

        // STORY GAME

        gbc.gridy++;

        chkStory = new JCheckBox("Story Game");

        chkStory.setFont(UI_FONT);
        chkStory.setBackground(PANEL);
        chkStory.setForeground(TEXT);

        sidebar.add(chkStory, gbc);

        // CHAPTERS

        String[] chapters = new String[102];

        chapters[0] = "Prologue";

        for (int i = 1; i <= 100; i++) {
            chapters[i] = "Chapter " + i;
        }

        chapters[101] = "Epilogue";

        comboChapter = new JComboBox<>(chapters);

        comboChapter.setFont(UI_FONT);

        gbc.gridy++;

        sidebar.add(comboChapter, gbc);

        // LEVELS

        String[] levels = new String[101];

        for (int i = 0; i <= 100; i++) {
            levels[i] = String.valueOf(i);
        }

        comboLevel = new JComboBox<>(levels);

        comboLevel.setFont(UI_FONT);

        gbc.gridy++;

        sidebar.add(comboLevel, gbc);

        comboChapter.setEnabled(false);
        comboLevel.setEnabled(false);

        chkStory.addActionListener(e -> {

            boolean selected = chkStory.isSelected();

            comboChapter.setEnabled(selected);
            comboLevel.setEnabled(selected);
        });

        // IMAGE

        gbc.gridy++;

        JButton btnImage = createModernButton(
                "Select Cover",
                new Color(52, 152, 219)
        );

        sidebar.add(btnImage, gbc);

        gbc.gridy++;

        lblImageName = new JLabel("No file chosen");

        lblImageName.setForeground(TEXT);

        sidebar.add(lblImageName, gbc);

        // =========================
        // ACTION BUTTONS
        // =========================

        gbc.gridy++;

        JButton btnAdd = createModernButton(
                "Insert",
                new Color(46, 204, 113)
        );

        sidebar.add(btnAdd, gbc);

        gbc.gridy++;

        JButton btnUpdate = createModernButton(
                "Update",
                new Color(241, 196, 15)
        );

        sidebar.add(btnUpdate, gbc);

        gbc.gridy++;

        JButton btnDelete = createModernButton(
                "Delete",
                new Color(231, 76, 60)
        );

        sidebar.add(btnDelete, gbc);

        gbc.gridy++;

        JButton btnOpen = createModernButton(
                "Open CSV",
                new Color(155, 89, 182)
        );

        sidebar.add(btnOpen, gbc);

        gbc.gridy++;

        JButton btnSaveAs = createModernButton(
                "Save As",
                new Color(26, 188, 156)
        );

        sidebar.add(btnSaveAs, gbc);

        // =========================
        // SPLIT PANE
        // =========================

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                sidebar,
                scrollPane
        );

        splitPane.setDividerLocation(360);

        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);

        // =========================
        // TABLE CLICK
        // =========================

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int row = table.getSelectedRow();

                if (row >= 0) {

                    txtTitle.setText(
                            tableModel.getValueAt(row, 1).toString()
                    );

                    txtGenre.setText(
                            tableModel.getValueAt(row, 2).toString()
                    );

                    txtExtra.setText(
                            tableModel.getValueAt(row, 3).toString()
                    );

                    comboTracker.setSelectedItem(
                            tableModel.getValueAt(row, 4).toString()
                    );

                    String storyInfo =
                            tableModel.getValueAt(row, 5).toString();

                    if (!storyInfo.equals("N/A")) {

                        chkStory.setSelected(true);

                        comboChapter.setEnabled(true);
                        comboLevel.setEnabled(true);

                    } else {

                        chkStory.setSelected(false);

                        comboChapter.setEnabled(false);
                        comboLevel.setEnabled(false);
                    }
                }
            }
        });

        // =========================
        // IMAGE BUTTON
        // =========================

        btnImage.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            if (fc.showOpenDialog(this)
                    == JFileChooser.APPROVE_OPTION) {

                currentImagePath =
                        fc.getSelectedFile().getAbsolutePath();

                lblImageName.setText(
                        fc.getSelectedFile().getName()
                );
            }
        });

        // =========================
        // INSERT
        // =========================

        btnAdd.addActionListener(e -> {

            Game g;

            if (chkStory.isSelected()) {

                g = new StoryGame(
                        txtTitle.getText(),
                        txtGenre.getText(),
                        txtExtra.getText(),
                        comboTracker.getSelectedItem().toString(),
                        currentImagePath,
                        comboChapter.getSelectedItem().toString(),
                        comboLevel.getSelectedItem().toString()
                );

            } else {

                g = new Game(
                        txtTitle.getText(),
                        txtGenre.getText(),
                        txtExtra.getText(),
                        comboTracker.getSelectedItem().toString(),
                        currentImagePath
                );
            }

            if (manager.insert(g)) {

                resetUI();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Game already exists!"
                );
            }
        });

        // =========================
        // UPDATE
        // =========================

        btnUpdate.addActionListener(e -> {

            if (manager.update(
                    txtTitle.getText(),
                    txtGenre.getText(),
                    txtExtra.getText(),
                    comboTracker.getSelectedItem().toString(),
                    currentImagePath,
                    chkStory.isSelected(),
                    comboChapter.getSelectedItem().toString(),
                    comboLevel.getSelectedItem().toString()
            )) {

                resetUI();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Game not found!"
                );
            }
        });

        // =========================
        // DELETE
        // =========================

        btnDelete.addActionListener(e -> {

            if (manager.delete(txtTitle.getText())) {

                resetUI();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Game not found!"
                );
            }
        });

        // =========================
        // OPEN CSV
        // =========================

        btnOpen.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(
                    new javax.swing.filechooser
                            .FileNameExtensionFilter(
                            "CSV Files",
                            "csv"
                    )
            );

            if (fc.showOpenDialog(this)
                    == JFileChooser.APPROVE_OPTION) {

                manager.setFileName(
                        fc.getSelectedFile().getAbsolutePath()
                );

                resetUI();
            }
        });

        // =========================
        // SAVE AS
        // =========================

        btnSaveAs.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(
                    new javax.swing.filechooser
                            .FileNameExtensionFilter(
                            "CSV Files",
                            "csv"
                    )
            );

            if (fc.showSaveDialog(this)
                    == JFileChooser.APPROVE_OPTION) {

                String path =
                        fc.getSelectedFile().getAbsolutePath();

                if (!path.endsWith(".csv")) {
                    path += ".csv";
                }

                manager.saveAsFile(path);

                JOptionPane.showMessageDialog(
                        this,
                        "Saved to: " + path
                );
            }
        });
    }

    // =========================
    // MODERN COMPONENTS
    // =========================

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(UI_FONT);

        field.setPreferredSize(
                new Dimension(200, 40)
        );

        return field;
    }

    private JLabel createLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        lbl.setForeground(TEXT);

        return lbl;
    }

    private JButton createModernButton(
            String text,
            Color color
    ) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setForeground(Color.WHITE);

        btn.setBackground(color);

        btn.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setPreferredSize(
                new Dimension(120, 42)
        );

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    // =========================
    // FILTER TABLE
    // =========================

    private void filterTable() {

        String query =
                txtSearch.getText().toLowerCase();

        List<Game> filtered =
                manager.getGames()
                        .values()
                        .stream()
                        .filter(g ->
                                g.getTitle()
                                        .toLowerCase()
                                        .contains(query)

                                        ||

                                        g.getTracker()
                                                .toLowerCase()
                                                .contains(query)
                        )
                        .collect(Collectors.toList());

        refreshTable(filtered);
    }

    // =========================
    // REFRESH TABLE
    // =========================

    private void refreshTable(
            Iterable<Game> gamesToDisplay
    ) {

        tableModel.setRowCount(0);

        for (Game g : gamesToDisplay) {

            ImageIcon icon = null;

            if (!g.getImagePath().equals("none")
                    &&
                    new File(g.getImagePath()).exists()) {

                Image img =
                        new ImageIcon(
                                g.getImagePath()
                        ).getImage();

                Image scaled =
                        img.getScaledInstance(
                                60,
                                80,
                                Image.SCALE_SMOOTH
                        );

                icon = new ImageIcon(scaled);
            }

            String storyDisplay = "N/A";

            if (g instanceof StoryGame) {

                StoryGame sg = (StoryGame) g;

                storyDisplay =
                        "Ch: "
                                + sg.getChapter()
                                + " | Lvl: "
                                + sg.getLevel();
            }

            tableModel.addRow(
                    new Object[]{
                            icon,
                            g.getTitle(),
                            g.getGenre(),
                            g.getExtraDetail(),
                            g.getTracker(),
                            storyDisplay
                    }
            );
        }
    }

    // =========================
    // RESET UI
    // =========================

    private void resetUI() {

        txtSearch.setText("");

        txtTitle.setText("");
        txtGenre.setText("");
        txtExtra.setText("");

        comboTracker.setSelectedIndex(0);

        chkStory.setSelected(false);

        comboChapter.setEnabled(false);
        comboLevel.setEnabled(false);

        currentImagePath = "none";

        lblImageName.setText("No file chosen");

        refreshTable(manager.getGames().values());
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        FlatMacDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {

            GameVaultUI ui = new GameVaultUI();

            ui.setVisible(true);
        });
    }
}