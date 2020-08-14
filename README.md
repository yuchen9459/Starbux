# Starbux

The RESTful API allows users to order drinks/toppings and admins can create/update/delete drinks/toppings and have access to the reports

## Tech stack 

- **Spring Boot**

  The Starbux e-commerce API is built by Springboot. RESTful API can provide uniform interface and objects in REST are always manipulated from the URI, which is easy for different endpoints to consume.

- **MySql**

  Mysql is used as the Databas. Four Tables were created:

  | Table    | Primary Key | Foreign Key       | Description                                                  |
  | -------- | ----------- | ----------------- | ------------------------------------------------------------ |
  | Product  | product_id  | None              | Store the product infomation                                 |
  | CartItem | Item_id     | cart_id, order_id | Store the item info in the cart/order                        |
  | Cart     | cart_id     | None              | Store the item info and original/discount price of current cart |
  | Order    | order_id    | None              | Store the bought items info                                  |

- **Docker**

  The service is deployed using Docker. Containerization provides a loosely-coupled way to deploy the application. At the same time, auto-scaling feature and resilliency can be provided by several container orchastration system, such as Kubernetes, Docker Swarm. Therefore, if there's more and more requests coming to the service, resiience and concurrency can be guaranteed.

## Quickstart

1. Make sure Docker is running on your machine. 

2. Clone database to your local machine.

   ```bash
   git clone https://github.com/yuchen9459/Starbux.git
   ```

3. Switch to the correct folder

   ```bash
   cd Starbux
   ```

4. Start the application with following command

   ```bash
   docker-compose up --build -d
   ```

   \* Make sure the port 3036 is available.

5. Run the script to initialize the database

   ```bash
   cd scripts
   sh db_init.sh
   ```

## Usage

### Admin

The following APIs are for admin user to add/update/delete products or generate reports.

#### GetProducts

Get all products info stored in the database

- URL 

  http://localhost:8080/admin/product

- Method

  `GET`

- Success Response

  - Code: 200

  - Content:

    ```json
    [
        {
            "id": 1,
            "name": "Black Coffee",
            "price": 4.0,
            "categoryType": "DRINK",
            "salesNum": 0
        },
        {
            "id": 2,
            "name": "Latte",
            "price": 5.0,
            "categoryType": "DRINK",
            "salesNum": 5
        },
        {
            "id": 3,
            "name": "Mocha",
            "price": 6.0,
            "categoryType": "DRINK",
            "salesNum": 15
        },
        {
            "id": 4,
            "name": "Tea",
            "price": 3.0,
            "categoryType": "DRINK",
            "salesNum": 5
        },
        {
            "id": 5,
            "name": "Milk",
            "price": 2.0,
            "categoryType": "TOPPING",
            "salesNum": 0
        },
        {
            "id": 6,
            "name": "Hazelnut syrup",
            "price": 3.0,
            "categoryType": "TOPPING",
            "salesNum": 0
        },
        {
            "id": 7,
            "name": "Chocolate sauce",
            "price": 5.0,
            "categoryType": "TOPPING",
            "salesNum": 0
        },
        {
            "id": 8,
            "name": "Lemon",
            "price": 2.0,
            "categoryType": "TOPPING",
            "salesNum": 0
        }
    ]
    ```

- Sample Call

  ```bash
  CURL GET 'http://localhost:8080/admin/product'
  ```

#### GetProduct

Get a product info based on the product id

- URL 

  http://localhost:8080/admin/product

- Method

  `GET`

- Required URL Params

  `id=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "id": 1,
        "name": "Black Coffee",
        "price": 4.0,
        "categoryType": "DRINK",
        "salesNum": 0
    }
    ```

- Error Response

  - Code: 404

  - Content:

    ```json
    {
        "message": "The request product does not exist",
        "timestamp": "2020-08-14T08:21:05.076+02:00",
        "path": "/admin/product/9",
        "error": "Not Found",
        "status": 404
    }
    ```

- Sample Call

  ```
  CURL GET 'http://localhost:8080/admin/product/1'
  ```

#### AddProduct

Add a product with its name/price/categoryType

- URL 

  http://localhost:8080/admin/product/add

- Method

  `POST`

- Required URL Params

  `body=[JSON]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "id": 9,
        "name": "Latte",
        "price": 5.0,
        "categoryType": "DRINK",
        "salesNum": 0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The product already exists",
        "timestamp": "2020-08-14T08:25:34.64+02:00",
        "path": "/admin/product/add",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request POST 'http://localhost:8080/admin/product/add' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "name": "Latte",
    "price": 5,
    "categoryType": "DRINK"
  }'
  ```

#### UpdateProduct

Delete a product based on the id

- URL 

  http://localhost:8080/admin/product/update

- Method

  `PUT`

