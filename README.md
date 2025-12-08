# OAuth2 Full Stack Demo App

## Build and Run this poject
```bash
docker-compose -f "docker-compose.yml" up -d --build
```
P.S for Mac with M1(arm64) chip please update keycloak image


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

### Exporting a realm
```bash
# Run export
docker exec -it keycloak /opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 \
-Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.realmName=demo \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/demo-realm.json
# Copy file to local machine (run terminal from project root)
docker cp keycloak:/tmp/demo-realm.json keycloak/realms/demo-realm.json
```

```shell
docker cp keycloak:/opt/keycloak/data/export/demo-realm.json keycloak/realms/demo-realm.json
```

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
## Frontend

Open Frontend UI: http://localhost:8080 \

For more configuration details see here: https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html#oauth2login-boot-property-mappings