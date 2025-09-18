# B-Day Genie

**B-Day Genie** is a functional Android application that helps users remember birthdays. Each entry stores a person's details (such as name and birth date), and the app calculates and displays how many days remain until their next birthday.

---

### Features

- **Firebase Authentication**
  
  - Sign up and log in using email and password.
  
  - Only authenticated users can access the app.

- **Custom FastAPI Back-End**
  
  - Data is stored and retrieved through a REST API, not on the device itself.
  
  - Centralized data management helps to manage and scale the app better.

- **Birthday Entry Management**
  
  - Create, view, edit, delete and list birthdays.
  
  - Display remaining days until each birthday on the main screen.

- **Sorting and Filtering**
  
  - Functionality to organize entries by different criteria for easier management.

---

### Planned Future Improvements

- **Back-End Enhancements**, including clous-based database deployment, request validation and server-side filtering/sorting.

- **Expanded Authentication Options**, such as alternative login options and ability to use the application without an account in offline mode.

- **Password Reset Functionality** to support users who forget their credentials.

- **UI Overhaul** for a cleaner and more modern look.

---

### Setup Instructions

##### Backend (FastAPI) server

1. Navigate to the `backend` folder.

2. Start the backend server by running `uvicorn main:app --reload`.

**Note!** You may have to install the required Python packages before running the server: `pip install fastapi uvicorn pydantic`.

##### Android App

- Download the APK from the **Releases** page and run it on an emulator or a physical device.

- Alternatively, you can clone the repo and run the project in **Android Studio**.

**Important!** Since the FastAPI server currently runs locally, the Android application can only communicate with it when both are running on the same device (e.g., using the emulator or a local setup).

---

### Screenshots

| ![Login Screen](assets/screenshot1.png)   | ![Main Screen](assets/screenshot2.png)       |
| ----------------------------------------- | -------------------------------------------- |
| ![Profile Screen](assets/screenshot3.png) | ![Organizing Dialog](assets/screenshot4.png) |




