/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.betterui;

/**
 *
 * @author gmlol
 */
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.GridLayout;

public class GameVaultUI extends JFrame {

    private VaultManager manager;

    private JTextField txtTitle, txtGenre, txtExtra, txtSearch;

    private JComboBox<String> comboTracker;
    private JComboBox<String> comboChapter;
    private JComboBox<String> comboLevel;

    private JCheckBox chkStory;

    private JLabel lblImageName;

    private JPanel gameGrid;

    private final String COVER_FOLDER = "covers";

    private String currentImagePath = "none";

    private final Color BACKGROUND = new Color(24, 26, 32);
    private final Color PANEL = new Color(36, 38, 46);
    private final Color TEXT = new Color(230, 230, 230);

    private final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public GameVaultUI() {

        manager = new VaultManager();

        setupUI();
        setupLayout();
    }

    private void setupUI() {

        setTitle("GameVault");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(BACKGROUND);

        UIManager.put("Component.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        UIManager.put("Button.arc", 20);
    }

    private void setupLayout() {

        setLayout(new BorderLayout());

        // ========================================
        // TOP SEARCH BAR
        // ========================================

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.setBackground(BACKGROUND);

        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        txtSearch = new JTextField();

        txtSearch.setPreferredSize(new Dimension(300, 40));

        txtSearch.setFont(UI_FONT);

        txtSearch.putClientProperty(
                "JTextField.placeholderText",
                "Search games..."
        );

        topPanel.add(txtSearch, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filterGames();
            }

            public void removeUpdate(DocumentEvent e) {
                filterGames();
            }

            public void changedUpdate(DocumentEvent e) {
                filterGames();
            }
        });

        // ========================================
        // GAME GRID
        // ========================================

        setupGameGrid();

        // ========================================
        // SIDEBAR FORM
        // ========================================

        JPanel sidebar = new JPanel(new GridBagLayout());

        sidebar.setPreferredSize(new Dimension(350, 0));

        sidebar.setBackground(PANEL);

        sidebar.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // TITLE

        gbc.gridy = 0;
        sidebar.add(createLabel("Title"), gbc);

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

        comboTracker = new JComboBox<>(new String[]{
                "Still Playing",
                "Finished",
                "Dropped"
        });

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

        // IMAGE BUTTON

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

        // ACTION BUTTONS

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

        add(sidebar, BorderLayout.WEST);

        // ========================================
        // BUTTON ACTIONS
        // ========================================

        // Create covers folder automatically
        new File(COVER_FOLDER).mkdirs();

