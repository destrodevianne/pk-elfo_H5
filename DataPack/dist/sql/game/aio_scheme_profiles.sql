DROP TABLE IF EXISTS `aio_scheme_profiles`;
CREATE TABLE IF NOT EXISTS `aio_scheme_profiles` (
  `charId` int(10) unsigned NOT NULL,
  `profile` varchar(45) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;