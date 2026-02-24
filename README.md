# Sistema de Monitoramento e Controle de Prensa Hidráulica 🏭💧

![Status](https://img.shields.io/badge/Status-Concluído-success)
![Hardware](https://img.shields.io/badge/Hardware-ESP32-blue)
![Backend](https://img.shields.io/badge/Backend-Java%20%7C%20Maven-orange)
![Protocolo](https://img.shields.io/badge/Protocolo-MQTT-yellow)

## 📌 Sobre o Projeto
Este projeto apresenta o desenvolvimento de um cibersistema para o monitoramento de uma prensa hidráulica industrial. O objetivo é integrar um microcontrolador ESP32 a um software backend desenvolvido em Java, garantindo o monitoramento adequado para a segurança operacional da máquina. 

O sistema coleta dados de temperatura, pressão e corrente da prensa hidráulica de forma simulada e utiliza uma lógica de tomada de decisão remota via protocolo MQTT, acionando alertas visuais (LEDs) e mensagens no display em tempo real.

## 🏗️ Arquitetura de Dados e Tecnologias
O projeto unifica dois ecossistemas distintos no mesmo repositório (Monorepo), integrando Programação Estruturada (C++) e Programação Orientada a Objetos (Java).

* **Microcontrolador:** ESP32 (Firmware em C++)
* **Backend:** Java (Gerenciamento de dependências via Maven)
* **Protocolo de Comunicação:** MQTT (Message Queuing Telemetry Transport)
* **Broker MQTT:** HiveMQ Cloud (ab7bca8a88fb429ea9c6e193eb502776.s1.eu.hivemq.cloud, Porta: 8883)
* **Tópico MQTT:** senai/claudio/motor/dados
* **Formato do Payload:** JSON (Exemplo: {"Temperatura":24.00, "Pressao":8.00, "Corrente":32.00})

## 🛠️ Hardware Utilizado (Ambiente Simulado)
Devido à necessidade de manter a estabilidade para os testes, o hardware foi projetado no simulador Wokwi rodando localmente. Os componentes incluem:
* **ESP32:** Microcontrolador principal com Wi-Fi.
* **Sensor DHT22:** Leitura da temperatura.
* **Potenciômetro 1:** Simulação do transdutor de pressão (0 a 100%).
* **Potenciômetro 2:** Simulação do sensor de corrente (0 a 100A).
* **Display LCD 16x2 I2C:** Exibição local das informações de telemetria.
* **LEDs Indicadores:**
  * 🟢 Verde (GPIO 4): Operação Segura.
  * 🟡 Amarelo (GPIO 17): Alerta de Faixa.
  * 🔴 Vermelho (GPIO 13): Estado Crítico / Parada.
  * 🟢 Verde 2 (GPIO 12): Indicador de envio de dados MQTT.

## 💻 Estrutura do Repositório
O repositório está dividido em duas pastas principais:
* `simulador-wokwi/`: Contém o código C++ e a simulação do circuito. Deve ser aberto via VS Code com a extensão PlatformIO.
* `backend-maven/`: Contém o código Java do receptor MQTT. Estruturado com pom.xml para execução via terminal.

## 🚀 Como Executar o Projeto

### 1. Inicializando o Hardware (ESP32 / Wokwi)
1. Abra o VS Code.
2. Vá em **File > Open Folder...** e selecione **apenas a pasta simulador-wokwi** (não abra a raiz do repositório).
3. Aguarde o PlatformIO instalar as dependências.
4. Abra o arquivo json do circuito no Wokwi e inicie a simulação. O ESP32 conectará no Wi-Fi virtual e iniciará o envio de dados.

### 2. Inicializando o Backend (Java / Maven)
1. Abra o terminal do seu ambiente Linux (ou WSL, caso use Windows).
2. Navegue até o diretório do projeto Java (onde está o arquivo pom.xml), executando o comando:
   `cd backend-maven/demo`
3. Execute o comando abaixo para compilar e iniciar o sistema:
   `mvn clean compile exec:java -Dexec.mainClass="br.com.senai.automacao.App"`
4. O console exibirá a conexão com o broker e passará a registrar as leituras da prensa em tempo real.

## 👨‍💻 Créditos e Autoria
* **Autor:** Claudio Gabriel Litz
* **Docente:** Lucas Sousa dos Santos
* **Instituição:** WEG S.A / Sesi SENAI
* **Local:** Jaraguá do Sul, SC - 2026