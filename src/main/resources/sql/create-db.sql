CREATE TABLE EVENT
  (
     id       VARCHAR(100) PRIMARY KEY,
     duration BIGINT,
     type     VARCHAR(100),
     host     VARCHAR(100),
     alert    BOOLEAN
  );