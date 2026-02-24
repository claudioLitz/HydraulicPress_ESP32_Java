# Guia Definitivo: Rodando Wokwi com PlatformIO no VS Code

Este guia apresenta o passo a passo completo para migrar um projeto de ESP32 do site Wokwi para o ambiente local utilizando o **Visual Studio Code (VS Code)** e a extensão **PlatformIO**. Rodar localmente elimina o problema de filas de espera no simulador online e centraliza o desenvolvimento.

---

## 1. Sobre a Licença do Wokwi (É Gratuito!)

Uma dúvida comum é sobre a licença da extensão do Wokwi expirar após alguns dias. **Você não precisa pagar nada.** A extensão é 100% gratuita para uso pessoal e projetos de código aberto. 

Para renovar a licença quando ela expirar:
1. No VS Code, pressione a tecla `F1`.
2. Digite e selecione: `Wokwi: Request a New License`.
3. O navegador será aberto. Estando logado na sua conta gratuita do Wokwi, clique em **"GET YOUR LICENSE"**. O VS Code será ativado por mais um ciclo gratuitamente.

---

## 2. Pré-requisitos

Certifique-se de ter os seguintes itens instalados no seu computador:
* **Visual Studio Code (VS Code)**
* Extensão **PlatformIO IDE** (instalada via VS Code)
* Extensão **Wokwi Simulator** (instalada via VS Code)

---

## 3. Passo a Passo da Migração

### Passo 1: Criar o Projeto no PlatformIO
1. Abra o VS Code e clique no ícone do **PlatformIO** (uma cabeça de alienígena) na barra lateral.
2. Na aba **PIO Home**, clique em **New Project**.
3. Preencha as configurações:
   * **Name**: Nome do seu projeto (ex: `ProjetoESP32`).
   * **Board**: Selecione a sua placa (ex: `Espressif ESP32 Dev Module`).
   * **Framework**: Selecione `Arduino`.
4. Clique em **Finish** e aguarde a criação da estrutura de pastas.

### Passo 2: Configurar as Bibliotecas (Dependencies)
No PlatformIO, o gerenciamento de bibliotecas é feito de forma automática pelo arquivo de configuração. 
1. Na raiz do seu projeto recém-criado, abra o arquivo `platformio.ini`.
2. Adicione as bibliotecas necessárias usando o comando `lib_deps`. No nosso caso, o arquivo deve ficar parecido com isto:

```ini
[env:esp32dev]
platform = espressif32
board = esp32dev
framework = arduino

lib_deps =
    adafruit/DHT sensor library
    marcoschwartz/LiquidCrystal_I2C
    bblanchon/ArduinoJson
    knolleary/PubSubClient