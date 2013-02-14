package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Member;

@RunWith(value = JMock.class)
public class TestCamera {
    private Mockery context = new Mockery();
    private final Sensor sensor = context.mock(Sensor.class);
    private final MemoryCard memoryCard = context.mock(MemoryCard.class);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        context.checking(new Expectations() {{
            oneOf (sensor).powerUp();
        }});

        Camera camera = getCameraInOffState();
        camera.powerOn();

        context.assertIsSatisfied();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        Camera camera = getCameraInOnState();

        context.checking(new Expectations() {{
            oneOf (sensor).powerDown();
        }});


        camera.powerOff();

    }

    @Test
    public void pressingShutterWithPowerOnCopiesDataFromSensorToMemoryCard() {
        final byte[] sampleImage = new byte[0];

        Camera camera = getCameraInOnState();

        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(sampleImage));

            oneOf(memoryCard).write(sampleImage);
        }});

        camera.pressShutter();

    }



    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing() {
        Camera camera = getCameraInOffState();

        context.checking(new Expectations() {{
          never(sensor);
          never(memoryCard);
        }});


        camera.pressShutter();
    }

    @Test
    public void switchingOffWhenWritingDataDoesNotPowerDownSensor() {
        final byte[] sampleImage = new byte[0];

        Camera camera = getCameraInOnState();


        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(sampleImage));
            oneOf(memoryCard).write(sampleImage);
            never(sensor).powerDown();
        }});

        camera.pressShutter();
        camera.powerOff();
    }

    @Test
    public void powersDownSensorWhenWriteComplete() {
        final byte[] sampleImage = new byte[0];

        Camera camera = getCameraInOnState();


        context.checking(new Expectations() {{
            oneOf(sensor).readData();
            will(returnValue(sampleImage));
            oneOf(memoryCard).write(sampleImage);
            oneOf(sensor).powerDown();
        }});

        camera.pressShutter();
        camera.writeComplete();
        camera.powerOff();
    }

    private Camera getCameraInOnState() {
        context.checking(new Expectations() {{
            allowing (sensor).powerUp();
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOn();

        return camera;
    }

    private Camera getCameraInOffState() {
        context.checking(new Expectations() {{
            allowing (sensor).powerDown();
        }});

        Camera camera = new Camera(sensor, memoryCard);
        camera.powerOff();

        return camera;
    }

}
