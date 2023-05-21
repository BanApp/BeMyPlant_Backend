CREATE TABLE userinfo (
                          user_id INT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(50) UNIQUE,
                          password VARCHAR(100),
                          phones VARCHAR(50),
                          r_name VARCHAR(50),
                          cre_date VARCHAR(50),
                          activated BOOLEAN
);

CREATE TABLE authority (
    authority_name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE user_authority (
                                user_id INT,
                                authority_name VARCHAR(50),
                                PRIMARY KEY (user_id, authority_name)
);
