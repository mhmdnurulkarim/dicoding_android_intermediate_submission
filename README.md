# Dicoding: Android Intermediate Submission 📱
<p>This is a repository that contains the source code of my submissions project at Dicoding "Belajar Pengembangan Aplikasi Android Intermediate" course, start from the first submission until the final submission. This course is a part of self-paced learning at Bangkit 2022 Mobile Development learning path. I try to implement the best practices of the Kotlin programming language and Android framework to this project.</p>

## Disclaimer ⚠️
This repository is created for sharing and educational purposes only. Plagiarism is unacceptable and is not my responsibility as the author.

## Submission criteria
### 1. Authentication Page
The conditions that must be met are as follows.
- Displays the login page to enter the application. The following input is required.
    - Email (R.id.ed_login_email)
    - Password (R.id.ed_login_password)

- Create a registration page to register yourself in the application. The following input is required.
    - Name (R.id.ed_register_name)
    - Email (R.id.ed_register_email)
    - Password (R.id.ed_register_password)
- Password must be hidden.
- Create a Custom View in the form of EditText on the login or registration page with the following conditions.
    - If the password is less than 8 characters, displays an error message directly in EditText without having to move the form or click a button first.
- Store session data and tokens in preferences. Session data is used to manage application flow with specifications such as the following.
    - If you have logged in, go straight to the main page.
    - If not, you will go to the login page.
- There is a feature to log out (R.id.action_logout) on the main page with the following conditions.
    - When the logout button is pressed, the token, and session information should be deleted.

### 2. List of Stories
The conditions that must be met are as follows.
- Displays a list of stories from the provided API. The following is the minimum information that you must display.
    - User name (R.id.tv_item_name)
    - Photo (R.id.iv_item_photo)

- A detailed view appears when one of the story items is pressed. The following is the minimum information that you must display.
    - User name (R.id.tv_detail_name)
    - Photo (R.id.iv_detail_photo)
    - Description (R.id.tv_detail_description)

### 3. Add Story
The conditions that must be met are as follows.
- Create a page to add new stories that can be accessed from the story list page. The following is the minimum input required.
    - Photo file (must be from gallery)
    - Story description (R.id.ed_add_description)

- The following are the conditions for adding a new story:
    - There is a button (R.id.button_add) to upload data to the server.
    - After the button is clicked and the upload process is successful, it will return to the story list page.
    - The most recent story data should appear at the top.

### 4. Display Animation
The conditions that must be met are as follows.
- Create animations in applications using one of the following animation types.
    - Property Animation
    - Motion Animation
    - Shared Elements
- Write the type and location of animation in Student Notes.

## Wireframe Submission 1
![Wireframe_Submission_1!](/app/src/main/res/drawable/wireframe_submission_1.jpeg "Wireframe Submission 1")
