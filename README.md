
#  User Authentication and Authorization API with JWT and Email Verification


The API includes endpoints for user registration, login, and accessing protected resources. JWT tokens are used to authenticate and authorize users. The registration endpoint sends a verification email to the user's provided email address, which contains a unique link to complete the registration process.
## What you need to have
All you need is a JDK (preferrably JDK 17) and an IDE

## Installation
Clone the repo and navigate to the root directory:
```bash
  git clone https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token.git
```


Build the project and generate the JAR file by executing the following command:
```bash
./mvnw clean package -DskipTests
```

Once the JAR file is generated, navigate to the target directory and execute the JAR file using the following command:

```bash
java -jar .\email-service-0.0.1-SNAPSHOT.jar --MAIL_HOST_USERNAME=<your-email> --MAIL_HOST_PASSWORD=<your-email-generated-password>
```

Make sure to replace <your-email> and <your-email-generated-password> with your actual email address and the generated password respectively. This command will run the email service and authenticate it with your email client, allowing you to send emails from it.


#  Example

Open your favirote HTTP client and follow the steps:



![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-1.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-2.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-3.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-4.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-5.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-6.png)


![alt text](https://github.com/abdulazezms/SpringBoot-JWT-and-Email-Verification-Token/tree/main/src/main/resources/static/img/practical-email-service-7.png)

