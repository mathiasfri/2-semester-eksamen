# Contribute - Project Calculator Tool

### Introduction
The purpose of this project is to graduate from the second semester of data science. 
Along with that we as a group has been put on a job by the company Alpha Solutions to
create a tool for them to use for their everyday work to get an overview of their projects.

## Database
Our database consists of 4 entity tables. These entity tables also have relation
tables. The SQL-script can be seen below.
```mysql
CREATE TABLE users
(
    user_id       INTEGER NOT NULL AUTO_INCREMENT,
    email    VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    PRIMARY KEY (user_id)
);

CREATE TABLE project
(
    id    INTEGER NOT NULL AUTO_INCREMENT,
    title VARCHAR(50),
    description VARCHAR(999),
    deadline DATE,
    budget DOUBLE,
    time_spent DOUBLE,
    user_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subProject
(
    id          INTEGER NOT NULL AUTO_INCREMENT,
    title       VARCHAR(30),
    deadline    DATE,
    time_spent  DOUBLE,
    project_id  INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE
);

CREATE TABLE tasks
(
    id           INTEGER NOT NULL AUTO_INCREMENT,
    title        VARCHAR(30),
    deadline     DATE,
    time_spent   DOUBLE,
    sub_id       INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE
);

CREATE TABLE project_user
(
    project_id INTEGER,
    user_id    INTEGER,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subproject_user
(
    sub_id   INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (sub_id, user_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE tasks_user
(
    task_id  INTEGER,
    user_id  INTEGER,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE subproject_tasks
(
    sub_id  INTEGER,
    task_id INTEGER,
    PRIMARY KEY (sub_id, task_id),
    FOREIGN KEY (sub_id) REFERENCES subProject (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
```

## Classes and code setup
In our project we have 5 controller classes, 7 model classes and 8 repository classes.
- Controller
  - LoginController
  - mainPageController
  - ProjectController
  - SubProjectController
  - TaskController
- Models
  - Project
  - ProjectUser
  - SubProject
  - SubProjectUser
  - Tasks
  - TasksUser
  - User
- Repository
  - LoginRepository
  - ProjectRepository
  - ProjectUserRepository
  - SubProjectRepository
  - SubProjectUserRepository
  - TasksRepository
  - TasksUserRepository
  - UserRepository

## Login function
When signing up it's required to enter email and password.

These information will be taken by the class **LoginController** and here there 
will be created a new instance of a user and send it to the html-page "create-user". 
This can be seen in the controller method below -

```java
    @GetMapping("/create")
    public String createUser(Model model){
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "create-user";
    }
```
After the instance has been filled with the users information and the user presses the 'Submit' button, 
the url "created-user" will be called and **addUser** method from the controller will add the user to the table.

```java
    @PostMapping("/adduser")
    public String addUser(@ModelAttribute User newUser, Model model){
        int userId = userRepository.createUser(newUser);
        model.addAttribute("email", newUser.getEmail());
        model.addAttribute("password", newUser.getPassword());
        model.addAttribute("userId", userId);
        return "userCreated";
    }
```

The controller method **addUser** is calling our method **createUser** method from our repository. 
The snippet below contains this method.

```java
    public int createUser(User newUser) {
        int userId = 0;
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO users (email, password) values (?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newUser.getEmail());
            pstmt.setString(2, newUser.getPassword());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
```

The method retrieves the generated keys *first_name, last_name, user_email and user_password* and fills them into our SQL statement. 
The *user_id* will be automatically generated, taking the latest id into account.

### Sign-in
When signing in you have to put in your email and a password. Then when the sign-in button is pressed, our **login** method is called from our ***LoginController*** class.

```java
    @PostMapping("/login")
    public String login(@RequestParam("uid") String uid, @RequestParam("pw") String pw, HttpSession session, Model model) {
        User user = loginRepository.checkEmail(uid);
        if (user != null && user.getPassword().equals(pw)) {
            session.setAttribute("user", user);
            current_userId = user.getUserId();

            session.setMaxInactiveInterval(3600);

            return "redirect:/projectCalculator/mainPage/" + current_userId;
        }

        // wrong credentials
        model.addAttribute("wrongCredentials", true);
        return "login";
    }
```

