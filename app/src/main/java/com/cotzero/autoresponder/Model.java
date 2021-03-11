package com.cotzero.autoresponder;

import java.util.List;
import java.util.Random;

public class Model{

    int id;
    List<String> receiveMsgs;
    List<String> replies;
    List<Integer> messenger;
    List<Integer> receiverType;
    List<String> spContact;
    List<String> spIgnore;

    public Model(List<String> receiveMsgs, List<String> replies, List<Integer> messenger, List<Integer> receiverType, List<String> spContact, List<String> spIgnore) {

        Random rand = new Random();
        int rand_int1 = rand.nextInt(999999999);

        this.id = rand_int1;
        this.receiveMsgs = receiveMsgs;
        this.replies = replies;
        this.messenger = messenger;
        this.receiverType = receiverType;
        this.spContact = spContact;
        this.spIgnore = spIgnore;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getReceiveMsgs() {
        return receiveMsgs;
    }

    public void setReceiveMsgs(List<String> receiveMsgs) {
        this.receiveMsgs = receiveMsgs;
    }

    public List<String> getReplies() {
        return replies;
    }

    public void setReplies(List<String> replies) {
        this.replies = replies;
    }

    public List<Integer> getMessenger() {
        return messenger;
    }

    public void setMessenger(List<Integer> messenger) {
        this.messenger = messenger;
    }

    public List<Integer> getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(List<Integer> receiverType) {
        this.receiverType = receiverType;
    }

    public List<String> getSpContact() {
        return spContact;
    }

    public void setSpContact(List<String> spContact) {
        this.spContact = spContact;
    }

    public List<String> getSpIgnore() {
        return spIgnore;
    }

    public void setSpIgnore(List<String> spIgnore) {
        this.spIgnore = spIgnore;
    }
}
