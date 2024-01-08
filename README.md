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


## Keycloak
Original instractions see here: https://hub.docker.com/r/jboss/keycloak/

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

## Backend
## Frontend

For more configuration details see here: https://docs.spring.io/spring-security/site/docs/5.2.12.RELEASE/reference/html/oauth2.html#oauth2login-boot-property-mappings