First the method creates an instance of "User" that uses our method **checkEmail** from our ***LoginRepository*** class. 
This method will be explained further down. After the instance of a User has been retrieved from the method, 
it goes into an if-statement that checks whether or not a user is found and if the password and email matches together.
If they match, the user will be logged in and sent to their homepage, which is their page with all their projects.

In the snippet below you will see the code for the method **checkEmail**.
```java
    public User checkEmail(String email) {
        User user = new User();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM users WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user.setUserId(rs.getInt("user_id"));
                user.setPassword(rs.getString("password"));
            }

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```
This method sends an SQL statement to the database that takes all the login information about a user according to the email they have put in when loggin in. If a user is found with the email it will set the id and password of the user and return it. After this the controller method does the rest with it's if-statement.

### Check if logged in
When a user opens up our webpage, the program will check whether or not the user is logged in. If a user tries to open up their main page without being logged in, they will be sent to the login page. This is done by the method **isLoggedIn** from the ***LoginController*** class.
```java
    protected boolean isLoggedIn(HttpSession session, int uid) {
        return session.getAttribute("user") != null && current_userId == uid;
    }
```
The method checks if a user object is returned as not null and it also checks if the user_id for the page matches the logged in user_id. 
If this all matches, the user will be able to see their main page. This is done by returning a boolean value.

## Create project function
When adding a project to your user you need to press "add project" button on your own home page. 
When this button is pressed, the page "create-project" will open and the user can fill out information 
about the wish including *title, description, deadline and budget*. 
This also calls our method **createProject** from our controller.

```java
    @GetMapping("/createproject/{uid}")
    public String createProject(@PathVariable int uid, Model model){
        Project newProject = new Project();
        newProject.setUserId(uid);
        User user = userRepository.getUser(uid);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("newProject", newProject);
        return "create-project";
    }
```

This method creates an empty instance of the ***Project*** class and takes the user_id of the current user to add to the table.
When the user presses the button "Submit" the **addProject** method from the controller will now be called, which can be seen below.

```java
    @PostMapping("/addproject")
    public String addProject(@ModelAttribute Project newProject){
        projectRepository.createProject(newProject);
        return "redirect:/projectCalculator/mainPage/" + newProject.getUserId();
    }
```

This method will call our method **createProject** from our ***ProjectRepository*** class. 
This method can be seen below.

```java
    public void createProject(Project project) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO Project(title, deadline, budget, description, time_spent, user_id) VALUES(?, ?, ?, ?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, project.getTitle());
            LocalDate deadline = project.getDeadline();
            java.sql.Date sqlDeadLine = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadLine);
            pstmt.setInt(3, project.getBudget());
            pstmt.setString(4, project.getDescription());
            pstmt.setDouble(5, project.getTimeSpent());
            pstmt.setInt(6, project.getUserId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```

The method retrieves the information about the wish and puts it into the SQL statement, 
which then puts it onto the table. 
The user_id is retrieved from the empty instance of Project that was made.
<br><br>
This is done the same exact way with SubProject and Tasks.

## Update and delete
When a project or SubProject and Task is created - the user has the possibility to
both delete and edit it. These methods will be explained below.

When deleting a wish you call the controller of the object. In this example we chose
Project. So ***ProjectController*** is called on **deleteProject** seen below -

```java
    @PostMapping("/deleteproject/{pid}")
    public String deleteProject(@PathVariable int pid, @ModelAttribute Project projectDelete) {
        projectRepository.deleteProject(pid);
        return "redirect:/projectCalculator/mainPage/" + projectDelete.getUserId();
    }
```

This controller method does not really do anything other that call a method from
***ProjectRepository*** called **deleteProject(pid)**. Method shown below - 

```java
    public void deleteProject(int projectId) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "DELETE FROM project WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```

