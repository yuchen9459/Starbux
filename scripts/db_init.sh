curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Milk",
  "price": 2,
  "categoryType": "TOPPING"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Hazelnut syrup",
  "price": 3,
  "categoryType": "TOPPING"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Chocolate sauce",
  "price": 5,
  "categoryType": "TOPPING"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Lemon",
  "price": 2,
  "categoryType": "TOPPING"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Tea",
  "price": 3,
  "categoryType": "DRINK"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Mocha",
  "price": 6,
  "categoryType": "DRINK"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Latte",
  "price": 5,
  "categoryType": "DRINK"
}'

curl --location --request POST 'http://localhost:8080/admin/product/add' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "Black Coffee",
  "price": 4,
  "categoryType": "DRINK"
}'