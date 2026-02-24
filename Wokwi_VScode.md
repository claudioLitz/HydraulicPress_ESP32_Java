# 🚀 Guia Definitivo

# Rodando Wokwi com PlatformIO no VS Code

Este guia apresenta o passo a passo completo para migrar um projeto
**ESP32** do site Wokwi para um ambiente local utilizando:

-   **Visual Studio Code (VS Code)**
-   **PlatformIO**
-   **Wokwi Simulator (extensão VS Code)**

Rodar localmente elimina filas de espera do simulador online e deixa seu
desenvolvimento muito mais profissional.

------------------------------------------------------------------------

# 📌 1. Licença do Wokwi --- Precisa Pagar?

Não.

A extensão **Wokwi para VS Code** é gratuita para uso pessoal e projetos
open source.

O sistema de "licença" é apenas uma validação temporária com os
servidores deles. Quando expirar, basta renovar.

## 🔄 Como renovar a licença

1.  Pressione `F1` no VS Code\
2.  Digite: `Wokwi: Request a New License`\
3.  O navegador abrirá automaticamente\
4.  Faça login na sua conta gratuita\
5.  Clique em **GET YOUR LICENSE**

------------------------------------------------------------------------

# 🧩 2. Pré-requisitos

Antes de começar, verifique se você possui:

-   Visual Studio Code instalado\
-   Extensão **PlatformIO IDE**\
-   Extensão **Wokwi Simulator**

------------------------------------------------------------------------

# 🏗 3. Migração do Projeto (Passo a Passo)

## 🛠 Passo 1 --- Criar o Projeto no PlatformIO

1.  Abra o VS Code\
2.  Clique no ícone do **PlatformIO**\
3.  Vá em **PIO Home → New Project**

Preencha:

-   **Name**: Projeto_ESP32_Wokwi\
-   **Board**: Espressif ESP32 Dev Module\
-   **Framework**: Arduino

Clique em **Finish** e aguarde.

------------------------------------------------------------------------

## 📚 Passo 2 --- Configurar Bibliotecas (`platformio.ini`)

Edite o arquivo `platformio.ini`:

``` ini
[env:esp32dev]
platform = espressif32
board = esp32dev
framework = arduino

lib_deps =
    adafruit/DHT sensor library
    marcoschwartz/LiquidCrystal_I2C
    bblanchon/ArduinoJson
    knolleary/PubSubClient
```

O PlatformIO instalará automaticamente as bibliotecas na primeira
compilação.

------------------------------------------------------------------------

## 💻 Passo 3 --- Trazer o Código Fonte

1.  Copie o `sketch.ino` do Wokwi\
2.  Cole em `src/main.cpp`\
3.  Certifique-se que a primeira linha seja:

``` cpp
#include <Arduino.h>
```

------------------------------------------------------------------------

## 🧪 Passo 4 --- Compilar (Build)

Clique no botão ✓ na barra inferior.

Aguarde a mensagem:

    SUCCESS

Arquivos gerados em:

    .pio/build/

------------------------------------------------------------------------

## 🔌 Passo 5 --- Migrar o Diagrama

1.  Crie `diagram.json` na raiz\
2.  Copie o conteúdo do `diagram.json` do Wokwi\
3.  Cole no arquivo criado

------------------------------------------------------------------------

## 🌉 Passo 6 --- Configurar `wokwi.toml`

Crie `wokwi.toml` na raiz e adicione:

``` toml
[wokwi]
version = 1
firmware = ".pio/build/esp32dev/firmware.bin"
elf = ".pio/build/esp32dev/firmware.elf"
```

Se sua placa for diferente, ajuste o nome da pasta dentro de
`.pio/build/`.

------------------------------------------------------------------------

## ▶️ Passo 7 --- Iniciar a Simulação

1.  Pressione `F1`\
2.  Execute `Wokwi: Start Simulator`

------------------------------------------------------------------------

# 🧠 Estrutura Final do Projeto

    Projeto_ESP32_Wokwi/
    │
    ├── src/
    │   └── main.cpp
    │
    ├── diagram.json
    ├── wokwi.toml
    ├── platformio.ini
    └── .pio/

------------------------------------------------------------------------

# 🏁 Conclusão

Agora você possui um ambiente local profissional para desenvolver e
simular projetos ESP32 sem depender de filas do simulador online.
