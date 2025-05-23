# Challenge-Response Authentication Mechanism (CRAM) Simulation
This project simulates the Challenge-Response Authentication Mechanism (CRAM) using biometric authentication in Android application.  
The backend is built with C# using .NET Framework.

### Tech Stack

**Frontend (Android):**
- Kotlin
- Jetpack Compose
- Biometric API


**Backend:**
- C#
- .NET 8

## Introduction
The idea for this project came from a random thought I had when I was assigned a task to implement biometric authentication. 

The question was: Is it possible for biometrics to replace username-password authentication and simplify the login process? The answer is yes, it’s possible. But is it secure? That’s what I wanted to explore. 

So, I elaborated on my idea using the Challenge-Response Authentication Mechanism (CRAM). As a starting point, I built a simple backend API using C# and .NET, which includes basic login functionality and an endpoint to simulate the challenge-response mechanism.

For the frontend (Android), I created a simple login app using Kotlin and Jetpack Compose. The app offers an option to log in using biometrics, implementing the CRAM approach.

The core concept of this project revolves around the use of an RSA-generated key pair (private and public keys). The client (Android app) holds the private key, securely stored in the Keystore, while the server stores the associated public key. These keys are mathematically linked, allowing the app to sign data and the server to verify it, without ever sharing sensitive credentials.

In the next section, you'll learn more about what a key pair is and how it’s used in this project.

## Flow

How does CRAM (Challenge-Response Authentication Mechanism) work in this project?

The approach follows a pattern commonly seen in apps that support biometric authentication. It begins with an "initiation" phase (typically found in the app’s settings or configuration menu) where the user enables biometric login.

Here is the initial flow:

### Initial Flow
![Initial Flow](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/init.jpg)

1. User choose to set biometric as authentication method.
2. The app displays the biometric prompt.
3. User confirms biometric authentication.
4. The app generates a key pair using the RSA algorithm. In this setup, the public key (used for verification) is sent to the backend, while the private key (used for signing) is securely stored in the Android Keystore. This ensures that sensitive cryptographic operations never expose the private key outside the device.

### Authentication Flow
After completing the initial flow, which results in a key pair generated using RSA, let's move on to the main implementation of the Challenge-Response Authentication Mechanism (CRAM).

Here is the authentication flow:
![Authentication Flow](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/auth.jpg)

1. User chooses biometric authentication to log in to the app.
2. The app sends a challenge request to the backend.
3. The backend responds with a random string (challenge) to be signed using the previously stored private key.
4. The app signs the challenge using the previously stored private key with the RSA algorithm and sends it back to the backend for verification.
5. The backend verifies the signed challenge using the RSA algorithm with the public key previously stored and associated with the user ID. If the signature is valid, the backend generates an authentication token.

## Database
For the database, I used **SQL Server**. The structure is simple and consists of two tables: `User` and `Challenges`.

Here is the table structure:
![Table Structure](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/table.png)

In the `User` table, I added a `PublicKey` column to store the public key generated using the RSA algorithm during the initial flow.
In the `Challenges` table, I added `UserId` as a foreign key that links to the `User` table. 
This table stores a new entry each time a user requests a challenge. A random string is generated and saved in the `ChallengeText` column. 
I also included the `ExpiresAt` column to define the expiration time for each challenge, ensuring that challenges are time-bound and valid only for a limited period.

## API
After completing the database structure for this project, I built a simple set of APIs using C# and the .NET Framework.

These are the endpoints:

![API](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/api.png)

I created two endpoints related to authentication. The first one handles login with a username and password, and the second implements challenge-response authentication, as shown in the 'Challenge' API section.

## Front-End (Android)
For the Android app, I created a simple application using Kotlin and Jetpack Compose to simulate two authentication methods: 
- username-password 
- challenge-response mechanism.
  
So, coming back to the overall flow, you can now visualize how the initial flow and authentication flow (explained earlier in this document) fit together.

![APP](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/appfront.jpg)

<img src="https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/vid.gif" alt="AppGif" width="600"/>

## Next

For improvement, I plan to improve the security aspect of the challenge creation process.

Right now, the challenge is tied to the `userId`, but it would be more secure to use a `deviceId` instead. This way, each challenge is linked to a specific device, not just the user, which helps prevent misuse or replay attacks.


