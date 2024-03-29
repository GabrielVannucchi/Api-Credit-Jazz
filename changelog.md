:01-06-2023
@toDo
configurar o docker
configurar corretamente resilience4j

@changelog:

:30-05-2023
@toDo
configurar o docker

@changelog:
regras de negócio transferidas para a domain
services de criação e consulta separadas, bem como suas classes de teste

:25-05-2023
@toDo
configurar o docker

@changelog:
alterado tratamento de excessão com a clientApi

:24-05-2023
@toDo
configurar o docker

@changelog:
adicionado checkstyle e jacoco.
fixes apontaados pelo checkstyle
adicionado novos testes não mapeados anteriormente

:23-05-2023
@toDo
configurar o docker
adicionar o jacoco

@changelog:
escala dos valores validada para entrada de uma nova analise de crédito

:22-05-2023
@toDo
validar para receber apenas duas casas decimais ou ignorar da terceira para cima
adicionar o jacoco

@changelog:
criados testes unitarios para a classe Service e a dto Request
alterado extensão do arquivo changelog.txt para changelog.md
annualInteres altearada de Integer para Float

:19-05-2023
@toDo
validar para receber apenas duas casas decimais ou ignorar da terceira para cima
aplicar testes unitarios

@changelog:
código HTTP lançado para todos os endpoints
validation aplicado para CreditAnalysisRequest
entradas de CreditAnalysisService/listAnalysisByClient validadas
entradas de CreditAnalysisService/findByAnalysis validadas
removido validation
excessões personalizadas lançadas para CreditAnalysisRequest
mensagens de erro traduzidas para o ingles
adicionada excessão para consulta por Id de cliente para caso não conste na base
atributo clientId na model CreditAnalysis transformado em UUID

:18-05-2023
@toDo
aplicar código de resposta HTTP para todos os endpoints
aplicar o Validation
tratar excessoes do Validation de maneira personalizada
validar entradas de findByClient
validar entradas de findByAnalysis
Transformar o clientId para UUID+

@changelog:
regra de negocio aplicada para o metodo createAnalysis na CreditAnalysisService
Alterado para BigDecimal dados da CreditAnalysisEntity que estavam como Float
alterado nome do docker de postgres para postgrescredit
criado findByClient funcional, sem validação
criado findByAnalysis funcional, sem validação
findByClient consultando clientes via CPF
removidos AutoWired
implementado RestControllerAdvice
adicionado ProblemDetailBuilder para ExceptionHandler
adicionado regex para validar CPF e UUID na consulta

:17-05-2023
@toDo
aplicar regra de negocio para salvar CreditAnalysis
alterar de AutoWired para RequiredArgsConstructor em todo o código
aplicar código de resposta HTTP para todos os endpoints
tratar excessões com o HandlerExceptionAdvice(criar)

@changelog:
adicionado Builder na model CreditAnalysis
criado Mapper temporário de CreditAnalysisRequest para CreditAnalysisEntity.
criado endpoint de salvar. Salvando no banco
criado endpoint para listar todas as analises
criado o data/ddl.sql
conexão com a api de clientes feita atraves do FeignClient

:16-05-2023
@changelog apiCredit
Criado a estrutura inicial do projeto;
Aplicado docker-compose, com banco em postgre e alterado servidor de tomcat para o undertow;
Criado principais classes do sistema, como Controller, Service, Repository, Model e Entity