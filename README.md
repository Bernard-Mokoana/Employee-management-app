# Employee Management App

## Environment Setup
This project loads secrets from `app/.env` during the Android build. Create that file locally and set the values:

```
FIREBASE_API_KEY=
FIREBASE_PROJECT_ID=
FIREBASE_APP_ID=
FIREBASE_STORAGE_BUCKET=
FIREBASE_MESSAGING_SENDER_ID=
FIREBASE_DATABASE_URL=
API_KEY=
SECRET_KEY=
ENCRYPTION_KEY=
JWT_SECRET=
```

Notes:
- `app/.env` is gitignored and should never be committed.
- `app/google-services.json` is required by the Google Services plugin. The version in this repo uses placeholder values; replace it locally with your real Firebase config and do not commit the change.
