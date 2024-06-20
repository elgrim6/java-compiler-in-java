import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import analise_lexica.Lexer;
import analise_lexica.Token;
import analise_semantica.AnalisadorSemantico;
import analise_sintactica.Analisador_Sintactico;
import analise_sintactica.Simbolo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JPanel {
    private JButton compile;
    private JTextArea code_area;
    private JTabbedPane tabbedPane;
    private JTable simbolosTable;
    private JTable lexemasTable;

    public GUI() {
        // Construct components
        compile = new JButton("COMPILAR");
        code_area = new JTextArea(5, 5);
        tabbedPane = new JTabbedPane();

        // Create table models and tables
        String[] simbolosColumnNames = {"Token", "Categoria", "Tipo", "Dimensao"};
        String[][] simbolosData = {}; 

        String[] lexemasColumnNames = {"Lexema", "Tipo", "Linha"};
        Object[][] lexemasData = {};

        simbolosTable = new JTable(simbolosData, simbolosColumnNames);
        lexemasTable = new JTable(lexemasData, lexemasColumnNames);

        // Create scroll panes for tables
        JScrollPane simbolosScrollPane = new JScrollPane(simbolosTable);
        JScrollPane lexemasScrollPane = new JScrollPane(lexemasTable);

        // Add tables to tabs
        tabbedPane.addTab("Simbolos", simbolosScrollPane);
        tabbedPane.addTab("Lexemas", lexemasScrollPane);

        // Set components properties
        code_area.setToolTipText("ESCREVA O SEU CODIGO AQUI");

        // Add a border around the code_area
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY);
        code_area.setBorder(BorderFactory.createCompoundBorder(border, 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Set dark theme colors
        setBackground(Color.DARK_GRAY);
        compile.setBackground(Color.BLACK);
        compile.setForeground(Color.WHITE);
        code_area.setBackground(Color.BLACK);
        code_area.setForeground(Color.WHITE);
        code_area.setCaretColor(Color.WHITE);

        tabbedPane.setBackground(Color.BLACK);
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setOpaque(true);
        
        simbolosTable.setBackground(Color.BLACK);
        simbolosTable.setForeground(Color.WHITE);
        lexemasTable.setBackground(Color.BLACK);
        lexemasTable.setForeground(Color.WHITE);

        simbolosScrollPane.setBackground(Color.DARK_GRAY);
        lexemasScrollPane.setBackground(Color.DARK_GRAY);

        // Adjust size and set layout
        setPreferredSize(new Dimension(911, 581));
        setLayout(null);

        // Add components
        add(compile);
        add(code_area);
        add(tabbedPane);

        // Set component bounds (only needed by Absolute Positioning)
        compile.setBounds(420, 260, 100, 20);
        code_area.setBounds(20, 55, 370, 475);
        tabbedPane.setBounds(540, 60, 355, 470);

        // Add action listener to the compile button
        compile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = code_area.getText();

                Lexer lexer = new Lexer(code, 1);
                List<Token> tokens = lexer.tokenize();

                updateLexemasTable(criarTabelaLexemas(tokens, lexemasColumnNames));

                Analisador_Sintactico parser = new Analisador_Sintactico(tokens);
                try {
                    parser.parse();
                    //System.out.println("Parsing successful!");
                    updateSimbolosTable(criarTabelaSimbolos(new ArrayList<>(parser.getTabelaSimbolos().getTabela().values()), simbolosColumnNames));

                    AnalisadorSemantico semanticAnalyzer = new AnalisadorSemantico(parser.getTabelaSimbolos());
                    semanticAnalyzer.analisar(tokens);
                    System.out.println("Semantic analysis successful!");
                    
                } catch (IllegalArgumentException a) {
                	JOptionPane.showMessageDialog(null, a.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);
                    System.err.println("Parsing failed: " + a.getMessage());
                }
            }
        });
    }

    // Methods to update the tables (example implementation, adjust as needed)
    private void updateSimbolosTable(Object[][] data) {
        simbolosTable.setModel(new DefaultTableModel(data, new String[]{"Token", "Categoria", "Tipo", "Dimensao"}));
    }

    private void updateLexemasTable(Object[][] data) {
        lexemasTable.setModel(new DefaultTableModel(data, new String[]{"Lexema", "Tipo", "Linha"}));
    }

    private String[][] criarTabelaLexemas(List<Token> tokens, String[] lexemasColumnNames) {
        String[][] lexemas = new String[tokens.size()][lexemasColumnNames.length];
        int linha = 0;
        for (Token t : tokens) {
            lexemas[linha][0] = t.getText();
            lexemas[linha][1] = t.getType() + "";
            lexemas[linha][2] = t.getLine() + "";
            linha++;
        }

        return lexemas;
    }

    private String[][] criarTabelaSimbolos(List<Simbolo> tokens, String[] simbolosColumnNames) {
        String[][] lexemas = new String[tokens.size()][simbolosColumnNames.length];
        int linha = 0;
        for (Simbolo s : tokens) {
            lexemas[linha][0] = s.getToken();
            lexemas[linha][1] = s.getCategoria() + "";
            lexemas[linha][2] = s.getTipo() + "";
            lexemas[linha][3] = s.getDimensao() + "";
            linha++;
        }

        return lexemas;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MyPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GUI());
        frame.pack();
        frame.setVisible(true);
    }
}
