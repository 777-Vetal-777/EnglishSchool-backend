CREATE TABLE USERS(
id BIGINT NOT NULL AUTO_INCREMENT,
firstName VARCHAR(30),
lastName VARCHAR(30),
phoneNumber VARCHAR(30) unique,
role VARCHAR(30),
active BOOLEAN
);
CREATE TABLE STUDENTS(
address VARCHAR(30)
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE TEACHERS(
maxCourses BIGINT,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE COURSE(
id BIGINT NOT NULL AUTO_INCREMENT,
name VARCHAR(30),
startDate DATE,
endDate DATE,
startTime TIME,
endTime TIME,
teacher_id INT,
price INT,
maxCapacity INT,
CONSTRAINT FK_users_courses FOREIGN KEY (teacher_id) REFERENCES teachers (id)
);

CREATE TABLE CONTRACTS(
id BIGINT NOT NULL AUTO_INCREMENT,
student_id INT,
course_id INT,
contractStatusType VARCHAR(30)
CONSTRAINT FK_contracts_students FOREIGN KEY (student_id) REFERENCES users (id)
CONSTRAINT FK_contracts_courses FOREIGN KEY (course_id) REFERENCES courses (id)
)

CREATE TABLE STUDENT_INVOICES(
id BIGINT NOT NULL AUTO_INCREMENT,
startDate DATE,
endDate DATE,
money INT,
payed BOOLEAN
CONSTRAINT FK_contracts_student_invoices FOREIGN KEY (contract_id) REFERENCES contracts (id)

)