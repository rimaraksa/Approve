package com.example.rimaraksa.approve.Model;

import java.io.Serializable;

/**
 * Created by rimaraksa on 25/5/15.
 */
public class Account implements Serializable {
    int account_id;
    String name, nric, phone, username, password, profpic, signature;

    public Account(int account_id, String name, String nric, String phone, String username, String password, String profpic, String signature) {
        this.account_id = account_id;
        this.name = name;
        this.nric = nric;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.profpic = profpic;
        this.signature = signature;
    }

    public Account(String name, String nric, String phone, String username, String password, String profpic, String signature) {
        this.name = name;
        this.nric = nric;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.profpic = profpic;
        this.signature = signature;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setNric(String nric){
        this.nric = nric;
    }

    public String getNric(){
        return nric;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return phone;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setProfpic(String profpic) {
        this.profpic = profpic;

    }

    public String getProfpic() {
        return profpic;
    }

    public void setSignature(String signature) {
        this.signature = signature;

    }

    public String getSignature() {
        return signature;
    }


}