The method uses SQL syntax statement "DELETE". All we have to do is put the projectId
into the statement, as that attribute is unique for all projects.

To edit a project is a little more to it. You call a method from the same controller,
this time we are just using method **updateProject**, this is seen below -

```java
    @GetMapping("/updateproject/{pid}")
    public String updateProject(@PathVariable int pid, Model model){
        Project updateProject = projectRepository.getSpecificProject(pid);
        model.addAttribute("updateProject", updateProject);
        User user = userRepository.getUser(updateProject.getUserId());
        model.addAttribute("userId", user.getUserId());
        return"update-project";
    }
```

The method creates an instance of a Project using the method **getSpecificProject(pid)**
from ***ProjectRepository*** which retrieves a specific project from the database. 
This method is shown below -

```java
    public Project getSpecificProject(int projectID) {
        Project project = null;

        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM project WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, projectID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                java.sql.Date sqlDeadLine = rs.getDate("deadline");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(sqlDeadLine);
                LocalDate deadline = sqlDeadLine.toLocalDate();
                int budget = rs.getInt("budget");
                int id = rs.getInt("user_id");
                String description = rs.getString("description");
                double timeSpent = rs.getDouble("time_spent");

                project = new Project(projectID, title, deadline, budget, id, description, timeSpent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return project;
    }
```

It pretty much just gather all the information about a Project using the projectId,
as again the id is unique and the system knows which Project it is just by using the id.

It will display the settings of the project for the user and the user can now edit
what they want to edit. When the user presses 'Submit', another controller will be 
called. This is again from ***ProjectController*** and can be seen below -

```java
    @PostMapping("/updateproject")
    public String updateUserProject(@ModelAttribute Project projectUpdate){
        projectRepository.updateProject(projectUpdate);
        return "redirect:/projectCalculator/mainPage/" + projectUpdate.getUserId();
    }
```
This project takes the project we made before and the changes that the user made and
collects them into one and calls the method **updateProject** from the repository.
This method can be seen below - 

```java
    public void updateProject(Project project) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "UPDATE Project SET title=?, deadline=?, budget=?, description =?, time_spent=? WHERE id = ?;";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, project.getTitle());
            LocalDate deadline = project.getDeadline();
            java.sql.Date sqlDeadline = java.sql.Date.valueOf(deadline);
            pstmt.setDate(2, sqlDeadline);
            pstmt.setInt(3, project.getBudget());
            pstmt.setString(4, project.getDescription());
            pstmt.setDouble(5, project.getTimeSpent());
            pstmt.setInt(6, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```

The update is done by the SQL syntax statement "UPDATE". The information is passed
on from the object and can be sent into the database. The project is now updated.


## Assign function
When assigning a user to a project, it calls the controller method **assignUserToProject**
this method retrieves a project id and a user email. The email is used to get the id of the user.
The project id is used to specify which project we want a user to be assigned to.
The controller method can be seen below -
```java
    @PostMapping("/assignuser/{projectId}")
    public String assignUserToProject(@PathVariable int projectId, @RequestParam("email") String userEmail, Model model) {
        List<Integer> listOfUserIds = new ArrayList<>();
        int userId = projectUserRepository.getUserIdByEmail(userEmail);
        model.addAttribute("projectID", projectId);
        if(userId != -1) {
            listOfUserIds.add(userId);
            projectUserRepository.assignUsersToProject(projectId, listOfUserIds);
        }

        return "redirect:/projectCalculator/mainPage/" + projectId;
    }
```

In the controller a method is called **assignUsersToProject**, which uses the relation
table that we have create specifically for projects and users. 
We take the userId and the projectId and puts them into the table.
This can be seen below - 

```java
    public void assignUsersToProject(int projectId, List<Integer> userIds) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "INSERT INTO Project_User(project_id, user_id) VALUES(?, ?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            for (int userId : userIds) {
                pstmt.setInt(1, projectId);
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
```

