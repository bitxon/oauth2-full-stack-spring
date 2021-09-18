# OAuth2 Full Stack Demo App

## Build and Run this poject
```bash
docker-compose -f "docker-compose.yml" up -d --build
```


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
Note: if you run docker compose and use postman to get token to get access to backend(resource-server) you will face a problem because postman and backend app in diferent networks
```
DEBUG o.s.s.oauth2.jwt.JwtClaimValidator       : The iss claim is not valid
DEBUG o.s.s.o.s.r.a.JwtAuthenticationProvider  : Failed to authenticate since the JWT was invalid
```
`iss` in your postman token is `http://localhost.....`

`iss` that resource server expected  `http://keycloak...... `
