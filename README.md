# Orders Management System - Java

## 📌 Descriere Proiect
O aplicație de tip desktop (Swing GUI) destinată gestionării unei baze de date de clienți, produse și comenzi. Sistemul implementează automatizarea procesării comenzilor, verificarea stocurilor și generarea facturilor (bills).

## 🏗️ Arhitectura Proiectului
Proiectul respectă o arhitectură pe straturi (N-tier) pentru a asigura separarea responsabilităților:
* **Presentation Layer:** Interfețe grafice realizate cu Swing[cite: 161].
* **Business Logic Layer (BLL):** Procesează regulile de business și validează datele[cite: 162].
* **Data Access Layer (DAO):** Interacționează cu baza de date folosind **Generic DAO** și **Reflection API**[cite: 164].
* **Model:** Clase de date (POJO) care mapă tabelele SQL[cite: 163].

## 🛠️ Functionalități (Use Cases)
Conform diagramei de Use Case, utilizatorul poate[cite: 140]:
* Gestiona Clienții (Adăugare, Actualizare, Ștergere, Vizualizare)[cite: 143, 146, 156].
* Gestiona Produsele (Controlul stocurilor și prețurilor)[cite: 144, 147, 155].
* Plasa Comenzi: Sistemul verifică automat dacă există stoc suficient înainte de a procesa comanda[cite: 148].
* Vizualiza Facturi (Bills): Generare automată a istoricului de plată[cite: 145].

## 🧪 Detalii Tehnice & Design Patterns
* **Generic DAO:** Implementare reutilizabilă pentru operații CRUD folosind Reflection[cite: 105].
* **Factory/Reflection:** Generarea automată a tabelelor UI (`TableGenerator`) pe baza structurii claselor.
* **Baza de date:** Integrare prin JDBC (recomandat să menționezi aici dacă ai folosit MySQL/PostgreSQL).

## 📊 Diagrame
![UML Class Diagram](link_catre_imagine_sau_calea_din_repo)
![Package Diagram](link_catre_imagine_sau_calea_din_repo)

---
Proiect realizat pentru disciplina: Tehnici de Programare.F
