package com.example.recycli11;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ronalyn nanong
 * Item constructor
 */
public class ItemModel {

    public String barcode, name, brand, weight, lid, container, label, image;

    public ItemModel() {
    }

    public ItemModel(String barcode, String name, String brand, String weight, String lid, String container, String label, String image) {
        this.barcode = barcode;
        this.name = name;
        this.brand = brand;
        this.weight = weight;
        this.lid = lid;
        this.container = container;
        this.label = label;
        this.image = image;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("brand", brand);
        result.put("weight", weight);
        result.put("lid", lid);
        result.put("container", container);
        result.put("label", label);
        return result;
    }
}
