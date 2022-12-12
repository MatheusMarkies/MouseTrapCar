#include "SoftwareSerial.h"
#include <TimerOne.h>

// Motor encoder output pulses per 360 degree revolution (measured manually)
#define ENC_COUNT_REV 500
#define AXLE_RELATION 0.75

volatile long encoder_value = 0;

// One-second interval for measurements
int interval = 125;

// Counters for milliseconds during interval
long previousMillis = 0;
long currentMillis = 0;

// Variable for RPM measuerment
float rpm = 0;
float distance =0;

// Variable for angular velocity measurement
float angular_velocity = 0;
float angular_velocity_deg = 0;

const float rpm_to_radians = 0.10471975512;
const float rad_to_deg = 57.29578;

int DT_pin = 2;
int CLK_pin = 3;

SoftwareSerial XBee(0,1);

void setup() {
  pinMode(DT_pin , INPUT_PULLUP);
  pinMode(CLK_pin , INPUT);

  Serial.begin(9600);
  XBee.begin(9600);

  // Every time the pin goes high, this is a pulse
  attachInterrupt(digitalPinToInterrupt(DT_pin), interrupt_on_pulse, RISING);

}

void loop() {

  // Record the time
  currentMillis = millis();

  // If one second has passed, print the number of pulses
  if (currentMillis - previousMillis > interval) {

    previousMillis = currentMillis;

    // Calculate revolutions per minute
    rpm = (float)(encoder_value * 60 / (ENC_COUNT_REV * interval));
    angular_velocity = rpm * rpm_to_radians;
    angular_velocity_deg = angular_velocity * rad_to_deg;
    distance = (float)(encoder_value * 360 / ENC_COUNT_REV);

    Serial.print(" Pulses: ");
    Serial.println(encoder_value);
    Serial.print(" Speed: ");
    Serial.print(rpm);
    Serial.println(" RPM");
    XBee.println("R:");
    XBee.println(encoder_value);
    //XBee.println("D:");
    //XBee.println(distance);
    Serial.print(" Distance: ");
    Serial.println(distance);
    Serial.print(" Angular Velocity: ");
    Serial.print(angular_velocity_deg);
    Serial.println(" deg per second");
    Serial.println();

    encoder_value = 0;
  }
}

// Increment the number of pulses by 1
void interrupt_on_pulse() {

  // Read the value for the encoder for the right wheel
  int val = digitalRead(CLK_pin);

  if(val == LOW) {
    encoder_value++;
  }
  else {
    encoder_value--;
  }
}