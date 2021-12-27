# Microservice_Banking_Application
# Banking Microservice Application
# @jyotiprakash_jena

This **Banking Microservice Application** has the following *components*:
1. API-GATEWAY  (acts as the single point of entry for our application)
2. DISCOVERY-SERVICE    (used for discovering other microservices)
3. CONFIG-SERVER    (configuration server for configuration management)
4. CUSTOMER-SERVICE (customer management microservice)
5. ACCOUNT-SERVICE  (account management microservice)

How to start the application:
1. Start discovery-service
2. Start config-server
3. Start api-gateway
4. Start account-service
5. Start customer-service
Why start account-service before customer-service?
Because there is a dependency from customer-service to account-service
