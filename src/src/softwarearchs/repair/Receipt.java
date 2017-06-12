package softwarearchs.repair;

import softwarearchs.enums.ReceiptStatus;
import softwarearchs.enums.RepairType;
import softwarearchs.storage.Repository;
import softwarearchs.user.Client;
import softwarearchs.user.Master;
import softwarearchs.user.Receiver;

import java.util.Date;

/**
 * Created by arseny on 07.04.17.
 */
public class Receipt {
    private String receiptNumber;
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
        this.receiptDate = new Date();
    }

    public Receipt(String receiptNumber, Date receiptDate, RepairType repairType, Device device, Client client, Receiver receiver, String malfuncDescr){
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.repairType = repairType;
        this.device = device;
        this.client = client;
        this.malfuncDescr = malfuncDescr;
        this.receiver = receiver;
    }

    public String getReceiptNumber() { return receiptNumber; }

    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

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

    /**
     * Добавление квитанции в базу
     * @return Результат операции
     */
    public boolean addReceipt(){
        return (new Repository()).addReceipt(this);
    }

    public boolean updateReceipt() { return (new Repository()).updateReceipt(this); }

    public Receipt findReceipt() { return (new Repository()).findReceipt(this.receiptNumber); }
}
