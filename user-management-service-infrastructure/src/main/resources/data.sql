INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'John Doe' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'John Doe'
);

INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'Maria' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'Maria'
);

INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'Josefa' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'Josefa'
);

INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'Anastacia' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'Anastacia'
);

INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'Camila' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'Camila'
);

INSERT INTO rchallenge_user_pragma.person (name)
SELECT 'Valentina' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM rchallenge_user_pragma.person WHERE name = 'Valentina'
);