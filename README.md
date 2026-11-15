# SafeRoute API

API RESTful do **SafeRoute**, a CitizenTech que sugere rotas seguras em dias de chuva forte na Vila Mariana (São Paulo – SP).
Global Solution 2026.2 · FIAP · Engenharia de Software · Domain Driven Design – Java · Turma 2ESA

| Integrante | RM |
|---|---|
| Guilherme Rodrigues de Andrade | 565863 |
| Gusthavo Costa de Melo | 561330 |

- **Repositório:** https://github.com/RODRIGUES7469/saferoute-api
- **Deploy (Render):** `https://SEU-SERVICO.onrender.com` ← substituir após o deploy
- **Swagger:** `https://SEU-SERVICO.onrender.com/swagger-ui.html`

## O que a API faz

Além do CRUD de bairros, trechos de rua, usuários, previsões de chuva e relatos de alagamento, a API tem a
**capacidade central** do SafeRoute: calcular o **risco de alagamento de cada trecho** para uma chuva prevista.
O cálculo usa os coeficientes dos modelos de regressão treinados na disciplina de Data Science, aplicados às
características do trecho guardadas no Oracle (cota, declividade, distância do córrego, bocas de lobo, dias desde
a limpeza e histórico de alagamentos). Um relato ativo de morador eleva o trecho para risco ALTO na hora.

## Arquitetura (camadas)

```
controller  ->  bo (regras de negócio)  ->  dao (JDBC / Oracle)  ->  banco Oracle FIAP
     |                                         |
  model (classes do domínio)            exception (tratamento global de erros)
```

| Pacote | Conteúdo |
|---|---|
| `model` | `Bairro`, `Trecho`, `Usuario`, `PrevisaoChuva`, `Relato`, `RiscoTrecho` e os enums `PerfilUsuario`, `NivelAgua`, `StatusRelato`, `NivelRisco` |
| `dao` | `DAOBase` + um DAO por tabela, com SQL via `PreparedStatement` e IDs gerados por sequences |
| `bo` | validações e regras (e-mail único, senha só em hash, relato expira em 2 h, cálculo de risco...) |
| `controller` | endpoints REST: GET, POST, PUT e DELETE |
| `exception` | exceções do domínio + `GlobalExceptionHandler` (`@RestControllerAdvice`), que devolve um JSON de erro padronizado |

## Ferramentas e versões

| Ferramenta | Versão |
|---|---|
| Java (JDK) | 21 |
| Spring Boot | 3.3.5 (Web + JDBC) |
| Maven | 3.9+ (ou o Maven embutido no IntelliJ) |
| Banco de dados | Oracle Database 19c (servidor da FIAP) com driver `ojdbc11` |
| IDE | IntelliJ IDEA |
| Deploy | Render.com (Docker) |
| Testes de API | Swagger UI (springdoc 2.6.0) ou Postman/Insomnia |

## Como executar localmente

1. **Banco:** no SQL Developer, conectado ao Oracle da FIAP, execute `sql/SafeRoute_DDL.sql` e depois `sql/SafeRoute_DML.sql` (F5 – Executar Script).
2. **Variáveis de ambiente** (no IntelliJ: *Run > Edit Configurations > Environment variables*):
   ```
   DB_USER=rmXXXXXX
   DB_PASSWORD=sua_senha_do_oracle
   ```
   `DB_URL` é opcional (padrão `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`).
3. **Rodar:** execute a classe `SafeRouteApplication` ou, no terminal, `mvn spring-boot:run`.
4. Abra `http://localhost:8080/swagger-ui.html` para testar todos os endpoints.

## Deploy no Render

1. Suba o projeto para um repositório no GitHub.
2. No Render: **New + → Web Service → Build and deploy from a Git repository** → escolha o repositório.
3. **Language/Runtime:** `Docker` (o Render usa o `Dockerfile` da raiz). **Instance type:** Free.
4. Em **Environment Variables**, cadastre `DB_USER`, `DB_PASSWORD` (e `DB_URL`, se diferente do padrão).
5. **Create Web Service**. O primeiro build leva alguns minutos. Quando aparecer *Live*, acesse a URL + `/swagger-ui.html`.

