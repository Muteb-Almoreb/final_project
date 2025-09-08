# 🚚 Trucksy – Food Truck Aggregator Platform  

## Overview  
**Trucksy** is a backend system that aggregates food trucks in Riyadh, Saudi Arabia.  
It enables customers to easily find and order from nearby food trucks, while owners manage their trucks, menus, orders, and subscriptions.  

Premium owners gain access to **AI-powered insights and analytics** after subscribing.  

---

##  Features  

###  Customer  
- Sign up / Login.  
- Share location (via HERE API → converts city & district into lat/lng).  
- Browse nearby food trucks (distance calculation).  
- Search and filter trucks by category (e.g., burgers, desserts).  
- View truck profiles (menu, reviews, operating hours).  
- Add items to cart and place orders.  
- Pay online (Moyasar payment gateway).  
- Track orders (PLACED → READY → COMPLETED).  
- Write reviews and view average ratings.  
- Receive notifications for offers and order updates.  

###  Owner  
- Register / Login.  
- Add / Update / Delete food trucks.  
- Upload truck images (AWS S3).  
- Manage menu items (CRUD, price updates, availability).  
- Upload item images (AWS S3).  
- Create and manage discounts.  
- Open / Close truck.  
- Manage orders (change status).  
- Dashboard with key metrics:  
  - Total orders & revenue.  
  - Peak hours & best-selling items.  
- **Premium Features (after subscription)**:  
  - AI-powered insights on sales trends.  
  - Review sentiment analysis (positive/negative).  
  - AI-based recommendations (e.g., “Busiest time is 8 PM”).  
  - Advanced reporting & analytics.  
- Subscription and renewal through **Moyasar** payment API.  

###  Admin  
- View all users.  
- Delete users.  
- Delete food trucks.  
- Access system-wide reports.  

---

##  Tech Stack  
- **Backend:** Spring Boot 3, Spring Security, Hibernate/JPA  
- **Database:** MySQL  
- **Storage:** AWS S3 (for truck and item images)  
- **Payment:** Moyasar API  
- **Geocoding:** HERE API  
- **Authentication:** Spring Security with roles (`CLIENT`, `OWNER`, `ADMIN`)  
- **Build Tool:** Maven  

---

##  Roles & Example Endpoints  

### Public  
- `POST /api/v1/client/add` → Register a customer.  
- `POST /api/v1/owner/add` → Register an owner.  

### Customer  
- `GET /api/v1/foodTruck/get-nearest/{client_id}?limit=5` → Fetch nearest food trucks.  
- `POST /api/v1/order/add/{clientId}/{truckId}` → Place an order.  
- `POST /api/v1/review/add/{clientId}/{truckId}` → Write a review.  

### Owner  
- `POST /api/v1/foodTruck/add/{ownerId}` → Add a food truck.  
- `POST /api/v1/item/add/{ownerId}/{truckId}` → Add item to a food truck.  
- `POST /api/v1/foodTruck/upload-image/{truckId}` → Upload truck image.  
- `POST /api/v1/owner/subscribe` → Subscribe to premium plan.  
- `POST /api/v1/owner/callback/{ownerId}` → Handle subscription callback.  

### Admin  
- `GET /api/v1/auth/get` → Get all users.  
- `DELETE /api/v1/auth/delete/{user_id}` → Delete a user.  
- `DELETE /api/v1/auth/delete-foodTruck/{truckId}` → Delete a food truck.  

---

Diagrams

ER Diagram: (add docs/ERD.png).

Use Case Diagram:  
