/*
 * Name: Wilson, Tyler
 * Date: 15NOV22
 * Course: CMSC 451
 * Professor: Dr Potolea, Rodica
 */
// Import(s)
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BenchmarkReport {
    JFrame window;
    JLabel mainLabel; // Mostly used for errors
    File inputFile;

    public void run() {
        // Define the main window for the report
        window = new JFrame("Benchmark Report");
        window.setSize(800, 225);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); // Method to display JFrame in center of screen
        JFileChooser fileChooser = new JFileChooser();
        JButton fileButton = new JButton("File Select");
        window.add(fileButton, BorderLayout.CENTER);
        window.setVisible(true);

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setCurrentDirectory(new java.io.File("data/"));
                int returnValue = fileChooser.showOpenDialog(window);
                mainLabel = new JLabel("", SwingConstants.CENTER);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    inputFile = fileChooser.getSelectedFile();
                    window.remove(fileButton);
                    mainLabel.setText("Report from: " + inputFile.getName());
                    window.add(mainLabel);
                    makeReport();
                }
            }
        });

        
    }

    public void makeReport() {
        Scanner inputScanner = null;
        Integer n = 0;
        String pError = "Error, see terminal.";

        try {
            inputScanner = new Scanner(inputFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Cannot open file.");
            return;
        }

        String line = null;
        String[] columnNames = { "Size", "Avg Count", "Coef Count",
                "Avg Time", "Coef Time" };

        // Use an array list to add each row
        LinkedList<String[]> data = new LinkedList<String[]>();
        LinkedList<Number> counts = new LinkedList<Number>();
        LinkedList<Number> times = new LinkedList<Number>();
        DecimalFormat decimalFormat = new DecimalFormat(".##");

        // Setting delimiter variable, handling lines
        char lineDel = ':';
        while (inputScanner.hasNextLine()) {
            line = inputScanner.nextLine();
            int index = 0;
            while (index < line.length() && line.charAt(index) != lineDel) {
                index++;
            }

            try {
                n = Integer.parseInt(line.substring(0, index));
            } catch (NumberFormatException ex) {
                System.out.println("Failed to parse integer.");
                mainLabel.setText(pError);
                return;
            }

            // Parse each pair
            int leftIndex = 0;
            String intPair = null;
            while (index < line.length()) {
                while (index < line.length() && line.charAt(index) == lineDel) {
                    index++;
                }

                leftIndex = index;
                while (index < line.length() && line.charAt(index) != lineDel) {
                    index++;
                }

                if (index == line.length()) {
                    break;
                }

                // Extract pair and split by the comma
                intPair = line.substring(leftIndex, index);
                leftIndex = 0;
                while (leftIndex < intPair.length() && intPair.charAt(leftIndex) != ',') {
                    leftIndex++;
                }

                if (leftIndex == intPair.length()) {
                    System.out.println("Couldn't find delimiter.");
                    mainLabel.setText(pError);
                    return;
                }

                // Separate the two numbers
                try {
                    counts.add(Integer.parseInt(intPair.substring(0, leftIndex)));
                    times.add(Long.parseLong(intPair.substring(leftIndex + 1)));
                } catch (NumberFormatException ex) {
                    System.out.println("Failed to parse pair.");
                    mainLabel.setText(pError);
                    return;
                }
            }

            String[] thisRow = new String[columnNames.length];
            thisRow[0] = String.valueOf(n);
            thisRow[1] = decimalFormat.format(average(counts));
            thisRow[2] = decimalFormat.format(
                    cov(counts, Double.parseDouble(thisRow[1]))) + "%";
            thisRow[3] = decimalFormat.format(average(times));
            thisRow[4] = decimalFormat.format(
                    cov(times, Double.parseDouble(thisRow[3]))) + "%";

            data.add(thisRow);
        }

        // Display the table using a JTable
        String[][] emptyData = new String[data.size()][columnNames.length];
        JTable table = new JTable(emptyData, columnNames);
        for (int row = 0; row < data.size(); row++) {
            for (int col = 0; col < columnNames.length; col++) {
                table.setValueAt(data.get(row)[col], row, col);
            }
        }

        // Add table to window
        table.setBounds(0, 0, 600, 600);
        JScrollPane scrollPane = new JScrollPane(table);
        window.add(scrollPane);
        mainLabel.setText("Done");
    }

    // Compute the average of a list of numbers
    public Double average(List<Number> list) {
        Long total = 0L;
        Double average;
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).longValue();
        }

        average = (total + 0.0) / list.size();
        return average;
    }

    // Compute the coefficient of variance of a list of numbers
    public Double cov(List<Number> list, Double avg) {
        return std(list, avg) / avg * 100;
    }

    // Compute the standard deviation of a list of numbers
    public Double std(List<Number> list, Double avg) {
        Double variation = 0D;
        Double difference;
        for (int i = 0; i < list.size(); i++) {
            difference = list.get(i).doubleValue() - avg;
            variation = variation + (difference * difference);
        }

        return Math.sqrt(variation / list.size());
    }

    public static void main(String[] Args) {
        BenchmarkReport bReport = new BenchmarkReport();
        bReport.run();
    }
}