        btnImage.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                try {

                    File selected = fc.getSelectedFile();

                    String fileName =
                            System.currentTimeMillis()
                                    + "_"
                                    + selected.getName();

                    Path destination = Path.of(
                            COVER_FOLDER,
                            fileName
                    );

                    Files.copy(
                            selected.toPath(),
                            destination,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    currentImagePath = destination.toString();

                    lblImageName.setText(selected.getName());

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to copy image!"
                    );
                }
            }
        });

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

                JOptionPane.showMessageDialog(this, "Game not found!");
            }
        });

        btnDelete.addActionListener(e -> {

            if (manager.delete(txtTitle.getText())) {

                resetUI();

            } else {

                JOptionPane.showMessageDialog(this, "Game not found!");
            }
        });

        btnOpen.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "CSV Files",
                            "csv"
                    )
            );

            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                manager.setFileName(
                        fc.getSelectedFile().getAbsolutePath()
                );

                resetUI();
            }
        });

        btnSaveAs.addActionListener(e -> {

            JFileChooser fc = new JFileChooser();

            fc.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter(
                            "CSV Files",
                            "csv"
                    )
            );

            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

                String path = fc.getSelectedFile().getAbsolutePath();

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

    // ========================================
    // GAME GRID
    // ========================================

    private void setupGameGrid() {

        gameGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        
        gameGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gameGrid.setLayout(new GridLayout(0, 3, 20, 20));

        gameGrid.setBackground(BACKGROUND);

        refreshGameGrid(manager.getGames().values());

        JScrollPane scroll = new JScrollPane(gameGrid);
        
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.setBorder(null);

        scroll.getViewport().setBackground(BACKGROUND);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createGameCard(Game g) {

        JPanel card = new JPanel(new BorderLayout());

        card.setPreferredSize(new Dimension(180, 280));

        card.setBackground(new Color(35, 37, 43));

        card.setBorder(
                BorderFactory.createLineBorder(
                        new Color(50, 50, 50)
                )
        );

        // =========================
        // TOP BAR
        // =========================

        JPanel topBar = new JPanel(new BorderLayout());

        topBar.setOpaque(false);

        JButton menuBtn = new JButton("⋮");

        menuBtn.setFocusPainted(false);

        menuBtn.setBorderPainted(false);

        menuBtn.setContentAreaFilled(false);

        menuBtn.setForeground(Color.WHITE);

        menuBtn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        JPopupMenu menu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");

        JMenuItem deleteItem = new JMenuItem("Delete");

        menu.add(editItem);
        menu.add(deleteItem);

        menuBtn.addActionListener(e ->
                menu.show(menuBtn, 0, menuBtn.getHeight())
        );

        topBar.add(menuBtn, BorderLayout.EAST);

        // =========================
        // IMAGE
        // =========================

        JLabel imgLabel = new JLabel();

        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        if (!g.getImagePath().equals("none")
                &&
                new File(g.getImagePath()).exists()) {

            ImageIcon icon = new ImageIcon(g.getImagePath());

            Image scaled =
                    icon.getImage().getScaledInstance(
                            180,
                            240,
                            Image.SCALE_SMOOTH
                    );

            imgLabel.setIcon(new ImageIcon(scaled));

        } else {

            imgLabel.setText("No Image");

            imgLabel.setForeground(Color.WHITE);
        }

        // =========================
        // TITLE
        // =========================

        JLabel title = new JLabel(
                g.getTitle(),
                JLabel.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        title.setBorder(
                new EmptyBorder(10, 5, 10, 5)
        );

        // =========================
        // CARD LAYOUT
        // =========================

        card.add(topBar, BorderLayout.NORTH);

        card.add(imgLabel, BorderLayout.CENTER);

        card.add(title, BorderLayout.SOUTH);

        // =========================
        // CLICK CARD → DETAILS
        // =========================

        card.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                showGameDetails(g);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(50, 53, 61));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(35, 37, 43));
            }
        });

    // ----------------------
   //EDIT FUNCTION ----------------------
  // ----------------------

        editItem.addActionListener(e -> {

            txtTitle.setText(g.getTitle());

            txtGenre.setText(g.getGenre());

            txtExtra.setText(g.getExtraDetail());

            comboTracker.setSelectedItem(
                    g.getTracker()
            );

            currentImagePath = g.getImagePath();

            if (g instanceof StoryGame) {

                StoryGame sg = (StoryGame) g;

                chkStory.setSelected(true);

                comboChapter.setEnabled(true);

                comboLevel.setEnabled(true);

                comboChapter.setSelectedItem(
                        sg.getChapter()
                );

                comboLevel.setSelectedItem(
                        sg.getLevel()
                );
            }
        });

    // ----------------------
   //DELETE ----------------------
  // ----------------------

        deleteItem.addActionListener(e -> {

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Delete " + g.getTitle() + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirm == JOptionPane.YES_OPTION) {

                manager.delete(g.getTitle());

                refreshGameGrid(
                        manager.getGames().values()
                );
            }
        });

        return card;
    }

    private void refreshGameGrid(Iterable<Game> games) {

        gameGrid.removeAll();

        for (Game g : games) {
            gameGrid.add(createGameCard(g));
        }

        gameGrid.revalidate();
        gameGrid.repaint();
    }

    // ----------------------
   //GAME DETAIL POP-UP ----------------------
  // ----------------------

    private void showGameDetails(Game g) {

        JDialog dialog = new JDialog(this, "Game Details", true);

        dialog.setSize(450, 700);

        dialog.setLocationRelativeTo(this);

        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(new Color(30, 32, 38));

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel img = new JLabel();

        img.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (!g.getImagePath().equals("none") &&
                new File(g.getImagePath()).exists()) {

            ImageIcon icon = new ImageIcon(g.getImagePath());

            Image scaled = icon.getImage().getScaledInstance(
                    280,
                    380,
                    Image.SCALE_SMOOTH
            );

            img.setIcon(new ImageIcon(scaled));
        }

        panel.add(img);

        panel.add(Box.createVerticalStrut(20));

        panel.add(createInfo("Title: " + g.getTitle()));
        panel.add(createInfo("Genre: " + g.getGenre()));
        panel.add(createInfo("Status: " + g.getTracker()));
        panel.add(createInfo("Extra: " + g.getExtraDetail()));

        if (g instanceof StoryGame) {

            StoryGame sg = (StoryGame) g;

            panel.add(createInfo("Chapter: " + sg.getChapter()));
            panel.add(createInfo("Level: " + sg.getLevel()));
        }

        dialog.add(panel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    // ----------------------
   //SEARCH FILTER FUNCTION ----------------------
  // ----------------------
    private void filterGames() {

        String query = txtSearch.getText().toLowerCase();

        List<Game> filtered = manager.getGames()
                .values()
                .stream()
                .filter(g ->
                        g.getTitle().toLowerCase().contains(query)
                                ||
                                g.getTracker().toLowerCase().contains(query)
                )
                .collect(Collectors.toList());

        refreshGameGrid(filtered);
    }
    // ----------------------
   //RESET UI FUNCTION ----------------------
  // ----------------------
    private void resetUI() {

        txtTitle.setText("");
        txtGenre.setText("");
        txtExtra.setText("");

        comboTracker.setSelectedIndex(0);

        chkStory.setSelected(false);

        comboChapter.setEnabled(false);
        comboLevel.setEnabled(false);

        currentImagePath = "none";

        lblImageName.setText("No file chosen");

        refreshGameGrid(manager.getGames().values());
    }

    // ========================================
    // MODERN COMPONENTS
    // ========================================

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(UI_FONT);

        field.setPreferredSize(new Dimension(200, 40));

        return field;
    }

    private JLabel createLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setForeground(TEXT);

        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        return lbl;
    }

    private JLabel createInfo(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setForeground(Color.WHITE);

        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lbl.setBorder(new EmptyBorder(10, 10, 10, 10));

        return lbl;
    }

    private JButton createModernButton(String text, Color color) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);

        btn.setBorderPainted(false);

        btn.setForeground(Color.WHITE);

        btn.setBackground(color);

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setPreferredSize(new Dimension(120, 42));

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
    // ----------------------
   // MAIN ----------------------
  // ----------------------

    public static void main(String[] args) {

        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {

            GameVaultUI ui = new GameVaultUI();

            ui.setVisible(true);
        });
    }
}
