/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author akuku
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class BookManager extends JFrame {
    private JTextField txtBookID, txtTitle, txtAuthor,txtYear;
    private DefaultTableModel tableModel;
    private JTable table;
    private Connection con;
    
    
    public BookManager(){
        super("Book Manager");
        
        //Initializing the components
        txtBookID = new JTextField(10);
        txtTitle = new JTextField(20);
        txtAuthor = new JTextField(20);
        txtYear = new JTextField(10);
        
        JButton btnAdd = new JButton("Add Book");
        btnAdd.addActionListener(e -> addBook());
        
        JButton btnDelete = new JButton("Delete Book");
        btnDelete.addActionListener(e -> deleteBook());
        
         JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.addActionListener(e -> refreshList());
        
        //setting up table 
        tableModel = new DefaultTableModel(new String[]{"Book ID","Title","Author","Year"},0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        //setting the Layout
        JPanel inputPanel = new JPanel(new GridLayout(5,2));
        inputPanel.add(new JLabel("Book ID:"));
        inputPanel.add(txtBookID);
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(txtAuthor);
        inputPanel.add(new JLabel("Year"));
        inputPanel.add(txtYear);
        inputPanel.add(btnAdd);
        inputPanel.add(btnDelete);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRefresh);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
         mainPanel.add(scrollPane, BorderLayout.CENTER);
          mainPanel.add(buttonPanel, BorderLayout.SOUTH);
          add(mainPanel);
          
          //Database connection
          try {
              Class.forName("net.ucanaccess.jbc.UcanaccessDriver");
              String dbURL = "jdbc:ucanaccess:User\\akuku\\Document\\NetBeansProjects\\coursework\\scr\\main\\java\\YourDatabase.accdb";
              con = DriverManager.getConnection(dbURL);
        
          } catch (ClassNotFoundException | SQLException ex){
              ex.printStackTrace();
              JOptionPane.showMessageDialog(this, "Error: Unable to connect to database.");
              System.exit(1);
              
          }
          
          //Loading the initial data:
          refreshList();
          
          //setting up window  
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setSize(600,400);
          setLocationRelativeTo(null);
          setVisible(true);
          
        
    }
    private void addBook(){
        String bookID = txtBookID.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String year = txtYear.getText();
        try {
            Statement stmt = con.createStatement();
            String query = "INSERT INTO Books (BookID, Title, Author, Year) VALUES ('"+bookID +"', '"+ title +"','"+author +"','"+ year +"')";
            int rowsAffected = stmt.executeUpdate(query);
            if(rowsAffected > 0){
                JOptionPane.showMessageDialog(this, "Book added successfully.");
                refreshList();
                clearFields();
            }else{
                       JOptionPane.showMessageDialog(this, "Failed to add book.");
            }
            
        }catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error:" + ex.getMessage());
        }}
           
private void deleteBook(){
int selectedRow = table.getSelectedRow();
if (selectedRow == -1){
    JOptionPane.showMessageDialog(this, "PLease select a book to delete.");
    return;
        }
String bookID = table.getValueAt(selectedRow, 0).toString();
try {
    Statement stmt = con.createStatement();
    String query = "DELETE FORM Books WHERE BookID ='" + bookID +"'";
    int rowsAffected = stmt.executeUpdate(query);
    if(rowsAffected >0){
        JOptionPane.showMessageDialog(this, "Book deleted successfully.");
        refreshList();
    
    } else {
        JOptionPane.showMessageDialog(this, "Failed to delete book.");
    }
}catch(SQLException ex){
    ex.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error:" + ex.getMessage());
   
    }
    
}
private void refreshList(){
    tableModel.setRowCount(0);
    try{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FORM Books");
        while (rs.next()){
            String bookID = rs.getString("BookID");
            String title = rs.getString("Title");
            String author = rs.getString("Author");
            String year = rs.getString("Year");
            tableModel.addRow(new Object[]{bookID,title,author,year});
        }
        } catch (SQLException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error:" + ex.getMessage());
        }
    }
private void clearFields(){
    txtBookID.setText("");
    txtTitle.setText("");
    txtAuthor.setText("");
    txtYear.setText("");
}
public static void main(String[]args){
    SwingUtilities.invokeLater(BookManager::new);
}
}
