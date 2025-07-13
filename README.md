# Embedded Android Summer School (AOSP + RPi)

The purpose of this summer school is to learn about Android System, and why not, learning about how can you interact with it, how to make a basic driver (GPIO) and to code a small app.
<br>

Using a Raspberry 5 Display 2, I learnt about creating a simple *GPIO driver* to control a LED. Creating this driver, the course presented *AIDL* (Android Interface Definition Language) and *HAL* (Hardware Abstraction Layer). It is necessary to create an HAL API to be used by Binder to send the signal from an app to the LED.
<br>

<h3>Driver</h3>
Driver's location is on '/build/interfaces/hardware/gpio'. The package I created is '*android.hardware.gpio*' (service: android.hardware.gpio.IGpio/default: [android.hardware.gpio.IGpio]) (library: libgpiod). The driver has a class/library 'Gpio', having only 2 methods (setGpio & getGpio). The arguments are the port number and the LED state (on/off in numeric -> 1/0). To see the scripts => 'gpio/aidl/default' or just click [here](gpio/aidl/default/Gpio.cpp). For the driver to work, I had to add sepolicy for SELinux's verification.
<br>

<h3>App</h3>
With the new 'vendor', I made a simple app in **Jetlag Compose** & **Kotlin** on which I can control the LED by pressing a button. The button is executing a command line in ADB Shell to run the service (the service is a background operation running the API) if ON and to disconnect the LED if OFF. Script can be seen [here](FirstApp/app/src/main/java/com/aospi/firstapp/MainActivity.kt).
