# [cite_start]Sistema de Monitoramento e Controle de Prensa Hidráulica [cite: 1] 🏭💧

![Status](https://img.shields.io/badge/Status-Concluído-success)
![Hardware](https://img.shields.io/badge/Hardware-ESP32-blue)
![Backend](https://img.shields.io/badge/Backend-Java%20%7C%20Maven-orange)
![Protocolo](https://img.shields.io/badge/Protocolo-MQTT-yellow)

## 📌 Sobre o Projeto
[cite_start]O presente projeto consiste no desenvolvimento de um cibersistema para o monitoramento de uma prensa hidráulica industrial[cite: 3]. [cite_start]O objetivo é integrar um microcontrolador ESP32 a um software backend desenvolvido em Java (rodando em um servidor externo) para garantir o monitoramento adequado da segurança operacional[cite: 4]. [cite_start]O sistema coleta dados vitais e emprega uma lógica remota de tomada de decisão via protocolo MQTT para acionamento de alertas em tempo real[cite: 5].

## 🏗️ Arquitetura de Dados e Tecnologias
[cite_start]Para uma integração adequada, o sistema engloba Programação Orientada a Objetos no backend e Programação Estruturada no firmware[cite: 6].
* [cite_start]**Microcontrolador**: ESP32[cite: 4].
* [cite_start]**Backend**: Java com gerenciamento de dependências via Maven[cite: 82].
* [cite_start]**Protocolo**: MQTT (Message Queuing Telemetry Transport)[cite: 7].
* [cite_start]**Broker MQTT**: HiveMQ Cloud (`ab7bca8a88fb429ea9c6e193eb502776.s1.eu.hivemq.cloud`), Porta `8883`[cite: 7].
* [cite_start]**Tópico de Comunicação**: `senai/claudio/motor/dados`[cite: 7].
* [cite_start]**Formato do Payload**: Os dados de leitura chegam ao backend organizados em formato JSON[cite: 104, 105]. [cite_start]Exemplo: `{"Temperatura":24.00, "Pressao":8.00, "Corrente":32.00}`[cite: 7].

## 🛠️ Hardware Utilizado (Ambiente Simulado)
[cite_start]O circuito foi prototipado para garantir a integridade dos sinais analógicos e digitais[cite: 10]. Foram utilizados:
* [cite_start]**ESP32**: Microcontrolador base com módulo Wi-Fi[cite: 164].
* [cite_start]**Sensor DHT22**: Para a leitura da temperatura operacional[cite: 8].
* [cite_start]**Potenciômetros (2x)**: Para simulação do transdutor de pressão e do sensor de corrente[cite: 8, 9].
* [cite_start]**Display LCD 16x2 I2C**: Para exibição da telemetria e de mensagens locais[cite: 9].
* [cite_start]**Sistema de LEDs**: Verde (Operação Segura), Amarelo (Alerta de Faixa), Vermelho (Estado Crítico/Parada) e Verde Secundário (Status de envio de dados)[cite: 10, 11].

## 💻 Estrutura do Repositório (Monorepo)
[cite_start]O projeto unifica dois ecossistemas em um único repositório GitHub[cite: 122]:

* **`simulador-wokwi/`**: Contém o firmware em C++. [cite_start]Esta área foi estruturada para uso no VS Code usando a extensão do **PlatformIO**, que compila o código e gerencia as bibliotecas, em conjunto com a extensão local do **Wokwi**[cite: 108, 109].
* [cite_start]**`backend-maven/`**: Contém o software receptor Java, estruturado pelo Maven (`pom.xml`) sob o `groupId` *br.com.senai.automacao* e o `artifactId` *press-mqtt-collector*[cite: 86, 91].

## 🚀 Como Executar o Projeto

Para executar a solução de forma fluida, siga os passos referentes aos diferentes ambientes de execução.

### 1. Inicializando o Hardware (ESP32 via PlatformIO + Wokwi)
[cite_start]Devido à instabilidade dos servidores online, a simulação foi adaptada para rodar de forma confiável no seu próprio computador[cite: 107].
1. No VS Code (no ambiente Windows), vá em **File > Open Folder...** e selecione **exclusivamente a pasta `simulador-wokwi`**. [cite_start]Isso é crucial para o PlatformIO identificar corretamente o ambiente[cite: 108].
2. [cite_start]Aguarde o PlatformIO carregar as dependências[cite: 108].
3. [cite_start]Abra o arquivo de simulação do circuito pelo Wokwi local[cite: 109].
4. [cite_start]O ESP32 iniciará a conexão na rede virtual (`Wokwi-GUEST`) e enviará o payload via MQTT[cite: 43, 76].

### 2. Inicializando o Backend (Java via Maven)
1. Abra um terminal do ambiente Linux (WSL).
2. [cite_start]Navegue até o diretório onde encontra-se a base da arquitetura do Maven (o arquivo `pom.xml`)[cite: 89, 90]. Exemplo: `cd caminho/para/backend-maven/demo`.
3. [cite_start]Execute o seguinte comando do Maven para limpar o ambiente, compilar o código mais recente e executar a classe principal (`App.java`)[cite: 88, 89]:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="br.com.senai.automacao.App"