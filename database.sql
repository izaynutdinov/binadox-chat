CREATE DATABASE chat;
CREATE USER 'chat'@'localhost' IDENTIFIED BY 'qwerty';
GRANT ALL ON chat.* TO 'chat'@'localhost';

USE chat;

CREATE TABLE USERS(
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	FIRST_NAME VARCHAR(256),
	LAST_NAME VARCHAR(256),
	LOGIN VARCHAR(256) UNIQUE,
    PASSWORD_MD5 CHAR(32),
	EMAIL VARCHAR(255),
	EMAIL_VERIFIED BOOLEAN,
	EMAIL_VERIFICATION_TOKEN CHAR(32) UNIQUE
) ENGINE=InnoDB;

INSERT INTO USERS(FIRST_NAME, LAST_NAME, LOGIN, PASSWORD_MD5, EMAIL, EMAIL_VERIFIED)
VALUES('Linus', 'Torvalds', 'linus', 'ef1fedf5d32ead6b7aaf687de4ed1b71', 'linus@gmail.com', true);

INSERT INTO USERS(FIRST_NAME, LAST_NAME, LOGIN, PASSWORD_MD5, EMAIL, EMAIL_VERIFIED)
VALUES('Bjarne', 'Stroustrup', 'bjarne', 'ef1fedf5d32ead6b7aaf687de4ed1b71', 'bjarne@gmail.com', true);

INSERT INTO USERS(FIRST_NAME, LAST_NAME, LOGIN, PASSWORD_MD5, EMAIL, EMAIL_VERIFIED)
VALUES('James', 'Gosling', 'james', 'ef1fedf5d32ead6b7aaf687de4ed1b71', 'james@gmail.com', true);

CREATE TABLE CHATS(
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	OWNER_ID INTEGER NOT NULL,
	TITLE VARCHAR(256),
	DESCRIPTION TEXT,
	FOREIGN KEY(OWNER_ID) REFERENCES USERS(ID)
) ENGINE=InnoDB;

INSERT INTO CHATS(OWNER_ID, TITLE, DESCRIPTION)
VALUES(1, "Main Chat", "This is main chat");

INSERT INTO CHATS(OWNER_ID, TITLE, DESCRIPTION)
VALUES(1, "Linux Chat", "This is linux chat");

INSERT INTO CHATS(OWNER_ID, TITLE, DESCRIPTION)
VALUES(2, "C++ Chat", "This is C++ chat");

INSERT INTO CHATS(OWNER_ID, TITLE, DESCRIPTION)
VALUES(3, "Java Chat", "This is linux chat");

CREATE TABLE CHAT_USERS(
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	CHAT_ID INTEGER NOT NULL,
	USER_ID INTEGER NOT NULL,
	BLOCKED BOOLEAN NOT NULL DEFAULT false
) ENGINE=InnoDB;

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(1, 1, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(1, 2, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(1, 3, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(2, 1, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(2, 2, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(2, 3, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(3, 1, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(3, 2, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(3, 3, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(4, 1, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(4, 2, false);

INSERT INTO CHAT_USERS(CHAT_ID, USER_ID, BLOCKED)
VALUES(4, 3, false);

CREATE TABLE CHAT_MESSAGES(
	ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	CHAT_USER_ID INTEGER,
	MSG_TIME TIMESTAMP,
	MSG_TEXT TEXT,
	FOREIGN KEY(CHAT_USER_ID) REFERENCES CHAT_USERS(ID)
) ENGINE=InnoDB;

INSERT INTO CHAT_MESSAGES(CHAT_USER_ID, MSG_TIME, MSG_TEXT) VALUES(1, NOW(), "Hi, I entered the chat!");
INSERT INTO CHAT_MESSAGES(CHAT_USER_ID, MSG_TIME, MSG_TEXT) VALUES(2, NOW(), "Hi, I entered the chat!");
INSERT INTO CHAT_MESSAGES(CHAT_USER_ID, MSG_TIME, MSG_TEXT) VALUES(3, NOW(), "Hi, I entered the chat!");
