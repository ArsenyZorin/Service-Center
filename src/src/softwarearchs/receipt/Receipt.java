package softwarearchs.receipt;

import softwarearchs.additional.Device;
import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;

import java.util.Date;

/**
 * Created by arseny on 07.04.17.
 */
public class Receipt {
    private static int receiptNumber = 0;
    private Date receiptDate;
    private RepairType repairType;
    private Device device;
    private Client client;
    private String malfuncDescr;
    private String note;
    private Master master;
    private ReceiptStatus status;
    private Receiver receiver;

    public Receipt (){
        this.receiptNumber ++;
        this.receiptDate = new Date();
    }

    public static int getReceiptNumber() {
        return receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public RepairType getRepairType() {
        return repairType;
    }

    public void setRepairType(RepairType repairType) {
        this.repairType = repairType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getMalfuncDescr() {
        return malfuncDescr;
    }

    public void setMalfuncDescr(String malfuncDescr) {
        this.malfuncDescr = malfuncDescr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public ReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiptStatus status) {
        this.status = status;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

}