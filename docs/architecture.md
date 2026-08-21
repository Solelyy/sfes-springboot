# QCU Student-Faculty Evaluation System

## Technology Stack

- Frontend: Next.js
- Backend: Spring Boot
- Database: PostgreSQL

---

## Users

1. Super Admin
2. Registrar
3. Guidance
4. Student

---

### Super Admin

- creates and manages registrar and guidance accounts.

### 1. Registrar

#### Responsibility

Manages the university's master and academic data used by the evaluation system.

#### Modules / Functions

##### Departments

- Create departments
- Update department information
- Activate/deactivate departments
- View department information

##### Faculty Members

- Add faculty members
- Bulk import faculty members
- Update faculty information
- Assign faculty members to a department
- Activate/deactivate faculty members

##### Students

- Add students
- Bulk import students
- Update student information
- View student information
- Change student status
- View academic history

> A student is created once in the Student Master and is not recreated every semester.

#### Academic Records

Manage academic information for a specific:

- School Year
- Semester
- Department
- Year Level
- Section / Irregular Student
- Subject
- Faculty Assignment
- Student Enrollment

Bulk import should be used whenever possible instead of manually assigning thousands of records.

#### Does NOT Handle

- Evaluation scheduling
- Participant selection
- Evaluation invitations
- OTP authentication
- Evaluation responses
- Faculty evaluation results

---

### 2. Guidance

#### Responsibility

Manages the Student-Faculty Evaluation process using the academic data provided by the Registrar.

#### Evaluation Functions

- Create evaluation period
- Select school year and semester
- Set evaluation start and end date
- Select departments/sections
- Set number of student participants
- Randomly select students
- Handle eligible irregular students
- Generate evaluation invitations
- Send invitations through email
- Monitor evaluation progress
- View evaluation results
- Generate faculty reports
- Send faculty results through email

#### Faculty Assignment

Guidance does not manually assign professors to students.

The system determines the required evaluations using:

Student
-> Academic Record
-> Student Enrollment
-> Subject
-> Faculty

#### Results

Guidance can view:

- Aggregated scores
- Average ratings
- Anonymous comments
- Evaluation completion statistics

Student identity should not be displayed alongside their individual responses/comments by default.

---

### 3. Student

#### Responsibility

Completes assigned faculty evaluations.

#### Account

Students do not create permanent accounts.

Authentication is invitation-based.

#### Authentication Flow

Guidance selects student
-> Evaluation invitation created
-> Invitation sent through email
-> Student opens unique link
-> Student requests OTP
-> OTP sent to registered email
-> Student enters OTP
-> OTP verified
-> Temporary session created
-> Student accesses evaluations

No permanent account, username, password, or registration is required.

#### Evaluation

Student can:

- View assigned faculty evaluations
- Complete each evaluation independently
- Rate using the Likert scale
- Submit comments
- Review before submission
- Submit the evaluation

Once submitted, the evaluation cannot be edited.

---

### Role Summary

| Role      | Main Responsibility             |
| --------- | ------------------------------- |
| Registrar | Manage master and academic data |
| Guidance  | Manage the evaluation process   |
| Student   | Complete assigned evaluations   |

---

### Core Principle

> Registrar provides the academic data. Guidance uses that data to conduct evaluations. Students provide the evaluation responses.
