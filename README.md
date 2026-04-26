# 🍽️ Profit Plate - Restaurant Management System

A modern, full-featured restaurant management system built with Spring Boot. Perfect for managing customer orders, table bookings, meal planning, and restaurant operations with role-based dashboards.

## ✨ Features

### 🛒 **Customer Features**
- Browse complete menu with category filtering (All, Breakfast, Lunch, Dinner, Snacks)
- View menu items with prices, descriptions, and images
- Quick snacks section with curated selections
- Place orders and track order history
- Book tables in advance with date/time selection
- Generate AI-powered personalized meal plans based on dietary preferences

### 👨‍💼 **Manager Features**
- Dashboard to monitor restaurant operations
- View all orders and their status
- Manage table bookings and reservations
- Track meal plan requests
- Analytics and insights

### 🔐 **Admin Features**
- Complete system administration dashboard
- Manage users (Create, Read, Update, Delete)
- Monitor all orders and transactions
- View all table bookings
- System settings and configurations

### 🏠 **General Features**
- Secure user authentication and authorization
- Role-based access control (Customer, Manager, Admin)
- Responsive design with Tailwind CSS
- Dark mode support
- Multi-user support with MongoDB



### Backend
- **Java 25.0.1** - Programming language
- **Spring Boot 3.2.3** - Web framework
- **MongoDB** - NoSQL database
- **Spring Data MongoDB** - Data access layer
- **Maven** - Build tool

### Frontend
- **Thymeleaf** - Server-side template engine
- **Tailwind CSS 3** - Utility-first CSS framework
- **HTML5** - Markup
- **JavaScript** - Client-side logic

### Database
- **MongoDB** (Local: localhost:27017)
- Collections: menuitems, users, orders, table_bookings, ai_generated_meal_plans, users_diet_profile

## 📋 Prerequisites

- Java 11 or higher (tested with Java 25.0.1)
- MongoDB running locally on port 27017
- Maven 3.6+ (or use included Maven wrapper)

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/vickyysshh/RESTAURANT_MANAGER.git
cd restaurant-app  
```

### 2. Ensure MongoDB is Running
```bash
# Make sure MongoDB is running on localhost:27017
# Default database: test
```

### 3. Build the Project
```bash
./mvnw clean install
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

### 5. Access the Application
- **Home**: http://localhost:8080
- **Menu**: http://localhost:8080/restaurant-menu
- **Login**: http://localhost:8080/login
- **Signup**: http://localhost:8080/signup

## 📁 Project Structure

```
restaurant-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/restaurant/
│   │   │   ├── ApiController.java          # REST API endpoints
│   │   │   ├── ViewController.java         # View routing
│   │   │   ├── RestaurantApplication.java  # Spring Boot entry point
│   │   │   ├── model/                      # Data models
│   │   │   ├── repository/                 # MongoDB repositories
│   │   │   └── service/                    # Business logic
│   │   └── resources/
│   │       ├── application.properties      # App configuration
│   │       └── templates/                  # HTML templates
│   │           ├── index.html              # Homepage
│   │           ├── restaurant-menu.html    # Menu & booking
│   │           ├── login.html              # Login page
│   │           ├── signup.html             # Registration
│   │           ├── customer-browse.html    # Customer dashboard
│   │           ├── admin-dashboard.html    # Admin panel
│   │           ├── manager-dashboard.html  # Manager panel
│   │           ├── meal-plan.html          # Meal planning
│   │           └── role-selection.html     # Role chooser
├── pom.xml                 # Maven configuration
├── mvnw & mvnw.cmd        # Maven wrapper scripts
└── README.md              # This file
```

## 🔌 API Endpoints

### Menu
- `GET /api/menu` - Get all menu items
- `GET /api/menu/{category}` - Filter by category

### Orders
- `GET /api/orders` - Get user orders
- `POST /api/orders` - Create new order
- `DELETE /api/orders/{id}` - Cancel order

### Table Bookings
- `GET /api/table-bookings` - View bookings
- `POST /api/table-bookings` - Book a table
- `PUT /api/table-bookings/{id}` - Update booking
- `DELETE /api/table-bookings/{id}` - Cancel booking

### Meal Plans
- `POST /api/generate-meal-plan` - Generate AI meal plan
- `GET /api/meal-plans` - View meal plans
- `GET /api/meal-plans/{id}` - Get specific plan
- `POST /api/meal-plans/{id}/order-day/{day}` - Order from plan

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/logout` - Logout user

## 🎨 Branding

- **Restaurant Name**: Profit Plate
- **Primary Color**: #ff906a (Orange)
- **Secondary Color**: #ff734c (Dark Orange)
- **Theme**: Dark mode enabled

## 📱 Menu Categories

- **Snacks**: Samosa, Spring Roll, Onion Bhajia, Paneer Tikka, French Fries, Chicken Tikka Bites
- **Breakfast**: Various breakfast items
- **Lunch**: Complete lunch menu
- **Dinner**: Dinner specials
- **All**: Complete menu

## 🔐 User Roles

1. **Customer** - Browse menu, order, book tables, view meal plans
2. **Manager** - Monitor operations, view bookings and orders
3. **Admin** - Full system access, user management, all features

## 📝 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions are welcome! Feel free to fork this repository and submit pull requests.

---

**Made with ❤️ by Vicky**
