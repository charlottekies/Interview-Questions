sBEGIN TRANSACTION;
DROP TABLE IF EXISTS question;

-- Create question table


DROP TABLE IF EXISTS question;
CREATE TABLE question (
                        question_id SERIAL,
                        is_star BOOLEAN,
                        is_behavioral BOOLEAN, 
                        question TEXT UNIQUE,
                        
                        CONSTRAINT PK_question PRIMARY KEY (question_id,question)
                       
);


-- insert initial values         
INSERT INTO question (is_star,is_behavioral,question)
VALUES (true,false,'Describe a time when you cooperated with someone outside of your team/department/group in an activity that turned out to benefit both teams/departments/groups.');




COMMIT;