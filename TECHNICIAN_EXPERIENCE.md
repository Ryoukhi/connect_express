# Expérience Utilisateur Technicien - Documentation Complète

## 🎯 Fonctionnalités Implémentées

Cette documentation couvre toutes les fonctionnalités du dashboard technicien complètement intégrées.

### 1. **Authentification & Déconnexion**
- ✅ Connexion avec email/mot de passe
- ✅ Redirection automatique vers `/dashboard-technicien` après login
- ✅ Menu de déconnexion dans le header
- ✅ Logout avec suppression du token et redirection vers login

### 2. **Configuration du Profil Technicien**
- ✅ Ajouter des compétences (TechnicianSkill)
  - Nom de la compétence
  - Catégorie (dropdown avec liste des catégories actives)
  - Description
  - Niveau (1-5)
  - Années d'expérience
  - Tarif horaire (FCFA)
  
- ✅ Gestion des documents pour certifier les compétences
  - Ajouter documents par compétence
  - Télécharger documents
  - Supprimer documents
  - Types de documents: IDENTITY_CARD, CERTIFICATION, DIPLOMA, etc.

### 3. **Gestion des Réservations**
- ✅ Consulter toutes les réservations du technicien (`/dashboard-technicien/reservations`)
- ✅ Filtrer par statut:
  - PENDING (En attente)
  - ACCEPTED (Acceptée)
  - EN_ROUTE (En route)
  - IN_PROGRESS (En cours)
  - COMPLETED (Complétée)
  - REJECTED (Rejetée)
  - CANCELLED (Annulée)

### 4. **Actions sur les Réservations**
Les actions disponibles dépendent du statut actuel:

**PENDING (En attente):**
- ✅ Accepter la réservation → Statut devient ACCEPTED
- ✅ Refuser la réservation → Statut devient REJECTED (avec raison)

**ACCEPTED (Acceptée):**
- ✅ Démarrer (En route) → Statut devient EN_ROUTE
- ✅ Annuler → Statut devient CANCELLED (avec raison)

**EN_ROUTE (En route):**
- ✅ Commencer le travail → Statut devient IN_PROGRESS
- ✅ Annuler → Statut devient CANCELLED (avec raison)

**IN_PROGRESS (En cours):**
- ✅ Marquer comme complétée → Statut devient COMPLETED

**COMPLETED / REJECTED / CANCELLED:**
- Aucune action disponible

### 5. **Gestion de la Disponibilité**
- ✅ Mise à jour du statut de disponibilité (AvailabilityStatus):
  - ✅ AVAILABLE (Disponible) - Peut accepter les réservations
  - ✅ BUSY (Occupé) - En cours d'une intervention
  - ✅ UNAVAILABLE (Indisponible) - N'accepte pas les réservations
  - ✅ ON_BREAK (Pause) - Pause temporaire

L'état est partagé pour toutes les compétences du technicien.

## 🏗️ Architecture & Implémentation

### Backend (Java)
**Contrôleurs:**
- `TechnicianController.java` - Gestion du profil et disponibilité
- `TechnicianSkillController.java` - Gestion des compétences
- `DocumentsTechnicienController.java` - Gestion des documents
- `ReservationController.java` - Gestion des réservations

**Services:**
- `TechnicianServiceImpl.java` - Logique métier technicien
- `TechnicianSkillServiceImpl.java` - Gestion des compétences
- `TechnicianDocumentServiceImpl.java` - Gestion des documents
- `ReservationServiceImpl.java` - Gestion des réservations

**Entités:**
- `UserEntity` - Utilisateur (base de tous les rôles)
- `TechnicianSkillEntity` - Compétences avec AvailabilityStatus
- `TechnicianDocumentEntity` - Documents de certification
- `ReservationEntity` - Réservations

### Frontend (Angular)
**Services:**
- `TechnicianReservationService` - API pour les réservations
- `TechnicianAvailabilityService` - API pour la disponibilité
- `AuthService` - Authentification et session

**Composants:**
- `dashboard-technicien/` - Dashboard principal
- `form-skill/` - Formulaire d'ajout/modification de compétences
- `technician-reservations/` - Liste et gestion des réservations
- `availability-status/` - Gestion de la disponibilité

**Routes:**
```
/dashboard-technicien - Dashboard principal
/dashboard-technicien/profile - Configuration du profil
/dashboard-technicien/reservations - Gestion des réservations
/dashboard-technicien/today - Réservations d'aujourd'hui
/dashboard-technicien/pending - Réservations en attente
```

