package com.example.rimaraksa.approve.Model;

import com.example.rimaraksa.approve.Util;

import java.io.Serializable;

/**
 * Created by rimaraksa on 4/6/15.
 */
public class Contract implements Serializable {

    int contract_id, sender_id, receiver_id;
    String contractKey, subject, body, location, status;
    String dateRequest;
    String dateAppOrReject;
    String video;
    String reasonForRejection;

    public Contract(int contract_id, String contractKey, int sender_id, int receiver_id, String subject, String body, String location, String status, String dateRequest, String dateAppOrReject, String video, String reasonForRejection) {
        this.contract_id = contract_id;
        this.contractKey = contractKey;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.subject = subject;
        this.body = body;
        this.location = location;
        this.status = status;
        this.dateRequest = dateRequest;
        this.dateAppOrReject = dateAppOrReject;
        this.video = video;
        this.reasonForRejection = reasonForRejection;
    }

    public Contract(String contractKey, int sender_id, int receiver_id, String subject, String body, String location, String status, String dateRequest, String dateAppOrReject, String video, String reasonForRejection) {
        this.contractKey = contractKey;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
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

    public void setSender_id(int sender_id){
        this.sender_id = sender_id;
    }

    public int getSender_id(){
        return sender_id;
    }
    
    public void setReceiver_id(int receiver){
        this.receiver_id = receiver_id;
    }

    public int getReceiver_id(){
        return receiver_id;
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

        if(!status.equals("pending")){
            setDateAppOrReject(Util.getDateTime());
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
