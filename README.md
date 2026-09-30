# JWT Spring Boot 3 & Security 6 (Nimbus JOSE+JWT)

Dự án này là bài tập thực hành JWT (JSON Web Token) kết hợp với **Spring Boot 3** và **Spring Security 6**. Điểm đặc biệt của dự án là việc thay thế thư viện JWT mặc định (như `jjwt`) bằng thư viện **Nimbus JOSE+JWT** để xử lý ký (sign) và xác thực (verify) token theo chuẩn.

## Các tính năng chính
- Tích hợp Spring Security 6 hoàn toàn Stateless (không dùng Session).
- Đăng ký và Đăng nhập (Authentication & Authorization).
- Mã hóa mật khẩu bằng `BCryptPasswordEncoder`.
- Xử lý Token sử dụng thư viện `com.nimbusds:nimbus-jose-jwt` (với thuật toán `MACSigner` và `MACVerifier` chuẩn HMAC-SHA256).
- Xử lý các lỗi JWT Validation thông qua `GlobalExceptionHandler`.

## Yêu cầu môi trường
- Java 17+
- Maven 3.6+
- SQL Server

## Cấu hình Database
1. Mở SQL Server Management Studio (SSMS) và chạy lệnh sau để khởi tạo database:
   ```sql
   CREATE DATABASE jwt_springboot3;
   ```
2. Mặc định dự án đang cấu hình kết nối tới SQL Server (`localhost\\SQLSever`) với tài khoản `sa` / mật khẩu `12345`. Nếu máy bạn cấu hình khác, hãy sửa lại tại file `src/main/resources/application.properties`.

## Hướng dẫn chạy dự án
- **Dùng IDE (Spring Tool Suite / Eclipse, IntelliJ IDEA):** Import project dưới dạng **Existing Maven Project** và chạy file `JwtNimbusApplication.java`.
- **Dùng Terminal/Command Prompt:**
  ```bash
  mvn spring-boot:run
  ```

## Danh sách API (Endpoints)

### 1. Đăng ký tài khoản (Sign Up)
- **URL:** `POST /auth/signup`
- **Body (JSON):**
  ```json
  {
    "email": "test@gmail.com",
    "password": "123",
    "fullName": "Nguyen Van A"
  }
  ```

### 2. Đăng nhập (Log In)
- **URL:** `POST /auth/login`
- **Body (JSON):**
  ```json
  {
    "email": "test@gmail.com",
    "password": "123"
  }
  ```
- **Response:** Trả về một chuỗi `token` (JWT) dùng để xác thực các request sau.

### 3. Lấy thông tin tài khoản đang đăng nhập
- **URL:** `GET /users/me`
- **Header:** `Authorization: Bearer <token_nhận_được_ở_phần_đăng_nhập>`
- **Response:** Trả về thông tin chi tiết của User.
