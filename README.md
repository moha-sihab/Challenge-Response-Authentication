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
- Entity Framework

## Introduction
The idea for this project came from a random thought I had when I was assigned a task to implement biometric authentication. 
The question was: Is it possible to log in using biometrics securely without revoking or refreshing tokens? And can biometrics replace username-password authentication to simplify the login process? The answer is yes, it’s possible. But is it secure? That’s what I wanted to explore. 
So, I elaborated on my idea using the Challenge-Response Authentication Mechanism (CRAM). As a starting point, I built a simple backend API using C# and .NET, which includes basic login functionality and an endpoint to simulate the challenge-response mechanism.
For the frontend (Android), I created a simple login app using Kotlin and Jetpack Compose. The app offers an option to log in using biometrics, implementing the CRAM approach.

## Flow

How does CRAM (Challenge-Response Authentication Mechanism) work in this project?

The approach follows a pattern commonly seen in apps that support biometric authentication. It begins with an "initiation" phase—typically found in the app’s settings or configuration menu—where the user enables biometric login.

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
