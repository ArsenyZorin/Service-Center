package softwarearchs.additional;

import softwarearchs.storage.Repository;
import softwarearchs.user.Client;

import java.util.Date;

/**
 * Created by arseny on 07.04.17.
 */
public class Device {
    private String serialNumber;

    private String deviceType;
    private String deviceBrand;
    private String deviceModel;

    private Date dateOfPurchase;
    private Date warrantyEpiration;
    private Date prevRepair;
    private Date repairWarrantyExpiration;

    private Boolean warrantyAvailable;
    private Boolean repairWarrantyAvailable;

    private Client client;


    public Device(String serialNumber, String deviceType,
                  String deviceBrand, String deviceModel, Client client){
        this.serialNumber = serialNumber;
        this.deviceType = deviceType;
        this.deviceBrand = deviceBrand;
        this.deviceModel = deviceModel;
        this.client = client;
    }

    public Device (String serialNumber){
        this.serialNumber = serialNumber;
    }

    public Device (Device device){

        this.serialNumber = device.serialNumber;
        this.deviceType = device.deviceType;
        this.deviceBrand = device.deviceBrand;
        this.deviceModel = device.deviceModel;

        this.dateOfPurchase = device.dateOfPurchase;
        this.warrantyEpiration = device.warrantyEpiration;
        this.prevRepair = device.prevRepair;
        this.repairWarrantyExpiration = device.repairWarrantyExpiration;

        this.warrantyAvailable = device.warrantyAvailable;
        this.repairWarrantyAvailable = device.repairWarrantyAvailable;

        this.client = device.client;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Date getWarrantyEpiration() {
        return warrantyEpiration;
    }

    public void setWarrantyEpiration(Date warrantyEpiration) {
        this.warrantyEpiration = warrantyEpiration;
    }

    public Date getPrevRepair() {
        return prevRepair;
    }

    public void setPrevRepair(Date prevRepair) {
        this.prevRepair = prevRepair;
    }

    public Date getRepairWarrantyExpiration() {
        return repairWarrantyExpiration;
    }

    public void setRepairWarrantyExpiration(Date repairWarrantyExpiration) {
        this.repairWarrantyExpiration = repairWarrantyExpiration;
    }

    public Boolean getWarrantyAvailable() {
        return warrantyAvailable;
    }

    public void setWarrantyAvailable(Boolean warrantyAvailable) {
        this.warrantyAvailable = warrantyAvailable;
    }

    public Boolean getRepairWarrantyAvailable() {
        return repairWarrantyAvailable;
    }

    public void setRepairWarrantyAvailable(Boolean repairWarrantyAvailable) {
        this.repairWarrantyAvailable = repairWarrantyAvailable;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}


