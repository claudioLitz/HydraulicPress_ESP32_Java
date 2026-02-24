#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "DHT.h" // Comunicação com o Sensor SHT22
#include <WiFiClientSecure.h>
#include <LiquidCrystal_I2C.h> // Comunicação com o display LCD


// Definindo pinos do LCD
#define lcd_sda 21  
#define lcd_scl 22

// Pinos dos led
#define Gled 4
#define Rled 13
#define Yled 17
#define SENDled 12

// Pinos dos sensores
#define DHTPIN 14
#define DHTTYPE DHT22
#define POTPIN 35
#define CORPIN 34

// --- Configurações de Rede (Wokwi) ---
const char* ssid = "Wokwi-GUEST"; // wifi simulado
const char* password = "";

// --- Configurações MQTT (Broker Público) ---
const char* mqtt_server = "ab7bca8a88fb429ea9c6e193eb502776.s1.eu.hivemq.cloud"; // Broker público gratuito
const int mqtt_port = 8883;
const char* mqtt_user = "claudio_litz";
const char* mqtt_pass = "senhaForte123";
const char* mqtt_topic = "senai/claudio/motor/dados";
const char* mqtt_topic_cmd = "senai/claudio/motor/comandos"; // Topic for callback

// Cria o objeto 'dht' para controlar o sensor
DHT dht(DHTPIN, DHTTYPE);

// Gerente do túnel TCP
WiFiClientSecure espClient;

// Tradutor MQTT
PubSubClient client(espClient);
LiquidCrystal_I2C lcd(0x27, 16 , 2);

String comandoAtual = "INICIANDO"; // Global variable to callback


// --- Fuction callback (receive data from java) ---
void callback(char* topic, byte* payload, unsigned int length){ 
  String message = ""; 
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  Serial.println("Comando recebido: " + message);
  
  // Salva o comando recebido para o loop() usar no LCD
  comandoAtual = message; 

  // ATIVAÇÃO DOS LEDS
  digitalWrite(Gled, message == "VERDE" ? HIGH:LOW); 
  digitalWrite(Yled, message == "AMARELO" ? HIGH:LOW); 
  digitalWrite(Rled, message == "PROBLEMA" ? HIGH:LOW); 
}


// --- Função para conectar a rede ---
void setup_wifi() {
  Serial.print("\nConectando em ");
  Serial.println(ssid);

  lcd.setCursor(0,0);
  lcd.print("Conectando em ");
  lcd.setCursor(0,1);
  lcd.print(ssid);

  // Conecta a rede local
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password); 

  // Espera dar o retorno do sinal conectado
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  // Indica conexão com o WiFi Local
  Serial.println("");
  Serial.println("WiFi conectado");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}


void connect() {
  // Loop até conectar a rede MQTT 
  while (!client.connected()) {
    Serial.print("Tentando conexão MQTT...");

    lcd.setCursor(0,0);
    lcd.print("Tentando conexao");
    lcd.setCursor(0,1);
    lcd.print("MQTT...         "); // Espaços para limpar sujeira

    if (client.connect("ESP32_ClienteID_Claudio", mqtt_user, mqtt_pass)) {
      Serial.println("conectado");
      client.subscribe(mqtt_topic_cmd); // Connecting to callback topic
    
    } else {
      Serial.print("falhou, rc=");
      Serial.print(client.state());
      Serial.println(" tentando novamente em 5 segundos");
      delay(5000);
    }
  }
}


void setup() {
  randomSeed(analogRead(0));
  Serial.begin(115200); // Start terminal
  dht.begin(); // Start DHT sensor
  lcd.init(); // Start lcd 
  lcd.backlight(); // Turn on the backlight of lcd

  // Leds mode define
  pinMode(Gled, OUTPUT);
  pinMode(Rled, OUTPUT);
  pinMode(Yled, OUTPUT);
  pinMode(SENDled, OUTPUT);
  
  // Fuction to Connect with local WiFi
  setup_wifi(); 

  // Diz ao cliente Wi-Fi para criptografar os dados
  espClient.setInsecure();
  client.setServer(mqtt_server, mqtt_port);

  // Ativa a função em segundo plano para mostrar tudo o que retornar
  client.setCallback(callback);

  // Test lcd scream
  lcd.setCursor(0,0);
  lcd.print("-Sistema ligado-");
  lcd.setCursor(0,1);
  lcd.print("Teste Hardware  ");
  
  int NRled;
  int NYled;
  int NGled;
  
  // Loop for test LEDs
  for (int i = 0; i < 17;i++){
    NGled = random(100);
    digitalWrite(Gled, (NGled % 2) == 0 ? HIGH : LOW);

    NYled = random(100);
    digitalWrite(Yled, (NYled % 2) == 0 ? HIGH : LOW);
    
    NRled = random(100);
    digitalWrite(Rled, (NRled % 2) == 0 ? HIGH : LOW);

    delay(120);
  }

  digitalWrite(Rled, LOW);
  digitalWrite(Gled, LOW);
  digitalWrite(Yled, LOW);

  // Limpa a tela para segurança
  lcd.clear();
}


void loop() {
  if (!client.connected()) {
    connect();
  }
  client.loop(); // Method to maintain connected in broker

  // Turn off green led that responsible to send message
  digitalWrite(SENDled, LOW);

  // ---Read Sensors---
  float temp = dht.readTemperature();
  float press = map(analogRead(POTPIN),0,4095,0.0,100.0);
  float corr = map(analogRead(CORPIN),0,4095,0.0,100.0);

  // ---Creating the payload---
  String payload = "{\"Temperatura\":";
  payload += String(temp);
  payload += ", \"Pressao\":";
  payload += String(press);
  payload += ", \"Corrente\":";
  payload += String(corr);
  payload += "}";

  // ---publishing the datas---
  Serial.println("\nPublicando...");
  client.publish(mqtt_topic, payload.c_str());
  Serial.println("Publicado: " + payload);
  digitalWrite(SENDled,HIGH);

  // ==========================================
  // --- EXIBIÇÃO DE DADOS NO LCD ---
  // ==========================================
  if (comandoAtual == "PROBLEMA") {
    // Modo Alerta: Sobrepõe tudo na tela
    lcd.setCursor(0,0);
    lcd.print("!!! ALERTA !!!  "); 
    
    lcd.setCursor(0,1);
    lcd.print("STATUS: PROBLEMA"); 
  } else {
    // Modo Normal: Exibe os 3 sensores e esconde o status
    // Linha 1: "T:25.3C P:100.0 " (Exatos 16 caracteres com o espaço no final)
    lcd.setCursor(0,0);
    lcd.print("T:" + String(temp, 1) + "C P:" + String(press, 1) + " ");
    
    // Linha 2: "Corr:45.5A      " (Preenche com espaços para apagar a palavra "PROBLEMA")
    lcd.setCursor(0,1);
    lcd.print("Corr:" + String(corr, 1) + "A      ");
  }

  delay(3000);
}