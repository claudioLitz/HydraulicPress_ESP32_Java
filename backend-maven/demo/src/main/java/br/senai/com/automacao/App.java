package br.com.senai.automacao;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//Executar java mvn compile exec:java -Dexec.mainClass="br.com.senai.automacao.App"

public class App {
    public static void main(String[] args) {
        //defining broker address, topic (way that file will be) and clientid (Just the name of my device)
        String broker = "ssl://ab7bca8a88fb429ea9c6e193eb502776.s1.eu.hivemq.cloud:8883";
        String topic = "senai/claudio/motor/dados";
        String clientId = "Java_Receiver_Client";

     
        try {
            // 1. Create client
            MqttClient sampleClient = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            // Add user and password
            options.setUserName("claudio_lit");
            options.setPassword("senhaForte3".toCharArray());

            // 2. Connect
            System.out.println("Conectando ao broker: " + broker);
            sampleClient.connect(options);
            System.out.println("Conectado!");

            // 3. Config Callback (What the system do after the message arrive)
            // Callback is a fuction that run in background, tracking the progress and decide what it should do in this situations (connectionLost, messageArrived and deliveryComplete)
            sampleClient.setCallback(new MqttCallback() {

                // Method to warn "Connection lost"
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                // Show the message sent by ESP32
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("------------------------------------------------");
                    System.out.println("DADO RECEBIDO DO ESP32:");
                    System.out.println("Tópico: " + topic);
                    System.out.println("Mensagem: " + payload);
                    
        
                }

                // Method to warn when the data get on ESP32
                // Not used yet because the system just receive data
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            // 4. Sing in right topic (senai/claudio/motor/dados)
            sampleClient.subscribe(topic);
            System.out.println("Ouvindo o tópico: " + topic);

        // If happen an error, print in terminal
        } catch (MqttException me) {
            System.out.println("razão " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            me.printStackTrace();
        }
    }
}