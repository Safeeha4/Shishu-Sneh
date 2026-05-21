# SHISHU-SNEH APP - COMPLETE DEVELOPMENT SOP
## Standard Operating Procedure for Android Development with GenAI Integration
---
## 📋 TABLE OF CONTENTS
1. Project Overview
2. Screen-by-Screen Specification
3. Screen Flow & Navigation Architecture
4. Frontend Development Guide
5. Backend Development Guide
6. Database Schema
7. AI Integration Specification
8. Testing & Quality Assurance
9. Deployment Checklist
---
## 1. PROJECT OVERVIEW

**App Name:** Shishu-Sneh (Baby's First Year Guide)  
**Platform:** Android (Kotlin/Java)  
**Target Audience:** New mothers in rural & semi-urban India  
**Primary Goal:** Reduce infant mortality through growth tracking, vaccination reminders, and nutritional guidance  
**Theme:** Dark theme with warm, mother-friendly pastel accents
---
## 2. SCREEN-BY-SCREEN SPECIFICATION
### 🎨 DESIGN THEME SPECIFICATIONS

**Color Palette (Dark Theme):**
- **Primary Background:** `#1A1A2E` (Deep navy blue)
- **Secondary Background:** `#16213E` (Slightly lighter navy)
- **Accent Primary:** `#E94560` (Warm coral pink - for CTAs)
- **Accent Secondary:** `#F4A3A8` (Soft pastel pink)
- **Text Primary:** `#EAEAEA` (Off-white)
- **Text Secondary:** `#B8B8B8` (Light gray)
- **Success:** `#6BCF7F` (Soft green)
- **Warning:** `#FFB84D` (Warm amber)
- **Error:** `#FF6B6B` (Soft red)
- **Card Background:** `#2A2A40` (Elevated surface)

**Typography:**
- **Headers:** Poppins Bold, 24sp-32sp
- **Body:** Roboto Regular, 16sp-18sp
- **Captions:** Roboto Light, 14sp
- **Minimum Touch Target:** 48dp x 48dp
- **Border Radius:** 16dp for cards, 24dp for buttons
---
### SCREEN 1: SPLASH SCREEN

**File Name:** `SplashActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│                             │
│                             │
│         [APP LOGO]          │
│    Mother & Baby Image      │
│      (Illustration)         │
│                             │
│      SHISHU-SNEH            │
│   Baby's First Year Guide   │
│                             │
│     [Bringing expert care to Fringertips...(Quotes like this)*]     │
│                             │
└─────────────────────────────┘
```
**Specifications:**
- **Background:** Gradient from `#1A1A2E` to `#16213E`
- **Logo:** Centered illustration of mother holding baby (warm line art style)
- **App Name:** Poppins Bold, 32sp, `#EAEAEA`
- **Tagline:** Roboto Light, 16sp, `#B8B8B8`
- **Loading Indicator:** Circular progress with `#E94560` color
- **Duration:** 2-3 seconds
- **Auto-transition to:** Language Selection Screen

**Backend Actions:**
- Check if user has completed onboarding
- Initialize Room Database
- Load Firebase Remote Config
- Check network connectivity
- If returning user → Navigate to Dashboard
- If new user → Navigate to Language Selection
### SCREEN 2: LANGUAGE SELECTION SCREEN
**File Name:** `LanguageSelectionActivity.kt`
**Visual Design:**
```
┌─────────────────────────────┐
│    ← Skip                   │
│                             │
│   Select Your Language      │
│   ನಿಮ್ಮ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ       │
│                             │
│  ┌───────────────────────┐  │
│  │    English 🇬🇧         │  │
│  └───────────────────────┘
|  ┌───────────────────────┐  │
│  │     हिंदी   IN          │  │
│  └───────────────────────┘│
│                             │
│  ┌───────────────────────┐  │
│  │    ಕನ್ನಡ 🇮🇳            │  │
│  └───────────────────────┘  │
│                            │
│      [Continue Button]      │
│                             │
└─────────────────────────────┘
```
**Specifications:**
- **Header:** "Select Your Language" in all 3 languages stacked
- **Language Cards:** 
  - Size: Match parent width, 72dp height
  - Border: 2dp, `#E94560` when selected, `#2A2A40` when unselected
  - Text: 20sp, Poppins Medium
  - Ripple effect on tap
- **Continue Button:** 
  - Full width, 56dp height
  - Background: `#E94560`
  - Text: "Continue" (in selected language)
  - Disabled state (gray) until selection made
  - Bottom margin: 24dp

**Backend Actions:**
- Store selected language in SharedPreferences (`app_language`)
- Load language-specific strings.xml
- Update Firebase Analytics property (`user_language`)
- Navigate to Baby Profile Setup Screen

**Data Storage:**
```kotlin
SharedPreferences:
  - KEY: "app_language"
  - VALUES: "en" |"hi" | "kn" 
```
---
### SCREEN 3: BABY PROFILE SETUP SCREEN

**File Name:** `BabyProfileSetupActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back        Step 1 of 1  │
│                             │
│   Create Baby's Profile     │
│                             │
│  ┌───────────────────────┐  │
│  │ 📸 Add Photo          │  │
│  │   (Optional)          │  │
│  └───────────────────────┘  │
│                             │
│  Baby's Name*               │
│  ┌───────────────────────┐  │
│  │ Enter name...         │  │
│  └───────────────────────┘  │
│                             │
│  Date of Birth*             │
│  ┌───────────────────────┐  │
│  │ DD/MM/YYYY 📅         │  │
│  └───────────────────────┘  │
│                             │
│  Birth Weight (kg)*         │
│  ┌───────────────────────┐  │
│  │ 0.00                  │  │
│  └───────────────────────┘  │
│                             │

    Current Height (cm)        │
│  ┌───────────────────────┐  │
│  │ 0.0                   │  │
│  └───────────────────────┘  │
│  	             │
 Gender*                    │
│  [  Male  ] [  Female  ]    │
│                             │
│  
│                             │
│    [Create Profile] ✓       │
│                             │
└─────────────────────────────┘
```

**Specifications:**

**Photo Upload Circle:**
- 120dp diameter
- Dashed border `#B8B8B8`
- Camera icon in center
- On tap: Open camera/gallery picker
- Stores image in app's private storage

**Input Fields:**
- **TextField Style:** OutlinedTextField
- **Border Color:** `#2A2A40` default, `#E94560` focused
- **Label Color:** `#B8B8B8`
- **Error Color:** `#FF6B6B`
- **Corner Radius:** 12dp

**Date Picker:**
- Material DatePicker dialog
- Max date: Today
- Min date: 2 years ago
- Format: DD/MM/YYYY in local format

**Weight Input:**
- Decimal keyboard
- Validation: 1.0 - 8.0 kg
- Helper text: "Typical range: 2.5-4.5 kg"

**Gender Selection:**
- Toggle button group
- Selected: `#E94560` background, white text
- Unselected: Outlined, `#B8B8B8` border

**Height Input:**
- Optional field
- Decimal keyboard
- Validation: 30.0 - 150.0 cm

**Create Profile Button:**
- Enabled only when all required fields valid
- On click: Show loading spinner, save to DB, generate vaccination schedule
- Success: Navigate to Dashboard with animation

**Validation Rules:**
```kotlin
Validations:
  - Name: Min 2 chars, max 50 chars, letters only
  - DOB: Must be <= today, >= 2 years ago
  - Weight: 1.0-8.0 kg
  - Gender: Required selection
  - Height: Optional, 30-80 cm if provided
```

**Backend Actions:**
```kotlin
// Database Insert
BabyProfile(
    id = UUID,
    name = String,
    dateOfBirth = Date,
    birthWeight = Double,
    gender = "male" | "female",
    photoUri = String?,
    createdAt = Timestamp
)

// Auto-generate vaccination schedule
VaccinationScheduleGenerator.generate(babyDOB)

// Schedule WorkManager notifications
VaccinationReminderWorker.schedule(babyId)

// Navigate to Dashboard
startActivity(DashboardActivity)
```

---

### SCREEN 4: DASHBOARD (HOME SCREEN)

**File Name:** `DashboardActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ☰ Menu    Shishu-Sneh  🔔3 │
├─────────────────────────────┤
│  ┌───────────────────────┐  │
│  │  👶 Baby Name, 8 weeks│  │
│  │  Tap to view profile  │  │
│  └───────────────────────┘  │
│                             │
│  💉 Next Vaccination        │
│  ┌───────────────────────┐  │
│  │  DPT Dose 1           │  │
│  │  Due: 15 March 2025   │  │
│  │  In 5 days            │  │
│  │  [View Calendar] →    │  │
│  └───────────────────────┘  │
│                             │
│  💪 Today's Motivation      │
│  ┌───────────────────────┐  │
│  │  "You're doing great! │  │
│  │   Every day you care  │  │
│  │   for your baby is a  │  │
│  │   step toward health."│  │
│  └───────────────────────┘  │
│                             │
│  🍎 Mother's Wellness       │
│  ┌───────────────────────┐  │
│  │  Diet Tips     [View] │  │
│  │  Exercise      [View] │  │
│  │  Mental Health [View] │  │
│  └───────────────────────┘  │
│                             │
│  📊 Quick Actions           │
│  [Growth] [Milestones] [AI] │
│                             │
├─────────────────────────────┤
│ 🏠  📈  💉  🍼  👤         │
└─────────────────────────────┘
```

**Specifications:**

**Top App Bar:**
- Height: 64dp
- Background: `#1A1A2E`
- Menu icon (hamburger): Opens navigation drawer
- App title: Center aligned, Poppins Medium 20sp
- Notification bell: Badge showing unread count (vaccination reminders)

**Baby Profile Card:**
- Rounded card, 16dp radius
- Background: `#2A2A40`
- Baby photo (circular, 48dp) + Name + Age calculation
- Tap: Navigate to Full Profile Screen

**Vaccination Alert Card:**
- Accent border: 2dp, `#E94560` if due within 7 days
- Shows next upcoming vaccine
- Countdown timer in days
- CTA button to vaccination calendar

**Motivation Card:**
- Rotates daily
- Soft gradient background: `#2A2A40` to `#E94560` (10% opacity)
- Inspirational quote for mothers
- Content fetched from Firebase Remote Config

**Mother's Wellness Section:**
- Expandable list items
- **Diet Tips:** Links to nutrition guide screen
- **Exercise:** Postnatal exercise recommendations
- **Mental Health:** Resources for postpartum wellness
- Each item shows preview + "View" button

**Quick Action Buttons:**
- 3 circular buttons
- Icons: Growth chart, checklist, chatbot
- Background: `#E94560`
- Size: 64dp diameter
- Label below each icon

**Bottom Navigation Bar:**
- 5 items: Home, Growth, Vaccination, Profile
- Selected color: `#E94560`
- Unselected: `#B8B8B8`
- Icons: Material Design standard

**Backend Actions:**
```kotlin
onResume():
  - Fetch baby profile from Room DB
  - Calculate baby age in weeks/months
  - Query next vaccination from schedule
  - Load daily motivation from Remote Config
  - Check notification permissions
  - Sync with Firestore (if online)

Real-time Updates:
  - Vaccination countdown (LiveData)
  - Age calculation (computed property)
  - Notification badge count
```

**Data Bindings:**
```kotlin
ViewModel Data:
  - babyProfile: LiveData<BabyProfile>
  - nextVaccine: LiveData<VaccineRecord>
  - dailyMotivation: String
  - notificationCount: Int
  - motherWellnessTips: List<WellnessTip>
```

---

### SCREEN 5: GROWTH TRACKING SCREEN

**File Name:** `GrowthTrackingFragment.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back    Growth Chart     │
├─────────────────────────────┤
│                             │
│  📊 Weight & Height Trends  │
│                             │
│  ┌───────────────────────┐  │
│  │       Graph Area      │  │
│  │   ╱──────────────     │  │
│  │  ╱          ●         │  │
│  │ ●       ●             │  │
│  │ WHO 97th -------      │  │
│  │ WHO 50th -------      │  │
│  │ WHO 3rd  -------      │  │
│  │                       │  │
│  │ Month: 1 2 3 4 5 6    │  │
│  └───────────────────────┘  │
│                             │
│  [Weight] [Height] [Both]   │
│                             │
│  📝 Add New Measurement     │
│  ┌───────────────────────┐  │
│  │ Date: Today  📅       │  │
│  │                       │  │
│  │ Weight (kg): [  4.5 ] │  │
│  │ Height (cm): [ 55.0 ] │  │
│  │                       │  │
│  │  [Save Measurement]   │  │
│  └───────────────────────┘  │
│                             │
│  📋 Measurement History     │
│  ┌───────────────────────┐  │
│  │ 10 Mar • 4.5kg • 55cm │  │
│  │ 10 Feb • 4.2kg • 53cm │  │
│  │ 10 Jan • 3.8kg • 51cm │  │
│  └───────────────────────┘  │
│                             │
└─────────────────────────────┘
```

**Specifications:**

**Chart Component (MPAndroidChart):**
- Library: `com.github.PhilJay:MPAndroidChart:v3.1.0`
- Chart Type: LineChart
- Dimensions: Full width, 300dp height
- Background: `#2A2A40`
- Grid: `#16213E` color, dashed

**Data Sets:**
1. **Baby's Weight:** 
   - Line color: `#E94560`
   - Point style: Filled circles, 8dp
   - Line width: 3dp

2. **WHO Reference Lines:**
   - 97th percentile: `#6BCF7F` dashed
   - 50th percentile: `#FFB84D` dashed
   - 3rd percentile: `#FF6B6B` dashed
   - Line width: 2dp

**Chart Features:**
- X-axis: Months (0-12)
- Y-axis: Weight (kg) or Height (cm)
- Pinch to zoom enabled
- Double-tap to reset zoom
- Long-press on point shows tooltip
- Legend showing WHO percentiles

**Toggle Buttons:**
- 3 options: Weight, Height, Both
- Active: `#E94560` background
- Inactive: Outlined `#2A2A40`

**Add Measurement Card:**
- Date picker (default: today)
- Weight input: Decimal, 0.1-15.0 kg
- Height input: Decimal, 30-100 cm
- Save button: Validates inputs, writes to DB, refreshes chart

**Measurement History:**
- RecyclerView with CardViews
- Shows last 12 entries
- Format: "DD MMM • X.Xkg • XXcm"
- Tap to edit (dialog)
- Swipe to delete (confirmation)

**Growth Status Indicator:**
```kotlin
Status Logic:
  if (currentWeight < WHO_3rd_percentile):
    Show warning: "Below expected range - consult doctor"
    Color: #FF6B6B
  elif (currentWeight > WHO_97th_percentile):
    Show info: "Above expected range - routine checkup"
    Color: #FFB84D
  else:
    Show success: "Healthy growth!"
    Color: #6BCF7F
```

**Backend Actions:**
```kotlin
Database:
  - Table: GrowthEntry
  - Columns: id, babyId, date, weight, height, createdAt

Chart Data Loading:
  fun loadChartData(babyId: UUID): List<Entry> {
    val entries = growthDao.getEntriesByBaby(babyId)
    val whoData = WHOStandards.getPercentiles(babyGender)
    return entries.map { 
      Entry(monthAge, weight, metadata)
    }
  }

WHO Data:
  - Stored as JSON in assets/who_standards.json
  - Loaded on app init
  - Separate curves for boys/girls
```

**Data Model:**
```kotlin
@Entity
data class GrowthEntry(
    @PrimaryKey val id: UUID,
    val babyId: UUID,
    val date: Date,
    val weight: Double, // kg
    val height: Double?, // cm, optional
    val note: String?,
    val createdAt: Long
)
```

---

### SCREEN 6: VACCINATION CALENDAR SCREEN

**File Name:** `VaccinationCalendarFragment.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back  Vaccination Plan   │
├─────────────────────────────┤
│                             │
│  🗓️ Next: DPT-1 in 5 days   │
│                             │
│  ┌───────────────────────┐  │
│  │ 🔔 Upcoming (2)       │  │
│  ├───────────────────────┤  │
│  │ ● DPT Dose 1          │  │
│  │   Due: 15 Mar 2025    │  │
│  │   Prevents: Diphtheria│  │
│  │   Tetanus, Pertussis  │  │
│  │   [Mark as Done] ✓    │  │
│  │   [Learn More] →      │  │
│  ├───────────────────────┤  │
│  │ ● Polio Dose 2        │  │
│  │   Due: 22 Mar 2025    │  │
│  │   [Details]           │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ ✅ Completed (3)      │  │
│  ├───────────────────────┤  │
│  │ ✓ BCG                 │  │
│  │   Given: 10 Jan 2025  │  │
│  ├───────────────────────┤  │
│  │ ✓ OPV Birth Dose      │  │
│  │   Given: 10 Jan 2025  │  │
│  ├───────────────────────┤  │
│  │ ✓ Hepatitis B         │  │
│  │   Given: 10 Jan 2025  │  │
│  └───────────────────────┘  │
│                             │
│  [View Full Schedule PDF]   │
│                             │
└─────────────────────────────┘
```

**Specifications:**

**Next Vaccine Header:**
- Prominent card at top
- Background: Gradient `#E94560` to `#F4A3A8`
- Large text: Vaccine name + countdown
- Pulsing animation if due within 3 days

**Vaccine List Sections:**

**1. Upcoming Vaccines:**
- Accent border: `#FFB84D`
- Sorted by date (earliest first)
- Shows: Name, due date, disease prevented
- **Mark as Done Button:**
  - On click: Show date confirmation dialog
  - Moves to completed section
  - Cancels scheduled notification
  - Shows confetti animation
- **Learn More:** Opens vaccine info bottom sheet

**2. Completed Vaccines:**
- Green checkmark icon
- Gray text color
- Shows date administered
- Collapsible section

**Vaccine Card Details:**
```
┌─────────────────────────┐
│ 💉 DPT Dose 1           │
│ Due: 15 March 2025      │
│ Age: 6 weeks            │
│                         │
│ Prevents:               │
│ • Diphtheria            │
│ • Tetanus               │
│ • Pertussis (Whooping   │
│   Cough)                │
│                         │
│ [Mark as Done] [Info]   │
└─────────────────────────┘
```

**Vaccine Information Bottom Sheet:**
```kotlin
Components:
  - Vaccine name header
  - Disease information (expandable)
  - Side effects to expect
  - What to do after vaccination
  - Emergency signs (when to call doctor)
  - Close button
```

**Full Schedule PDF:**
- Generates PDF using iText library
- Includes baby name, DOB
- Complete vaccine table
- Shareable via WhatsApp/Email
- Saved to Downloads folder

**Backend Actions:**
```kotlin
Vaccination Schedule Generation:
  fun generateSchedule(babyDOB: Date): List<VaccineRecord> {
    val schedule = IndianVaccinationSchedule.load()
    return schedule.map { vaccine ->
      VaccineRecord(
        name = vaccine.name,
        dueDate = babyDOB + vaccine.ageOffset,
        diseases = vaccine.prevents,
        status = PENDING
      )
    }
  }

Notification Scheduling:
  fun scheduleReminders(vaccines: List<VaccineRecord>) {
    vaccines.forEach { vaccine ->
      // 3 days before
      WorkManager.schedule(
        VaccineReminderWorker(
          vaccineId = vaccine.id,
          triggerAt = vaccine.dueDate - 3.days
        )
      )
      // On the day
      WorkManager.schedule(
        VaccineReminderWorker(
          vaccineId = vaccine.id,
          triggerAt = vaccine.dueDate
        )
      )
    }
  }
```

**Data Model:**
```kotlin
@Entity
data class VaccineRecord(
    @PrimaryKey val id: UUID,
    val babyId: UUID,
    val name: String,
    val dueDate: Date,
    val givenDate: Date?,
    val status: VaccineStatus, // PENDING, COMPLETED, SKIPPED
    val diseases: List<String>,
    val dosageNumber: Int,
    val notes: String?
)

enum class VaccineStatus {
    PENDING, COMPLETED, SKIPPED, OVERDUE
}
```

**Indian Vaccination Schedule (JSON):**
```json
[
  {
    "name": "BCG",
    "ageOffset": "0 weeks",
    "prevents": ["Tuberculosis"],
    "dosageCount": 1
  },
  {
    "name": "OPV Birth Dose",
    "ageOffset": "0 weeks",
    "prevents": ["Poliomyelitis"]
  },
  {
    "name": "Hepatitis B Birth Dose",
    "ageOffset": "0 weeks",
    "prevents": ["Hepatitis B"]
  },
  {
    "name": "DPT Dose 1",
    "ageOffset": "6 weeks",
    "prevents": ["Diphtheria", "Tetanus", "Pertussis"]
  },
  // ... continues for all vaccines
]
```

---

### SCREEN 7: MILESTONE TRACKING SCREEN

**File Name:** `MilestoneTrackingFragment.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back    Baby Milestones  │
├─────────────────────────────┤
│                             │
│  Week 8 Developmental Check │
│                             │
│  ┌───────────────────────┐  │
│  │ 👶 Physical           │  │
│  ├───────────────────────┤  │
│  │ □ Holds head up       │  │
│  │   briefly when on     │  │
│  │   tummy?              │  │
│  │   [Yes] [No] [Skip]   │  │
│  ├───────────────────────┤  │
│  │ ✓ Moves arms and      │  │
│  │   legs actively?      │  │
│  │   Answered: Yes       │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 🧠 Cognitive          │  │
│  ├───────────────────────┤  │
│  │ □ Tracks objects with │  │
│  │   eyes?               │  │
│  │   [Yes] [No] [Skip]   │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 😊 Social             │  │
│  ├───────────────────────┤  │
│  │ □ Smiles at people?   │  │
│  │   [Yes] [No] [Skip]   │  │
│  └───────────────────────┘  │
│                             │
│  Progress: 3/8 completed    │
│  ▓▓▓▓▓▓░░░░░░░░░░░          │
│                             │
│  [Save & Continue]          │
│                             │
└─────────────────────────────┘
```

**Specifications:**

**Milestone Categories:**
1. **Physical:** Gross motor skills
2. **Cognitive:** Learning, thinking
3. **Social:** Interaction, emotions
4. **Communication:** Speech, gestures

**Milestone Card:**
- Question text: 18sp, clear language
- Answer buttons: Large, 48dp height
- Yes (green), No (amber), Skip (gray)
- Visual feedback: Ripple + checkmark animation

**Age-Based Loading:**
```kotlin
fun loadMilestones(babyAgeWeeks: Int): List<Milestone> {
  val milestones = Firebase.remoteConfig
    .getString("milestones_week_$babyAgeWeeks")
  return Json.decode(milestones)
}
```

**Delayed Milestone Alert:**
```
If "No" selected for critical milestone:
┌─────────────────────────┐
│ ⚠️ Gentle Reminder      │
├─────────────────────────┤
│ Every baby develops at  │
│ their own pace. If you  │
│ have concerns, consider │
│ discussing with your    │
│ pediatrician.           │
│                         │
│ This is normal          │
│ development variation.  │
│                         │
│ [Got it] [Contact Doc]  │
└─────────────────────────┘
```

**Progress Tracking:**
- Visual progress bar
- Percentage completed
- Saves intermediate state (can return later)

**Backend Actions:**
```kotlin
@Entity
data class MilestoneLog(
    @PrimaryKey val id: UUID,
    val babyId: UUID,
    val milestoneId: String,
    val week: Int,
    val question: String,
    val answer: Answer, // YES, NO, SKIPPED
    val answeredAt: Date,
    val category: Category
)

enum class Answer { YES, NO, SKIPPED }
enum class Category { PHYSICAL, COGNITIVE, SOCIAL, COMMUNICATION }

// Weekly notification
WorkManager.scheduleWeekly(
  MilestoneReminderWorker(babyId)
)
```

**Milestone Content (Firebase Remote Config):**
```json
{
  "week_8": [
    {
      "id": "m_8_p1",
      "category": "physical",
      "question_en": "Does baby hold head up briefly when on tummy?",
      "question_kn": "ಹೊಟ್ಟೆಯ ಮೇಲೆ ಇರುವಾಗ ಮಗು ತಲೆಯನ್ನು ಸ್ವಲ್ಪ ಸಮಯ ಮೇಲಕ್ಕೆ ಹಿಡಿದಿಟ್ಟುಕೊಳ್ಳುತ್ತದೆಯೇ?",
      "critical": true
    }
  ]
}
```

---

### SCREEN 8: MOTHER'S WELLNESS SCREEN

**File Name:** `MotherWellnessFragment.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back  Mother's Wellness  │
├─────────────────────────────┤
│                             │
│  👩 Your Health Matters Too │
│                             │
│  ┌───────────────────────┐  │
│  │ 🥗 Diet & Nutrition   │  │
│  ├───────────────────────┤  │
│  │ • Postnatal nutrition │  │
│  │ • Lactation foods     │  │
│  │ • Iron-rich meals     │  │
│  │ [View Full Guide] →   │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 🧘 Exercise & Rest    │  │
│  ├───────────────────────┤  │
│  │ • Pelvic floor        │  │
│  │   exercises           │  │
│  │ • Walking routine     │  │
│  │ • Sleep tips          │  │
│  │ [View Exercises] →    │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 💚 Mental Health      │  │
│  ├───────────────────────┤  │
│  │ • Postpartum emotions │  │
│  │ • Stress management   │  │
│  │ • Support resources   │  │
│  │ [Get Support] →       │  │
│  └───────────────────────┘  │
│                             │
│  📅 Today's Tip             │
│  ┌───────────────────────┐  │
│  │ Drink water every time│  │
│  │ you breastfeed. Stay  │  │
│  │ hydrated for better   │  │
│  │ milk production! 💧   │  │
│  └───────────────────────┘  │
│                             │
└─────────────────────────────┘
```

**Sub-Screen: Diet Guide**
```
┌─────────────────────────────┐
│  ← Back    Diet Guide       │
├─────────────────────────────┤
│                             │
│  🥗 Postnatal Nutrition     │
│                             │
│  Essential Foods:           │
│  ┌───────────────────────┐  │
│  │ 🥛 Dairy               │  │
│  │ Milk, curd, paneer    │  │
│  │ Benefits: Calcium,    │  │
│  │ protein for milk      │  │
│  │ production            │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 🍃 Green Leafy Veg    │  │
│  │ Spinach, fenugreek    │  │
│  │ Benefits: Iron,       │  │
│  │ prevents anemia       │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 🌰 Nuts & Seeds       │  │
│  │ Almonds, sesame       │  │
│  │ Benefits: Energy,     │  │
│  │ healthy fats          │  │
│  └───────────────────────┘  │
│                             │
│  Sample Meal Plan →         │
│  Foods to Avoid →           │
│  Hydration Tips →           │
│                             │
└─────────────────────────────┘
```

**Sub-Screen: Mental Health Resources**
```
┌─────────────────────────────┐
│  ← Back  Mental Wellness    │
├─────────────────────────────┤
│                             │
│  💚 You're Not Alone        │
│                             │
│  Common Feelings:           │
│  • Mood swings ✓           │
│  • Overwhelming tiredness ✓│
│  • Anxiety about baby ✓    │
│  • Feeling tearful ✓       │
│                             │
│  These are normal in the    │
│  first weeks after delivery.│
│                             │
│  ⚠️ Seek Help If:           │
│  • Persistent sadness       │
│  • Loss of interest in baby │
│  • Thoughts of harm         │
│  • Unable to sleep          │
│                             │
│  [Helpline Numbers]         │
│  [Talk to AI Companion]     │
│  [Find Support Group]       │
│                             │
│  Emergency: 📞 104          │
│  (Health Helpline)          │
│                             │
└─────────────────────────────┘
```

**Backend Actions:**
```kotlin
Content Structure:
  - Stored in Firebase Remote Config
  - Organized by category + language
  - Daily rotation for tips

Analytics Tracking:
  - Log which sections viewed
  - Track helpline button taps
  - Monitor engagement time

Data Model:
@Entity
data class WellnessTip(
    @PrimaryKey val id: UUID,
    val category: String, // diet, exercise, mental
    val title: String,
    val content: String,
    val benefits: List<String>,
    val language: String,
    val imageUrl: String?
)

### SCREEN 9: AI CHATBOT SCREEN

**File Name:** `AIChatbotActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Close    AI Companion    │
├─────────────────────────────┤
│                             │
│  🤖 Shishu AI               │
│  "I'm here to help with     │
│   your parenting questions" │
│                             │
│  ┌───────────────────────┐  │
│  │ Hi! How can I help?   │◄──Bot
│  │ Ask about baby care,  │  │
│  │development. │  │
│  └───────────────────────┘  │
│                             │
│           ┌───────────────┐ │
│   You────►│ My baby cries │ │
│           │ a lot at night│ │
│           └───────────────┘ │
│                             │
│  ┌───────────────────────┐  │
│  │ Night crying is common│◄──Bot
│  │ in young babies. Here │  │
│  │ are some tips:        │  │
│  │                       │  │
│  │ 1. Check diaper       │  │
│  │ 2. Feed if hungry     │  │
│  │ 3. Comfort & swaddle  │  │
│  │ 4. White noise helps  │  │
│  │                       │  │
│  │ How old is your baby? │  │
│  └───────────────────────┘  │
│                             │
│  Quick Questions:           │
│   [Sleep] [Health] │
│                             │
├─────────────────────────────┤
│ 🎤 | Type your question... |│
│    |                      ↑││
└─────────────────────────────┘
```

**Specifications:**

**Chat Interface:**
- Material Design chat bubbles
- Bot messages: Left-aligned, `#2A2A40` background
- User messages: Right-aligned, `#E94560` background
- Timestamp: 12sp, gray, below each message
- Avatar: Robot icon for bot, user initial for user

**Message Types:**
1. **Text:** Standard messages
2. **Quick Replies:** Buttons for common questions
3. **Cards:** Rich content (e.g., vaccine info)
4. **Images:** Illustrations for techniques

**Input Methods:**
- **Text:** Keyboard input
- **Voice:** Speech-to-text (ML Kit)
- **Language:** Respects app language setting

**Quick Question Buttons:**
```kotlin
Categories:
  - Sleep (sleep training)
  - Health (symptoms checker)
  - Development (milestones)
  - Vaccination (vaccine info)
  - Mother Wellness (self-care)
```

**Safety Features:**
```kotlin
System Prompt:
"You are Shishu AI, a compassionate pediatric health 
assistant for new mothers in India. Provide evidence-based 
guidance on infant care, and development.

STRICT RULES:
- Only answer questions about baby/mother health
- Never diagnose medical conditions
- Always recommend doctor visit for serious symptoms
- Be culturally sensitive to Indian context
- Provide answers in user's selected language
- Cite WHO guidelines when relevant

If asked about topics outside infant care, politely 
redirect to your scope."
```

**Medical Disclaimer:**
```
Displayed on first launch:
┌─────────────────────────┐
│ ⚕️ Important Notice     │
├─────────────────────────┤
│ Shishu AI provides      │
│ general guidance only.  │
│                         │
│ NOT a replacement for   │
│ professional medical    │
│ advice. Always consult  │
│ your pediatrician for   │
│ health concerns.        │
│                         │
│ [I Understand]          │
└─────────────────────────┘
```

**Backend Integration:**

```kotlin
// Gemini API Setup
val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    apiKey = BuildConfig.GEMINI_API_KEY,
    systemInstruction = content { text(SYSTEM_PROMPT) }
)

// Chat Session
val chat = generativeModel.startChat(
    history = previousMessages.map {
        content(it.role) { text(it.text) }
    }
)

// Send Message
suspend fun sendMessage(userInput: String): String {
    val response = chat.sendMessage(userInput)
    return response.text ?: "I'm having trouble responding. Please try again."
}

// Streaming Response (for typing effect)
chat.sendMessageStream(userInput).collect { chunk ->
    updateUI(chunk.text)
}
```

**Conversation Logging:**
```kotlin
@Entity
data class ChatMessage(
    @PrimaryKey val id: UUID,
    val userId: UUID,
    val role: Role, // USER, BOT
    val message: String,
    val timestamp: Date,
    val language: String
)

// Privacy: Stored locally only
// Optional cloud sync if user enables
```

**Context Awareness:**
```kotlin
// Include baby profile in context
val context = """
Baby: ${baby.name}, ${baby.ageWeeks} weeks old
Gender: ${baby.gender}
Recent vaccines: ${recentVaccines}
Recent growth: ${latestWeight}kg
"""

// Prepend to user query for personalized responses
```

---

### SCREEN 10: SETTINGS SCREEN

**File Name:** `SettingsActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back    Settings         │
├─────────────────────────────┤
│                             │
│  👤 Account                 │
│  ┌───────────────────────┐  │
│  │ Mother's Name         │  │
│  │ Phone: +91 98765...   │  │
│  │ [Edit Profile]        │  │
│  └───────────────────────┘  │
│                             │
│  🌐 Language                │
│  ┌───────────────────────┐  │
│  │ Current: English      │  │
│  │ [Change Language] →   │  │
│  └───────────────────────┘  │
│                             │
│  🔔 Notifications           │
│  ┌───────────────────────┐  │
│  │ ☑ Vaccination reminders│  │
│  │ ☑ Milestone check-ins │  │
│  │ ☑ Daily tips          │  │
│  │ ☑ Growth tracking     │  │
│  └───────────────────────┘  │
│                             │
│  ☁️ Data & Sync             │
│  ┌───────────────────────┐  │
│  │ Cloud Backup: OFF     │  │
│  │ [Enable Sync]         │  │
│  │                       │  │
│  │ Last backup: Never    │  │
│  │ [Backup Now]          │  │
│  └───────────────────────┘  │
│                             │
│  🎨 Appearance              │
│  ┌───────────────────────┐  │
│  │ Theme: Dark (Default) │  │
│  └───────────────────────┘  │
│                             │
│  ℹ️ About                   │
│  Version 1.0.0              │
│  [Privacy Policy]           │
│  [Terms of Service]         │
│  [Help & Support]           │
│                             │
│  🚪 [Logout]                │
│                             │
└─────────────────────────────┘
```

**Notification Settings Detail:**
```
Each toggle shows:
  - Name of notification type
  - Brief description
  - Last sent time
  - Test button to preview
```

**Cloud Sync Feature:**
```
Enable Sync Dialog:
┌─────────────────────────┐
│ 🔐 Secure Cloud Backup  │
├─────────────────────────┤
│ Your data will be:      │
│ • Encrypted end-to-end  │
│ • Synced across devices │
│ • Recoverable if phone  │
│   is lost               │
│                         │
│ Requires Google account │
│                         │
│ [Sign In] [Cancel]      │
└─────────────────────────┘
```

**Data Management:**
```kotlin
SharedPreferences Keys:
  - notifications_vaccine: Boolean
  - notifications_milestone: Boolean
  - notifications_tips: Boolean
  - cloud_sync_enabled: Boolean
  - last_backup_time: Long
  - theme_mode: String ("dark" | "light")

Firebase Sync:
  fun enableCloudSync() {
    FirebaseAuth.signIn()
    syncLocalDataToFirestore()
    enableAutoSync()
  }
```

---

### SCREEN 11: BABY PROFILE DETAIL SCREEN

**File Name:** `BabyProfileActivity.kt`

**Visual Design:**
```
┌─────────────────────────────┐
│  ← Back    Baby Profile     │
├─────────────────────────────┤
│                             │
│       ┌─────────┐           │
│       │  Photo  │           │
│       └─────────┘           │
│         [Edit]              │
│                             │
│  Name: Aarav                │
│  DOB: 10 January 2025       │
│  Age: 2 months, 1 week      │
│  Gender: Male               │
│                             │
│  Birth Details:             │
│  • Weight: 3.2 kg           │
│  • Height: 50 cm            │
│                             │
│  Current Stats:             │
│  • Weight: 5.1 kg (+59%)    │
│  • Height: 57 cm (+14%)     │
│  • Last measured: 5 days ago│
│                             │
│  Health Summary:            │
│  • Vaccines: 4/14 completed │
│  • Growth: Healthy trend ✓  │
│  • Milestones: On track ✓   │
│                             │
│  [Edit Profile]             │
│  [View Full Growth Chart]   │
│  [Download Health Report]   │
│                             │
│  ⚠️ [Delete Profile]        │
│                             │
└─────────────────────────────┘
```

**Edit Profile:**
- Same form as profile creation
- Pre-filled with existing data
- Update validation rules apply

**Health Report Generation:**
```
PDF Report Includes:
  - Baby photo + details
  - Growth chart (last 6 months)
  - Vaccination record (completed + upcoming)
  - Milestone achievements
  - Generated date
  - Shareable via WhatsApp/Email
```

---

## 3. SCREEN FLOW & NAVIGATION ARCHITECTURE

### Navigation Graph

```
SplashScreen
    ├─→ [New User] LanguageSelection
    │              └─→ BabyProfileSetup
    │                   └─→ Dashboard
    │
    └─→ [Returning User] Dashboard

Dashboard (Main Hub)
    ├─→ Bottom Nav: Home (Dashboard)
    ├─→ Bottom Nav: Growth Chart
    ├─→ Bottom Nav: Vaccination Calendar
    ├─→ Bottom Nav: Profile Settings
    │
    ├─→ FAB: AI Chatbot (floating, always accessible)
    │
    ├─→ Navigation Drawer:
    │   ├─→ Baby Profile Detail
    │   ├─→ Milestone Tracking
    │   ├─→ Mother's Wellness
    │   ├─→ Settings
    │   ├─→ Help & Support
    │   └─→ About App
    │
    └─→ Quick Actions:
        ├─→ Add Growth Entry (dialog)
        ├─→ Mark Vaccine Done (dialog)
        └─→ Daily Tip Detail (bottom sheet)
```

### Deep Links

```kotlin
// Notification deep links
shishusneh://vaccine/{vaccineId}
shishusneh://milestone/{week}
shishusneh://growth/add
shishusneh://tip/{tipId}

// Handle in MainActivity
navController.navigate(deepLink)
```

### Back Stack Management

```kotlin
Navigation Rules:
  - Dashboard is root (back press = exit app dialog)
  - All other screens: back press = return to previous
  - AI Chatbot: Slides up modally, close = return
  - Settings: Standard back navigation
  - Profile setup: Back disabled (must complete)
```

---

## 4. FRONTEND DEVELOPMENT GUIDE

### 🏗️ Architecture: MVVM + Repository Pattern

```
Presentation Layer (UI)
    ↓
ViewModel (Business Logic)
    ↓
Repository (Data Coordination)
    ↓
Data Sources (Room DB, Firebase, Gemini API)
```

### Project Structure

```
app/
├── src/main/
│   ├── java/com/shishusneh/
│   │   ├── ui/
│   │   │   ├── splash/
│   │   │   │   ├── SplashActivity.kt
│   │   │   │   └── SplashViewModel.kt
│   │   │   ├── language/
│   │   │   │   ├── LanguageSelectionActivity.kt
│   │   │   │   └── LanguageViewModel.kt
│   │   │   ├── profile/
│   │   │   │   ├── BabyProfileSetupActivity.kt
│   │   │   │   ├── BabyProfileViewModel.kt
│   │   │   │   └── BabyProfileDetailActivity.kt
│   │   │   ├── dashboard/
│   │   │   │   ├── DashboardActivity.kt
│   │   │   │   ├── DashboardViewModel.kt
│   │   │   │   └── DashboardFragment.kt
│   │   │   ├── growth/
│   │   │   │   ├── GrowthTrackingFragment.kt
│   │   │   │   ├── GrowthViewModel.kt
│   │   │   │   └── GrowthChartView.kt
│   │   │   ├── vaccination/
│   │   │   │   ├── VaccinationCalendarFragment.kt
│   │   │   │   ├── VaccinationViewModel.kt
│   │   │   │   └── VaccineDetailBottomSheet.kt
│   │   │   ├── milestone/
│   │   │   │   ├── MilestoneTrackingFragment.kt
│   │   │   │   └── MilestoneViewModel.kt
│   │   │   ├── mother/
│   │   │   │   ├── MotherWellnessFragment.kt
│   │   │   │   └── MotherWellnessViewModel.kt
│   │   │   ├── chatbot/
│   │   │   │   ├── AIChatbotActivity.kt
│   │   │   │   ├── ChatViewModel.kt
│   │   │   │   └── ChatAdapter.kt
│   │   │   ├── settings/
│   │   │   │   ├── SettingsActivity.kt
│   │   │   │   └── SettingsViewModel.kt
│   │   │   └── common/
│   │   │       ├── components/
│   │   │       ├── dialogs/
│   │   │       └── adapters/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── database/
│   │   │   │   │   ├── ShishuSnehDatabase.kt
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── BabyProfileDao.kt
│   │   │   │   │   │   ├── GrowthEntryDao.kt
│   │   │   │   │   │   ├── VaccineRecordDao.kt
│   │   │   │   │   │   ├── MilestoneLogDao.kt
│   │   │   │   │   │   └── ChatMessageDao.kt
│   │   │   │   │   └── entities/
│   │   │   │   │       ├── BabyProfile.kt
│   │   │   │   │       ├── GrowthEntry.kt
│   │   │   │   │       ├── VaccineRecord.kt
│   │   │   │   │       ├── MilestoneLog.kt
│   │   │   │   │       └── ChatMessage.kt
│   │   │   │   └── preferences/
│   │   │   │       └── AppPreferences.kt
│   │   │   ├── remote/
│   │   │   │   ├── firebase/
│   │   │   │   │   ├── FirestoreService.kt
│   │   │   │   │   ├── RemoteConfigService.kt
│   │   │   │   │   └── AuthService.kt
│   │   │   │   └── gemini/
│   │   │   │       └── GeminiService.kt
│   │   │   └── repository/
│   │   │       ├── BabyRepository.kt
│   │   │       ├── GrowthRepository.kt
│   │   │       ├── VaccinationRepository.kt
│   │   │       ├── MilestoneRepository.kt
│   │   │       ├── ContentRepository.kt
│   │   │       └── ChatRepository.kt
│   │   ├── domain/
│   │   │   ├── models/
│   │   │   └── usecases/
│   │   ├── utils/
│   │   │   ├── Constants.kt
│   │   │   ├── DateUtils.kt
│   │   │   ├── ValidationUtils.kt
│   │   │   ├── NotificationHelper.kt
│   │   │   └── WHOStandards.kt
│   │   └── workers/
│   │       ├── VaccinationReminderWorker.kt
│   │       ├── MilestoneReminderWorker.kt
│   │       └── DataSyncWorker.kt
│   ├── res/
│   │   ├── layout/
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   ├── themes.xml
│   │   │   └── dimens.xml
│   │   ├── values-kn/  (Kannada)
│   │   │   └── strings.xml
│   │   ├── values-hi/  (Hindi)
│   │   │   └── strings.xml
│   │   ├── drawable/
│   │   ├── mipmap/
│   │   └── navigation/
│   │       └── nav_graph.xml
│   └── assets/
│       ├── who_standards_boys.json
│       ├── who_standards_girls.json
│       └── vaccination_schedule_india.json
└── build.gradle
```

### build.gradle (Module: app)

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'com.google.gms.google-services'
    id 'androidx.navigation.safeargs.kotlin'
}

