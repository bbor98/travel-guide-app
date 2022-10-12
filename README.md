<h1 align="center">✈ Travel Guide App 🏖</h1>

<p align="center">
  <a href="https://www.android.com/"><img alt="Platform" src="https://img.shields.io/badge/platform-android-brightgreen.svg"/></a>
  <a href="https://developer.android.com/about/versions/lollipop"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/JetBrains/kotlin/releases/tag/v1.7.10"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-1.7.10-blueviolet"/></a>
</p>

Bu proje, **MVVM design pattern**'i ve **clean architecture** prensipleri uygulanarak ve bir [mockApi](https://6339f07e471b8c3955686976.mockapi.io/travel_list) kullanılarak tasarlanmıştır. Proje tamamen **Kotlin** ile yazılmış olup **single activity** mimarisi izlenmiştir. 

## 📷 Ekran Görüntüleri
Home Screen|Search Screen|Search Results Screen
:-:|:-:|:-:
![home](https://user-images.githubusercontent.com/88214480/195253745-d858e0eb-6750-4d19-abd2-2bf7ed335aab.jpg) | ![search](https://user-images.githubusercontent.com/88214480/195253806-ed4e366a-0218-423c-91dd-2204d6bcd5f6.jpg) | ![search_results](https://user-images.githubusercontent.com/88214480/195253834-c9d419be-d2b2-4f8a-8873-e21185ee8072.jpg)

|Trip Screen (Trips)|Trip Screen (Adding Trip)|Trip Screen (Bookmarks)|
:-:|:-:|:-:
![trip_trips](https://user-images.githubusercontent.com/88214480/195253920-3203d407-2d96-4cac-953e-6cac6559d3b0.jpg) | ![trip_adding_trip](https://user-images.githubusercontent.com/88214480/195254948-6eeda5fd-dab0-4973-bb67-43bc97039ed0.jpg) | ![trip_bookmarks](https://user-images.githubusercontent.com/88214480/195253973-6ed5bfdc-60b1-4dd5-bcc2-f240fcaef7f3.jpg)

Guide Screen|Detail Screen|Fullscreen Image
:-:|:-:|:-:
![guide](https://user-images.githubusercontent.com/88214480/195253977-3c4e9376-06b1-434e-96b2-a025759f70b5.jpg) | ![detail](https://user-images.githubusercontent.com/88214480/195253991-24cebdb7-3084-4119-82e1-8067e24a3cd6.jpg) | ![fullscreen_image](https://user-images.githubusercontent.com/88214480/195254003-031f85d5-0fb2-41da-9388-ed232490a8b1.jpg) |

## ▶ Demo
https://user-images.githubusercontent.com/88214480/195255245-45175b27-6b88-4d2f-89d4-f7afbeb70e72.mp4

## 🛠 Projenin Yapısı
![project_structure](https://user-images.githubusercontent.com/88214480/195248644-177f8d54-c18a-41b3-b366-beebab3d3113.png)

Proje 3 ana  katmana ayrılmıştır:
- Data
- Domain
- Presentation

### 🔸 Data Katmanı
*Data* katmanında ekranda gösterilecek verilerin API'dan veya yerel veritabanından alınması işlemi gerçekleştirilir. Hiyerarşideki diğer katmanlar, buradaki veri kaynağına doğrudan erişmemektedir; *Data* katmanına giriş noktası **repository** classıdır. **ViewModel** veya **use case** classları hiçbir zaman doğrudan veri kaynağına erişim sağlamamaktadır. Bu sayede, mimarideki diğer katmanların bağımsız olarak ölçeklenebilmesi sağlanmıştır. Bu katman tarafından iletilen veriler; diğer classlar tarafından değiştirilemeyecek şekilde, yani **immutable** olarak iletilmektedir. Bu sayede, iletilen verilerin tutarsız olma riski ortadan kaldılırmıştır.

### 🔸 Domain Katmanı
*Domain* katmanı, projenin merkezi katmanıdır. Bu katman, *Data* katmanı ve *Presentation* katmanı arasında bir köprü görevi görmektedir: *Data* katmanının API'dan aldığı verilerin, **use case**'ler aracılığıyla *Presentation* katmanında kullanılabilmesini sağlamaktadır. Bu katman; hiyeraşideki diğer katmanlardan bağımsızdır, diğer katmanlarda yapılan kod değişiklikleri bu katmanı etkilemez.

### 🔸 Presentation Katmanı
*Presentation* katmanında; **MainActivity**, **Fragment'lar**, **ViewModel'lar** ve **RecyclerView adapterleri** bulunmaktadır. Bu katman, kullanıcı etkileşimini yönetmekten ve verilerin ekranda ne şekilde gösterileceğini belirlemekten sorumludur. Fragment'lar, ViewModel'lara kullanıcı etkileşiminden oluşan istekleri gönderir, ViewModel'lar ise bu istekler doğrultusunda *Domain* katmanı ile iletişim kurar. ViewModel'lar, *Data* katmanı ile doğrudan iletişim kurmamaktadır.

### Projenin Veri Akış Diyagramı
![flow_chart](https://user-images.githubusercontent.com/88214480/195246398-65d6fd54-4945-4d2f-9cfe-4e94b5eb514f.png)

## 📚 Kullanılan Teknolojiler
- [Jetpack](https://developer.android.com/jetpack) kütüphaneleri
  - [Navigation Component](https://developer.android.com/guide/navigation)
  - [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
  - [DataBinding](https://developer.android.com/topic/libraries/data-binding)
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
  - [Room](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [Gson](https://github.com/google/gson)
- [Glide](https://github.com/bumptech/glide)
- [PhotoView](https://github.com/Baseflow/PhotoView)
- [SplashScren](https://developer.android.com/reference/kotlin/androidx/core/splashscreen/SplashScreen)

## 📜 License
```
Copyright (c) 2022 Bora Bor

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
