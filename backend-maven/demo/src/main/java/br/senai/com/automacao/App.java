package br.senai.com.automacao;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

// Executar: mvn compile exec:java -Dexec.mainClass="br.senai.com.automacao.App"

public class App {
    public static void main(String[] args) {
        // defining broker address, topic (way that file will be) and clientid (Just the name of my device)
        String broker = "ssl://ab7bca8a88fb429ea9c6e193eb502776.s1.eu.hivemq.cloud:8883";
        String topicData = "senai/claudio/motor/dados"; // Topic to receive message by ESP32
        String topicBack = "senai/claudio/motor/comandos"; // Topic to send message
        String clientId = "Java_Receiver_Client";

        try {
            // 1. Create client
            final MqttClient sampleClient = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            // Add user and password
            options.setUserName("claudio_litz");
            options.setPassword("senhaForte123".toCharArray());

            // 2. Connect
            System.out.println("Conectando ao broker: " + broker);
            sampleClient.connect(options);
            System.out.println("Conectado!");

            // 3. Config Callback (What the system do after the message arrive)
            sampleClient.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("------------------------------------------------");
                    System.out.println("DADO RECEBIDO DO ESP32:");
                    System.out.println("Tópico: " + topic);
                    System.out.println("Mensagem: " + payload);

                    try {
                        // Extrai o valor da Pressão da string JSON
                        String[] partes = payload.split("\"Pressao\":");
                        
                        if (partes.length > 1) {
                            // Pega o que vem depois de "Pressao":, remove chaves do JSON e corta na vírgula
                            String pressaoStr = partes[1].split(",")[0].replace("}", "").trim(); 
                            // Converte o texto para número Decimal
                            float pressao = Float.parseFloat(pressaoStr); 
                            
                            System.out.println("Pressão identificada: " + pressao);
                            
                            String comando = "VERDE"; // Padrão
                            
                            if (pressao <= 20) {
                                comando = "VERDE";
                            } else if (pressao > 20 && pressao <= 70) {
                                comando = "AMARELO";
                            } else {
                                comando = "PROBLEMA"; // Acima de 70
                            }

                            // Transformamos em "final" para poder enviar para a Thread paralela
                            final String comandoFinal = comando; 

                            // ==========================================
                            // SOLUÇÃO: CRIAR UMA THREAD PARA NÃO TRAVAR O CALLBACK
                            // ==========================================
                            new Thread(() -> {
                                try {
                                    MqttMessage cmdMessage = new MqttMessage(comandoFinal.getBytes());
                                    cmdMessage.setQos(0);
                                    
                                    sampleClient.publish(topicBack, cmdMessage);
                                    System.out.println("Comando enviado para o ESP32: " + comandoFinal);
                                } catch (Exception e) {
                                    System.out.println("Erro ao enviar comando: " + e.getMessage());
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao processar os dados: " + e.getMessage());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Method to warn when the data get on ESP32
                }
            });

            // 4. Sign in right topic
            sampleClient.subscribe(topicData);
            System.out.println("Ouvindo o tópico: " + topicData);

        } catch (MqttException me) {
            System.out.println("razão " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            me.printStackTrace();
        }
    }
}