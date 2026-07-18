CREATE SCHEMA jungle;

CREATE table jungle.food
(
    id   INT auto_increment,
    name VARCHAR(50),

    PRIMARY KEY (id)
);
CREATE table jungle.animal
(
    id   INT auto_increment,
    name VARCHAR(50),
    legs INT,
    food INT,

    PRIMARY KEY (id),
    FOREIGN KEY (food) REFERENCES food(id)
);


INSERT INTO jungle.food VALUES ('1', 'carrot');
INSERT INTO jungle.food VALUES ('2', 'honey');
INSERT INTO jungle.food VALUES ('3', 'leafs');
INSERT INTO jungle.food VALUES ('4', 'insects');
INSERT INTO jungle.food VALUES ('5', 'vermin');
INSERT INTO jungle.food VALUES ('6', 'birdseed');
INSERT INTO jungle.food VALUES ('7', 'mouse');


INSERT INTO jungle.animal VALUES ('0', 'Rabbit', '4', '1');
INSERT INTO jungle.animal VALUES ('1', 'Bear', '4', '2');
INSERT INTO jungle.animal VALUES ('2', 'Deer', '4', '3');
INSERT INTO jungle.animal VALUES ('3', 'Snake', '0', '7');
INSERT INTO jungle.animal VALUES ('4', 'Crocodile', '4', '5');
INSERT INTO jungle.animal VALUES ('5', 'Chicken', '2', '6');
INSERT INTO jungle.animal VALUES ('6', 'Spider', '8', '4');
