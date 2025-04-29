DROP TABLE IF EXISTS BLOG;
CREATE TABLE IF NOT EXISTS `BLOG`
(
   `id` varchar (64) NOT NULL PRIMARY KEY,
   `title` text,
   `content` text,
   `inner_id` int NOT NULL
);