After this, the user that is assigned to a project can view the project without owning it.
This page is shown by a controller in ***mainPageController*** and can be seen below

```java
    @GetMapping("/assignedprojects/{uid}")
    public String getAssignedProjects(@PathVariable int uid, Model model) {
        List<Project> assignedProjects = projectRepository.getAssignedProjects(uid);
        User loggedInUser = userRepository.getUser(uid);
        model.addAttribute("userId", loggedInUser.getUserId());
        model.addAttribute("assignedProjects", assignedProjects);
        return "assignedProjects";
    }
```

This page is based on a returned list of projects. The projects returned is based on
which projects, that the user is assigned to. This is checked by method **getAssignedProjects**
from the ***ProjectController***. Code snippet can be seen below -

```java
    public List<Project> getAssignedProjects(int userId) {
        List<Project> assignedProjects = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM project JOIN project_user ON project_user.project_id = project.id WHERE project_user.user_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setTitle(rs.getString("title"));
                project.setDeadline(rs.getDate("deadline").toLocalDate());
                project.setDescription(rs.getString("description"));
                project.setTimeSpent(rs.getDouble("time_spent"));

                assignedProjects.add(project);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return assignedProjects;
    }
```

If a user clicks further into a project to view it, that they are assigned to - 
***ProjectController*** is called again and returns mainPage.html.

In this occasion it will be run through an if statement seen below -
```java
if (project.getUserId() == loggedInUser.getUserId() || projectUserRepository.checkIfAssignedToProject(pid).contains(loggedInUser.getUserId()))
```

First it checks if you own the project. If not it calls the **checkIfAssignedToProject**
method. This is shown further down.
If that if-statement or that method returns true, they can see the project.
Below the method **checkifAssignedToProject** can be seen -

```java
    public List<Integer> checkIfAssignedToProject(int projectID) {
        List<Integer> listOfAssignedUsers = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM project_user WHERE project_id = ?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, projectID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("user_id");

                listOfAssignedUsers.add(userID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfAssignedUsers;
    }
```

In short, it just checks the before-mentioned relation table, if anything matches.

Now when the user is inside the project they are also checked if they are assigned
to any SubProjects or Tasks. This is done with a method and a HashMap. First we can 
show the snippet from the ***ProjectController*** method that does this -

```java
            Map<Integer, Boolean> assignedSubStatusMap = new HashMap<>();
            for (SubProject s : subProjects) {
                List<Tasks> tasks = tasksRepository.getTaskList(s.getId());
                s.setTasks(tasks);

                boolean isAssignedToSub = subProjectUserRepository.isAssignedToSubproject(loggedInUser.getUserId(), s.getId());
                assignedSubStatusMap.put(s.getId(), isAssignedToSub);
            }
            model.addAttribute("assignedSubStatusMap", assignedSubStatusMap);
```

First we make an empty new HashMap. We then check through all the SubProjects that 
we have in a list using projectId. We make a boolean isAssignedToSub that calls
a method from ***SubProjectUserRepository***. The result of this method is then
put into the HashMap together with the correlating SubProjectId.
The method mentioned can be seen below.

```java
    public boolean isAssignedToSubproject(int userID, int subID) {
        try (Connection con = DriverManager.getConnection(url, user_id, user_pwd)) {
            String SQL = "SELECT * FROM subproject_user WHERE user_id = ? AND sub_id = ?";
            PreparedStatement pstm = con.prepareStatement(SQL);
            pstm.setInt(1, userID);
            pstm.setInt(2, subID);
            ResultSet rs = pstm.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
```

The HashMap is then sent to the HTML site, where we iterate through all the values
inside the HashMap and return a CSS class style if any value is returned true.
The HTML code can be seen below -

```html
th:classappend="${assignedTaskStatusMap.get(task.getId()) != null && assignedTaskStatusMap.get(task.getId()) ? 'assigned' : ''}"
```

This append-method is applied to a div object and can therefore be changed if there
are any true values coming from the HashMap. 

The same is done when checking through Tasks.