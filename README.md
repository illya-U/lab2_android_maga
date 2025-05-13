# 💼 Student Information

**Full Name:** Illia Vitaliyovych Ustymenko  
**Group:** _KV-41MP_  
**Lab Title:** MKR – Mobile Application Development with REST API  
**Assignment:** General task: develop a mobile application that connects to a backend service from Lab #1 of the "Web Application Development Technology" course (or another REST API service).  
**Report Document:** [Google Drive Report](https://docs.google.com/document/d/18Y-nqDSfV2hopwftSk-6El1ACINlundOqlU7Nk4Pc-A/edit?tab=t.0)

---

# 💱 Currency Exchanger App — Lab 2

Welcome! This is Lab Work #2 in Android Development — a mobile application for **currency exchange**, built with modern Android technologies.

> **Author:** Illia Ustymenko  
> **Project Goal:** Build an app that allows currency conversion using online exchange rates, with transaction history saved to a remote server via REST API.

---

## 🧩 Features

- 🔄 Fetch real-time exchange rates from an API
- 💰 Convert between any currencies
- 🕓 **Record exchange history to a remote server** via REST API
- 📜 View past transactions
- 🌐 Select base currency
- 🧼 Modern interface built with **Jetpack Compose**
- 📦 Dependency injection via **Hilt**

---

## 🛠️ Technologies & Libraries

| Category         | Libraries                                                                  |
|------------------|-----------------------------------------------------------------------------|
| **UI**           | Jetpack Compose, Material3, Navigation Compose                             |
| **DI**           | Hilt, Hilt Navigation Compose                                               |
| **Networking**   | Retrofit, Gson                                                              |
| **Async**        | Kotlin Coroutines                                                           |
| **REST API**     | Send transaction history to server using Retrofit                          |
| **Misc**         | `dotenv-kotlin` for working with `.env` files on Android                   |

<details>
<summary>📦 Key Library Versions</summary>

```toml
Kotlin: 2.0.21  
AGP: 8.9.2  
Compose: 1.4.3 (BOM 2024.09.00)  
Coroutines: 1.7.3  
Hilt: 2.48  
Retrofit: 2.9.0  
Gson: 2.10.1  
```
</details>

---

## 🚀 Getting Started

1. **Clone the repository:**

```bash
git clone https://github.com/illya-U/lab2_android_maga.git
cd lab2_android_maga
```

2. **Create a file named `env`** in the root directory:

```
API_KEY=your_api_key_here
```

3. **Open the project in Android Studio and run the app**

---

## 🌐 Exchange History Recording

After each conversion, the app **sends transaction data to a remote server** via REST API. Stored data includes:

- Source and target currencies
- Amount exchanged
- Timestamp

Exchange **rate is not stored**.

---

## 👤 Author

**Illia Ustymenko**  
🔗 [GitHub Profile](https://github.com/illya-U)

---

## 📃 License

Educational project. All rights reserved © 2025