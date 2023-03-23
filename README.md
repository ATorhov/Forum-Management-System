# **Forum management system**

### Project Description

_Crypto Forum, where the users can create posts, add comments, and upvote/downvote the things that they like or dislike the most._

---
### Functionality

#### Public Part

_The public part is accessible without authentication.On the home page, anonymous users can present with the core features of the platform as well as how many people are using it and how many posts have been created so far. Anonymous users able to register and log in.
Anonymous users able to see a list of the top 10 most commented posts and a list of the 10 most recently created posts._

#### Private part
***<ins>Accessible only if the user is authenticated.</ins>***

#### Registered users will be able to:
- Browse posts created by the other users with an option to sort and filter them.
- To view a single post including its title, content, comments, likes, etc. The details of the post and any available user actions (comment/like/edit) should be presented on the same page.
- To update their profile information, including a profile photo.
- To create a new post with at least a title and content.
- To edit only personal posts or comments.
- To view all their or any other user’s posts and comments (with the option to filter and sort them).
- To remove one or more of their own posts.
- To comment/reply to any other forum post.

#### Registered users won't be able to:
- To change their username once registered. 
- To create duplicate account with the same email address.
- To edit someone else's posts.

### Roadmap:
- [x] Create a README file.
- [x] Images of the database relations.
- [x] Create classes: User, Post, Comment.
- [x] CRUD operations for User. **<ins>Important**
- [x] CRUD operations for Post. **<ins>Important**
- [x] CRUD operations for Comment. **<ins>Important**
- [x] Search by username, email, or first name for User class. **<ins>Important**
- [x] Filter and sort certain users’ posts for User class. **<ins>Important**
- [x] Implement `make other user admin` feature. **<ins>Important**
- [x] Implement `delete post` feature. **<ins>Important**
- [x] Implement `block/unblock user` feature. **<ins>Important**
- [x] Create 'Home page' [HTML page]
- [x] Create 'Guest page' (for non-registered/logged in users) [HTML page]
- [x] Create 'Login' [HTML page]
- [x] Create 'Register' [HTML page]
- [ ] Create User dashboard  [HTML page]
- [ ] Create Admin dashboard [HTML page]
- [x] Create 'Post view' [HTML page]
- [x] Create 'Create new post' [HTML page]
- [x] Create 'Edit post' [HTML page]
- [ ] Implement LIKES / DISLIKES on 'Post view' page [CRUD + Thymeleaf] **<ins> Very Important**
- [x] Integrate list of comments to 'Post view' page [Thymeleaf]
- [x] Implement add/delete comment in 'Post view' page [Thymeleaf]
- [ ] Implement MVC authentication for User.
- [x] Implement MVC authentication for Post.
- [x] Implement MVC authentication for Comment.
- [ ] Implement MVC Search and Filtering for Post.
- [ ] Implement MVC Search and Filtering for User.
- [ ] Implement MVC Search and Filtering for Comment.
- [ ] Create and provide a link to the Swagger documentation. **<ins> Very Important**
- [ ] Instructions on how to set up and run the project locally **<ins> Very Important**

#### Optional features:
- [ ] Implement 'add tag' to the Post  [First priority task]
- [ ] Add to favorites Post [User]
- [ ] Add friend [User]
- [ ] Report spam/inappropriate content [Comment and Post]
- [ ] Notifications:
- new comment added to your post. 
- your friend created a new post.
- [ ] Send personal messages between users. [User dashboard]
- [ ] Mass reply to all users [Admin dashboard]

---

### Swagger documentation

***<ins>Documentation is pending to be completed...***

---

### REST API

***<ins>Documentation is pending to be completed...***

---

### Database image

## [Image link](https://ibb.co/Ptz2GPh) 

---

### Use Cases

**Scenario 1**

*A friend of Pavel’s told him about this amazing forum, where lots of people share their ideas and perspectives on the crypto/stock market. Pavel enters the website and sees a feed of posts. He can sort them by most liked or newest. He can also filter them by a certain word/s. He is an anonymous user so he cannot create a post yet. He registers and then logs in to the forum. He can now start sharing his ideas with his buddy crypto “hodlers.”*

**Scenario 2**

*Your forum has accumulated thousands of new users. Most of them are proactively helpful and positive, but some of them started posting spam or/and irrelevant information to the forum. You hire a moderator. You instruct the moderator to enter the forum and create a first-time registration. You as an admin give admin rights to your moderator through the forum. They can now start deleting posts and ban users that do not follow the forum rules!*

**Technical Requirements**

<ins>General · Follow OOP principles when coding · Follow KISS, SOLID, DRY principles when coding · Follow REST API design best practices when designing the REST API (see Appendix)

    * Use tiered project structure (separate the application in layers)

    * The service layer (i.e., "business" functionality) must have at least 80%-unit test code coverage

    * You should implement proper exception handling and propagation

    * Try to think ahead. When developing something, think – “How hard would it be to change/modify this later?”

---

### Optional Requirements

· Integrate your project with a Continuous Integration server (e.g., GitLab’s own) and configure your unit tests to run on each commit to your master branch

· Host your application's backend in a public hosting provider of your choice (e.g., AWS, Azure, Heroku)

· Use branches while working with Git
