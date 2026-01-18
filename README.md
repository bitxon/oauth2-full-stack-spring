# OAuth2 Full Stack Demo App

<!-- TOC -->
* [OAuth2 Full Stack Demo App](#oauth2-full-stack-demo-app)
  * [Build and Run this project](#build-and-run-this-project)
  * [Frontend](#frontend)
  * [Test Data](#test-data)
    * [Test Users](#test-users)
    * [Test Clients](#test-clients)
  * [Keycloak](#keycloak)
    * [Keycloak API](#keycloak-api)
      * [Get Token using `client_credentials` Grant](#get-token-using-client_credentials-grant)
      * [Get Token using `authorization_code` Grant](#get-token-using-authorization_code-grant)
    * [Keycloak Export Realm](#keycloak-export-realm)
<!-- TOC -->

## Build and Run this project
```bash
docker compose -f "docker-compose.yml" up -d --build
```

---
## Frontend
Open Frontend UI: [Login page](http://localhost:8080)\
For more configuration details see [Spring Security OAuth2 Login Boot Property Mappings](https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html#oauth2login-boot-property-mappings)

---
## Test Data

### Test Users
| Login           | Password |
|-----------------|----------|
| user01          | password |
| administrator01 | password |

### Test Clients
| grant_type         | client_id                  | client_secret                        |
|--------------------|----------------------------|--------------------------------------|
| client_credentials | demo-client-creds          | f5bb2859-a486-47ab-b7f8-c5454d5332b3 |
| authorization_code | demo-client-auth-code      | fe5f8402-cb20-40e4-988f-43239c55095f |
| authorization_code | demo-client-pkce-auth-code | 9909a4a2-00cc-4a42-b1b9-c238ac3c2028 |


---
## Keycloak
Open Keycloak Admin Console: http://localhost:9000/auth/admin \
Open Keycloak User Console: http://localhost:9000/auth/realms/demo/account \
Open Keycloak Debug Hostname Page: http://localhost:9000/auth/realms/demo/hostname-debug

---
### Keycloak API

#### Get Token using `client_credentials` Grant
1. Get token
  ```shell
  curl --request POST --url http://localhost:9000/auth/realms/demo/protocol/openid-connect/token \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data grant_type=client_credentials \
    --data client_id=demo-client-creds \
    --data client_secret=f5bb2859-a486-47ab-b7f8-c5454d5332b3 | jq .
  ```

#### Get Token using `authorization_code` Grant
1. Open in browser -> Login -> Get `code=` from URL:\
   http://localhost:9000/auth/realms/demo/protocol/openid-connect/auth?response_type=code&state=j4kl3im2&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fkeycloak&client_id=demo-client-auth-code
2. Get token
  ```shell
  curl --request POST --url http://localhost:9000/auth/realms/demo/protocol/openid-connect/token \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data grant_type=authorization_code \
    --data client_id=demo-client-auth-code \
    --data client_secret=fe5f8402-cb20-40e4-988f-43239c55095f \
    --data redirect_uri=http://localhost:8080/login/oauth2/code/keycloak \
    --data state=j4kl3im2 \
    --data code=<PUT_YOUR_CODE_FROM_STEP_1_HERE> | jq .
  ```

---
### Keycloak Export Realm

1. Stop Keycloak (but keep the container)
    ```shell
    docker compose -f "docker-compose.yml" stop keycloak
    ```
2. Export Realm (using the stopped container's data)
    ```shell
    docker compose -f "docker-compose.yml" run --rm keycloak export --realm demo --file /opt/keycloak/data/export-demo-realm.json
    ```
3. Copy Exported Realm File to Host
    ```shell
    docker cp keycloak:/opt/keycloak/data/export-demo-realm.json ./keycloak/realms/demo-realm.json
    ```
4. ReStart Keycloak
    ```shell
    docker compose -f "docker-compose.yml" start keycloak
    ```
