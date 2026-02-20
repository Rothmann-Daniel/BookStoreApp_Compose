# 📚 BookStore App

Android-приложение книжного магазина с каталогом книг, системой категорий и панелью администратора, построенное с использованием современного стека технологий Android.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-DD2C00?style=for-the-badge&logo=firebase&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

---

## ✨ Возможности

- Просмотр списка книг и категорий
- Авторизация и регистрация пользователей через **Firebase Authentication**
- Хранение и управление данными о книгах и категориях в **Firebase Firestore**
- Локальное кэширование данных с помощью **Room Database**

### 🔐 Панель администратора

Приложение включает отдельный раздел для администратора с расширенными правами управления контентом. Все изменения синхронизируются с Firebase Firestore в реальном времени.

**📖 Управление книгами:**
- Добавление новой книги
- Редактирование информации о книге (название, автор, описание, цена и др.)
- Удаление книги

**🗂️ Управление категориями** (отдельное меню):
- Добавление новой категории
- Редактирование названия / описания категории
- Удаление категории
- Активация / деактивация категории (управление видимостью для пользователей)

---

## 🛠️ Технологии

- **Kotlin** — основной язык разработки
- **Jetpack Compose** — декларативный UI-фреймворк
- **Firebase Authentication** — авторизация и управление пользователями
- **Firebase Firestore** — облачное хранилище данных (книги, категории)
- **Room** — локальное кэширование данных (SQLite абстракция)
- **ViewModel** — управление состоянием UI
- **Coroutines / Flow** — асинхронная работа с данными
- **Material 3** — дизайн-система

---

## 🏗️ Архитектура

Проект следует архитектурному паттерну **MVVM (Model-View-ViewModel)**:

```
app/
└── src/
    └── main/
        └── java/
            └── com.example.bookstoreapp/
                ├── data/
                │   ├── BookDao.kt          # Data Access Object
                │   ├── BookDatabase.kt     # Room Database
                │   └── BookEntity.kt       # Entity / модель данных
                ├── repository/
                │   └── BookRepository.kt   # Репозиторий
                ├── viewmodel/
                │   └── BookViewModel.kt    # ViewModel
                └── ui/
                    ├── screens/            # Экраны приложения
                    ├── components/         # Переиспользуемые компоненты
                    └── theme/              # Тема и стили
```

---

## 🚀 Запуск проекта

### Требования

- Android Studio Hedgehog или новее
- JDK 17+
- Android SDK 26+
- Устройство или эмулятор с Android 8.0 (API 26) и выше
- Аккаунт Firebase и файл `google-services.json`

### Шаги

1. Клонируй репозиторий:

```bash
git clone https://github.com/Rothmann-Daniel/BookStoreApp_Compose.git
```

2. Открой проект в **Android Studio**

3. Добавь файл `google-services.json` из Firebase Console в папку `app/`

4. Дождись синхронизации Gradle

5. Запусти приложение на эмуляторе или устройстве (**Run → Run 'app'**)

---

## 📦 Зависимости

```kotlin
// Jetpack Compose
implementation(platform("androidx.compose:compose-bom"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose")

// Firebase
implementation(platform("com.google.firebase:firebase-bom"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")

// Room
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
kapt("androidx.room:room-compiler")

// ViewModel + Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.lifecycle:lifecycle-runtime-ktx")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
```

---

## 📸 Скриншоты

| | | |
|:---:|:---:|:---:|
| ![](https://github.com/user-attachments/assets/bd6878be-01c1-4dae-9399-49703ad841a9) | ![](https://github.com/user-attachments/assets/55ca15c8-e45b-4886-ad99-eb30dce0a1cd) | ![](https://github.com/user-attachments/assets/524eebfc-11d6-4a00-a941-c5c5b50d7623) |
| ![](https://github.com/user-attachments/assets/6ad129d6-b078-4eba-8e8b-0f1aa8758f93) | ![](https://github.com/user-attachments/assets/2ab4b2fe-0fec-4e94-9674-1b7e1792457c) | ![](https://github.com/user-attachments/assets/5ad31c12-0037-4878-b171-f385bb4b2591) |

---


## 📄 Лицензия

Проект доступен для ознакомления и обучения. Коммерческое использование требует согласования.

---

# 📋 Возможные улучшения

Проект работает и выполняет свои функции, однако есть направления для дальнейшего развития:

- 📐 **MVVM архитектура** — перевод всего приложения на паттерн ViewModel + Repository для лучшего разделения логики и UI
- 🧪 **Unit Tests** — покрытие тестами бизнес-логики (ViewModel, Repository, DAO)
- 🔗 **Hilt / Koin** — внедрение dependency injection для более чистой и масштабируемой архитектуры
- 🔔 **Push-уведомления** — интеграция Firebase Cloud Messaging для оповещений пользователей
- 🌐 **Многоязычность** — поддержка нескольких языков интерфейса

---

## 👨‍💻 Автор

**Данила Ротман** - Android Developer

- 📱 Telegram: [@danielrothmann](https://t.me/danielrothmann)
- 🌐 GitHub: [@Rothmann-Daniel](https://github.com/Rothmann-Daniel)

<div align="center">

**⭐ Если проект понравился, поставьте звезду!**

Made  by Daniel Rothmann

</div>
