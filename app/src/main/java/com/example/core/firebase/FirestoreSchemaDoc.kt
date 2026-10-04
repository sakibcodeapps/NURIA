package com.example.core.firebase

/**
 * ============================================================================
 * NURIA FIRESTORE DATABASE ARCHITECTURE SPECIFICATION FOR ADMIN PANEL
 * ============================================================================
 *
 * This documentation defines the canonical Firestore structure shared between:
 * 1. NURIA Android User Application (Read-only for public content, read-write for user profile)
 * 2. NURIA Admin Web Panel (Full CRUD operations for content and configuration)
 *
 * ----------------------------------------------------------------------------
 * 1. /branding/app (Document)
 * ----------------------------------------------------------------------------
 * Description: Real-time application branding and appearance configuration.
 * Fields:
 *   - appName (String, required): Display name of the application (e.g., "NURIA").
 *   - tagline (String, required): Subtitle / slogan (e.g., "Prayer • Quran • Qibla").
 *   - logoUrl (String, optional): Remote URL for app logo (fallback: local asset).
 *   - splashLogoUrl (String, optional): Remote URL for splash screen logo.
 *   - primaryColor (String, optional): Primary accent hex (e.g., "#006C4C").
 *   - secondaryColor (String, optional): Secondary accent hex (e.g., "#C5A059").
 *   - updatedAt (Timestamp / Number, required): Unix timestamp of last modification.
 *
 * ----------------------------------------------------------------------------
 * 2. /app_config/version (Document)
 * ----------------------------------------------------------------------------
 * Description: Remote version control and maintenance toggle.
 * Fields:
 *   - minSupportedVersion (Number, required): Minimum versionCode required.
 *   - latestVersion (Number, required): Current latest released versionCode.
 *   - forceUpdate (Boolean, required): If true, prompt mandatory update.
 *   - isMaintenanceMode (Boolean, required): If true, user app enters maintenance UI.
 *   - message_bn (String, optional): Maintenance reason in Bengali.
 *   - message_en (String, optional): Maintenance reason in English.
 *   - message_ar (String, optional): Maintenance reason in Arabic.
 *
 * ----------------------------------------------------------------------------
 * 3. /app_config/ads (Document)
 * ----------------------------------------------------------------------------
 * Description: Monetization and banner ad controls.
 * Fields:
 *   - isEnabled (Boolean, required): Master toggle for advertisements.
 *   - bannerAdUnitId (String, required): AdMob / provider banner unit ID.
 *   - interstitialAdUnitId (String, required): AdMob / provider interstitial ID.
 *   - showBannerOnHome (Boolean, required): Show non-intrusive card on Home screen.
 *   - showBannerOnDua (Boolean, required): Show non-intrusive banner on Dua screen.
 *   - showBannerOnAzkar (Boolean, required): Show banner on Azkar screen.
 *
 * ----------------------------------------------------------------------------
 * 4. /announcements/{announcementId} (Collection)
 * ----------------------------------------------------------------------------
 * Description: Public notices, Islamic reminders, and community announcements.
 * Fields:
 *   - title_bn (String, required): Title in Bengali.
 *   - title_en (String, required): Title in English.
 *   - title_ar (String, required): Title in Arabic.
 *   - content_bn (String, required): Full body in Bengali.
 *   - content_en (String, required): Full body in English.
 *   - content_ar (String, required): Full body in Arabic.
 *   - date (String / Timestamp, required): Publication date string.
 *   - isImportant (Boolean, required): Highlight with golden badge.
 *   - isPublished (Boolean, required): Only published documents are shown to users.
 *   - createdAt (Timestamp, required): Creation timestamp.
 *
 * ----------------------------------------------------------------------------
 * 5. /dua_categories/{categoryId} (Collection)
 * ----------------------------------------------------------------------------
 * Description: Categories for Duas and Supplications.
 * Fields:
 *   - name_bn (String, required): Category title in Bengali.
 *   - name_en (String, required): Category title in English.
 *   - name_ar (String, required): Category title in Arabic.
 *   - icon (String, optional): Material icon name or URL.
 *   - sortOrder (Number, required): Display sequence integer.
 *
 * ----------------------------------------------------------------------------
 * 6. /duas/{duaId} (Collection)
 * ----------------------------------------------------------------------------
 * Description: Authentic Duas with multilingual translations and references.
 * Fields:
 *   - categoryId (String, required): Reference to /dua_categories/{categoryId}.
 *   - title_bn (String, required): Short Dua title in Bengali.
 *   - title_en (String, required): Short Dua title in English.
 *   - title_ar (String, required): Short Dua title in Arabic.
 *   - content_ar (String, required): Original Arabic text with Tashkeel.
 *   - content_bn (String, required): Meaning / translation in Bengali.
 *   - content_en (String, required): Meaning / translation in English.
 *   - reference (String, required): Hadith / Quranic source (e.g. Sahih Muslim 2720).
 *   - isPublished (Boolean, required): Publication status.
 *
 * ----------------------------------------------------------------------------
 * 7. /azkar_categories/{categoryId} & /azkar/{azkarId} (Collections)
 * ----------------------------------------------------------------------------
 * Description: Daily morning, evening, post-prayer, and sleep Azkar.
 * Fields:
 *   - categoryId (String, required): e.g. "morning", "evening", "after_prayer", "sleep".
 *   - title_bn / title_en / title_ar (Strings, required)
 *   - text_ar (String, required): Arabic Dhikr text.
 *   - text_bn / text_en (Strings, required): Translations.
 *   - benefit_bn / benefit_en / benefit_ar (Strings, optional): Rewards / benefits.
 *   - targetCount (Number, required): Recommended repetitions (e.g., 1, 3, 33, 100).
 *   - reference (String, required): Sahih source.
 *
 * ----------------------------------------------------------------------------
 * 8. /users/{userId} (Collection)
 * ----------------------------------------------------------------------------
 * Description: Private user preferences (isolated by Firestore Security Rules).
 * Fields:
 *   - bookmarks (Array of Strings): e.g. ["1:1", "2:255"]
 *   - favoriteDuas (Array of Strings): Dua document IDs
 *   - calculationMethod (String)
 *   - madhab (String)
 *   - language (String): "bn", "en", "ar"
 *   - lastActiveAt (Timestamp)
 *
 * ----------------------------------------------------------------------------
 * FIRESTORE SECURITY RULES ARCHITECTURE
 * ----------------------------------------------------------------------------
 * rules_version = '2';
 * service cloud.firestore {
 *   match /databases/{database}/documents {
 *     // Public readable collections (read-only for all normal app users)
 *     match /branding/{doc} { allow read: if true; allow write: if request.auth.token.admin == true; }
 *     match /app_config/{doc} { allow read: if true; allow write: if request.auth.token.admin == true; }
 *     match /announcements/{doc} { allow read: if resource.data.isPublished == true || request.auth.token.admin == true; allow write: if request.auth.token.admin == true; }
 *     match /dua_categories/{doc} { allow read: if true; allow write: if request.auth.token.admin == true; }
 *     match /duas/{doc} { allow read: if resource.data.isPublished == true || request.auth.token.admin == true; allow write: if request.auth.token.admin == true; }
 *     match /azkar/{doc} { allow read: if true; allow write: if request.auth.token.admin == true; }
 *     match /islamic_calendar/{doc} { allow read: if true; allow write: if request.auth.token.admin == true; }
 *
 *     // User private data (only readable and writable by the authenticated user)
 *     match /users/{userId}/{document=**} {
 *       allow read, write: if request.auth != null && request.auth.uid == userId;
 *     }
 *   }
 * }
 */
object FirestoreSchemaDoc {
    const val COLLECTION_BRANDING = "branding"
    const val COLLECTION_APP_CONFIG = "app_config"
    const val COLLECTION_ANNOUNCEMENTS = "announcements"
    const val COLLECTION_DUAS = "duas"
    const val COLLECTION_DUA_CATEGORIES = "dua_categories"
    const val COLLECTION_AZKAR = "azkar"
    const val COLLECTION_USERS = "users"
}
