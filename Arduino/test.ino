#include <ESP8266WiFi.h>
#include <PubSubClient.h>

#define LED1   D6
#define LED2   D7
#define LED3   D8

// Vamos primeiramente conectar o ESP8266 com a rede Wireless (mude os parâmetros abaixo para sua rede).
const char* ssid = "fabio";
const char* password = "magoo321";

//Criando a função de callback
void callback(char* topic, byte* payload, unsigned int length) {
  // Essa função trata das mensagens que são recebidas no tópico no qual o Arduino esta inscrito.
}

//Criando os objetos de conexão com a rede e com o servidor MQTT.
WiFiClient espClient;
PubSubClient client("mqtt.hackathon.konkerlabs.net", 1883, callback,espClient);
ADC_MODE(ADC_VCC);
char mensagemC[100];

const char* SSID_BUS = "fabio";

// Return RSSI or 0 if target SSID not found
int32_t getRSSI(String target_ssid) {
  byte available_networks = WiFi.scanNetworks();
  for (int network = 0; network < available_networks; network++) {
    if (WiFi.SSID(network).equals(target_ssid)) {
      return WiFi.RSSI(network);
    }
  }
  return 0;
}

void enviarStatus(int statusAcao)
{
  //Configurando a conexão com o servidor MQTT
  if (client.connect("ID", "maeia7o25sa5", "eJT6I7KaoaK7"))  
  {
    //Subscrevendo no tópico <tópico para subscrição>
    client.subscribe("sub/maeia7o25sa5/Konker");
  
    //Agora vamos publicar uma mensagem no <tópico para pubicação>. Nota: a mensagem deve estar no formato JSON.
    String mensagem;
    switch (statusAcao)
    {
      case 1: mensagem = "{\"status\":\"1\"}"; break; //Zerou
      case 2: mensagem = "{\"status\":\"2\"}"; break; //Chamou
      case 3: mensagem = "{\"status\":\"3\"}"; break; //Parou
      default: mensagem = "{\"status\":\"1\"}"; break;
    }
    
    mensagem.toCharArray(mensagemC,mensagem.length()+1);
    Serial.println(mensagemC);
    client.publish("pub/maeia7o25sa5/Konker",mensagemC);      
  }
}

int statusAtual = 1;

void setup() 
{
  Serial.begin(9600);
 /* pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);*/
     
  //Conectando na Rede
  /*WiFi.begin(ssid, password); 
  Serial.print("connecting");
  
  while (WiFi.status() != WL_CONNECTED) 
  {
    delay(500);
    Serial.print(".");
  }   

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());*/

  //statusAtual = 1; //Zerou
  //enviarStatus(1);
}

void loop() 
{  
  /*unsigned long before = millis();
  int32_t rssi = getRSSI(SSID_BUS);
  unsigned long after = millis();*/

 /* if ((rssi >= -40) & (rssi < 0))
  {
    digitalWrite(LED1, HIGH);
    digitalWrite(LED2, HIGH);
    digitalWrite(LED3, HIGH); 
        
    if (statusAtual != 3)
    {
      enviarStatus(3);
    }
    statusAtual = 3;
  }
  else
  {
    digitalWrite(LED1, LOW);
    digitalWrite(LED2, LOW);
    digitalWrite(LED3, LOW);
    if (statusAtual != 1)
    {
      enviarStatus(1);
    }
    statusAtual = 1;
  }*/
  
  /*Serial.print("Signal strength: ");
  Serial.print(rssi);
  Serial.println("dBm");
  Serial.print("Took ");
  Serial.print(after - before);
  Serial.println("ms");
  Serial.println("Status: ");*/
  delay(250);
}
