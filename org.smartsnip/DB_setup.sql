/* 
 * MySQL database file
 * 
 * 20.04.2012
 * Gerhard Aigner
 * 
 * This file contains the databased schema for the Smartsnip - Snippet Community.
 * Use it to set up the tabels and triggers. The database itself must be created 
 * previusly.
 */

/*
 * assure the delimiter is set to the default
 */
DELIMITER ;

/*
 * set up the tables
 */
CREATE TABLE `User` (
  `user_name` VARCHAR(20)  NOT NULL,
  `full_name` VARCHAR(255)  DEFAULT NULL,
  `email` VARCHAR(255)  NOT NULL,
  `last_login` TIMESTAMP  DEFAULT 0,
  `user_state` ENUM("unvalidated", "validated", "deleted", "moderator", "administrator")  NOT NULL DEFAULT "unvalidated",
  PRIMARY KEY (`user_name`),
  CONSTRAINT `u_usr_eml` UNIQUE `u_usr_eml` (`email`)
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * User
 */
CREATE TABLE `Login` (
  `user_name` varchar(20)  NOT NULL,
  `password` varchar(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `grant_login` BOOLEAN  NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`user_name`),
  CONSTRAINT `fk_usr_pwd` FOREIGN KEY `fk_usr_pwd` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB
PASSWORD = 'd2aefeac9dc661bc98eebd6cc12f0b82'
CHARACTER SET utf8 COLLATE utf8_general_ci
COMMENT = 'Encrypted Table';

CREATE TABLE `Tag` (
  `tag_name` VARCHAR(50) NOT NULL,
  `usage_freq` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`tag_name`)
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `Language` (
  `language` VARCHAR(50) NOT NULL,
  `highlighter` VARCHAR(255) DEFAULT NULL,
  `is_default` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`language`)
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `License` (
  `license_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `short_descr` VARCHAR(255)  NOT NULL,
  `license_text` TEXT  DEFAULT NULL,
  PRIMARY KEY (`license_id`),
  CONSTRAINT `lic_short_descr` UNIQUE `lic_short_descr` (`short_descr`)
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `Category` (
  `category_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` INTEGER UNSIGNED DEFAULT NULL,
  `name` VARCHAR(255)  NOT NULL,
  `description` TEXT  DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  CONSTRAINT `u_cat_name` UNIQUE `u_cat_name` (`name`),
  CONSTRAINT `fk_parent_id` FOREIGN KEY `fk_parent_id` (`parent_id`)
    REFERENCES `Category` (`category_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * User
 * Category
 * License
 */
CREATE TABLE `Snippet` (
  `snippet_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `headline` VARCHAR(255) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `viewcount` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  `rating_average` FLOAT NOT NULL DEFAULT 0,
  `last_edited` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP 
    ON UPDATE CURRENT_TIMESTAMP,
  `user_name` VARCHAR(20)  DEFAULT NULL,
  `category_id` INTEGER UNSIGNED NOT NULL,
  `license_id` INTEGER UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`snippet_id`),
  CONSTRAINT `fk_snip_user` FOREIGN KEY `fk_snip_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_snip_cat` FOREIGN KEY `fk_snip_cat` (`category_id`)
    REFERENCES `Category` (`category_id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_snip_lic` FOREIGN KEY `fk_snip_lic` (`license_id`)
    REFERENCES `License` (`license_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * Snippet
 * Tag
 */
CREATE TABLE `RelTagSnippet` (
  `snippet_id` INTEGER UNSIGNED NOT NULL,
  `tag_name` VARCHAR(50)  NOT NULL,
  PRIMARY KEY (`snippet_id`, `tag_name`),
  CONSTRAINT `fk_rel_ts_snippet` FOREIGN KEY `fk_rel_ts_snippet` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rel_ts_tag` FOREIGN KEY `fk_rel_ts_tag` (`tag_name`)
    REFERENCES `Tag` (`tag_name`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci
COMMENT = 'Relationship between Tables Tag and Snippet';

/*
 * Dependent on tables:
 * User
 * Snippet
 */
CREATE TABLE `Favourite` (
  `user_name` VARCHAR(20)  NOT NULL,
  `snippet_id` INTEGER UNSIGNED NOT NULL,
  `is_favourite` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`user_name`, `snippet_id`),
  CONSTRAINT `fk_fav_user` FOREIGN KEY `fk_fav_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_fav_snippet` FOREIGN KEY `fk_fav_snippet` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * User
 * Snippet
 */
CREATE TABLE `Notification` (
  `notification_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(20) NOT NULL,
  `snippet_id` INTEGER UNSIGNED DEFAULT NULL,
  `viewed` BOOLEAN NOT NULL DEFAULT FALSE,
  `message` TEXT DEFAULT NULL,
  `origin` VARCHAR(255) DEFAULT NULL,
  `created_at` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  CONSTRAINT `fk_notif_user` FOREIGN KEY `fk_notif_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_notif_snippet` FOREIGN KEY `fk_notif_snippet` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * Snippet
 * Language
 */
CREATE TABLE `Code` (
  `code_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `snippet_id` INTEGER UNSIGNED NOT NULL,
  `language` VARCHAR(50) NOT NULL,
  `file` TEXT  DEFAULT NULL,
  `file_name` VARCHAR(255) DEFAULT NULL,
  `file_content` MEDIUMBLOB DEFAULT NULL,
  `version` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`code_id`),
  CONSTRAINT `fk_code_snippet` FOREIGN KEY `fk_code_snippet` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_code_lang` FOREIGN KEY `fk_code_lang` (`language`)
    REFERENCES `Language` (`language`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * Snippet
 * User
 */
CREATE TABLE `Rating` (
  `snippet_id` INTEGER UNSIGNED NOT NULL,
  `user_name` VARCHAR(20)  NOT NULL,
  `rating_value` INTEGER  NOT NULL DEFAULT 0,
  PRIMARY KEY (`snippet_id`, `user_name`),
  CONSTRAINT `fk_rating_snip` FOREIGN KEY `fk_rating_snip` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_rating_user` FOREIGN KEY `fk_rating_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * Snippet
 * User
 */
CREATE TABLE `Comment` (
  `comment_id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `snippet_id` INTEGER UNSIGNED NOT NULL,
  `refers_to` VARCHAR(255)  DEFAULT NULL,
  `message` TEXT  DEFAULT NULL,
  `created_at` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_name` VARCHAR(20)  DEFAULT NULL,
  `pos_votes_sum` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  `neg_votes_sum` INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`comment_id`),
  CONSTRAINT `fk_comm_snippet` FOREIGN KEY `fk_comm_snippet` (`snippet_id`)
    REFERENCES `Snippet` (`snippet_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_comm_user` FOREIGN KEY `fk_comm_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/*
 * Dependent on tables:
 * Comment
 * User
 */
CREATE TABLE `Vote` (
  `comment_id` INTEGER UNSIGNED NOT NULL,
  `user_name` VARCHAR(20) NOT NULL,
  `vote` ENUM("none","negative","positive")  NOT NULL DEFAULT "none",
  PRIMARY KEY (`comment_id`, `user_name`),
  CONSTRAINT `fk_vote_comm` FOREIGN KEY `fk_vote_comm` (`comment_id`)
    REFERENCES `Comment` (`comment_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_vote_user` FOREIGN KEY `fk_vote_user` (`user_name`)
    REFERENCES `User` (`user_name`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB
CHARACTER SET utf8 COLLATE utf8_general_ci;

/* set an alternative delimiter for multi-statement operations */
DELIMITER $

/*
 * triggers to manage the rating average of a snippet
 */
CREATE TRIGGER `insert_rating_trigger` 
  AFTER INSERT
  ON `Rating`
  FOR EACH ROW
    BEGIN
	    DECLARE average FLOAT DEFAULT 0;
	    SET average = (SELECT SUM(rat.`rating_value`)/COUNT(*) FROM `Rating` rat 
	      WHERE rat.`snippet_id` = NEW.`snippet_id` 
	      AND rat.`rating_value` != 0);
	    UPDATE `Snippet` snip SET snip.`rating_average` = average 
	      WHERE snip.`snippet_id` = NEW.`snippet_id`;
    END; 
$

CREATE TRIGGER `update_rating_trigger` 
  AFTER UPDATE
  ON `Rating`
  FOR EACH ROW
    BEGIN
	    DECLARE average FLOAT DEFAULT 0;
	    SET average = (SELECT SUM(rat.`rating_value`)/COUNT(*) FROM `Rating` rat 
	      WHERE rat.`snippet_id` = NEW.`snippet_id` 
	      AND rat.`rating_value` != 0);
	    UPDATE `Snippet` snip SET snip.`rating_average` = average 
	      WHERE snip.`snippet_id` = NEW.`snippet_id`;

	      IF NEW.`snippet_id` != OLD.`snippet_id`
	      THEN
	        SET average = (SELECT SUM(rat.`rating_value`)/COUNT(*) FROM `Rating` rat 
	          WHERE rat.`snippet_id` = OLD.`snippet_id` 
	          AND rat.`rating_value` != 0);
	        UPDATE `Snippet` snip SET snip.`rating_average` = average 
	          WHERE snip.`snippet_id` = OLD.`snippet_id`;	      
	      END IF;
    END; 
$

CREATE TRIGGER `delete_rating_trigger` 
  AFTER DELETE
  ON `Rating`
  FOR EACH ROW
    BEGIN
	    DECLARE average FLOAT DEFAULT 0;
	    SET average = (SELECT SUM(rat.`rating_value`)/COUNT(*) FROM `Rating` rat 
	      WHERE rat.`snippet_id` = OLD.`snippet_id` 
	      AND rat.`rating_value` != 0);
	    UPDATE `Snippet` snip SET snip.`rating_average` = average 
	      WHERE snip.`snippet_id` = OLD.`snippet_id`;
    END; 
$

/*
 * triggers to manage the number of positive and nagative votes
 * of a comment
 */
CREATE TRIGGER `insert_vote_trigger` 
  AFTER INSERT
  ON `Vote`
  FOR EACH ROW
    BEGIN
	    DECLARE pos INTEGER DEFAULT 0;
	    DECLARE neg INTEGER DEFAULT 0;
	    SET pos = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = NEW.`comment_id` 
	      AND vot.`vote` = "positive");
	    SET neg = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = NEW.`comment_id` 
	      AND vot.`vote` = "negative");
	    UPDATE `Comment` comm SET comm.`pos_votes_sum` = pos, comm.`neg_votes_sum` = neg
	      WHERE comm.`comment_id` = NEW.`comment_id`;
    END; 
$

CREATE TRIGGER `update_vote_trigger` 
  AFTER UPDATE
  ON `Vote`
  FOR EACH ROW
    BEGIN
	    DECLARE pos INTEGER DEFAULT 0;
	    DECLARE neg INTEGER DEFAULT 0;
	    SET pos = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = NEW.`comment_id` 
	      AND vot.`vote` = "positive");
	    SET neg = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = NEW.`comment_id` 
	      AND vot.`vote` = "negative");
	    UPDATE `Comment` comm SET comm.`pos_votes_sum` = pos, comm.`neg_votes_sum` = neg
	      WHERE comm.`comment_id` = NEW.`comment_id`;

	      IF NEW.`comment_id` != OLD.`comment_id`
	    THEN
	    SET pos = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = OLD.`comment_id` 
	      AND vot.`vote` = "positive");
	    SET neg = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = OLD.`comment_id` 
	      AND vot.`vote` = "negative");
	    UPDATE `Comment` comm SET comm.`pos_votes_sum` = pos, comm.`neg_votes_sum` = neg
	      WHERE comm.`comment_id` = OLD.`comment_id`;
	    END IF;
    END; 
$

CREATE TRIGGER `delete_vote_trigger` 
  AFTER DELETE
  ON `Vote`
  FOR EACH ROW
    BEGIN
	    DECLARE pos INTEGER DEFAULT 0;
	    DECLARE neg INTEGER DEFAULT 0;
	    SET pos = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = OLD.`comment_id` 
	      AND vot.`vote` = "positive");
	    SET neg = (SELECT COUNT(*) FROM `Vote` vot 
	      WHERE vot.`comment_id` = OLD.`comment_id` 
	      AND vot.`vote` = "negative");
	    UPDATE `Comment` comm SET comm.`pos_votes_sum` = pos, comm.`neg_votes_sum` = neg
	      WHERE comm.`comment_id` = OLD.`comment_id`;
    END; 
$

/*
 * triggers to manage the usage frequency of a tag
 */
CREATE TRIGGER `insert_tag_trigger` 
  AFTER INSERT
  ON `RelTagSnippet`
  FOR EACH ROW
    BEGIN
	    DECLARE frequency INTEGER DEFAULT 0;
	    SET frequency = (SELECT COUNT(*) FROM `RelTagSnippet` rel 
	      WHERE rel.`tag_name` = NEW.`tag_name`);
	    UPDATE `Tag` tag SET tag.`usage_freq` = frequency 
	    WHERE tag.`tag_name` = NEW.`tag_name`;
    END; 
$

CREATE TRIGGER `update_tag_trigger` 
  AFTER UPDATE
  ON `RelTagSnippet`
  FOR EACH ROW
    BEGIN
	    DECLARE frequency INTEGER DEFAULT 0;
	    SET frequency = (SELECT COUNT(*) FROM `RelTagSnippet` rel 
	      WHERE rel.`tag_name` = NEW.`tag_name`);
	    UPDATE `Tag` tag SET tag.`usage_freq` = frequency 
	    WHERE tag.`tag_name` = NEW.`tag_name`;

	    IF NEW.`tag_name` != OLD.`tag_name`
	    THEN
	      SET frequency = (SELECT COUNT(*) FROM `RelTagSnippet` rel 
	        WHERE rel.`tag_name` = OLD.`tag_name`);
	      UPDATE `Tag` tag SET tag.`usage_freq` = frequency 
	      WHERE tag.`tag_name` = OLD.`tag_name`;
	    END IF;
    END; 
$

CREATE TRIGGER `delete_tag_trigger` 
  AFTER DELETE
  ON `RelTagSnippet`
  FOR EACH ROW
    BEGIN
	    DECLARE frequency INTEGER DEFAULT 0;
	    SET frequency = (SELECT COUNT(*) FROM `RelTagSnippet` rel 
	      WHERE rel.`tag_name` = OLD.`tag_name`);
	    UPDATE `Tag` tag SET tag.`usage_freq` = frequency 
	    WHERE tag.`tag_name` = OLD.`tag_name`;
    END; 
$

/*
 * reset the delimiter to it's default
 */
DELIMITER ;

/*
 * set up some language entries
 */
INSERT INTO `Language` (`language`, `highlighter`, `is_default`) VALUES
  ("Java", "java", TRUE),
  ("C", "c", TRUE),
  ("C++", "cpp", TRUE),
  ("Shell script", "sh", TRUE),
  ("Plain Text", NULL, TRUE),
  ("LaTeX", "tex", FALSE),
  ("SQL", "sql", FALSE),
  ("JavaScript", "js", FALSE),
  ("JSON", "json", FALSE),
  ("CoffeeScript", "coffee", FALSE),
  ("HTML", "html", FALSE),
  ("XHTML", "xhtml", FALSE),
  ("XML", "xml", FALSE),
  ("CSS Stylesheet", "css", FALSE),
  ("Visual Basic", "vb", FALSE),
  ("OCaml", "ml", FALSE),
  ("VHDL", "vhdl", FALSE),
  ("Haskell", "hs", FALSE),
  ("Ruby", "rb", FALSE),
  ("Perl", "perl", FALSE),
  ("Python", "py", FALSE),
  ("C#", "cs", FALSE),
  ("F#", "fs", FALSE),
  ("XQuery", "xquery", FALSE),
  ("Prolog", NULL, FALSE)
  ("Pseudocode", NULL, FALSE)
;

/*
 * set up some category entries
 */
INSERT INTO `Category` (`category_id`, `parent_id`, `name`, `description`) VALUES 
  (1, NULL, "Uncategorized", "This is a failback category for snippets without a category."), 
  (2, 1, "Text only", "Category which belongs to no explicite programming language, nor is there any category which does fit."), 

  (3, NULL, "Misc", "Miscelanous snippets, they don't fit in another category"), 
  (4, NULL, "File Handling", "Everything which creates or modifies files"), 
  (5, NULL, "I/O Streams", "Input and output streams"), 
  (6, 5, "Input", "Input streams"), 
  (7, 5, "Output", "Output streams"), 
  (8, NULL, "Search", "Searching algorithms"), 
  (9, NULL, "Sort", "Sorting algorithms")
;
