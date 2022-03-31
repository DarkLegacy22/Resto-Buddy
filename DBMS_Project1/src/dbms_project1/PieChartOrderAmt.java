package dbms_project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.proteanit.sql.DbUtils;

public class PieChartOrderAmt extends Application {

    static String[] str = null;
    public Label label;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("");
        stage.setWidth(500);
        stage.setHeight(500);

        String rid = OwnerPage1.ridd;
        int t1 = 0, t2 = 0, s = 0, z = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmsmini", "root", "mysql");
            String query = "select order_type,sum(bill_amt) from bill where rest_id=? group by order_type";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, String.valueOf(rid));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase("table1")) {
                    t1 = rs.getInt(2);
                }
                if (rs.getString(1).equalsIgnoreCase("table2")) {
                    t2 = rs.getInt(2);
                }
                if (rs.getString(1).equalsIgnoreCase("Swiggy")) {
                    s = rs.getInt(2);
                }
                if (rs.getString(1).equalsIgnoreCase("Zomato")) {
                    z = rs.getInt(2);
                }
            }
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ManagerReg.class.getName()).log(Level.SEVERE, null, ex);
        }

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Swiggy", s),
                        new PieChart.Data("Zomato", z),
                        new PieChart.Data("Table 1", t1), new PieChart.Data("Table 2", t2));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Ordered Via");
        chart.setLabelLineLength(10);
        chart.setLegendSide(Side.LEFT);

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");
        Tooltip container = new Tooltip();
        container.setGraphic(caption);

        chart.getData().forEach((data)
                -> {
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_ENTERED, e
                            -> {
                        if (container.isShowing()) {
                            container.hide();
                        }
                        caption.setText(String.valueOf(data.getPieValue()) + "Rs");
                        container.show(stage, e.getScreenX(), e.getScreenY());
                    });
        });
    }

    public static int fun() {
        launch(str);
        return 0;

    }

    public static void main(String[] args) {
        str = args;
        fun();
    }
}
