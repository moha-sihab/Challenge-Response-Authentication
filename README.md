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

![Initial Flow](https://raw.githubusercontent.com/moha-sihab/Challenge-Response-Authentication/refs/heads/main/ss/init.jpg)

