# Development Phases

## Phase 1: Super Admin & Administrative Authentication

### Goal

Establish the system's administrative account and access management.

### Features

1. Create the initial Super Admin account directly in the database.
2. Super Admin can create:
    - Registrar accounts
    - Guidance accounts
3. Super Admin can send account invitations through email.
4. Registrar/Guidance can activate their account through the invitation.
5. Super Admin can:
    - Invite an account
    - Activate an account
    - Inactivate an account
    - Remove an account
6. Implement authentication and authorization for administrative users.
7. Implement account status management.
8. Implement secure password setup/reset flow.

### Output

A working administrative authentication system:

Super Admin
→ creates Registrar/Guidance account
→ invitation email
→ account activation
→ login
→ role-based access

---

## Phase 2: Registrar

### Goal

Build the academic/master data management foundation.

### Planning

Before implementation:

1. Finalize Registrar responsibilities.
2. Finalize Registrar modules.
3. Finalize database entities and relationships.
4. Finalize data import requirements.
5. Finalize Registrar workflows.
6. Create wireframes.
7. Define validation rules.
8. Define access permissions.

### Main Areas

- Dashboard
- Departments
- Faculty Members
- Students
- Academic Records
- Profile
- Settings

### Core Data

- Departments
- Faculty Members
- Students
- School Years
- Semesters
- Academic Records
- Sections
- Subjects
- Faculty Assignments
- Student Enrollments

### Output

A working Registrar module that can provide the academic data required by the evaluation system.

---

## Phase 3: Guidance

### Goal

Build the Student-Faculty Evaluation management process.

### Planning

1. Finalize Guidance responsibilities.
2. Finalize Guidance modules.
3. Finalize evaluation workflow.
4. Finalize participant selection rules.
5. Finalize evaluation scheduling.
6. Finalize invitation workflow.
7. Finalize result generation.
8. Create wireframes.
9. Finalize database relationships.

### Main Areas

- Dashboard
- Evaluation Periods
- Participant Selection
- Invitations
- Evaluation Monitoring
- Results
- Reports
- Profile
- Settings

### Core Process

Registrar Academic Data
→ Guidance creates evaluation period
→ Select eligible students
→ Random participant selection
→ Generate invitations
→ Send invitations
→ Monitor completion
→ Generate results

---

## Phase 4: Student

### Goal

Build the student evaluation experience.

### Authentication

1. Student receives evaluation invitation.
2. Student opens the unique invitation link.
3. Student requests OTP.
4. OTP is sent to the student's registered email.
5. Student enters OTP.
6. System verifies OTP.
7. Temporary authenticated session is created.
8. Student accesses assigned evaluations.

### Evaluation

1. View assigned faculty.
2. Open evaluation.
3. Answer evaluation questions.
4. Provide comments.
5. Review answers.
6. Submit evaluation.
7. Mark evaluation as completed.

### Output

A secure and simple evaluation experience without requiring permanent student accounts.

---

# Final Integration

After all four phases:

Super Admin
→ Administrative Account Management

Registrar
→ Academic Data

Guidance
→ Evaluation Management

Student
→ Evaluation Response

Academic Data
→ Participant Selection
→ Evaluation Assignment
→ Student Evaluation
→ Aggregated Results
→ Faculty Reports
