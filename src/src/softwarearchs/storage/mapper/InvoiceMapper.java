package softwarearchs.storage.mapper;

import softwarearchs.Main;
import softwarearchs.enums.InvoiceStatus;
import softwarearchs.invoice.Invoice;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Receiver;
import softwarearchs.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class InvoiceMapper {
    private static AbstractMap<String, Invoice> invoices = new HashMap<>();

    public boolean addInvoice(Invoice invoice) throws SQLException{
        String statement = "INSERT INTO invoice VALUES (" + invoice.getInvoiceNumber() +
                ", DATE \'" + Main.stringFromDate(invoice.getInvoiceDate()) +
                "\', " + invoice.getReceipt().getReceiptNumber() +
                ", " + invoice.getPrice() + ", " + invoice.getClient().getId() +
                ", " + invoice.getReceiver().getId() + ", " + invoice.getStatus() + ");";

        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);

        if(!insert.execute()) return false;

        invoices.put(invoice.getInvoiceNumber(), invoice);
        return true;
    }

    public static Invoice findInvoice(String invoiceNumber) throws SQLException{
        if(invoices.containsKey(invoiceNumber))
            return invoices.get(invoiceNumber);

        String statement = "SELECT * FROM invoice WHERE id = \"" + invoiceNumber + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        find.execute();
        ResultSet rs = find.executeQuery();

        if(!rs.next()) return null;

        Invoice invoice = new Invoice(rs.getDate("InvoiceDate"),
                ReceiptMapper.findReceipt(rs.getString("Receipt")));
        invoice.setPrice(rs.getDouble("Price"));
        invoice.setStatus(InvoiceStatus.valueOf(rs.getString("Status")));

        invoices.put(invoice.getInvoiceNumber(), invoice);
        return invoice;
    }

    public static AbstractMap<String, Invoice> findAllInvoices() throws SQLException{
        invoices.clear();
        String query = "SELECT * FROM invoice;";
        Statement statement = Gateway.getGateway().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next()) {
            Invoice invoice = new Invoice(rs.getDate("InvoiceDate"),
                    ReceiptMapper.findReceipt(rs.getString("Receipt")));
            invoice.setPrice(rs.getDouble("Price"));
            invoice.setStatus(InvoiceStatus.valueOf(rs.getString("Status")));
            invoices.put(rs.getString("id"), findInvoice(rs.getString("id")));
        }

        return invoices;
    }

    public static AbstractMap<String, Invoice> findByUser(User user) throws SQLException{
        String className = user.getClass().getSimpleName();
        if("Master".equals(className))
            return null;
        invoices.clear();
        String statement = "SELECT * FROM invoice WHERE " + className + " = " + user.getId();
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        ResultSet rs = find.executeQuery();

        while(rs.next()) {
            Invoice invoice = new Invoice(rs.getDate("InvoiceDate"),
                    ReceiptMapper.findReceipt(rs.getString("Receipt")));
            invoice.setPrice(rs.getDouble("Price"));
            invoice.setStatus(InvoiceStatus.valueOf(rs.getString("Status")));

            invoices.put(invoice.getInvoiceNumber(), invoice);
        }

        return invoices;
    }


}
