# Encoding video para Sambatech

Este projeto foi desenvolvido para concorrer à posição de desenvolvedor JAVA back-end

## Introdução

Este projeto foi desenvolvido utilizando Java 8, Spring Boot, Spring security e JWT, MongoDB, Swagger, Maven, lombok, AWS SDK, Docker and Docker-Compose.

Utiliza-se como padrão a porta ``8080`` no spring.

Existe um Maven incorporado que pode ser usado digitando ``./mvnw``

Foi criado o docker-compose.yml para acesso ao mongoDB rodando dentro de um container no EC2.

Foi criando dois Spring Profiles : ``prod`` and ``dev``:

- ``dev`` conecta no mongoDB em um container docker em ``mongodb://<IP_SERVER_EC2>:27017/sambatech``
- ``prod`` conecta no mongoDB em um container docker em ``mongodb://IP_SERVER_EC2:27018/sambatech``
  
Esse projeto foi testado no ``Google Chrome`` and ``Firefox``.
## IDE's

Esse projeto foi desenvolvido utilizando a biblioteca lombok(facilita geração de getters, seters, constructors, deixando a classe mais limpa). 
Para instalar o lombok no STS ou eclipse, baixe o jar em https://projectlombok.org/download e informe o executavel .exe do eclipse/STS. 
Foi utilizado o STS 4.2.1.RELEASE como IDE para construir a aplicação

## Executar sem docker
Só é possível a execução sem utilizar o docker para o profile dev

### Comandos
- ``./mvnw spring-boot:run -Dspring.profiles.active=dev` para desenvolvimento
- ``./mvnw test` para testes

## Executar com docker

### Comandos
Foi utilizado o plugin dockerfile-maven-plugin do spotify para gerar a imagem encoding-video-test-sambatech-docker-image do docker. 
As configurações estão dentro do arquivo ``Dockerfile``. Para isso, tenha o ``Docker`` instalado em sua máquina. Feito isso, execute os comandos:
  - ``./mvnw clean package dockerfile:build`` na raiz do projeto para gerar a imagem.
  - ``docker-compose up -d`` para executar o arquivo ``docker-compose.yml`` com todas as configurações necessária para que o projeto funcione.
  - Para verificar se o container está executando, use ``docker ps -a`` , deverá mostrar algo como isso:
  
    | CONTAINER_ID 	| IMAGE                                	                        | COMMAND                	| CREATED        	| STATUS        	| PORTS                  	| NAMES                                   	  |
    |--------------	|-------------------------------------------------------------	|------------------------	|----------------	|---------------	|------------------------	|-------------------------------------------	|
    | acc11e7869aa 	| <SEU_USUARIO_DOCKER_HUB>/encoding-video-test-sambatech-docker-image:latest 	| "java -Dspring.profi…" 	| 18 minutes ago 	| Up 18 minutes 	| 0.0.0.0:8080->8080/tcp 	| encodingvideotestsambatech_teste-sambatech_1            	|
    
  - Em seguida, corra o comando ``docker logs -f <CONTAINER_ID>``, para acessar o log do container

## Informações importantes antes de chamar os endpoints
  - A autenticação e geração de token é realizada pelo JWT através do serviço /login passando as credenciais via JSON, para isso,
  utilize sempre o JSON {"email":"cliente@sambatech.com.br","password":"acessoConvidado123"}
  - Acesse a documentação para mais detalhes em http://localhost:8080/swagger-ui.html#!/default/login (É preciso logar, pegar o token e utilizar nos acessos dos serviços)  
  - Uma aplicação front-end poderá ser acessada na url: http://sambatetch-teste-frond-end.s3-website-us-east-1.amazonaws.com
## API Endpoints

### Login

Envie um ``POST`` request para http://yourserverurl:8080/login (http://localhost:8080/login)

```json
{
  "email":"cliente@sambatech.com.br",
  "password":"acessoConvidado123"
}
```

Uma resposta com código 200 com a autorização no header caso o login aconteça com sucesso:
Authorization=Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbGllbnRlQHNhbWJhdGVjaC5jb20uYnIiLCJleHAiOjE1Njc1NjU0OTh9.XbnyxhRVWlzwT1PcBSHdMxa164i3rMRqWFs1rUPgt5cUAqjO3I4-ZthPvQiHkS8hu-pwFdt5KwSb12kMT1Gyhg

