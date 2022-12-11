#include "SoftwareSerial.h"
#include <TimerOne.h>

int t_interrup = 30000 ;// Tempo de 1 microsegundo
int decoderRead = 0;
float revolutionCont = 0; //Variação do contador

int resolution = 500;

SoftwareSerial XBee(0,1);

int bit15 = LOW;
int bit14 = LOW;
int bit13 = LOW;
int bit12 = LOW;
int bit11 = LOW;
int bit10 = LOW;
int bit09 = LOW;
int bit08 = LOW;
int bit07 = LOW;
int bit06 = LOW;
int bit05 = LOW;
int bit04 = LOW;
int bit03 = LOW;
int bit02 = LOW;
int bit01 = LOW;
int bit00 = LOW;

int OE = 5 ; // Bit OE setado na porta 5 do Arduino
int Sel = 4 ; // Bit Sel setado na porta 4 do Arduino
int RST = 3 ; // bit RST porta 3

int port13 = 13;
int port12 = 12;
int port11 = 11;

int port10 = 10;
int port09 = 9;
int port08 = 8;
int port07 = 7;
int port06 = 6;

void setup()
{
 // put your setup code here, to run once:
 Serial.begin(9600); //XBee pins 0 e 1
 Timer1.initialize(t_interrup);
 Timer1.attachInterrupt(readEncoder);

 XBee.begin(9600);

 pinMode(RST,OUTPUT); // Pino Reset
 pinMode(OE,OUTPUT); //Pino OE
 pinMode(Sel,OUTPUT); //Pino Sel
 pinMode(6,INPUT); //Bit 00
 pinMode(7,INPUT); //Bit 01
 pinMode(8,INPUT); //Bit 02
 pinMode(9,INPUT); //Bit 03
 pinMode(10,INPUT); //Bit 04
 pinMode(11,INPUT); //Bit 05
 pinMode(12,INPUT); //Bit 06
 pinMode(13,INPUT); //Bit 07

digitalWrite(OE,HIGH); //Inicia entrada de dados [TRIST STATE]
digitalWrite(Sel,HIGH); //Lendo High Byte
}

void loop(){}

void readEncoder()
{
 digitalWrite(OE,LOW); //Inicia Leitura
 digitalWrite(Sel,LOW); //Lendo High Byte

 bit15 = digitalRead(port13);
 bit14 = digitalRead(port12);
 bit13 = digitalRead(port11);
 bit12 = digitalRead(port10);
 bit11 = digitalRead(port09);
 bit10 = digitalRead(port08);
 bit09 = digitalRead(port07);
 bit08 = digitalRead(port06);

 digitalWrite(Sel,HIGH); //Lendo LOW Byte

 bit07 = digitalRead(port13);
 bit06 = digitalRead(port12);
 bit05 = digitalRead(port11);
 bit04 = digitalRead(port10);
 bit03 = digitalRead(port09);
 bit02 = digitalRead(port08);
 bit01 = digitalRead(port07);
 bit00 = digitalRead(port06);

 if(digitalRead(2))
  digitalWrite(2,LOW);
 else
  digitalWrite(2,HIGH);

digitalWrite(OE,HIGH);// Encerrar leitura
digitalWrite(RST,LOW); // Reseta o contador de posição
//delay (1);
digitalWrite(RST,HIGH); // Contador não resetado
// ----------------------------------------------
 decoderRead = 0 ; // Zerar a variavel de contagem da posição de leitura no encoder

 // concatenação dos bits
 decoderRead = decoderRead | (bit00 << 0 ); // bitXX vai para a posição XX e depois é somado
 decoderRead = decoderRead | (bit01 << 1 );
 decoderRead = decoderRead | (bit02 << 2 );
 decoderRead = decoderRead | (bit03 << 3 );
 decoderRead = decoderRead | (bit04 << 4 );
 decoderRead = decoderRead | (bit05 << 5 );
 decoderRead = decoderRead | (bit06 << 6 );
 decoderRead = decoderRead | (bit07 << 7 );
 decoderRead = decoderRead | (bit08 << 8 );
 decoderRead = decoderRead | (bit09 << 9 );
 decoderRead = decoderRead | (bit10 << 10 );
 decoderRead = decoderRead | (bit11 << 11 );
 decoderRead = decoderRead | (bit12 << 12 );
 decoderRead = decoderRead | (bit13 << 13 );
 decoderRead = decoderRead | (bit14 << 14 );
 decoderRead = decoderRead | (bit15 << 15 );

 revolutionCont = ((decoderRead));//2(3.14)*Raio)/(16*t_interrup));

 if (revolutionCont <= 32768) // ate metade do contador a velocidade é positiva
  revolutionCont = (decoderRead);
 else
  revolutionCont = (revolutionCont - 65535 ); // maximo do contador 2 elevado a

 //if(revolutionCont != 0){
 Serial.println(revolutionCont/resolution);
  XBee.println("R:");
 XBee.println(revolutionCont/resolution);
 //}

}