import com.mysql.cj.x.protobuf.MysqlxPrepare;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Admin extends JFrame {

    //connect to database
    private Connection connection;

    public Admin()
    {
        try
        {
            connection = Config.connectDB();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

  public void editMember(String keyword)
  {
      setTitle("Edit Member Detail");
      setSize(800,600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JTextField memberIdField = new JTextField(20);
      JTextField memberNameField = new JTextField(20);
      JTextField memberEmailField = new JTextField(20);
      JTextField majorField = new JTextField(20);
      JComboBox<String> typeField = new JComboBox<>(new String[]{"Student", "Vip Student", "Teacher", "VIP Teacher"});
      JTextField phoneNumberField = new JTextField(20);

      //home panel
      JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      JButton homeBtn = new JButton("Home");
      homeBtn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

              dispose();
              Home homepage = new Home();
          }
      });
      homePanel.add(homeBtn);


      try(PreparedStatement getMember = connection.prepareStatement("select * from member where memberID = ? OR name = ?"))
      {
          getMember.setString(1, keyword);
          getMember.setString(2, keyword);

          ResultSet result = getMember.executeQuery();

          if(result.next())
          {
              System.out.println(result.getString("name"));

              memberIdField.setText(result.getString("memberID"));
              memberNameField.setText(result.getString("name"));
              memberEmailField.setText(result.getString("email"));
              majorField.setText(result.getString("major"));
              typeField.setSelectedItem(result.getString("level"));
              phoneNumberField.setText(result.getString("telephone"));
          }

      }catch(SQLException error)
      {
          error.printStackTrace();
      }

      JButton editButton = new JButton("Edit");
      JButton backButton = new JButton("Back");

      // Create panel for components with a grid layout
      JPanel mainPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10); // Padding

      // Add header label
      JLabel headerLabel = new JLabel("Edit Member Detail");
      headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(headerLabel, gbc);

      // Reset gridwidth
      gbc.gridwidth = 1;

      // Add components to the panel
      gbc.gridy++;
      addLabelAndField("Member ID:", memberIdField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Member Name:", memberNameField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Member Email:", memberEmailField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Major:", majorField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndComboBox("Type:", typeField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Phone Number:", phoneNumberField, gbc, mainPanel);

      gbc.gridx = 0;
      gbc.gridy++;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(editButton, gbc);

      gbc.gridy++;
      mainPanel.add(backButton, gbc);

      // Add action listeners
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Handle submit button action
              String memberID = memberIdField.getText();
              String memberName = memberNameField.getText();
              String major = majorField.getText();
              String type = (String) typeField.getSelectedItem();
              String phone = phoneNumberField.getText();
              String memberEmail = memberEmailField.getText();

              try(PreparedStatement editMember = connection.prepareStatement("update member set name = ?, major = ?, level = ?, telephone = ?, email = ? where memberID = ?"))
              {
                  //set the parameter
                  editMember.setString(1, memberName);
                  editMember.setString(2, major);
                  editMember.setString(3, type);
                  editMember.setString(4, phone);
                  editMember.setString(5, memberEmail);
                  editMember.setString(6, memberID);

                  if(editMember.executeUpdate() == 1)
                  {
                      JOptionPane.showMessageDialog(null, keyword + " edited successfully");
                  }
                  else
                  {
                      JOptionPane.showMessageDialog(null, "Failed to edit " + keyword);
                  }

              }catch(SQLException err)
              {
                  err.printStackTrace();
              }

          }
      });

      backButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              if(e.getSource() == backButton)
              {
                  dispose();
                  new EditDeleteView();
              }
          }
      });

      // Set layout for the main frame
      setLayout(new BorderLayout());
      add(homePanel, BorderLayout.NORTH);
      add(mainPanel, BorderLayout.CENTER);

      // Center the form on the screen
      setLocationRelativeTo(null);
      setVisible(true);

  }

  public boolean editBook(String keyword)
  {
      setTitle("Edit Book Detail");
      setSize(800,600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JTextField bookIdField = new JTextField(20);
      JTextField titleField = new JTextField(20);
      JTextField authorField = new JTextField(20);
      JTextField publisherField = new JTextField(20);
      JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Journal", "Reference"});
      JTextField priceField = new JTextField(20);
      JTextField isbnField = new JTextField(20);

      //home panel
      JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      JButton homeBtn = new JButton("Home");
      homeBtn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

              dispose();
              Home homepage = new Home();
          }
      });
      homePanel.add(homeBtn);


      try(PreparedStatement getBook = connection.prepareStatement("select * from book where bookID = ? OR title = ?"))
      {
          getBook.setString(1, keyword);
          getBook.setString(2, keyword);

          ResultSet result = getBook.executeQuery();

          if(result.next())
          {
              bookIdField.setText(result.getString("bookID"));
              titleField.setText(result.getString("title"));
              authorField.setText(result.getString("author"));
              publisherField.setText(result.getString("publisher"));
              typeComboBox.setSelectedItem(result.getString("type"));
              priceField.setText(String.valueOf(result.getDouble("price")));
              isbnField.setText(result.getString("ISBN"));
          }

      }catch(SQLException error)
      {
          error.printStackTrace();
      }

      JButton editButton = new JButton("Edit");
      JButton backButton = new JButton("Back");

      // Create panel for components with a grid layout
      JPanel mainPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10); // Padding

      // Add header label
      JLabel headerLabel = new JLabel("Edit Book Detail");
      headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(headerLabel, gbc);

      // Reset gridwidth
      gbc.gridwidth = 1;

      // Add components to the panel
      gbc.gridy++;
      addLabelAndField("Book ID:", bookIdField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Title:", titleField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Author:", authorField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Publisher:", publisherField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndComboBox("Type:", typeComboBox, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Price:", priceField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("ISBN:", isbnField, gbc, mainPanel);

      gbc.gridx = 0;
      gbc.gridy++;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(editButton, gbc);

      gbc.gridy++;
      mainPanel.add(backButton, gbc);

      // Add action listeners
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Handle submit button action
              String bookID = bookIdField.getText();
              String title = titleField.getText();
              String author = authorField.getText();
              String publisher = publisherField.getText();
              String type = (String) typeComboBox.getSelectedItem();
              double price = Double.parseDouble(priceField.getText());
              String isbn = isbnField.getText();

              try(PreparedStatement editBook = connection.prepareStatement("update book set title = ?, author = ?, publisher = ?, type = ?, price = ?, isbn = ? where bookID = ?"))
              {
                  //set the parameter
                  editBook.setString(1, title);
                  editBook.setString(2, author);
                  editBook.setString(3, publisher);
                  editBook.setString(4, type);
                  editBook.setDouble(5, price);
                  editBook.setString(6, isbn);
                  editBook.setString(7, bookID);

                  if(editBook.executeUpdate() == 1)
                  {
                      JOptionPane.showMessageDialog(null, keyword + " edited successfully");
                  }
                  else
                  {
                      JOptionPane.showMessageDialog(null, "Failed to edit " + keyword);
                  }

              }catch(SQLException err)
              {
                  err.printStackTrace();
              }

          }
      });

      backButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              if(e.getSource() == backButton)
              {
                  dispose();
                  new EditDeleteView();
              }
          }
      });

      // Set layout for the main frame
      setLayout(new BorderLayout());
      add(homePanel, BorderLayout.NORTH);
      add(mainPanel, BorderLayout.CENTER);

      // Center the form on the screen
      setLocationRelativeTo(null);
      setVisible(true);
      return false;
  }

  public boolean editAdmin(String keyword)
  {
      setTitle("Edit Admin Detail");
      setSize(800,600);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      JTextField adminIDField = new JTextField(20);
      JTextField adminNameField = new JTextField(20);
      JComboBox<String> typeField = new JComboBox<>(new String[]{"Junior Librarian", "Senior Librarian"});

      //home panel
      JPanel homePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      JButton homeBtn = new JButton("Home");
      homeBtn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

              dispose();
              Home homepage = new Home();
          }
      });
      homePanel.add(homeBtn);


      try(PreparedStatement getAdmin = connection.prepareStatement("select * from admin where adminID = ? OR name = ?"))
      {
          getAdmin.setString(1, keyword);
          getAdmin.setString(2, keyword);

          ResultSet result = getAdmin.executeQuery();

          if(result.next())
          {
              adminIDField.setText(result.getString("adminID"));
              adminNameField.setText(result.getString("name"));
              typeField.setSelectedItem(result.getString("type"));
          }

      }catch(SQLException error)
      {
          error.printStackTrace();
      }

      JButton editButton = new JButton("Edit");
      JButton backButton = new JButton("Back");

      // Create panel for components with a grid layout
      JPanel mainPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10); // Padding

      // Add header label
      JLabel headerLabel = new JLabel("Edit Admin Detail");
      headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(headerLabel, gbc);

      // Reset gridwidth
      gbc.gridwidth = 1;

      // Add components to the panel
      gbc.gridy++;
      addLabelAndField("Admin ID:", adminIDField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndField("Admin Name:", adminNameField, gbc, mainPanel);
      gbc.gridy++;
      addLabelAndComboBox("Type:", typeField, gbc, mainPanel);

      gbc.gridx = 0;
      gbc.gridy++;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      mainPanel.add(editButton, gbc);

      gbc.gridy++;
      mainPanel.add(backButton, gbc);

      // Add action listeners
      editButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              // Handle submit button action
              String adminID = adminIDField.getText();
              String adminName = adminNameField.getText();
              String type = (String) typeField.getSelectedItem();

              try(PreparedStatement editAdmin = connection.prepareStatement("update admin set name = ?, type = ? where adminID = ?"))
              {
                  //set the parameter
                  editAdmin.setString(1, adminName);
                  editAdmin.setString(2, type);
                  editAdmin.setString(3, adminID);

                  if(editAdmin.executeUpdate() == 1)
                  {
                      JOptionPane.showMessageDialog(null, keyword + " edited successfully");
                  }
                  else
                  {
                      JOptionPane.showMessageDialog(null, "Failed to edit " + keyword);
                  }

              }catch(SQLException err)
              {
                  err.printStackTrace();
              }

          }
      });

      backButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              if(e.getSource() == backButton)
              {
                  dispose();
                  new EditDeleteView();
              }
          }
      });

      // Set layout for the main frame
      setLayout(new BorderLayout());
      add(homePanel, BorderLayout.NORTH);
      add(mainPanel, BorderLayout.CENTER);

      // Center the form on the screen
      setLocationRelativeTo(null);
      setVisible(true);
      return false;
  }

  public boolean registerAdmin(String adminID, String adminName, String password, String type)
  {
      try(PreparedStatement registerAdmin = connection.prepareStatement("insert into admin values(?,?,?,?)"))
      {
          registerAdmin.setString(1, adminID);
          registerAdmin.setString(2, adminName);
          registerAdmin.setString(3, password);
          registerAdmin.setString(4, type);

          if(registerAdmin.executeUpdate() == 1)
          {
              return true;
          }

      }catch(SQLException e)
      {
          e.printStackTrace();
      }

      return false;
  }

  public void deleteMember(String keyword)
  {
      if(keyword.equals(""))
          return;

      int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete member " + keyword + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);

      if(option == JOptionPane.YES_OPTION)
      {
          try(PreparedStatement deleteMember = connection.prepareStatement("delete from member where memberID = ?"))
          {
              deleteMember.setString(1, keyword);

              if(deleteMember.executeUpdate() == 1)
              {
                  JOptionPane.showMessageDialog(null, "Member " + keyword + " is deleted");
              }
              else
              {
                  JOptionPane.showMessageDialog(null, "Failed to delete member " + keyword);
              }
          }catch(SQLException e)
          {
              e.printStackTrace();
          }
      }
  }

  public void deleteBook(String keyword)
  {
      if(keyword.equals(""))
          return;

      int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete book " + keyword + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);

      if(option == JOptionPane.YES_OPTION)
      {
          try(PreparedStatement deleteBook = connection.prepareStatement("delete from book where bookID = ?"))
          {
              deleteBook.setString(1, keyword);

              if(deleteBook.executeUpdate() == 1)
              {
                  JOptionPane.showMessageDialog(null, "Book " + keyword + " is deleted");
              }
              else
              {
                  JOptionPane.showMessageDialog(null, "Failed to delete book  " + keyword);
              }
          }catch(SQLException e)
          {
              e.printStackTrace();
          }
      }

  }

    public void deleteAdmin(String keyword)
    {
        if(keyword.equals(""))
            return;

        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete admin " + keyword + "?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.YES_OPTION)
        {
            try(PreparedStatement deleteAdmin = connection.prepareStatement("delete from admin where adminID = ?"))
            {
                deleteAdmin.setString(1, keyword);

                if(deleteAdmin.executeUpdate() == 1)
                {
                    JOptionPane.showMessageDialog(null, "Admin " + keyword + " is deleted");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Failed to delete Admin  " + keyword);
                }
            }catch(SQLException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void addLabelAndField(String labelText, JTextField textField, GridBagConstraints gbc, JPanel panel) {
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textField, gbc);
    }

    private void addLabelAndComboBox(String labelText, JComboBox<String> comboBox, GridBagConstraints gbc, JPanel panel) {
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(comboBox, gbc);
    }



}

