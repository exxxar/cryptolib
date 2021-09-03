/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.components;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author SAMS
 */
public class Connections {

    public List<ConnectionObject> connections;

    public Connections() {
        this.connections = new LinkedList<>();
    }

    public boolean isExist(String devicePublicId) {
        boolean isExist = false;
        for (ConnectionObject item : connections) {
            if (item.getDevicePublicId().equals(devicePublicId)) {
                isExist = true;
                break;
            }
        }

        return isExist;
    }

    public ConnectionObject get(String devicePublicId) {
        ConnectionObject co = null;

        for (ConnectionObject item : connections) {
            if (item.getDevicePublicId().equals(devicePublicId)) {
                co = item;
                break;
            }
        }

        if (co == null) {
            co = new ConnectionObject(devicePublicId);
        }

        return co;
    }

    public int getIndex(String devicePublicId) {
        ConnectionObject co = null;

        int index = 0;
        for (ConnectionObject item : connections) {
            index++;
            if (item.getDevicePublicId().equals(devicePublicId)) {
                return index;
            }
        }

        return -1;
    }

    public void update(ConnectionObject co) {
        connections.set(getIndex(co.getDevicePublicId()), co);
    }

    public void put(ConnectionObject co) {
        if (!isExist(co.getDevicePublicId())) {
            connections.add(co);
        }
    }

    public void putOrUpdate(ConnectionObject co) {
        if (!isExist(co.getDevicePublicId())) {
            connections.add(co);
        } else {
            update(co);
        }
    }

    public List<ConnectionObject> getConnections() {
        return connections;
    }

    public void setConnections(List<ConnectionObject> connections) {
        this.connections = connections;
    }
}
