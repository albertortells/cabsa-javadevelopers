CREATE SCHEMA jungle;

CREATE table jungle.animal
(
  id   INT,
  name VARCHAR(50),
  legs INT,

  PRIMARY KEY (id)
);
CREATE table jungle.food
(
  id   INT,
  name VARCHAR(50),
  PRIMARY KEY (id)
);


INSERT INTO jungle.animal VALUES ('0', 'Rabbit', '4');
INSERT INTO jungle.animal VALUES ('1', 'Bear', '4');
INSERT INTO jungle.animal VALUES ('2', 'Deer', '4');
INSERT INTO jungle.animal VALUES ('3', 'Snake', '0');
INSERT INTO jungle.animal VALUES ('4', 'Coocodrile', '4');
INSERT INTO jungle.animal VALUES ('5', 'Chicken', '2');
INSERT INTO jungle.animal VALUES ('6', 'Spider', '8');


INSERT INTO jungle.food VALUES ('0', 'carrot');
INSERT INTO jungle.food VALUES ('1', 'honey');
INSERT INTO jungle.food VALUES ('2', 'leafs');
INSERT INTO jungle.food VALUES ('3', 'insects');
INSERT INTO jungle.food VALUES ('4', 'vermin');
INSERT INTO jungle.food VALUES ('5', 'birdseed');
