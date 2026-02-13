# Employee Roster (Android)

Employee Roster is an Android Java app for role-based employee operations:

- Authentication and role-based login routing
- Admin user management and roster setup
- Manager operational tools (attendance review, schedule, leave/swap approvals)
- General worker tools (clock in/out, leave requests, shift swaps, personal roster view)

## Tech Stack

- Android: Java, AppCompat, Material, ViewModel/LiveData
- Build: Gradle (AGP 8.8.1), minSdk 29, targetSdk 34
- Backend: Firebase
  - Firebase Authentication
  - Cloud Firestore
  - Firebase Realtime Database
  - Google Services Gradle plugin (`com.google.gms.google-services`)

## Architecture Overview

Package layout:

- `app/src/main/java/com/employeeroster/ui/activity`: screens and navigation
- `app/src/main/java/com/employeeroster/ui/adapter`: ListView adapters
- `app/src/main/java/com/employeeroster/viewmodel`: UI state + input validation
- `app/src/main/java/com/employeeroster/data/repository`: Firebase access
- `app/src/main/java/com/employeeroster/data/model`: data models
- `app/src/main/java/com/employeeroster/util`: utility result wrappers

High-level flow:

1. `SplashScreenActivity` starts first.
2. User goes to `LoginActivity`.
3. `LoginViewModel` validates input.
4. `AuthRepository` signs in with Firebase Auth, then reads `Users/{uid}` in Firestore to get `jobRole`.
5. App routes to:
   - `AdminActivity`
   - `ManagerActivity`
   - `GeneralWorkerActivity`

## Feature Modules

### Authentication

- Login: `LoginActivity`, `LoginViewModel`, `AuthRepository`
- Forgot password: `ForgotPasswordActivity` (`sendPasswordResetEmail`)
- Logout from role dashboards via `FirebaseAuth.signOut()`

### Admin

- Dashboard: `AdminActivity`
- Create employee: `CreateUserActivity` + `CreateUserViewModel` + `UserRepository`
- Update employee by staff number: `AdminUpdateUserActivity`
- Delete employee by staff number: `DeleteUserActivity`
- Roster editing: `RosterActivity`
- Shift management: `ShiftManager`
- Time-off handling: `TimeOffManagerActivity`

### Manager

- Dashboard: `ManagerActivity`
- View schedule: `ViewScheduleActivity`
- Approve/deny time-off: `TimeOffManagerActivity`
- Track attendance: `TrackAttendanceActivity`
- Manage shift swaps: `ManageShiftSwapsActivity`
- Update roster: `RosterActivity`

### General Worker

- Dashboard: `GeneralWorkerActivity`
- Clock in/out (writes attendance record)
- Submit leave request: `LeaveRequestActivity`
- Request shift swap: `SwapShiftActivity`
- View own roster by staff number: `ViewRosterActivity`

### Additional Screens

- `CompanyNewsActivity`
- `EmployeeDashboardActivity`
- `EmployeeProfile`

## Firebase Data Layout

The app currently uses both Firestore and Realtime Database.

Cloud Firestore collections used:

- `Users/{uid}`
  - Fields used: `firstName`, `lastName`, `idNumber`, `email`, `jobRole`, `contactNumber`, `staffNumber`
- `Roster/{staffNumber}`
  - Fields: `Monday` ... `Sunday`
- `AttendanceRecords/{autoId}`
  - Fields: `staffNumber`, `role`, `action`, `timestamp`

Realtime Database paths used:

- `LeaveRequests/{requestId}`
- `TimeOffRequests/{requestId}`
- `ShiftSwapRequests/{uid}/{requestId}`
- `Shifts/{shiftId}`
- `AvailableShifts/{id}`
- `ScheduledEvents/{id}`
- `CompanyNews/{id}`
- `Dashboard/...`
- `Users/{uid}` (used by `EmployeeProfile`)

## Build and Configuration

### Prerequisites

- Android Studio (latest stable recommended)
- JDK 17+
- Android SDK configured (`local.properties` with `sdk.dir` or `ANDROID_HOME`)

### Firebase configuration

1. Place real Firebase config at:
   - `app/google-services.json`
2. Make sure package name in Firebase matches:
   - `com.employeeroster`

### Environment variables

The app module reads `app/.env` and exposes these BuildConfig fields:

- `API_KEY`
- `SECRET_KEY`
- `ENCRYPTION_KEY`
- `JWT_SECRET`

Create `app/.env` locally:

```env
API_KEY=
SECRET_KEY=
ENCRYPTION_KEY=
JWT_SECRET=
```

Security notes:

- `app/.env` should stay local and uncommitted.
- `app/google-services.json` is environment-specific and should also be excluded from commits in team/public workflows.
- Do not store true secrets in client apps; treat client-side values as potentially recoverable.

### Build commands

From project root:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Navigation Map

- `SplashScreenActivity` -> `LoginActivity`
- `LoginActivity` -> `AdminActivity` | `ManagerActivity` | `GeneralWorkerActivity` (based on Firestore `jobRole`)
- Admin dashboard opens user/roster/shift/time-off screens
- Manager dashboard opens schedule/attendance/roster/time-off/swap screens
- Worker dashboard opens leave, swap, and roster screens

## Testing

Configured dependencies:

- Unit tests: JUnit4
- Instrumentation tests: AndroidX Test + Espresso

No comprehensive automated test suite is currently documented.

## Known Gaps and Risks

- Mixed datastore usage (Firestore + Realtime DB) for related entities increases maintenance complexity.
- Some role labels are inconsistent (`"general stuff"` vs `"General Worker"`).
- `StoreUserDetails` includes plaintext password storage logic; this should never be used in production.
- Several screens use `ListView` + string serialization, which limits typed data handling and filtering.

## Recommended Next Improvements

1. Standardize on one Firebase datastore per domain area.
2. Normalize role constants and validate server-side.
3. Remove/deprecate insecure password-handling paths.
4. Add repository and ViewModel unit tests for auth, user creation, and role routing.
5. Add CI checks for build and basic test execution.
