import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContactManagementSystem extends JFrame {
    private ArrayList<Contact> contacts;
    private JList<String> contactList;
    private DefaultListModel<String> contactListModel;
    private JTextArea contactDetailsTextArea;
    private JTextField searchField;

    public ContactManagementSystem() {
        contacts = new ArrayList<>();
        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(contactList);

        contactDetailsTextArea = new JTextArea(10, 30);
        contactDetailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(contactDetailsTextArea);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchContacts(searchField.getText());
            }
        });

        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        JButton sortButton = new JButton("Sort Contacts");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortContacts();
            }
        });

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(deleteButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(listScrollPane, BorderLayout.WEST);
        mainPanel.add(detailsScrollPane, BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Contact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);

        contactList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    displayContactDetails(selectedIndex);
                }
            }
        });
    }

    private void addContact() {
        String name = JOptionPane.showInputDialog(this, "Enter contact name:");
        if (name != null && !name.isEmpty()) {
            String mobileNumber = JOptionPane.showInputDialog(this, "Enter mobile number:");
            String email = JOptionPane.showInputDialog(this, "Enter email:");
            contacts.add(new Contact(name, mobileNumber, email));
            updateContactList();
        }
    }

    private void displayContactDetails(int index) {
        Contact contact = contacts.get(index);
        String details = "Name: " + contact.getName() + "\n"
                + "Mobile: " + contact.getMobileNumber() + "\n"
                + "Email: " + contact.getEmail();
        contactDetailsTextArea.setText(details);
    }

    private void updateContactList() {
        contactListModel.clear();
        for (Contact contact : contacts) {
            contactListModel.addElement(contact.getName());
        }
    }

    private void searchContacts(String searchTerm) {
        contactListModel.clear();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                contactListModel.addElement(contact.getName());
            }
        }
    }

    private void sortContacts() {
        Collections.sort(contacts, Comparator.comparing(Contact::getName, String.CASE_INSENSITIVE_ORDER));
        updateContactList();
    }

    private void deleteContact() {
        int selectedIndex = contactList.getSelectedIndex();
        if (selectedIndex != -1) {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this contact?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                contacts.remove(selectedIndex);
                updateContactList();
                contactDetailsTextArea.setText(""); // Clear details area after deletion
                JOptionPane.showMessageDialog(this, "Contact deleted successfully.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.", "No Contact Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContactManagementSystem();
            }
        });
    }
}

class Contact {
    private String name;
    private String mobileNumber;
    private String email;

    public Contact(String name, String mobileNumber, String email) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }
}
