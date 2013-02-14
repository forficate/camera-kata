package com.develogical.camera;

public class Camera implements WriteListener {

    private Sensor sensor;
    private MemoryCard memoryCard;
    private boolean hasPower = false;
    private boolean writing = false;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if(hasPower) {
            writing = true;
            memoryCard.write(sensor.readData());
        }
    }

    public void powerOn() {
        sensor.powerUp();
        hasPower = true;
    }

    public void powerOff() {
       if (!writing) {
            sensor.powerDown( );
            hasPower = false;
       }
    }


    @Override
    public void writeComplete() {
        writing = false;
    }
}

