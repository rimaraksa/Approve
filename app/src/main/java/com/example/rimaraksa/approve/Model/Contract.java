package com.example.rimaraksa.approve.Model;

import com.example.rimaraksa.approve.Global;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rimaraksa on 4/6/15.
 */
public class Contract implements Serializable {

    int contract_id;
    String contractKey,sender, receiver, subject, body, location, status;
    String dateRequest;
    String dateAppOrReject;
    String video;
    String reasonForRejection;

    public Contract(String contractKey, String sender, String receiver, String subject, String body, String location, String status, String dateRequest, String dateAppOrReject, String video, String reasonForRejection) {
        this.contractKey = contractKey;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.location = location;
        this.status = status;
        this.dateRequest = dateRequest;
        this.dateAppOrReject = dateAppOrReject;
        this.video = video;
        this.reasonForRejection = reasonForRejection;
    }

    public void setContractKey(String contractKey) {
        this.contractKey = contractKey;
    }

    public String getContractKey() {
        return contractKey;
    }

    public void setContract_id(int account_id) {
        this.contract_id = account_id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getSender(){
        return sender;
    }
    
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    public String getReceiver(){
        return receiver;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return body;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    public void setStatus(String status){
        this.status = status;

        if(!status.equals("waiting")){
            setDateAppOrReject(Global.getDateTime());
        }
    }

    public String getStatus(){
        return status;
    }

    public void setDateAppOrReject(String dateAppOrReject) {
        this.dateAppOrReject = dateAppOrReject;
    }

    public String getDateAppOrReject() {
        return dateAppOrReject;
    }

    public void setDateRequest(String dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getDateRequest() {
        return dateRequest;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }
}
