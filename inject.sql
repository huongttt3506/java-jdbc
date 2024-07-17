SELECT * FROM user WHERE id = ??;

-- 1 OR 1 = 1
SELECT * FROM user WHERE id = 1 OR 1 = 1;

-- 1; DROP TABLE user
SELECT * FROM user WHERE id = 1; DROP TABLE user;


SELECT * 
FROM user
WHERE
    username = '%s' AND password = '%s';

-- alex
-- 1' OR '1' = '1
SELECT * 
FROM user
WHERE
    username = 'alex' AND password = '1' OR '1' = '1';