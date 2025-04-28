DROP TABLE IF EXISTS BLOG;
CREATE TABLE IF NOT EXISTS `BLOG`
(
   `id` varchar (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL PRIMARY KEY,
   `title` text,
   `content` text,
   `inner_id` int NOT NULL
)
ENGINE= InnoDB DEFAULT CHARSET= utf8mb4 COLLATE= utf8mb4_0900_ai_ci;