> No plano gratuito, o serviço "dorme" após 15 min sem uso; a primeira requisição depois disso demora ~1 minuto.

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/` | status da API |
| GET | `/api/bairros` | lista bairros |
| GET | `/api/bairros/{id}` | busca bairro |
| POST | `/api/bairros` | cadastra bairro |
| PUT | `/api/bairros/{id}` | atualiza bairro |
| DELETE | `/api/bairros/{id}` | exclui bairro (409 se tiver trechos/previsões) |
| GET | `/api/bairros/{id}/mapa-risco?chuvaMmH=38&chuvaAcum72hMm=20` | risco de todos os trechos do bairro |
| GET | `/api/trechos?idBairro=1` | lista trechos (filtro opcional) |
| GET | `/api/trechos/{id}` | busca trecho |
| POST | `/api/trechos` | cadastra trecho |
| PUT | `/api/trechos/{id}` | atualiza trecho |
| DELETE | `/api/trechos/{id}` | exclui trecho |
| GET | `/api/trechos/{id}/risco?chuvaMmH=38&chuvaAcum72hMm=20` | **risco de alagamento do trecho** |
| GET | `/api/usuarios` | lista usuários |
| GET | `/api/usuarios/{id}` | busca usuário |
| POST | `/api/usuarios` | cadastra usuário |
| PUT | `/api/usuarios/{id}` | atualiza usuário |
| DELETE | `/api/usuarios/{id}` | exclui usuário |
| GET | `/api/previsoes?idBairro=1` | lista previsões |
| GET | `/api/previsoes/{id}` | busca previsão |
| POST | `/api/previsoes` | cadastra previsão |
| PUT | `/api/previsoes/{id}` | atualiza previsão |
| DELETE | `/api/previsoes/{id}` | exclui previsão |
| GET | `/api/relatos?status=ATIVO` | lista relatos |
| GET | `/api/relatos/{id}` | busca relato |
| POST | `/api/relatos` | envia relato de alagamento |
| PUT | `/api/relatos/{id}` | atualiza / modera relato |
| DELETE | `/api/relatos/{id}` | exclui relato |

### Exemplos de corpo (JSON)

**POST /api/relatos**
```json
{ "idTrecho": 3, "idUsuario": 2, "nivelAgua": "JOELHO", "urlFoto": "https://saferoute.example/fotos/nova.jpg" }
```

**POST /api/usuarios**
```json
{ "nome": "Carla Souza", "email": "carla.souza@email.example", "telefone": "11988887777", "senha": "segredo123" }
```

**POST /api/trechos**
```json
{ "idBairro": 1, "codigo": "T09", "logradouro": "Rua Áurea", "referencia": "Próximo à Rua Morgado de Mateus",
  "cotaM": 18.0, "declividadePct": 2.5, "distCorregoM": 380, "bocasLobo100m": 2.0,
  "dataUltimaLimpeza": "2026-10-15", "latitude": -23.5880, "longitude": -46.6400 }
```

**POST /api/previsoes**
```json
{ "idBairro": 1, "inicio": "2026-11-20T17:00:00", "fim": "2026-11-20T19:00:00",
  "chuvaMmH": 35.0, "probabilidadePct": 80, "fonte": "CGE" }
```

**PUT /api/relatos/{id}** (moderação)
```json
{ "status": "REMOVIDO", "motivoRemocao": "Relato duplicado" }
```

### Resposta de erro padronizada

```json
{
  "timestamp": "2026-11-10T10:15:30",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Os dados enviados são inválidos.",
  "caminho": "/api/usuarios",
  "detalhes": ["O campo 'email' não é um e-mail válido."]
}
```

| Situação | Status |
|---|---|
| Dados inválidos / JSON mal formado / parâmetro faltando | 400 |
| Registro não encontrado | 404 |
| Método HTTP não suportado | 405 |
| E-mail ou código duplicado, exclusão de registro com dependentes | 409 |
| Banco de dados indisponível | 503 |
| Erro inesperado | 500 |
