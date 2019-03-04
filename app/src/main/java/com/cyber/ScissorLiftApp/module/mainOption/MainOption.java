package com.cyber.ScissorLiftApp.module.mainOption;

public class MainOption {
    private int ImageId;
    private String Name;
    private int Id;
    public MainOption(int imageId, String name ,int id) {
        ImageId = imageId;
        Name = name;
        Id=id;
    }

    public int getId() {
        return Id;
    }

    public int getImageId() {
        return ImageId;
    }

    public String getName() {
        return Name;
    }
}