android {
    namespace 'com.shishusneh'
    compileSdk 34

    defaultConfig {
        applicationId "com.shishusneh"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"

        // Gemini API Key (store in local.properties)
        buildConfigField "String", "GEMINI_API_KEY", "\"${getApiKey()}\""
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    buildFeatures {
        viewBinding true
        dataBinding true
        buildConfig true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }
}

def getApiKey() {
    def properties = new Properties()
    properties.load(project.rootProject.file('local.properties').newDataInputStream())
    return properties.getProperty('GEMINI_API_KEY', '""')
}

dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Lifecycle & ViewModel
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-ktx:1.8.2'
    implementation 'androidx.fragment:fragment-ktx:1.6.2'

    // Navigation
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.6'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.6'

    // Room Database
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'

    // SQLCipher (Encryption)
    implementation 'net.zetetic:android-database-sqlcipher:4.5.4'
    implementation 'androidx.sqlite:sqlite-ktx:2.4.0'

    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-config-ktx'
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation 'com.google.firebase:firebase-crashlytics-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'

    // Gemini AI
    implementation 'com.google.ai.client.generativeai:generativeai:0.1.2'

    // WorkManager
    implementation 'androidx.work:work-runtime-ktx:2.9.0'

    // MPAndroidChart
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    // Image Loading
    implementation 'io.coil-kt:coil:2.5.0'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3'

    // Retrofit (if needed for future APIs)
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'

    // ML Kit (Speech-to-Text)
    implementation 'com.google.mlkit:speech-recognition:16.0.0'

    // PDF Generation
    implementation 'com.itextpdf:itext7-core:7.2.5'

    // Splash Screen API
    implementation 'androidx.core:core-splashscreen:1.0.1'

    // Lottie Animations
    implementation 'com.airbnb.android:lottie:6.2.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

### Key UI Components

#### 1. Custom Growth Chart View

```kotlin
// GrowthChartView.kt
class GrowthChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LineChart(context, attrs) {

    init {
        setupChart()
    }

    private fun setupChart() {
        description.isEnabled = false
        setTouchEnabled(true)
        setPinchZoom(true)
        setDrawGridBackground(false)
        
        // X-axis (Months)
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            textColor = Color.parseColor("#EAEAEA")
            valueFormatter = MonthAxisValueFormatter()
        }
        
        // Y-axis (Weight/Height)
        axisLeft.apply {
            setDrawGridLines(true)
            gridColor = Color.parseColor("#16213E")
            textColor = Color.parseColor("#EAEAEA")
        }
        
        axisRight.isEnabled = false
        legend.textColor = Color.parseColor("#EAEAEA")
    }

    fun loadGrowthData(
        babyData: List<Entry>,
        whoPercentiles: Map<Int, List<Entry>>,
        dataType: GrowthType
    ) {
        val dataSets = mutableListOf<ILineDataSet>()
        
        // Baby's actual data
        val babyDataSet = LineDataSet(babyData, "Baby's ${dataType.name}").apply {
            color = Color.parseColor("#E94560")
            setCircleColor(Color.parseColor("#E94560"))
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            valueTextColor = Color.parseColor("#EAEAEA")
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        dataSets.add(babyDataSet)
        
        // WHO percentile lines
        addWHOPercentileLine(dataSets, whoPercentiles[97], "97th", "#6BCF7F")
        addWHOPercentileLine(dataSets, whoPercentiles[50], "50th", "#FFB84D")
        addWHOPercentileLine(dataSets, whoPercentiles[3], "3rd", "#FF6B6B")
        
        data = LineData(dataSets)
        invalidate() // Refresh chart
    }
    
    private fun addWHOPercentileLine(
        dataSets: MutableList<ILineDataSet>,
        entries: List<Entry>?,
        label: String,
        color: String
    ) {
        entries?.let {
            val dataSet = LineDataSet(it, "WHO $label").apply {
                this.color = Color.parseColor(color)
                lineWidth = 2f
                setDrawCircles(false)
                enableDashedLine(10f, 5f, 0f)
                setDrawValues(false)
            }
            dataSets.add(dataSet)
        }
    }
}

enum class GrowthType {
    WEIGHT, HEIGHT
}

class MonthAxisValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return "${value.toInt()} mo"
    }
}
```

#### 2. Vaccination Calendar Adapter

```kotlin
// VaccinationAdapter.kt
class VaccinationAdapter(
    private val onMarkDone: (VaccineRecord) -> Unit,
    private val onLearnMore: (VaccineRecord) -> Unit
) : ListAdapter<VaccineRecord, VaccinationAdapter.VaccineViewHolder>(VaccineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val binding = ItemVaccineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VaccineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VaccineViewHolder(
        private val binding: ItemVaccineBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vaccine: VaccineRecord) {
            binding.apply {
                tvVaccineName.text = vaccine.name
                tvDueDate.text = formatDate(vaccine.dueDate)
                tvDiseases.text = vaccine.diseases.joinToString(", ")
                
                // Status styling
                when (vaccine.status) {
                    VaccineStatus.COMPLETED -> {
                        cardVaccine.strokeColor = Color.parseColor("#6BCF7F")
                        ivStatus.setImageResource(R.drawable.ic_check)
                        btnMarkDone.visibility = View.GONE
                    }
                    VaccineStatus.OVERDUE -> {
                        cardVaccine.strokeColor = Color.parseColor("#FF6B6B")
                        ivStatus.setImageResource(R.drawable.ic_warning)
                        btnMarkDone.text = "Overdue - Mark Done"
                    }
                    VaccineStatus.PENDING -> {
                        cardVaccine.strokeColor = Color.parseColor("#FFB84D")
                        ivStatus.setImageResource(R.drawable.ic_calendar)
                        
                        // Countdown
                        val daysUntil = calculateDaysUntil(vaccine.dueDate)
                        tvCountdown.text = when {
                            daysUntil < 0 -> "Overdue by ${-daysUntil} days"
                            daysUntil == 0 -> "Due today!"
                            else -> "In $daysUntil days"
                        }
                    }
                }
                
                // Click listeners
                btnMarkDone.setOnClickListener { onMarkDone(vaccine) }
                btnLearnMore.setOnClickListener { onLearnMore(vaccine) }
            }
        }
    }
}

class VaccineDiffCallback : DiffUtil.ItemCallback<VaccineRecord>() {
    override fun areItemsTheSame(oldItem: VaccineRecord, newItem: VaccineRecord) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: VaccineRecord, newItem: VaccineRecord) =
        oldItem == newItem
}
```

#### 3. Chat Message Adapter

```kotlin
// ChatAdapter.kt
class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(ChatDiffCallback()) {

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).role) {
            Role.USER -> VIEW_TYPE_USER
            Role.BOT -> VIEW_TYPE_BOT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> UserMessageViewHolder(
                ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> BotMessageViewHolder(
                ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserMessageViewHolder -> holder.bind(getItem(position))
            is BotMessageViewHolder -> holder.bind(getItem(position))
        }
    }

    class UserMessageViewHolder(
        private val binding: ItemChatUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = formatTime(message.timestamp)
        }
    }

    class BotMessageViewHolder(
        private val binding: ItemChatBotBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = formatTime(message.timestamp)
            
            // Typing animation for new messages
            if (message.isTyping) {
                binding.lottieTyping.visibility = View.VISIBLE
                binding.lottieTyping.playAnimation()
            } else {
                binding.lottieTyping.visibility = View.GONE
            }
        }
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
        oldItem == newItem
}
```

---

## 5. BACKEND DEVELOPMENT GUIDE

### 🗄️ Room Database Implementation

#### Database Setup

```kotlin
// ShishuSnehDatabase.kt
@Database(
    entities = [
        BabyProfile::class,
        GrowthEntry::class,
        VaccineRecord::class,
        MilestoneLog::class,
        ChatMessage::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShishuSnehDatabase : RoomDatabase() {
    
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun growthEntryDao(): GrowthEntryDao
    abstract fun vaccineRecordDao(): VaccineRecordDao
    abstract fun milestoneLogDao(): MilestoneLogDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: ShishuSnehDatabase? = null

        fun getDatabase(context: Context): ShishuSnehDatabase {
            return INSTANCE ?: synchronized(this) {
                // SQLCipher encryption
                val passphrase = SQLiteDatabase.getBytes("shishu_sneh_key".toCharArray())
                val factory = SupportFactory(passphrase)
                
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShishuSnehDatabase::class.java,
                    "shishu_sneh_db"
                )
                    .openHelperFactory(factory) // Enable encryption
                    .fallbackToDestructiveMigration()
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}

// Converters.kt
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}
```

#### Entity Definitions

```kotlin
// BabyProfile.kt
@Entity(tableName = "baby_profiles")
data class BabyProfile(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dateOfBirth: Date,
    val birthWeight: Double, // kg
    val birthHeight: Double?, // cm
    val gender: Gender,
    val photoUri: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    enum class Gender {
        MALE, FEMALE
    }
    
    // Computed property
    val ageInWeeks: Int
        get() = ChronoUnit.WEEKS.between(
            dateOfBirth.toInstant(),
            Date().toInstant()
        ).toInt()
    
    val ageInMonths: Int
        get() = ChronoUnit.MONTHS.between(
            dateOfBirth.toInstant(),
            Date().toInstant()
        ).toInt()
}

// GrowthEntry.kt
@Entity(
    tableName = "growth_entries",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId")]
)
data class GrowthEntry(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val date: Date,
    val weight: Double, // kg
    val height: Double?, // cm
    val note: String?,
    val createdAt: Date = Date()
)

// VaccineRecord.kt
@Entity(
    tableName = "vaccine_records",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index("dueDate")]
)
data class VaccineRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val name: String,
    val dueDate: Date,
    val givenDate: Date?,
    val status: VaccineStatus,
    val diseases: List<String>,
    val dosageNumber: Int,
    val ageWeeks: Int,
    val notes: String?,
    val createdAt: Date = Date()
) {
    enum class VaccineStatus {
        PENDING, COMPLETED, SKIPPED, OVERDUE
    }
}

// MilestoneLog.kt
@Entity(
    tableName = "milestone_logs",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfile::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("babyId"), Index("week")]
)
data class MilestoneLog(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val babyId: String,
    val milestoneId: String,
    val week: Int,
    val category: Category,
    val question: String,
    val answer: Answer,
    val answeredAt: Date = Date()
) {
    enum class Category {
        PHYSICAL, COGNITIVE, SOCIAL, COMMUNICATION
    }
    
    enum class Answer {
        YES, NO, SKIPPED
    }
}

// ChatMessage.kt
@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val role: Role,
    val message: String,
    val language: String,
    val timestamp: Date = Date()
) {
    enum class Role {
        USER, BOT
    }
}

#### DAO Interfaces

```kotlin
// BabyProfileDao.kt
@Dao
interface BabyProfileDao {
    @Query("SELECT * FROM baby_profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<BabyProfile>>
    
    @Query("SELECT * FROM baby_profiles WHERE id = :babyId")
    fun getProfileById(babyId: String): Flow<BabyProfile?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BabyProfile)
    
    @Update
    suspend fun updateProfile(profile: BabyProfile)
    
    @Delete
    suspend fun deleteProfile(profile: BabyProfile)
    
    @Query("SELECT * FROM baby_profiles LIMIT 1")
    suspend fun getPrimaryProfile(): BabyProfile?
}

// GrowthEntryDao.kt
@Dao
interface GrowthEntryDao {
    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date ASC")
    fun getEntriesByBaby(babyId: String): Flow<List<GrowthEntry>>
    
    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date DESC LIMIT 1")
    suspend fun getLatestEntry(babyId: String): GrowthEntry?
    
    @Insert
    suspend fun insertEntry(entry: GrowthEntry)
    
    @Delete
    suspend fun deleteEntry(entry: GrowthEntry)
    
    @Query("DELETE FROM growth_entries WHERE babyId = :babyId")
    suspend fun deleteAllForBaby(babyId: String)
}

// VaccineRecordDao.kt
@Dao
interface VaccineRecordDao {
    @Query("SELECT * FROM vaccine_records WHERE babyId = :babyId ORDER BY dueDate ASC")
    fun getVaccinesByBaby(babyId: String): Flow<List<VaccineRecord>>
    
    @Query("SELECT * FROM vaccine_records WHERE babyId = :babyId AND status = 'PENDING' ORDER BY dueDate ASC LIMIT 1")
    fun getNextPendingVaccine(babyId: String): Flow<VaccineRecord?>
    
    @Query("SELECT * FROM vaccine_records WHERE status = 'PENDING' AND dueDate <= :date")
    suspend fun getUpcomingVaccines(date: Date): List<VaccineRecord>
    
    @Insert
    suspend fun insertVaccine(vaccine: VaccineRecord)
    
    @Update
    suspend fun updateVaccine(vaccine: VaccineRecord)
    
    @Query("UPDATE vaccine_records SET status = 'COMPLETED', givenDate = :givenDate WHERE id = :vaccineId")
    suspend fun markAsCompleted(vaccineId: String, givenDate: Date)
}

// MilestoneLogDao.kt
@Dao
interface MilestoneLogDao {
    @Query("SELECT * FROM milestone_logs WHERE babyId = :babyId ORDER BY week ASC")
    fun getMilestonesByBaby(babyId: String): Flow<List<MilestoneLog>>
    
    @Query("SELECT * FROM milestone_logs WHERE babyId = :babyId AND week = :week")
    suspend fun getMilestonesForWeek(babyId: String, week: Int): List<MilestoneLog>
    
    @Insert
    suspend fun insertMilestone(milestone: MilestoneLog)
    
    @Query("SELECT COUNT(*) FROM milestone_logs WHERE babyId = :babyId AND answer = 'NO' AND week = :week")
    suspend fun getDelayedCount(babyId: String, week: Int): Int
}

// ChatMessageDao.kt
@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE userId = :userId ORDER BY timestamp ASC")
    fun getMessagesByUser(userId: String): Flow<List<ChatMessage>>
    
    @Query("SELECT * FROM chat_messages WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentMessages(userId: String): List<ChatMessage>
    
    @Insert
    suspend fun insertMessage(message: ChatMessage)
    
    @Query("DELETE FROM chat_messages WHERE userId = :userId")
    suspend fun clearChatHistory(userId: String)
}



---

### 📡 Firebase Integration

#### Firestore Structure

```
firestore/
├── users/
│   └── {userId}/
│       ├── profile: {name, email, phone, language}
│       ├── babies/
│       │   └── {babyId}/
│       │       ├── profile: {BabyProfile}
│       │       ├── growth_entries/
│       │       │   └── {entryId}: {GrowthEntry}
│       │       ├── vaccines/
│       │       │   └── {vaccineId}: {VaccineRecord}
│       │       ├── milestones/
│       │       │   └── {milestoneId}: {MilestoneLog}
│       │       
│       └── chat_history/
│           └── {messageId}: {ChatMessage}
└── content/ (managed by admin)
    ├── milestones/
    │   └── {weekNumber}: {Milestone[]}
    ├── mother_wellness/
    │   └── {category}: {Content[]}
    └── vaccination_schedule/
        └── india: {Schedule}
```

#### Firestore Service

```kotlin
// FirestoreService.kt
class FirestoreService(private val auth: FirebaseAuth) {
    
    private val db = Firebase.firestore
    private val currentUserId get() = auth.currentUser?.uid
    
    // Baby Profile Sync
    suspend fun syncBabyProfile(baby: BabyProfile) = suspendCoroutine<Boolean> { cont ->
        currentUserId?.let { userId ->
            db.collection("users").document(userId)
                .collection("babies").document(baby.id)
                .set(baby.toMap())
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        } ?: cont.resume(false)
    }
    
    suspend fun fetchBabyProfiles(): List<BabyProfile> = suspendCoroutine { cont ->
        currentUserId?.let { userId ->
            db.collection("users").document(userId)
                .collection("babies")
                .get()
                .addOnSuccessListener { snapshot ->
                    val profiles = snapshot.documents.mapNotNull { 
                        it.toBabyProfile() 
                    }
                    cont.resume(profiles)
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        } ?: cont.resume(emptyList())
    }
    
    // Growth Entry Sync
    suspend fun syncGrowthEntry(babyId: String, entry: GrowthEntry) {
        currentUserId?.let { userId ->
            db.collection("users").document(userId)
                .collection("babies").document(babyId)
                .collection("growth_entries").document(entry.id)
                .set(entry.toMap())
                .await()
        }
    }
    
    // Batch Sync (for offline queue)
    suspend fun batchSync(
        babyId: String,
        growthEntries: List<GrowthEntry>,
        vaccines: List<VaccineRecord>,
        milestones: List<MilestoneLog>
    ) {
        currentUserId?.let { userId ->
            db.runBatch { batch ->
                val babyRef = db.collection("users").document(userId)
                    .collection("babies").document(babyId)
                
                growthEntries.forEach { entry ->
                    batch.set(
                        babyRef.collection("growth_entries").document(entry.id),
                        entry.toMap()
                    )
                }
                
                vaccines.forEach { vaccine ->
                    batch.set(
                        babyRef.collection("vaccines").document(vaccine.id),
                        vaccine.toMap()
                    )
                }
                
                milestones.forEach { milestone ->
                    batch.set(
                        babyRef.collection("milestones").document(milestone.id),
                        milestone.toMap()
                    )
                }
            }.await()
        }
    }
}

// Extension functions for mapping
private fun BabyProfile.toMap() = mapOf(
    "id" to id,
    "name" to name,
    "dateOfBirth" to Timestamp(dateOfBirth),
    "birthWeight" to birthWeight,
    "birthHeight" to birthHeight,
    "gender" to gender.name,
    "photoUri" to photoUri,
    "createdAt" to Timestamp(createdAt),
    "updatedAt" to Timestamp(updatedAt)
)

private fun DocumentSnapshot.toBabyProfile(): BabyProfile? {
    return try {
        BabyProfile(
            id = getString("id") ?: return null,
            name = getString("name") ?: return null,
            dateOfBirth = getTimestamp("dateOfBirth")?.toDate() ?: return null,
            birthWeight = getDouble("birthWeight") ?: return null,
            birthHeight = getDouble("birthHeight"),
            gender = BabyProfile.Gender.valueOf(getString("gender") ?: "MALE"),
            photoUri = getString("photoUri"),
            createdAt = getTimestamp("createdAt")?.toDate() ?: Date(),
            updatedAt = getTimestamp("updatedAt")?.toDate() ?: Date()
        )
    } catch (e: Exception) {
        null
    }
}
```

#### Remote Config Setup

```kotlin
// RemoteConfigService.kt
class RemoteConfigService {
    
    private val remoteConfig = Firebase.remoteConfig
    
    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // 1 hour
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }
    
    suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate().await()
    }
    
    fun getMilestonesForWeek(week: Int, language: String): List<Milestone> {
        val json = remoteConfig.getString("milestones_week_${week}_$language")
        return Gson().fromJson(json, Array<Milestone>::class.java).toList()
    }
    
    fun getDailyMotivation(language: String): String {
        val motivations = remoteConfig.getString("daily_motivations_$language")
        val list = Gson().fromJson(motivations, Array<String>::class.java)
        return list.random()
    }
    
    fun getMotherWellnessContent(category: String, language: String): WellnessContent {
        val json = remoteConfig.getString("mother_wellness_${category}_$language")
        return Gson().fromJson(json, WellnessContent::class.java)
    }
}

// Remote Config JSON Structure
data class Milestone(
    val id: String,
    val category: String,
    val question: String,
    val critical: Boolean
)

data class WellnessContent(
    val title: String,
    val sections: List<Section>
) {
    data class Section(
        val heading: String,
        val content: String,
        val items: List<String>?
    )
}
```

---

### 🤖 Gemini AI Integration

```kotlin
// GeminiService.kt
class GeminiService(private val apiKey: String) {
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        systemInstruction = content { text(SYSTEM_PROMPT) }
    )
    
    private var chatSession: Chat? = null
    
    companion object {
        private const val SYSTEM_PROMPT = """
You are Shishu AI, a compassionate and knowledgeable pediatric health assistant designed for new mothers in India. Your role is to provide evidence-based guidance on infant care during the first year of life.

CORE RESPONSIBILITIES:
- Answer questions about baby care, sleep, development, and common health concerns
- Provide culturally sensitive advice relevant to Indian mothers
- Guide mothers on when to seek professional medical help
- Offer emotional support and encouragement

STRICT SAFETY RULES:
1. NEVER diagnose medical conditions or prescribe treatments
2. ALWAYS recommend consulting a pediatrician for:
   - Persistent fever (>100.4°F)
   - Difficulty breathing
   - Severe diarrhea or vomiting
   - Lethargy or unresponsiveness
   - Any concerning symptoms
3. Only discuss topics related to maternal and infant health (0-12 months)
4. If asked about topics outside your scope, politely redirect
5. Cite WHO guidelines when providing health recommendations
6. Be encouraging and non-judgmental in all responses

LANGUAGE:
- Respond in the user's selected language (English, Kannada, or Hindi)
- Use simple, accessible language avoiding complex medical jargon
- Structure responses with bullet points for clarity

TONE:
- Warm, supportive, and reassuring
- Empathetic to the challenges of new motherhood
- Confidence-building, not fear-inducing
"""
    }
    
    fun startNewChat(babyContext: BabyProfile? = null): Chat {
        val historyWithContext = babyContext?.let {
            listOf(
                content("user") { 
                    text("My baby ${it.name} is ${it.ageInWeeks} weeks old, ${it.gender.name.lowercase()}.")
                },
                content("model") { 
                    text("Thank you for sharing! I'm here to help with any questions about ${it.name}'s care. What would you like to know?") 
                }
            )
        } ?: emptyList()
        
        chatSession = generativeModel.startChat(history = historyWithContext)
        return chatSession!!
    }
    
    suspend fun sendMessage(message: String): Result<String> {
        return try {
            val chat = chatSession ?: startNewChat()
            val response = chat.sendMessage(message)
            Result.success(response.text ?: "I couldn't generate a response. Please try again.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Streaming response for typing effect
    fun sendMessageStream(message: String): Flow<String> = flow {
        val chat = chatSession ?: startNewChat()
        chat.sendMessageStream(message).collect { chunk ->
            emit(chunk.text ?: "")
        }
    }
    
    fun getChatHistory(): List<Content> {
        return chatSession?.history ?: emptyList()
    }
    
    // Quick response suggestions
    fun getQuickReplySuggestions(category: String): List<String> {
        return when (category) {
            "sleep" -> listOf(
                "How to establish sleep routine?",
                "Baby won't sleep at night",
                "Is co-sleeping safe?"
            )
            "health" -> listOf(
                "When to worry about fever?",
                "Baby has diarrhea, what to do?",
                "Normal poop color for newborn?"
            )
            "development" -> listOf(
                "When will baby smile?",
                "Is my baby developing normally?",
                "Tummy time tips?"
            )
            else -> emptyList()
        }
    }
}
```

---

### ⚙️ WorkManager (Notifications)

```kotlin
// VaccinationReminderWorker.kt
class VaccinationReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val vaccineId = inputData.getString("vaccine_id") ?: return Result.failure()
        
        val database = ShishuSnehDatabase.getDatabase(applicationContext)
        val vaccineDao = database.vaccineRecordDao()
        
        // Fetch vaccine details
        val vaccines = vaccineDao.getUpcomingVaccines(Date())
        val vaccine = vaccines.find { it.id == vaccineId } ?: return Result.success()
        
        // Check if still pending
        if (vaccine.status != VaccineRecord.VaccineStatus.PENDING) {
            return Result.success()
        }
        
        // Calculate days until due
        val daysUntil = ChronoUnit.DAYS.between(
            Instant.now(),
            vaccine.dueDate.toInstant()
        )
        
        // Show notification
        showVaccinationNotification(vaccine, daysUntil.toInt())
        
        return Result.success()
    }
    
    private fun showVaccinationNotification(vaccine: VaccineRecord, daysUntil: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create notification channel (Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Vaccination Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for upcoming vaccinations"
                enableLights(true)
                lightColor = Color.parseColor("#E94560")
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        // Build notification
        val title = when {
            daysUntil == 0 -> "Vaccination Due Today!"
            daysUntil > 0 -> "Vaccination in $daysUntil days"
            else -> "Vaccination Overdue!"
        }
        
        val message = "${vaccine.name} is ${if (daysUntil == 0) "due today" else "coming up"}. " +
                "Protects against: ${vaccine.diseases.joinToString(", ")}"
        
        // Deep link to vaccination screen
        val intent = Intent(applicationContext, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "vaccination")
            putExtra("vaccine_id", vaccine.id)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            vaccine.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_vaccine)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(Color.parseColor("#E94560"))
            .build()
        
        notificationManager.notify(vaccine.id.hashCode(), notification)
    }
    
    companion object {
        private const val CHANNEL_ID = "vaccination_reminders"
        
        fun schedule(context: Context, vaccine: VaccineRecord) {
            // Schedule 3 days before
            scheduleReminder(context, vaccine, vaccine.dueDate - 3.days)
            
            // Schedule on the day
            scheduleReminder(context, vaccine, vaccine.dueDate)
        }
        
        private fun scheduleReminder(context: Context, vaccine: VaccineRecord, triggerDate: Date) {
            val delay = triggerDate.time - System.currentTimeMillis()
            if (delay < 0) return // Don't schedule past dates
            
            val data = Data.Builder()
                .putString("vaccine_id", vaccine.id)
                .build()
            
            val workRequest = OneTimeWorkRequestBuilder<VaccinationReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag("vaccine_${vaccine.id}")
                .build()
            
            WorkManager.getInstance(context).enqueue(workRequest)
        }
        
        fun cancelReminder(context: Context, vaccineId: String) {
            WorkManager.getInstance(context).cancelAllWorkByTag("vaccine_$vaccineId")
        }
    }
}

// Extension for date arithmetic
private operator fun Date.minus(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_YEAR, -days)
    return calendar.time
}

private val Int.days: Long get() = this * 24 * 60 * 60 * 1000L
```

```kotlin
// MilestoneReminderWorker.kt
class MilestoneReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val babyId = inputData.getString("baby_id") ?: return Result.failure()
        
        val database = ShishuSnehDatabase.getDatabase(applicationContext)
        val babyDao = database.babyProfileDao()
        
        val baby = babyDao.getPrimaryProfile() ?: return Result.success()
        
        // Check if milestone for this week is already completed
        val milestoneDao = database.milestoneLogDao()
        val completedThisWeek = milestoneDao.getMilestonesForWeek(babyId, baby.ageInWeeks)
        
        if (completedThisWeek.isNotEmpty()) {
            // Already answered this week
            return Result.success()
        }
        
        // Show reminder
        showMilestoneNotification(baby)
        
        // Reschedule for next week
        scheduleNextWeek(baby.id)
        
        return Result.success()
    }
    
    private fun showMilestoneNotification(baby: BabyProfile) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Milestone Check-ins",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_milestone)
            .setContentTitle("Time for ${baby.name}'s week ${baby.ageInWeeks} check-in")
            .setContentText("Track developmental milestones to monitor healthy growth")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    companion object {
        private const val CHANNEL_ID = "milestone_reminders"
        private const val NOTIFICATION_ID = 2001
        
        fun scheduleWeekly(context: Context, babyId: String) {
            val data = Data.Builder()
                .putString("baby_id", babyId)
                .build()
            
            val workRequest = PeriodicWorkRequestBuilder<MilestoneReminderWorker>(
                7, TimeUnit.DAYS,
                1, TimeUnit.HOURS // Flex interval
            )
                .setInputData(data)
                .addTag("milestone_$babyId")
                .build()
            
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "milestone_reminder_$babyId",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
        
        private fun scheduleNextWeek(babyId: String) {
            // Automatically rescheduled by PeriodicWorkRequest
        }
    }
}
```

---

### 📊 WHO Growth Standards Implementation

```kotlin
// WHOStandards.kt
object WHOStandards {
    
    private var boysWeightData: Map<Int, Percentiles>? = null
    private var girlsWeightData: Map<Int, Percentiles>? = null
    private var boysHeightData: Map<Int, Percentiles>? = null
    private var girlsHeightData: Map<Int, Percentiles>? = null
    
    data class Percentiles(
        val month: Int,
        val p3: Double,
        val p50: Double,
        val p97: Double
    )
    
    fun initialize(context: Context) {
        if (boysWeightData == null) {
            boysWeightData = loadFromJson(context, "who_standards_boys_weight.json")
            girlsWeightData = loadFromJson(context, "who_standards_girls_weight.json")
            boysHeightData = loadFromJson(context, "who_standards_boys_height.json")
            girlsHeightData = loadFromJson(context, "who_standards_girls_height.json")
        }
    }
    
    private fun loadFromJson(context: Context, fileName: String): Map<Int, Percentiles> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val data = Gson().fromJson(json, Array<Percentiles>::class.java)
        return data.associateBy { it.month }
    }
    
    fun getWeightPercentiles(gender: BabyProfile.Gender, ageMonths: Int): Percentiles? {
        val data = when (gender) {
            BabyProfile.Gender.MALE -> boysWeightData
            BabyProfile.Gender.FEMALE -> girlsWeightData
        }
        return data?.get(ageMonths)
    }
    
    fun getHeightPercentiles(gender: BabyProfile.Gender, ageMonths: Int): Percentiles? {
        val data = when (gender) {
            BabyProfile.Gender.MALE -> boysHeightData
            BabyProfile.Gender.FEMALE -> girlsHeightData
        }
        return data?.get(ageMonths)
    }
    
    fun assessGrowthStatus(
        currentWeight: Double,
        gender: BabyProfile.Gender,
        ageMonths: Int
    ): GrowthStatus {
        val percentiles = getWeightPercentiles(gender, ageMonths) ?: return GrowthStatus.UNKNOWN
        
        return when {
            currentWeight < percentiles.p3 -> GrowthStatus.BELOW_NORMAL
            currentWeight > percentiles.p97 -> GrowthStatus.ABOVE_NORMAL
            else -> GrowthStatus.HEALTHY
        }
    }
    
    enum class GrowthStatus {
        BELOW_NORMAL, HEALTHY, ABOVE_NORMAL, UNKNOWN
    }
}
```

**WHO Standards JSON Sample (assets/who_standards_boys_weight.json):**
```json
[
  {
    "month": 0,
    "p3": 2.5,
    "p50": 3.3,
    "p97": 4.4
  },
  {
    "month": 1,
    "p3": 3.4,
    "p50": 4.5,
    "p97": 5.8
  },
  {
    "month": 2,
    "p3": 4.3,
    "p50": 5.6,
    "p97": 7.1
  },
  {
    "month": 3,
    "p3": 5.0,
    "p50": 6.4,
    "p97": 8.0
  },
  {
    "month": 6,
    "p3": 6.4,
    "p50": 7.9,
    "p97": 9.8
  },
  {
    "month": 12,
    "p3": 7.7,
    "p50": 9.6,
    "p97": 12.0
  }
]
```

---

### 💉 Vaccination Schedule Generator

```kotlin
// VaccinationScheduleGenerator.kt
object VaccinationScheduleGenerator {
    
    private val indianSchedule = listOf(
        VaccineTemplate("BCG", 0, listOf("Tuberculosis"), 1),
        VaccineTemplate("OPV 0", 0, listOf("Poliomyelitis"), 1),
        VaccineTemplate("Hepatitis B (Birth dose)", 0, listOf("Hepatitis B"), 1),
        VaccineTemplate("OPV 1", 6, listOf("Poliomyelitis"), 1),
        VaccineTemplate("Pentavalent 1", 6, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 1),
        VaccineTemplate("Rotavirus 1", 6, listOf("Rotavirus diarrhea"), 1),
        VaccineTemplate("PCV 1", 6, listOf("Pneumococcal disease"), 1),
        VaccineTemplate("OPV 2", 10, listOf("Poliomyelitis"), 2),
        VaccineTemplate("Pentavalent 2", 10, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 2),
        VaccineTemplate("Rotavirus 2", 10, listOf("Rotavirus diarrhea"), 2),
        VaccineTemplate("PCV 2", 10, listOf("Pneumococcal disease"), 2),
        VaccineTemplate("OPV 3", 14, listOf("Poliomyelitis"), 3),
        VaccineTemplate("Pentavalent 3", 14, listOf("Diphtheria", "Tetanus", "Pertussis", "Hepatitis B", "Hib"), 3),
        VaccineTemplate("Rotavirus 3", 14, listOf("Rotavirus diarrhea"), 3),
        VaccineTemplate("PCV 3", 14, listOf("Pneumococcal disease"), 3),
        VaccineTemplate("IPV", 14, listOf("Poliomyelitis"), 1),
        VaccineTemplate("MMR 1", 36, listOf("Measles", "Mumps", "Rubella"), 1),
        VaccineTemplate("Vitamin A (1st dose)", 36, listOf("Vitamin A deficiency"), 1),
        VaccineTemplate("DPT Booster 1", 64, listOf("Diphtheria", "Tetanus", "Pertussis"), 1),
        VaccineTemplate("MMR 2", 72, listOf("Measles", "Mumps", "Rubella"), 2),
        VaccineTemplate("DPT Booster 2", 80, listOf("Diphtheria", "Tetanus", "Pertussis"), 2)
    )
    
    data class VaccineTemplate(
        val name: String,
        val ageWeeks: Int,
        val diseases: List<String>,
        val dosageNumber: Int
    )
    
    fun generateSchedule(babyId: String, dateOfBirth: Date): List<VaccineRecord> {
        val calendar = Calendar.getInstance()
        calendar.time = dateOfBirth
        
        return indianSchedule.map { template ->
            calendar.time = dateOfBirth
            calendar.add(Calendar.WEEK_OF_YEAR, template.ageWeeks)
            
            VaccineRecord(
                id = UUID.randomUUID().toString(),
                babyId = babyId,
                name = template.name,
                dueDate = calendar.time,
                givenDate = null,
                status = VaccineRecord.VaccineStatus.PENDING,
                diseases = template.diseases,
                dosageNumber = template.dosageNumber,
                ageWeeks = template.ageWeeks,
                notes = null
            )
        }
    }
    
    suspend fun initializeSchedule(
        babyId: String,
        dateOfBirth: Date,
        dao: VaccineRecordDao,
        context: Context
    ) {
        val schedule = generateSchedule(babyId, dateOfBirth)
        
        schedule.forEach { vaccine ->
            dao.insertVaccine(vaccine)
            
            // Schedule notifications
            VaccinationReminderWorker.schedule(context, vaccine)
        }
    }
}
```

---

## 6. DATABASE SCHEMA

### Complete Schema Visualization

```sql
-- Baby Profiles Table
CREATE TABLE baby_profiles (
    id TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    dateOfBirth INTEGER NOT NULL,
    birthWeight REAL NOT NULL,
    birthHeight REAL,
    gender TEXT NOT NULL,
    photoUri TEXT,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL
);

-- Growth Entries Table
CREATE TABLE growth_entries (
    id TEXT PRIMARY KEY NOT NULL,
    babyId TEXT NOT NULL,
    date INTEGER NOT NULL,
    weight REAL NOT NULL,
    height REAL,
    note TEXT,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (babyId) REFERENCES baby_profiles(id) ON DELETE CASCADE
);
CREATE INDEX idx_growth_babyId ON growth_entries(babyId);

-- Vaccine Records Table
CREATE TABLE vaccine_records (
    id TEXT PRIMARY KEY NOT NULL,
    babyId TEXT NOT NULL,
    name TEXT NOT NULL,
    dueDate INTEGER NOT NULL,
    givenDate INTEGER,
    status TEXT NOT NULL,
    diseases TEXT NOT NULL,
    dosageNumber INTEGER NOT NULL,
    ageWeeks INTEGER NOT NULL,
    notes TEXT,
    createdAt INTEGER NOT NULL,
    FOREIGN KEY (babyId) REFERENCES baby_profiles(id) ON DELETE CASCADE
);
CREATE INDEX idx_vaccine_babyId ON vaccine_records(babyId);
CREATE INDEX idx_vaccine_dueDate ON vaccine_records(dueDate);

-- Milestone Logs Table
CREATE TABLE milestone_logs (
    id TEXT PRIMARY KEY NOT NULL,
    babyId TEXT NOT NULL,
    milestoneId TEXT NOT NULL,
    week INTEGER NOT NULL,
    category TEXT NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    answeredAt INTEGER NOT NULL,
    FOREIGN KEY (babyId) REFERENCES baby_profiles(id) ON DELETE CASCADE
);
CREATE INDEX idx_milestone_babyId ON milestone_logs(babyId);
CREATE INDEX idx_milestone_week ON milestone_logs(week);

-- Chat Messages Table
CREATE TABLE chat_messages (
    id TEXT PRIMARY KEY NOT NULL,
    userId TEXT NOT NULL,
    role TEXT NOT NULL,
    message TEXT NOT NULL,
    language TEXT NOT NULL,
    timestamp INTEGER NOT NULL
);

---

## 7. AI INTEGRATION SPECIFICATION

### Gemini System Prompt (Detailed)

```
You are Shishu AI, an expert pediatric health assistant created specifically to support new mothers in India during their baby's first year of life. You combine evidence-based medical knowledge with cultural sensitivity to the Indian context.

KNOWLEDGE BASE:
- WHO Child Growth Standards
- Indian Academy of Pediatrics (IAP) guidelines
- Universal Immunisation Programme (UIP) of India
- Postnatal maternal care
- Infant development milestones (0-12 months)

COMMUNICATION STYLE:
1. Empathetic and supportive - acknowledge the challenges of new motherhood
2. Clear and simple - avoid complex medical terminology
3. Structured - use bullet points and numbered lists
4. Encouraging - build confidence, never shame or criticize
5. Culturally aware - respect Indian traditions while promoting evidence-based care

RESPONSE FORMAT:
- Start with empathy/validation
- Provide clear, actionable advice
- Include "When to see a doctor" section if relevant
- End with encouragement

SAFETY PROTOCOLS:
1. RED FLAGS (Always recommend immediate doctor visit):
   - High fever (>100.4°F or 38°C)
   - Difficulty breathing or wheezing
   - Persistent vomiting or diarrhea
   - Lethargy, unresponsiveness
   - Seizures
   - Blood in stool
   - Severe dehydration signs
   - Yellowing of skin/eyes after 2 weeks
   - Poor weight gain over multiple weeks

2. NEVER:
   - Diagnose specific conditions
   - Prescribe medications or dosages
   - Recommend discontinuing doctor-prescribed treatments
   - Provide advice for babies with special medical conditions without doctor consultation
   - Answer questions about topics outside infant/maternal health

3. ALWAYS:
   - Cite WHO or IAP when providing medical guidance
   - Acknowledge limitations ("I'm an AI assistant, not a doctor")
   - Encourage regular pediatric checkups

COMMON MYTH CORRECTIONS (India-specific):
- Myth: "Colostrum should be discarded"
  Fact: Colostrum is liquid gold - packed with antibodies, essential for first 2-3 days

- Myth: "Babies need water in hot weather"
  Fact: Breast milk provides all hydration for first 6 months, even in summer

- Myth: "Apply kajal/surma for eye health"
  Fact: Lead-based cosmetics can harm baby's development, not recommended by IAP

- Myth: "Massage with heavy oil makes baby stronger"
  Fact: Gentle massage is beneficial, but heavy pressure or specific oils aren't necessary

LANGUAGE SUPPORT:
- Detect user's language from query
- Respond in same language (English, Kannada, or Hindi)
- Use culturally appropriate examples and references

EXAMPLE GOOD RESPONSE:
User: "My 2-month-old cries a lot at night. What should I do?"