IMPORTANTE: O token tem validade de 1 dia e deve ser ser passado no header ```Authorization``` para as próximas requisições.

Caso a resposta seja 401, o json abaixo será exibido:
```json
{
    "timestamp": 1567475949671,
    "status": 401,
    "error": "Usuário não autorizado",
    "message": "Email e/ou senha inválidos!",
    "details": "Verifique seu usuário/senha e tente novamente!",
    "path": "/login"
}
```
Obs: O idioma depende de sua JVM. No EC2 as respostas estão traduzidas para o English

### Fazer upload de um arquivo e codificar no bitmovin

Envie um ``POST`` request para http://yourserverurl:8080/api/encoding (http://localhost:8080/api/encoding) passando no header um Content-Type do tipo application/x-www-form-urlencoded
e no body um parametro file com seu vídeo no formato descrito em https://bitmovin.com/docs/encoding/articles/supported-input-and-output-formats#input-formats

### Consultar situação do encoding e obter a url do vídeo

Envie um ``GET`` request para http://yourserverurl:8080/api/encoding/<ID_DEVOLVIDO_NA_LOCATION> (http://localhost:8080/api/encoding/<ID_DEVOLVIDO_NA_LOCATION>), onde, 
<ID_DEVOLVIDO_NA_LOCATION> é um código devolvido na URI do post anterior

A resposta caso o encoding esteja completo será:

```json
{
    "outMessage": "O vídeo foi codificado com sucesso. Acesse em: https://<URL_BUCKET_S3>/ENCODINGS/d0baf786-3c73-4e5b-adde-d482e0e3a174/<nome_video>.mp4",
}
```
Ou se o vídeo ainda estiver sendo codificado pelo bitmovin:

```json
{
    "outMessage": "A codificação do vídeo está em processamento",
}
```
Qualquer erro na aplicação, será retornado além do código HTTP, o json:

```json
{
    "timestamp": "<TIMESTAMP>",
    "status": "<HTTP_CODE>",
    "error": "<TITULO>",
    "message": "<MENSAGEM_CURTA>",
    "details": "<DETALHE_ERRO_CASO_HOUVER>",
    "path": "<ENDPOINT_REQUISITADO>"
}
```

## Postman

Os endpoints podem ser acessados em https://www.getpostman.com/collections/794f91878456045e620c e importados para testes no postman.

## API Documentation

A documentação no ``Swagger`` pode ser acessada em http://localhost:8080/swagger-ui.html#!/default/login ou http://<IP_SEU_SERVER>:8080/swagger-ui.html#!/default/login

## Problemas encontrados / Melhorias a realizar

### Problemas:
  - Os vídeos estão sendo codificados sem o áudio;
  - Caso utilize o mongoDB do EC2, às vezes pode ocorrer erro de read timeout
  
### Melhorias a realizar:
  - Utilizar REST reativo para ir informando o usuário do processo e andamento;
  - Utilizar o webhooks do bitmovin para avisar quando o vídeo foi codificado;

## UM POUCO MAIS DE DOCKER
### Subir imagem para DockerHub
  - Caso queira subir sua imagem para o dockerHub (presumindo que você já tenha uma conta em https://hub.docker.com), execute:
  - ``docker login`` e entre com usuário e senha 
  - ``docker tag encoding-video-test-sambatech-docker-image <SEU_USUARIO_DOCKER_HUB>/encoding-video-test-sambatech-docker-image:latest``
  - ``docker push <SEU_USUARIO_DOCKER_HUB>/encoding-video-test-sambatech-docker-image:latest``
### Criar um container no EC2
  - Entre em sua instancia EC2 pelo ssh, execute ``docker login`` e entre com usuário e senha 
  - Crie um arquivo ``docker-compose.yml`` e copie o conteúdo do ``docker-compose.yml`` do seu projeto.
  - Faça pull de sua imagem com o comando ``docker pull <SEU_USUARIO_DOCKER_HUB>/encoding-video-test-sambatech-docker-image``
  - Execute ``docker-compose up -d``

## LICENSE

Este projeto está licensiado por ``Apache License 2.0``.