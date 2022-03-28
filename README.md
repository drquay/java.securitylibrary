# mnb-security-lib
1. docker-compose up
2. mvn clean install
3. curl --location --request POST 'http://localhost:8080/api/auth/signup' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "username": "username",
   "password": "password",
   "roles": ["ADMIN", "MOD", "USER"]
   }'
4. curl --location --request POST 'http://localhost:8080/api/auth/signin' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "username": "username",
   "password": "password"
   }'