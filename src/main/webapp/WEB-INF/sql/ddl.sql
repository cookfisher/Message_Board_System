
CREATE TABLE POST (
                      ID INT PRIMARY KEY AUTO_INCREMENT,
                      POSTED_BY VARCHAR(255) NOT NULL,
                      POSTED_AT BIGINT NOT NULL,
                      UPDATED BIT NOT NULL DEFAULT 0,
                      CONTENT TEXT NOT NULL,
                      HASH_TAG TEXT NULL,
                      ATTACHMENT VARCHAR(255) NULL,
                      ATTACHMENT_NAME VARCHAR(255) NULL,
                      GROUP_NAME VARCHAR(255)
)