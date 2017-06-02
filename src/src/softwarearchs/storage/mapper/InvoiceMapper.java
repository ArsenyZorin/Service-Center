package softwarearchs.storage.mapper;

import softwarearchs.enums.InvoiceStatus;
import softwarearchs.invoice.Invoice;
import softwarearchs.storage.Gateway;
import softwarearchs.user.Client;
import softwarearchs.user.Receiver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zorin on 23.05.2017.
 */
public class InvoiceMapper {
    private static Set<Invoice> invoices = new HashSet<>();


    public boolean addInvoice(Invoice invoice) throws SQLException{
        String statement = "INSERT INTO invoice VALUES (" + invoice.getInvoiceNumber() +
                ", " + invoice.getInvoiceDate() + ", " + invoice.getReceipt().getReceiptNumber() +
                ", " + invoice.getPrice() + ", " + invoice.getClient().getId() +
                ", " + invoice.getReceiver().getId() + ", " + invoice.getStatus() + ");";

        PreparedStatement insert = Gateway.getGateway().getConnection().prepareStatement(statement);
        insert.executeQuery();
        invoices.add(invoice);
        return true;
    }

    public static Invoice findInvoice(int invoiceNumber) throws SQLException{
        for(Invoice invoice : invoices)
            if(invoiceNumber == invoice.getInvoiceNumber())
                return invoice;

        String statement = "SELECT * FROM invoice WHERE id = \"" + invoiceNumber + "\";";
        PreparedStatement find = Gateway.getGateway().getConnection().prepareStatement(statement);
        find.execute();
        ResultSet rs = find.executeQuery();

        if(!rs.next()) return null;

        Invoice invoice = new Invoice(rs.getInt("id"));
        invoice.setInvoiceDate(rs.getDate("Date"));
        invoice.setReceipt(ReceiptMapper.findReceipt(rs.getInt("Receipt")));
        invoice.setPrice(rs.getDouble("Price"));
        invoice.setClient((Client)UserMapper.findUser(rs.getString("Client")));
        invoice.setReceiver((Receiver)UserMapper.findUser(rs.getString("Receiver")));
        invoice.setStatus(InvoiceStatus.valueOf(rs.getString("Status")));

        invoices.add(invoice);
        return invoice;
    }

}
