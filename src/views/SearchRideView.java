package views;

import controllers.RideController;
import models.Ride;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

  public class SearchRideView extends JFrame {

  private RideController rideController;
  private JTable rideTable;
  private DefaultTableModel tableModel;
  private JTextField txtOrigin, txtDestination;
  private JTextField txtDate;

  public SearchRideView(RideController rideController) {
  this.rideController = rideController;
  setTitle("Search Rides - Campus Carpool");
  setSize(900, 450);
  setLocationRelativeTo(null);
  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


   initComponents();


  }

  private void initComponents() {
  setLayout(new BorderLayout());

   JPanel topPanel = new JPanel();
   topPanel.setLayout(new FlowLayout());

   topPanel.add(new JLabel("Origin:"));
   txtOrigin = new JTextField(10);
   topPanel.add(txtOrigin);

   topPanel.add(new JLabel("Destination:"));
   txtDestination = new JTextField(10);
   topPanel.add(txtDestination);

   topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
   txtDate = new JTextField(8);
   topPanel.add(txtDate);

   JButton btnSearch = new JButton("Search");
   btnSearch.addActionListener(e -> searchRides());
   topPanel.add(btnSearch);

   add(topPanel, BorderLayout.NORTH);

   tableModel = new DefaultTableModel(
           new String[]{"ID", "Driver", "From", "To", "Date", "Time", "Seats", "Price (AED)", "Vehicle"},
           0
   );
   rideTable = new JTable(tableModel);
   JScrollPane scrollPane = new JScrollPane(rideTable);
   add(scrollPane, BorderLayout.CENTER);


  }

  private void searchRides() {
  tableModel.setRowCount(0);

   String origin = txtOrigin.getText().trim();
   String destination = txtDestination.getText().trim();
   String dateStr = txtDate.getText().trim();

   List<Ride> rides;

   try {
       if (!origin.isEmpty() && !destination.isEmpty()) {
           rides = rideController.searchRidesByRoute(origin, destination);
       } else if (!destination.isEmpty()) {
           rides = rideController.searchRidesByDestination(destination);
       } else if (!dateStr.isEmpty()) {
           LocalDate date = LocalDate.parse(dateStr);
           rides = rideController.searchRidesByDate(date);
       } else {
           rides = rideController.searchAllRides();
       }

       for (Ride r : rides) {
           tableModel.addRow(new Object[]{
                   r.getId(), r.getDriverName(), r.getOrigin(), r.getDestination(),
                   r.getDepartureDate(), r.getDepartureTime(),
                   r.getSeatsAvailable(), r.getPricePerSeat(), r.getVehicleInfo()
           });
       }

   } catch (Exception e) {
       JOptionPane.showMessageDialog(this, "Error searching rides: " + e.getMessage(),
               "Error", JOptionPane.ERROR_MESSAGE);
       e.printStackTrace();
   }


  }

  public void display() {
  setVisible(true);
  }
  }
