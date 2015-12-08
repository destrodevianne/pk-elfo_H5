DROP TABLE IF EXISTS `aio_teleports_categories`;
CREATE TABLE IF NOT EXISTS `aio_teleports_categories` (
  `category_id` int(10) NOT NULL,
  `category` varchar(45) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO aio_teleports_categories VALUES
(1, 'Towns'),
(2, 'Starting Village'),
(3, 'Hunting Grounds [10-20]'),
(4, 'Hunting Grounds [20-30]'),
(5, 'Hunting Grounds [30-40]'),
(6, 'Hunting Grounds [40-50]'),
(7, 'Hunting Grounds [50-60]'),
(8, 'Hunting Grounds [60-70]'),
(9, 'Hunting Grounds [70-80]'),
(10, 'Hunting Grounds [80-85]');
