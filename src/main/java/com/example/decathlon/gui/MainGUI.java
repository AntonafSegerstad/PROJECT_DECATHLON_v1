package com.example.decathlon.gui;

import com.example.decathlon.excel.ExcelPrinter;
import com.example.decathlon.excel.ExcelReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;
    private JTable standingsTable;
    private DefaultTableModel tableModel;
    private JRadioButton decathlonButton;
    private JRadioButton heptathlonButton;

    private final Map<String, CompetitorRow> competitors = new LinkedHashMap<>();

    private static final String[] DECATHLON_EVENTS = {
            "100m", "Long Jump", "Shot Put", "High Jump", "400m",
            "110m Hurdles", "Discus Throw", "Pole Vault", "Javelin Throw", "1500m"
    };

    private static final String[] HEPTATHLON_EVENTS = {
            "100m Hurdles", "High Jump", "Shot Put", "200m",
            "Long Jump", "Javelin Throw", "800m"
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        decathlonButton = new JRadioButton("Decathlon", true);
        heptathlonButton = new JRadioButton("Heptathlon");
        ButtonGroup group = new ButtonGroup();
        group.add(decathlonButton);
        group.add(heptathlonButton);
        modePanel.add(new JLabel("Competition:"));
        modePanel.add(decathlonButton);
        modePanel.add(heptathlonButton);
        topPanel.add(modePanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameField = new JTextField(15);
        resultField = new JTextField(10);
        disciplineBox = new JComboBox<>();

        inputPanel.add(new JLabel("Competitor Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Discipline:"));
        inputPanel.add(disciplineBox);
        inputPanel.add(new JLabel("Result:"));
        inputPanel.add(resultField);

        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(e -> calculateAndStoreResult());
        inputPanel.add(calculateButton);

        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(e -> exportToExcel());
        inputPanel.add(exportButton);

        JButton importButton = new JButton("Import from Excel");
        importButton.addActionListener(e -> importFromExcel());
        inputPanel.add(importButton);

        topPanel.add(inputPanel);
        frame.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        standingsTable = new JTable(tableModel);
        standingsTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(standingsTable);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        decathlonButton.addActionListener(e -> updateMode());
        heptathlonButton.addActionListener(e -> updateMode());

        updateMode();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void updateMode() {
        updateDisciplineBox();
        rebuildTable();
    }

    private void updateDisciplineBox() {
        disciplineBox.removeAllItems();
        String[] events = getCurrentEvents();
        for (String event : events) {
            disciplineBox.addItem(event);
        }
    }

    private void rebuildTable() {
        String[] events = getCurrentEvents();
        String[] columns = new String[events.length + 3];
        columns[0] = "Placement";
        columns[1] = "Name";
        System.arraycopy(events, 0, columns, 2, events.length);
        columns[columns.length - 1] = "Total";

        tableModel.setDataVector(new Object[0][0], columns);

        List<CompetitorRow> rows = new ArrayList<>();
        for (CompetitorRow competitor : competitors.values()) {
            if (competitor.mode.equals(getCurrentMode())) {
                rows.add(competitor);
            }
        }

        rows.sort(Comparator.comparingInt(this::calculateTotal).reversed().thenComparing(c -> c.name));

        int placement = 1;
        for (CompetitorRow competitor : rows) {
            tableModel.addRow(buildRowData(placement, competitor, events));
            placement++;
        }
    }

    private void calculateAndStoreResult() {
        String name = nameField.getText().trim();
        String discipline = (String) disciplineBox.getSelectedItem();
        String resultText = resultField.getText().trim();

        if (name.isEmpty() || discipline == null || resultText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please enter name, discipline and result.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            double result = Double.parseDouble(resultText);

            if (!isWithinRange(discipline, result)) {
                double min = getLowerLimit(discipline);
                double max = getUpperLimit(discipline);
                JOptionPane.showMessageDialog(
                        null,
                        "Value out of range for " + discipline + ". Allowed range: " + formatNumber(min) + " - " + formatNumber(max),
                        "Invalid Result",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int score = calculateScore(discipline, result);
            String mode = getCurrentMode();
            String key = name + "::" + mode;

            CompetitorRow competitor = competitors.get(key);
            if (competitor == null) {
                competitor = new CompetitorRow(name, mode);
                competitors.put(key, competitor);
            }

            competitor.name = name;
            competitor.mode = mode;
            competitor.scores.put(discipline, score);

            rebuildTable();

            resultField.setText("");

            JOptionPane.showMessageDialog(
                    null,
                    "Saved: " + score + " pts",
                    "Score Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid number for the result.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int calculateScore(String discipline, double result) {
        switch (discipline) {
            case "100m":
                return scoreTrack(25.4347, 18.0, 1.81, result);
            case "110m Hurdles":
                return scoreTrack(5.74352, 28.5, 1.92, result);
            case "400m":
                return scoreTrack(1.53775, 82.0, 1.81, result);
            case "1500m":
                return scoreTrack(0.03768, 480.0, 1.85, result);
            case "Discus Throw":
                return scoreField(12.91, 4.0, 1.1, result);
            case "High Jump":
                if (isDecathlonSelected()) {
                    return scoreField(0.8465, 75.0, 1.42, result);
                }
                return scoreField(1.84523, 75.0, 1.348, result);
            case "Javelin Throw":
                if (isDecathlonSelected()) {
                    return scoreField(10.14, 7.0, 1.08, result);
                }
                return scoreField(15.9803, 3.8, 1.04, result);
            case "Long Jump":
                if (isDecathlonSelected()) {
                    return scoreField(0.14354, 220.0, 1.4, result);
                }
                return scoreField(0.188807, 210.0, 1.41, result);
            case "Pole Vault":
                return scoreField(0.2797, 100.0, 1.35, result);
            case "Shot Put":
                if (isDecathlonSelected()) {
                    return scoreField(51.39, 1.5, 1.05, result);
                }
                return scoreField(56.0211, 1.5, 1.05, result);
            case "100m Hurdles":
                return scoreTrack(9.23076, 26.7, 1.835, result);
            case "200m":
                return scoreTrack(4.99087, 42.5, 1.81, result);
            case "800m":
                return scoreTrack(0.11193, 254.0, 1.88, result);
            default:
                return 0;
        }
    }

    private int scoreTrack(double a, double b, double c, double value) {
        double x = b - value;
        if (x <= 0) {
            return 0;
        }
        return (int) Math.floor(a * Math.pow(x, c));
    }

    private int scoreField(double a, double b, double c, double value) {
        double x = value - b;
        if (x <= 0) {
            return 0;
        }
        return (int) Math.floor(a * Math.pow(x, c));
    }

    private boolean isWithinRange(String discipline, double value) {
        return value >= getLowerLimit(discipline) && value <= getUpperLimit(discipline);
    }

    private double getLowerLimit(String discipline) {
        switch (discipline) {
            case "100m":
                return 5;
            case "110m Hurdles":
            case "100m Hurdles":
                return 10;
            case "400m":
                return 20;
            case "1500m":
                return 150;
            case "Discus Throw":
                return 0;
            case "High Jump":
                return 0;
            case "Javelin Throw":
                return 0;
            case "Long Jump":
                return 0;
            case "Pole Vault":
                return 0;
            case "Shot Put":
                return 0;
            case "200m":
                return 20;
            case "800m":
                return 70;
            default:
                return Double.NEGATIVE_INFINITY;
        }
    }

    private double getUpperLimit(String discipline) {
        switch (discipline) {
            case "100m":
                return 20;
            case "110m Hurdles":
            case "100m Hurdles":
                return 30;
            case "400m":
                return 100;
            case "1500m":
                return 400;
            case "Discus Throw":
                return 85;
            case "High Jump":
                return 300;
            case "Javelin Throw":
                return 110;
            case "Long Jump":
                return 1000;
            case "Pole Vault":
                return 1000;
            case "Shot Put":
                return 30;
            case "200m":
                return 100;
            case "800m":
                return 250;
            default:
                return Double.POSITIVE_INFINITY;
        }
    }

    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private Object[] buildRowData(int placement, CompetitorRow competitor, String[] events) {
        Object[] row = new Object[events.length + 3];
        row[0] = placement;
        row[1] = competitor.name;

        int total = 0;
        for (int i = 0; i < events.length; i++) {
            Integer score = competitor.scores.get(events[i]);
            row[i + 2] = score == null ? "" : score;
            if (score != null) {
                total += score;
            }
        }

        row[row.length - 1] = total;
        return row;
    }

    private int calculateTotal(CompetitorRow competitor) {
        return competitor.scores.values().stream().mapToInt(Integer::intValue).sum();
    }

    private void exportToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to Excel");

        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        String path = selectedFile.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".xlsx")) {
            path += ".xlsx";
        }

        try {
            ExcelPrinter printer = new ExcelPrinter(path);
            printer.add(getTableData(), getCurrentSheetName());
            printer.write();
            JOptionPane.showMessageDialog(null, "Export completed.", "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Export failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import from Excel");

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        try {
            ExcelReader reader = new ExcelReader();
            Object[][] data = reader.readSheet(selectedFile.getAbsolutePath(), 0);
            if (data.length <= 1) {
                return;
            }

            competitors.clear();

            String mode = detectModeFromHeader(data[0]);

            if ("heptathlon".equals(mode)) {
                heptathlonButton.setSelected(true);
            } else {
                decathlonButton.setSelected(true);
            }

            String[] events = "heptathlon".equals(mode) ? HEPTATHLON_EVENTS : DECATHLON_EVENTS;

            for (int i = 1; i < data.length; i++) {
                Object[] row = data[i];
                if (row.length < 3) {
                    continue;
                }

                String name = String.valueOf(row[1]).trim();
                if (name.isEmpty()) {
                    continue;
                }

                CompetitorRow competitor = new CompetitorRow(name, mode);

                for (int j = 0; j < events.length; j++) {
                    int colIndex = j + 2;
                    if (colIndex < row.length) {
                        String value = String.valueOf(row[colIndex]).trim();
                        if (!value.isEmpty()) {
                            try {
                                competitor.scores.put(events[j], Integer.parseInt(value));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }

                competitors.put(name + "::" + mode, competitor);
            }

            updateMode();
            JOptionPane.showMessageDialog(null, "Import completed.", "Import", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Import failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String detectModeFromHeader(Object[] headerRow) {
        for (Object cell : headerRow) {
            String value = String.valueOf(cell).trim();
            if ("200m".equals(value) || "800m".equals(value) || "100m Hurdles".equals(value)) {
                return "heptathlon";
            }
        }
        return "decathlon";
    }

    private Object[][] getTableData() {
        int rowCount = tableModel.getRowCount();
        int columnCount = tableModel.getColumnCount();
        Object[][] data = new Object[rowCount + 1][columnCount];

        for (int col = 0; col < columnCount; col++) {
            data[0][col] = tableModel.getColumnName(col);
        }

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                Object value = tableModel.getValueAt(row, col);
                data[row + 1][col] = value == null ? "" : value;
            }
        }

        return data;
    }

    private String getCurrentSheetName() {
        return isDecathlonSelected() ? "Decathlon" : "Heptathlon";
    }

    private String[] getCurrentEvents() {
        return isDecathlonSelected() ? DECATHLON_EVENTS : HEPTATHLON_EVENTS;
    }

    private boolean isDecathlonSelected() {
        return decathlonButton.isSelected();
    }

    private String getCurrentMode() {
        return isDecathlonSelected() ? "decathlon" : "heptathlon";
    }

    private static class CompetitorRow {
        String name;
        String mode;
        Map<String, Integer> scores = new LinkedHashMap<>();

        CompetitorRow(String name, String mode) {
            this.name = name;
            this.mode = mode;
        }
    }
}