- Required URL Params

  `id=[Long]`

  `body=[JSON]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "id": 9,
        "name": "Latte",
        "price": 6.0,
        "categoryType": "DRINK",
        "salesNum": 0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The product id in the body is missing",
        "timestamp": "2020-08-14T08:32:28.567+02:00",
        "path": "/admin/product/update/9",
        "status": 400,
        "error": "Bad Request"
    }
    ```

    ```json
    {
        "message": "Ids are not match",
        "timestamp": "2020-08-14T08:32:49.943+02:00",
        "path": "/admin/product/update/9",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request PUT 'http://localhost:8080/admin/product/update/9' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "id": "9",
    "name": "Latte",
    "price": 6,
    "categoryType": "DRINK"
  }'
  ```

#### DeleteProduct

Delete a product based on the id

- URL 

  http://localhost:8080/admin/product/delete

- Method

  `DELETE`

- Required URL Params

  `id=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    Product with id <9> is deleted
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The product id is invalid",
        "timestamp": "2020-08-14T08:33:55.483+02:00",
        "path": "/admin/product/delete/10",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request DELETE 'http://localhost:8080/admin/product/delete/9'
  ```

#### GetUserOrders

Get the total amount of the orders per customer

- URL 

  http://localhost:8080/admin/reports/user-orders

- Method

  `GET`

- Required URL Params

  `None`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "1": 1,
        "2": 2
    }
    // Format <userId>:<orderAmounts>
    ```

- Sample Call

  ```bash
  curl --location --request GET 'http://localhost:8080/admin/reports/user-orders'
  ```

#### GetMostSoldTopping

Get the most used topping for drinks

- URL 

  http://localhost:8080/admin/reports/most-sold-toppings

- Method

  `GET`

- Required URL Params

  `None`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "value": 15,
        "key": "Mocha"
    }
    ```

- Sample Call

  ```bash
  curl --location --request GET 'http://localhost:8080/admin/reports/most-sold-toppings'
  ```

### User

The following APIs are for user to order drinks/toppings and generate orders.

#### addToCart

Add products to the cart.

- URL 

  http://localhost:8080/cart/

- Method

  `POST`

- Required URL Params

  `userId=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "cartId": 96,
        "userId": 3,
        "items": [
            {
                "cartItemId": 97,
                "productId": 1,
                "quantity": 5
            }
        ],
        "priceOriginal": 20.0,
        "priceDiscount": 15.0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The product does not exist",
        "timestamp": "2020-08-14T08:43:43.08+02:00",
        "path": "/cart/3",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request POST 'http://localhost:8080/cart/3' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"productId": 1,
  	"quantity": 5
  }
  '
  ```

#### UpdateCartItem

Update the amounts of items in the cart. When the quantity is negative number, the amount of items will be removed from the cart.

- URL 

  http://localhost:8080/cart/

- Method

  `PUT`

- Required URL Params

  `userId=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "cartId": 96,
        "userId": 3,
        "items": [
            {
                "cartItemId": 97,
                "productId": 1,
                "quantity": 15
            }
        ],
        "priceOriginal": 60.0,
        "priceDiscount": 45.0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The item with product id <11> is not in this cart",
        "timestamp": "2020-08-14T08:47:59.403+02:00",
        "path": "/cart/3",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request POST 'http://localhost:8080/cart/3' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  	"productId": 1,
  	"quantity": 10
  }
  '
  ```

#### DeleteCartItem

Remove a product from the cart.

- URL 

  http://localhost:8080/cart/

- Method

  `DELETE`

- Required URL Params

  `userId=[Long]`

  `productId=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "cartId": 96,
        "userId": 3,
        "items": [],
        "priceOriginal": 0.0,
        "priceDiscount": 0.0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "The item is not in this cart",
        "timestamp": "2020-08-14T08:52:11.668+02:00",
        "path": "/cart/3/1",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request DELETE 'http://localhost:8080/cart/3/1'
  ```

#### Checkout

Checkout current cart and get the order.

- URL 

  http://localhost:8080/cart/checkout

- Method

  `POST`

- Required URL Params

  `userId=[Long]`

- Success Response

  - Code: 200

  - Content:

    ```json
    {
        "orderId": 99,
        "userId": 1,
        "items": [
            {
                "cartItemId": 98,
                "productId": 1,
                "quantity": 8
            }
        ],
        "priceOriginal": 32.0,
        "priceDiscount": 24.0
    }
    ```

- Error Response

  - Code: 400

  - Content:

    ```json
    {
        "message": "User does not exist",
        "timestamp": "2020-08-14T08:54:57.06+02:00",
        "path": "/cart/checkout/4",
        "status": 400,
        "error": "Bad Request"
    }
    ```

- Sample Call

  ```bash
  curl --location --request POST 'http://localhost:8080/cart/checkout/1'
  ```