## 🔌 Endpoints API Utilisés

### Réservations
```
GET    /api/reservations/me/technician - Récupérer toutes les réservations
GET    /api/reservations/{id} - Récupérer une réservation
POST   /api/reservations/{id}/status - Changer le statut
POST   /api/reservations/{id}/complete - Marquer comme complétée
POST   /api/reservations/{id}/cancel - Annuler
```

### Compétences
```
POST   /api/technician-skills - Ajouter une compétence
GET    /api/technician-skills/me - Récupérer mes compétences
PUT    /api/technician-skills/{id} - Modifier une compétence
DELETE /api/technician-skills/{id} - Supprimer une compétence
```

### Documents
```
POST   /api/technician-documents - Ajouter un document
GET    /api/technician-documents/skill/{skillId} - Récupérer les documents
DELETE /api/technician-documents/{id} - Supprimer un document
```

### Disponibilité
```
PUT    /api/technicians/{id}/availability - Mettre à jour la disponibilité
GET    /api/technicians/{id} - Récupérer le profil complet
```

### Catégories
```
GET    /api/categories/active - Récupérer les catégories actives
```

## 📱 Interface Utilisateur

### Dashboard Principal
- Statistiques: Réservations d'aujourd'hui, revenus, note moyenne, demandes en attente
- Réservations en attente avec action rapide (Accepter/Refuser)
- Vue d'ensemble des activités

### Page Réservations
- Filtre par statut (côté gauche)
- Liste scrollable des réservations (côté gauche)
- Détail de la réservation sélectionnée (côté droit)
- Actions contextuelles selon le statut
- Design responsive (mobile & desktop)

### Configuration Profil
- Formulaire d'ajout de compétence
- Sélecteur de catégorie (liste déroulante)
- Liste des compétences existantes
- Gestion des documents par compétence

### Statut de Disponibilité
- 4 boutons pour changer le statut
- Affichage du statut actuel
- Mise à jour en temps réel

## 🔐 Sécurité

- ✅ Authentification requise pour toutes les pages technicien
- ✅ Vérification du rôle TECHNICIAN
- ✅ JWT token utilisé pour les requêtes
- ✅ Logout avec suppression du token
- ✅ Redirection automatique si non authentifié
- ✅ Seul le technicien peut modifier ses données

## 🚀 Variables d'Environnement Requises

```
SPRING_PROFILES_ACTIVE=dev
DB_HOST=localhost
DB_PORT=3306
DB_NAME=connectdb
DB_USERNAME=root
DB_PASSWORD=
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION=86400000
HOST_NAME_CORS=http://localhost:4200
```

## ✅ Checklist des Fonctionnalités

- [x] Connexion/Déconnexion du technicien
- [x] Redirection automatique post-login
- [x] Configuration du profil (ajouter compétences)
- [x] Catégories affichées dans le formulaire
- [x] Gestion des documents pour les compétences
- [x] Consultation des réservations
- [x] Filtrage par statut
- [x] Actions sur les réservations (accepter/refuser/modifier statut)
- [x] Gestion de la disponibilité (AvailabilityStatus)
- [x] Interface utilisateur complète
- [x] Design responsive
- [x] Gestion des erreurs et messages de succès
- [x] Routes intégrées

## 🔧 Démarrage

### Backend
```bash
cd connect-backend
export SPRING_PROFILES_ACTIVE=dev
./mvnw spring-boot:run
```

### Frontend
```bash
cd connect-frontend
ng serve
```

### Accès
- Frontend: http://localhost:4200
- API: http://localhost:8085
- Swagger: http://localhost:8085/swagger-ui.html

## 📝 Notes Importantes

1. **Catégories:** Les catégories doivent être pré-créées dans la base de données par un admin
2. **Disponibilité:** Le statut de disponibilité est appliqué à TOUTES les compétences du technicien
3. **Réservations:** Seules les réservations du technicien connecté sont affichées
4. **Documents:** Les documents sont liés à une compétence spécifique

## 🐛 Débogage

- Ouvrir la console du navigateur (F12) pour voir les logs
- Vérifier les onglets Network pour les requêtes API
- En cas d'erreur, vérifier que:
  - Le token JWT est valide
  - Les variables d'environnement sont correctes
  - La base de données est accessible
  - Le rôle TECHNICIAN est assigné à l'utilisateur
