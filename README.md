Documentation

Authors' Data and Contribution to Different Parts of the Project

Marcin Borkowski: Implementation of the RMI server, GUI client, handling login/registration, and various functionalities (comments, photo rating, etc.).
Program Title

PhotoRankingApp
Brief Description of the Program's Purpose

The purpose of the program is to allow users to view, rate, and comment on photos. Admins can add new photos. Comments are filtered for inappropriate language. The program displays the average rating and number of views for each photo.

Description and Logical Structure Diagram of the Application

Structure:

Server: Responsible for storing and managing photos, comments, and ratings.
Client: User interface enabling interaction with the server.
Services: RMI interfaces for communication between the client and the server.
Diagram: (Diagram would be placed here if present)

Information about Custom Classes Used

Server Classes:

PhotoServiceImpl: Implementation of the PhotoService interface.
UserServiceImpl: Implementation of the UserService interface.
Photo: Class representing a photo, storing photo data.
User: Class representing a user, storing login data and role.
PhotoServer: Server implementation.

Client Classes:

PhotoClient: GUI class for user interaction and communication with the server.
Interfaces:

PhotoService: RMI interface for photo operations.
UserService: RMI interface for user operations.
Description of Specific Problem-Solving Methods

Profanity Filter:

containsBannedWords(String comment): A method that checks whether a comment contains profane words. It loads words from the file badwords.txt and compares them to the comment content.
Image Scaling:

scaleImage(byte[] imageData, int width, int height): A method that scales an image to specific dimensions, ensuring proper proportions and quality.
Short User Manual

Run the server: PhotoServer.java
Run the client: PhotoClient.java
Log in or register a new account (there is no predefined user at the start). Admins can add photos.
Select a photo from the list to view, rate, or comment on it.
Add a rating (1-5) and comment. Comments are filtered for profanity.
Photos can be viewed, and the number of views and the average rating are automatically updated.
Program Limitations

The maximum number of supported clients is limited by system resources.
